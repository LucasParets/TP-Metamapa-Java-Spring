package ar.utn.ba.ddsi.tpa.fuente_estatica.services.impl;

import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.fuente.Fuente;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.fuente.TipoDataset;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.hechos.Categoria;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.utilities.Coordenada;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.utilities.EstadoHecho;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.utilities.FechaParser;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.repositories.*;
import ar.utn.ba.ddsi.tpa.fuente_estatica.services.IHechosService;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatasetService {
    private final IHechosRepository hechosRepository;
    private final IHechosService hechosService;
    private final ICategoriaRepository categoriaRepository;
    private final IFuentesRepository fuentesRepository;
    private final Logger log = LoggerFactory.getLogger(DatasetService.class);
    @Value("${app.datasets.storage.path}")
    private String datasetsPath;

    public DatasetService(IHechosRepository hechosRepository, IHechosService hechosService,
                          ICategoriaRepository categoriaRepository,
                          IFuentesRepository fuentesRepository) {
        this.hechosService = hechosService;
        this.hechosRepository = hechosRepository;
        this.categoriaRepository = categoriaRepository;
        this.fuentesRepository = fuentesRepository;
    }

    public Path guardarDataset(MultipartFile dataset) throws IOException {
        Path directorioDatasets = Paths.get(datasetsPath);
        if (!Files.exists(directorioDatasets)) {
            Files.createDirectories(directorioDatasets);
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String extension = getFileExtension(dataset.getOriginalFilename());
        String nombreOriginalSinExtension = getFileNameWithoutExtension(dataset.getOriginalFilename());
        String nombreArchivo = String.format("%s_%s%s",
                nombreOriginalSinExtension,
                timestamp,
                extension);

        Path archivoDestino = directorioDatasets.resolve(nombreArchivo);

        dataset.transferTo(archivoDestino.toFile());

        log.info("Dataset guardado en: {}", archivoDestino);
        return archivoDestino;
    }

    @Async("datasetImport")
    public void importarDatasetDesdeArchivo(Path archivoPath) throws IOException {
        File file = archivoPath.toFile();
        FileInputStream inputStream = new FileInputStream(file);

        Fuente fuente = new Fuente(file.getName(), inputStream);

        Fuente fuenteUsada = fuentesRepository.save(fuente);

        List<Hecho> hechos = leerArchivo(fuenteUsada);

        hechosRepository.saveAll(hechos);

        inputStream.close();
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    private String getFileNameWithoutExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return filename;
        }
        return filename.substring(0, filename.lastIndexOf("."));
    }

    private List<Hecho> leerArchivo(Fuente f) {
        TipoDataset extension = f.extensionDataset();
        if (extension == TipoDataset.CSV) {
            return importarCSV(f);
        }
        throw new RuntimeException("Tipo de dataset no soportado");
    }

    private List<Hecho> importarCSV(Fuente fuente) {
        InputStream inputStream = fuente.getDatasetInputStream();
        List<Hecho> hechos = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] nextRecord;
            csvReader.readNext();
            while ((nextRecord = csvReader.readNext()) != null) {
                Hecho hecho = crearHechoAPartirDeCSV(nextRecord, fuente);
                hechos.add(hecho);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo CSV", e);
        }
        return hechos;
    }

    private Hecho crearHechoAPartirDeCSV(String[] args, Fuente fuente) {
        try {
            Hecho hecho = new Hecho();
            hecho.setTitulo(args[0]);
            hecho.setDescripcion(args[1]);

            String nombreCategoria = args[2].toLowerCase().trim();
            var categoria = categoriaRepository.findByNombre(nombreCategoria)
                    .orElseGet(() -> categoriaRepository.findByNombreAlterno(nombreCategoria)
                            .orElseGet(() -> categoriaRepository.save(new Categoria(nombreCategoria))));
            hecho.setCategoria(categoria);

            hecho.setCoordenada(new Coordenada(Float.parseFloat(args[3]), Float.parseFloat(args[4])));
            hecho.setFechaHecho(FechaParser.of(args[5]));
            hecho.setFechaCarga(LocalDateTime.now());
            hecho.setFechaModificacion(LocalDateTime.now());
            hecho.setEstadoDelHecho(EstadoHecho.ACTIVO);
            hecho.setFuente(fuente);
            return hecho;
        } catch (NumberFormatException e) {
            throw new RuntimeException("Error al parsear las coordenadas del CSV", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la línea del CSV", e);
        }

    }
}

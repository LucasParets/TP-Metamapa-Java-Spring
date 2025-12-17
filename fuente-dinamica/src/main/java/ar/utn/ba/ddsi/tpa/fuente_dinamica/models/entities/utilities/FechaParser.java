package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.utilities;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FechaParser {
    public static LocalDateTime of(String fecha) {
        String[] patrones = {
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                "dd-MM-yyyy HH:mm:ss",
                "dd/MM/yyyy HH:mm:ss",
                "dd-MM-yyyy HH:mm",
                "dd/MM/yyyy HH:mm",
                "dd-MM-yyyy",
                "dd/MM/yyyy"
        };

        try {
            return DateUtils.parseDateStrictly(fecha, patrones)
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido: " + fecha, e);
        }

    }
}
package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.consenso;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AlgoritmoConsensoConverter implements AttributeConverter<AlgoritmoConsenso, String> {
    @Override
    public String convertToDatabaseColumn(AlgoritmoConsenso attribute) {
        if(attribute == null) return null;
        return attribute.getClass().getSimpleName();
    }

    @Override
    public AlgoritmoConsenso convertToEntityAttribute(String dbData) {
        if(dbData == null) return null;
        if("Absoluta".equals(dbData)) return new Absoluta();
        if("MayoriaSimple".equals(dbData)) return new MayoriaSimple();
        if("MultiplesMenciones".equals(dbData)) return new MultiplesMenciones();
        return null;
    }
}

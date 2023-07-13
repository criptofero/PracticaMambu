package com.sofka.practicaMambu.domain.seedWork;

import com.sofka.practicaMambu.domain.model.query.MambuQueryFilter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

public class CommonUtils {
    /**
     * Permite generar un código UUID (versión 3) a partir de un texto de entrada.
     * @param inputText Texto de origen
     * @return Código UUID generado
     */
    public static UUID generateNamedUUID(String inputText){
        UUID resultUUID = null;
        if (inputText != null && !inputText.isEmpty()){
            //resultUUID = UUID.nameUUIDFromBytes(inputText.getBytes(StandardCharsets.UTF_8));
            resultUUID = UUID5Generator.fromUTF8(inputText);
        }
        return resultUUID;
    }

    public static MambuQueryFilter getFirstFilterByFieldAndOperator(MambuQueryFilter[] filters, String fieldName, String operator){
        MambuQueryFilter queryFilter = null;
        if (fieldName == null || fieldName.isEmpty()){
            throw new RuntimeException("Nombre de campo no puede ser un valor nulo o vacío");
        }
        if (operator == null || operator.isEmpty()){
            throw new RuntimeException("Operador no puede ser un valor nulo o vacío");
        }
        if (filters == null || filters.length == 0){
            throw new RuntimeException("Conjunto de filtros no puede ser un valor nulo o vacío");
        }
        var filtersStream = Arrays.stream(filters);
        queryFilter = filtersStream.filter(f -> f.getField().equals(fieldName) && f.getOperator().equals(operator)).findFirst().get();
        return queryFilter;
    }

    public static LocalDate parseDateString(String inputDate, String dateFormat){
        LocalDate outputDate = null;
        if (inputDate == null || inputDate.isEmpty()){
            throw new RuntimeException("Fecha de entrada no puede ser un valor nulo o vacío");
        }
        if (dateFormat == null || dateFormat.isEmpty()){
            throw new RuntimeException("Formato de fecha ser un valor nulo o vacío");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        outputDate = LocalDate.parse(inputDate, formatter);
        return  outputDate;
    }
}

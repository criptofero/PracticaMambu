package com.sofka.practicaMambu.domain.seedWork;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CommonUtils {
    /**
     * Permite generar un código UUID (versión 3) a partir de un texto de entrada.
     * @param inputText Texto de origen
     * @return Código UUID generado
     */
    public static UUID generateNamedUUID(String inputText){
        UUID resultUUID = null;
        if (inputText != null && !inputText.isEmpty()){
            resultUUID = UUID.nameUUIDFromBytes(inputText.getBytes(StandardCharsets.UTF_8));
        }
        return resultUUID;
    }
}

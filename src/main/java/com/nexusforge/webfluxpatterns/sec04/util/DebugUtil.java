package com.nexusforge.webfluxpatterns.sec04.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexusforge.webfluxpatterns.sec04.dto.OrchestrationRequestContext;

public class DebugUtil {
    public static void print(OrchestrationRequestContext ctx) {
        ObjectMapper mapper = new ObjectMapper();
        String s = null;
        try {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ctx));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printObject(Object ctx) {
        ObjectMapper mapper = new ObjectMapper();
        String s = null;
        try {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ctx));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

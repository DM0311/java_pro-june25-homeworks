package ru.otus.java.pro.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private String filename;
    private final ObjectMapper mapper;

    public FileSerializer(String fileName) {
        this.filename = fileName;
        this.mapper = JsonMapper.builder().build().configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try {
            mapper.writeValue(new File(filename), data);
        } catch (IOException e) {
            throw new FileProcessException("Error while writing data to file");
        }
    }
}

package ru.otus.java.pro.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import ru.otus.java.pro.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private String filename;
    private final ObjectMapper objectMapper;

    public ResourcesFileLoader(String fileName) {
        this.filename = fileName;
        this.objectMapper = JsonMapper.builder().build();
    }

    @Override
    public List<Measurement> load() {

        try (InputStream input = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(filename)) {
            try {
                List<Measurement> parsed =
                        objectMapper.readValue(input.readAllBytes(), new TypeReference<List<Measurement>>() {});
                return parsed;

            } catch (IOException e) {
                throw new FileProcessException("Error while reading file");
            }
        } catch (IOException e) {
            throw new FileProcessException("Error while opening file");
        }
    }
}

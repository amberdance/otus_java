package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final String filename;

    public FileSerializer(String fileName) {
        this.filename = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try {
            Files.write(Path.of(filename), new Gson().toJson(data).getBytes());
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}

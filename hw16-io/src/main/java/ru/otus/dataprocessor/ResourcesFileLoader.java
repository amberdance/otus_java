package ru.otus.dataprocessor;

import com.google.gson.Gson;
import ru.otus.model.Measurement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        // На всякий случай осуждаю
        try {
            return Arrays.asList(new Gson().fromJson(Files.readString(new File(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getFile()).toPath()), Measurement[].class));
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}

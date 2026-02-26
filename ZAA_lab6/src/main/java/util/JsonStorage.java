package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class JsonStorage<T> {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void save(T object, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(object, writer);
        }
    }

    public T load(String filename, Class<T> type) throws IOException {
        try (FileReader reader = new FileReader(filename)) {
            return gson.fromJson(reader, type);
        }
    }
}

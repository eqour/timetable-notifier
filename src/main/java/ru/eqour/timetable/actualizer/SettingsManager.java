package ru.eqour.timetable.actualizer;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class SettingsManager {

    private final Gson gson;
    private final String settingsPath;

    public SettingsManager(String settingsPath) {
        this.settingsPath = settingsPath;
        this.gson = new Gson().newBuilder().setPrettyPrinting().create();
    }

    public void save(Settings settings) {
        String json = gson.toJson(settings);
        try (PrintWriter writer = new PrintWriter(settingsPath, "UTF-8")) {
            writer.write(json);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public Settings load() {
        try {
            String json = new String(Files.readAllBytes(Paths.get(settingsPath)), StandardCharsets.UTF_8);
            return gson.fromJson(json, Settings.class);
        } catch (IOException | InvalidPathException e) {
            return null;
        }
    }
}

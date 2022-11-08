package ru.eqour.timetable;

import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.actualizer.Settings;
import ru.eqour.timetable.actualizer.SettingsManager;
import ru.eqour.timetable.util.ResourceLoader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class SettingsManagerTests {

    @Test
    public void whenLoadAndFileNotFoundThenReturnNull() {
        SettingsManager settingsManager = new SettingsManager("settings.json");
        Settings actual = settingsManager.load();
        Assert.assertNull(actual);
    }

    @Test
    public void whenLoadAndFileExistsThenReturnValidResult() {
        String path = ResourceLoader.getFullPathToResource("/settings-manager/settings-0.json");
        SettingsManager settingsManager = new SettingsManager(path);
        Settings expected = ResourceLoader.loadFromResources("/settings-manager/settings-0.json", Settings.class);
        Settings actual = settingsManager.load();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void whenSaveAndLoadThenReturnValidResult() throws IOException {
        String path = getTempSettingsPath();
        SettingsManager settingsManager = new SettingsManager(path);
        Settings expected = createSettings();
        settingsManager.save(expected);
        Settings actual = settingsManager.load();
        Assert.assertEquals(expected, actual);
        Files.deleteIfExists(Paths.get(path));
    }

    @Test
    public void whenSaveThenFileWithSettingsCreated() throws IOException {
        String path = getTempSettingsPath();
        SettingsManager settingsManager = new SettingsManager(path);
        Settings settings = createSettings();
        settingsManager.save(settings);
        Settings actual = ResourceLoader.loadFromResources("/settings-manager/settings.json", Settings.class);
        Assert.assertEquals(settings, actual);
        Files.deleteIfExists(Paths.get(path));
    }

    private Settings createSettings() {
        Settings settings = new Settings();
        settings.savedWeek = null;
        settings.timetableFileId = "id";
        settings.telegramToken = "telegram";
        settings.vkToken = "vk";
        return settings;
    }

    private String getTempSettingsPath() {
        try {
            Path folderPath = Paths.get(Objects.requireNonNull(getClass().getResource("/settings-manager/")).toURI());
            return Paths.get(folderPath.toString(), "settings.json").toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

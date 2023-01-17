package ru.eqour.timetable;

import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.settings.Settings;
import ru.eqour.timetable.settings.SettingsManager;
import ru.eqour.timetable.settings.SimpleSettingsManager;
import ru.eqour.timetable.util.JsonFileHelper;
import ru.eqour.timetable.util.ResourceHelper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class SettingsManagerTests {

    @Test
    public void whenLoadAndFileNotFoundThenReturnNull() {
        SettingsManager settingsManager = new SimpleSettingsManager("settings-temp.json");
        Settings actual = settingsManager.load();
        Assert.assertNull(actual);
    }

    @Test
    public void whenLoadAndFileExistsThenReturnValidResult() {
        String path = ResourceHelper.getFullPathToResource("/settings-manager/settings-0.json").toString();
        SettingsManager settingsManager = new SimpleSettingsManager(path);
        Settings expected = JsonFileHelper.loadFromFile(
                ResourceHelper.getFullPathToResource("/settings-manager/settings-0.json").toString(), Settings.class);
        Settings actual = settingsManager.load();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void whenSaveAndLoadThenReturnValidResult() throws IOException {
        String path = getTempSettingsPath();
        SettingsManager settingsManager = new SimpleSettingsManager(path);
        Settings expected = createSettings();
        settingsManager.save(expected);
        Settings actual = settingsManager.load();
        Assert.assertEquals(expected, actual);
        Files.deleteIfExists(Paths.get(path));
    }

    @Test
    public void whenSaveThenFileWithSettingsCreated() throws IOException {
        String path = getTempSettingsPath();
        SettingsManager settingsManager = new SimpleSettingsManager(path);
        Settings settings = createSettings();
        settingsManager.save(settings);
        Settings actual = JsonFileHelper.loadFromFile(
                ResourceHelper.getFullPathToResource("/settings-manager/settings.json").toString(), Settings.class);
        Assert.assertEquals(settings, actual);
        Files.deleteIfExists(Paths.get(path));
    }

    private Settings createSettings() {
        Settings settings = new Settings();
        settings.savedWeeks = null;
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

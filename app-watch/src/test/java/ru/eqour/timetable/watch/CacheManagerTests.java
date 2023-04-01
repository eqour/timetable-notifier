package ru.eqour.timetable.watch;

import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.watch.settings.CacheManager;
import ru.eqour.timetable.watch.settings.Settings;
import ru.eqour.timetable.watch.settings.SimpleCacheManager;
import ru.eqour.timetable.watch.util.JsonFileHelper;
import ru.eqour.timetable.watch.util.ResourceHelper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class CacheManagerTests {

    @Test
    public void whenLoadAndFileNotFoundThenReturnNull() {
        CacheManager<Settings> cacheManager = new SimpleCacheManager<>("settings-temp.json", Settings.class);
        Settings actual = cacheManager.load();
        Assert.assertNull(actual);
    }

    @Test
    public void whenLoadAndFileExistsThenReturnValidResult() {
        String path = ResourceHelper.getFullPathToResource("/settings-manager/settings-0.json").toString();
        CacheManager<Settings> cacheManager = new SimpleCacheManager<>(path, Settings.class);
        Settings expected = JsonFileHelper.loadFromFile(
                ResourceHelper.getFullPathToResource("/settings-manager/settings-0.json").toString(), Settings.class);
        Settings actual = cacheManager.load();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void whenSaveAndLoadThenReturnValidResult() throws IOException {
        String path = getTempSettingsPath();
        CacheManager<Settings> cacheManager = new SimpleCacheManager<>(path, Settings.class);
        Settings expected = createSettings();
        cacheManager.save(expected);
        Settings actual = cacheManager.load();
        Assert.assertEquals(expected, actual);
        Files.deleteIfExists(Paths.get(path));
    }

    @Test
    public void whenSaveThenFileWithSettingsCreated() throws IOException {
        String path = getTempSettingsPath();
        CacheManager<Settings> cacheManager = new SimpleCacheManager<>(path, Settings.class);
        Settings settings = createSettings();
        cacheManager.save(settings);
        Settings actual = JsonFileHelper.loadFromFile(
                ResourceHelper.getFullPathToResource("/settings-manager/settings.json").toString(), Settings.class);
        Assert.assertEquals(settings, actual);
        Files.deleteIfExists(Paths.get(path));
    }

    private Settings createSettings() {
        Settings settings = new Settings();
        settings.timetableFileId = "id";
        settings.telegramToken = "telegram";
        settings.vkToken = "vk";
        settings.dbConnection = "db-connection";
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

package ru.eqour.timetable.watch.mock;

import ru.eqour.timetable.watch.settings.CacheManager;

public class CacheManagerMock<T> implements CacheManager<T> {

    private final boolean withBadSave, withBadLoad;
    private T value;
    private int saveCalls, loadCalls;

    public CacheManagerMock(boolean withBadSave, boolean withBadLoad, T value) {
        this.withBadSave = withBadSave;
        this.withBadLoad = withBadLoad;
        this.value = value;
    }

    @Override
    public void save(T value) {
        saveCalls++;
        if (withBadSave) {
            throw new RuntimeException();
        } else {
            this.value = value;
        }
    }

    @Override
    public T load() {
        loadCalls++;
        if (withBadLoad) {
            return null;
        } else {
            return value;
        }
    }

    public int getSaveCalls() {
        return saveCalls;
    }

    public int getLoadCalls() {
        return loadCalls;
    }
}

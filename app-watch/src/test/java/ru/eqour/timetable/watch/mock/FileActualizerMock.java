package ru.eqour.timetable.watch.mock;

import ru.eqour.timetable.watch.api.FileActualizer;

import java.util.List;

public class FileActualizerMock implements FileActualizer {

    private final List<Integer> trueIndexes;
    private int index;

    public FileActualizerMock() {
        this(null);
    }

    public FileActualizerMock(List<Integer> trueIndexes) {
        this.trueIndexes = trueIndexes;
        index = -1;
    }

    @Override
    public boolean actualize() {
        index++;
        return trueIndexes != null && trueIndexes.contains(index);
    }

    @Override
    public byte[] getActualFile() {
        return new byte[0];
    }
}

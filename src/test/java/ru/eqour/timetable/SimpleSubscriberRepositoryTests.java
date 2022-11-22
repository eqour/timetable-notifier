package ru.eqour.timetable;

import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.exception.RepositoryException;
import ru.eqour.timetable.repository.SimpleSubscriberRepository;
import ru.eqour.timetable.repository.SubscriberRepository;
import ru.eqour.timetable.util.ResourceLoader;

public class SimpleSubscriberRepositoryTests {

    private static final String GROUP_1 = "ОБ-Вт-09.03.03.02-11";
    private static final String GROUP_2 = "ОБ-Вт-09.03.03.02-41";

    @Test
    public void whenValidFileThenReturnNotNull() {
        String path = ResourceLoader.getFullPathToResource("/simple-subscriber-repository/subscribers-0.json");
        SubscriberRepository repository = new SimpleSubscriberRepository(path);
        Assert.assertNotNull(repository.getSubscribers(GROUP_1));
        Assert.assertNotNull(repository.getSubscribers(GROUP_2));
    }

    @Test
    public void whenInvalidFilePathThenThrowException() {
        Assert.assertThrows(RepositoryException.class, () -> {
            SubscriberRepository repository = new SimpleSubscriberRepository("/invalid/path/");
            repository.getSubscribers(GROUP_1);
        });
    }

    @Test
    public void whenInvalidFileContentThenThrowException() {
        Assert.assertThrows(RepositoryException.class, () -> {
            SubscriberRepository repository = new SimpleSubscriberRepository("/simple-subscriber-repository/subscribers-1.json");
            repository.getSubscribers(GROUP_1);
        });
    }
}

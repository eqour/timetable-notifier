package ru.eqour.timetable.watch;

import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.watch.exception.RepositoryException;
import ru.eqour.timetable.watch.repository.SimpleSubscriberRepository;
import ru.eqour.timetable.watch.repository.SubscriberRepository;
import ru.eqour.timetable.watch.util.ResourceHelper;

public class SimpleSubscriberRepositoryTests {

    private static final String GROUP_1 = "ОБ-Вт-09.03.03.02-11";
    private static final String GROUP_2 = "ОБ-Вт-09.03.03.02-41";
    private static final String TEACHER_1 = "Иванов И.И.";
    private static final String GROUP_UNKNOWN = "ОБ-Вт-111-41";
    private static final String TEACHER_UNKNOWN = "Петров П.П.";

    @Test
    public void whenValidGroupFileThenReturnNotNull() {
        String path = ResourceHelper.getFullPathToResource("/simple-subscriber-repository/subscribers-0.json").toString();
        SubscriberRepository repository = new SimpleSubscriberRepository(path);
        Assert.assertNotNull(repository.getSubscribersByStudentGroup(GROUP_1));
        Assert.assertNotNull(repository.getSubscribersByStudentGroup(GROUP_2));
    }

    @Test
    public void whenValidGroupTeacherFileThenReturnNotNull() {
        String path = ResourceHelper.getFullPathToResource("/simple-subscriber-repository/subscribers-2.json").toString();
        SubscriberRepository repository = new SimpleSubscriberRepository(path);
        Assert.assertNotNull(repository.getSubscribersByStudentGroup(GROUP_1));
        Assert.assertNotNull(repository.getSubscribersByStudentGroup(GROUP_2));
        Assert.assertNotNull(repository.getSubscribersByTeacher(TEACHER_1));
    }

    @Test
    public void whenValidGroupTeacherFileThenReturnValidTeacherData() {
        String path = ResourceHelper.getFullPathToResource("/simple-subscriber-repository/subscribers-2.json").toString();
        SubscriberRepository repository = new SimpleSubscriberRepository(path);
        Assert.assertNotNull(repository.getSubscribersByTeacher(TEACHER_1));
        Assert.assertEquals("010", repository.getSubscribersByTeacher(TEACHER_1).get(0).vkId);
        Assert.assertEquals("011", repository.getSubscribersByTeacher(TEACHER_1).get(0).telegramId);
        Assert.assertEquals("012", repository.getSubscribersByTeacher(TEACHER_1).get(0).email);
        Assert.assertEquals("qwe@qwe.qwe", repository.getSubscribersByTeacher(TEACHER_1).get(1).email);
        Assert.assertEquals("013", repository.getSubscribersByTeacher(TEACHER_1).get(2).telegramId);
    }

    @Test
    public void whenInvalidFilePathThenThrowException() {
        Assert.assertThrows(RepositoryException.class, () -> {
            SubscriberRepository repository = new SimpleSubscriberRepository("/invalid/path/");
            repository.getSubscribersByStudentGroup(GROUP_1);
        });
    }

    @Test
    public void whenInvalidFileContentThenThrowException() {
        Assert.assertThrows(RepositoryException.class, () -> {
            SubscriberRepository repository = new SimpleSubscriberRepository("/simple-subscriber-repository/subscribers-1.json");
            repository.getSubscribersByStudentGroup(GROUP_1);
        });
    }

    @Test
    public void whenValidFileAndUnknownGroupThenReturnEmptyList() {
        String path = ResourceHelper.getFullPathToResource("/simple-subscriber-repository/subscribers-0.json").toString();
        SubscriberRepository repository = new SimpleSubscriberRepository(path);
        Assert.assertNotNull(repository.getSubscribersByStudentGroup(GROUP_UNKNOWN));
    }

    @Test
    public void whenValidFileAndUnknownTeacherThenReturnEmptyList() {
        String path = ResourceHelper.getFullPathToResource("/simple-subscriber-repository/subscribers-0.json").toString();
        SubscriberRepository repository = new SimpleSubscriberRepository(path);
        Assert.assertNotNull(repository.getSubscribersByTeacher(TEACHER_UNKNOWN));
    }
}

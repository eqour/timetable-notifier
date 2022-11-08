package ru.eqour.timetable;

import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.mock.GoogleDriveApiMock;
import ru.eqour.timetable.api.FileActualizer;
import ru.eqour.timetable.api.google.GoogleDriveExcelFileActualizer;

public class GoogleDriveExcelFileActualizerTests {

    @Test
    public void whenGetWorkspaceDocumentThenNotThrowException() {
        testActualizer(new GoogleDriveExcelFileActualizer(GoogleDriveApiMock.WORKSPACE_EXCEL_FILE_ID,
                new GoogleDriveApiMock().withVersion(1L)));
    }

    @Test
    public void whenGetMicrosoftExcelFileThenNotThrowException() {
        testActualizer(new GoogleDriveExcelFileActualizer(GoogleDriveApiMock.MICROSOFT_EXCEL_FILE_ID,
                new GoogleDriveApiMock().withVersion(1L)));
    }

    private void testActualizer(FileActualizer actualizer) {
        actualizer.actualize();
        actualizer.getActualFile();
    }

    @Test
    public void whenVersionNotChangedThenFileNotUpdated() {
        FileActualizer actualizer = new GoogleDriveExcelFileActualizer(GoogleDriveApiMock.WORKSPACE_EXCEL_FILE_ID,
                new GoogleDriveApiMock().withVersion(7L), 7L);
        actualizer.actualize();
        byte[] actual = actualizer.getActualFile();
        Assert.assertNull(actual);
    }

    @Test
    public void whenVersionChangedUpgradedThenFileUpdated() {
        FileActualizer actualizer = new GoogleDriveExcelFileActualizer(GoogleDriveApiMock.WORKSPACE_EXCEL_FILE_ID,
                new GoogleDriveApiMock().withVersion(10L), 11L);
        actualizer.actualize();
        byte[] actual = actualizer.getActualFile();
        Assert.assertNotNull(actual);
    }

    @Test
    public void whenVersionChangedDowngradedThenFileUpdated() {
        FileActualizer actualizer = new GoogleDriveExcelFileActualizer(GoogleDriveApiMock.WORKSPACE_EXCEL_FILE_ID,
                new GoogleDriveApiMock().withVersion(10L), 9L);
        actualizer.actualize();
        byte[] actual = actualizer.getActualFile();
        Assert.assertNotNull(actual);
    }

    @Test
    public void whenVersionChangedOnceAndActualizedTwiceThenFileUpdatedOnce() {
        FileActualizer actualizer = new GoogleDriveExcelFileActualizer(GoogleDriveApiMock.WORKSPACE_EXCEL_FILE_ID,
                new GoogleDriveApiMock().withVersion(4L), 5L);
        actualizer.actualize();
        byte[] actual = actualizer.getActualFile();
        actualizer.actualize();
        Assert.assertNotNull(actual);
        Assert.assertSame(actual, actualizer.getActualFile());
    }
}

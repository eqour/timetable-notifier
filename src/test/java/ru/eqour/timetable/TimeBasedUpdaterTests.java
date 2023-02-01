package ru.eqour.timetable;

import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.mock.FileActualizerMock;
import ru.eqour.timetable.util.time.TimeBasedUpdater;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeBasedUpdaterTests {

    private static final int MILLISECONDS_IN_MINUTE = 60 * 1000;
    private static final int MILLISECONDS_IN_SECOND = 1000;

    @Test
    public void whenTimetableChangedAnd5MinutesPassedThenRunnableCalledOnce() {
        FileActualizerMock mockActualizer = new FileActualizerMock(Collections.singletonList(0));
        AtomicInteger numberOfCalls = new AtomicInteger();
        TimeBasedUpdater updater = new TimeBasedUpdater(mockActualizer::actualize, numberOfCalls::incrementAndGet, 5);
        for (int i = 0; i <= MILLISECONDS_IN_MINUTE * 5; i += MILLISECONDS_IN_MINUTE) {
            updater.update(i);
        }
        Assert.assertEquals(numberOfCalls.get(), 1);
    }

    @Test
    public void whenTimetableChangedAnd4MinutesPassedThenRunnableNotCalled() {
        FileActualizerMock mockActualizer = new FileActualizerMock(Collections.singletonList(0));
        AtomicBoolean actualizerIsCalled = new AtomicBoolean();
        TimeBasedUpdater updater = new TimeBasedUpdater(mockActualizer::actualize, () -> actualizerIsCalled.set(true), 5);
        for (int i = 0; i <= MILLISECONDS_IN_MINUTE * 4; i += MILLISECONDS_IN_MINUTE) {
            updater.update(i);
        }
        Assert.assertFalse(actualizerIsCalled.get());
    }

    @Test
    public void whenTimetableChangedAnd5MinutesPassedWithSecondStepThenRunnableCalledOnce() {
        FileActualizerMock mockActualizer = new FileActualizerMock(Collections.singletonList(0));
        AtomicInteger numberOfCalls = new AtomicInteger();
        TimeBasedUpdater updater = new TimeBasedUpdater(mockActualizer::actualize, numberOfCalls::incrementAndGet, 5);
        for (int i = 0; i <= MILLISECONDS_IN_MINUTE * 5; i += MILLISECONDS_IN_SECOND) {
            updater.update(i);
        }
        Assert.assertEquals(numberOfCalls.get(), 1);
    }

    @Test
    public void whenTimetableChangedSeveralTimesInOnePeriodAnd5MinutesPassedThenRunnableNotCalled() {
        FileActualizerMock mockActualizer = new FileActualizerMock(Arrays.asList(0, 5));
        AtomicInteger numberOfCalls = new AtomicInteger();
        TimeBasedUpdater updater = new TimeBasedUpdater(mockActualizer::actualize, numberOfCalls::incrementAndGet, 5);
        for (int i = 0; i <= MILLISECONDS_IN_MINUTE * 5; i += MILLISECONDS_IN_MINUTE) {
            updater.update(i);
        }
        Assert.assertEquals(numberOfCalls.get(), 0);
    }

    @Test
    public void whenTimetableChangedSeveralTimesInOnePeriodAnd10MinutesPassedThenRunnableCalledOnce() {
        FileActualizerMock mockActualizer = new FileActualizerMock(Arrays.asList(0, 5));
        AtomicInteger numberOfCalls = new AtomicInteger();
        TimeBasedUpdater updater = new TimeBasedUpdater(mockActualizer::actualize, numberOfCalls::incrementAndGet, 5);
        for (int i = 0; i <= MILLISECONDS_IN_MINUTE * 10; i += MILLISECONDS_IN_MINUTE) {
            updater.update(i);
        }
        Assert.assertEquals(numberOfCalls.get(), 1);
    }



    @Test
    public void whenTimetableChangedSeveralTimesInDifferentPeriodsAnd11MinutesPassedThenRunnableCalledTwice() {
        FileActualizerMock mockActualizer = new FileActualizerMock(Arrays.asList(0, 6));
        AtomicInteger numberOfCalls = new AtomicInteger();
        TimeBasedUpdater updater = new TimeBasedUpdater(mockActualizer::actualize, numberOfCalls::incrementAndGet, 5);
        for (int i = 0; i <= MILLISECONDS_IN_MINUTE * 11; i += MILLISECONDS_IN_MINUTE) {
            updater.update(i);
        }
        Assert.assertEquals(numberOfCalls.get(), 2);
    }

    @Test
    public void whenTimeGoesBackwardsThenThrowException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            FileActualizerMock mockActualizer = new FileActualizerMock();
            AtomicInteger numberOfCalls = new AtomicInteger();
            TimeBasedUpdater updater = new TimeBasedUpdater(mockActualizer::actualize, numberOfCalls::incrementAndGet, 5);
            updater.update(0);
            updater.update(-MILLISECONDS_IN_MINUTE);
        });
    }

    @Test
    public void whenTimetableChangedAnd0MinutesPassedAndPeriodIs0ThenRunnableCalledOnce() {
        FileActualizerMock mockActualizer = new FileActualizerMock(Collections.singletonList(0));
        AtomicInteger numberOfCalls = new AtomicInteger();
        TimeBasedUpdater updater = new TimeBasedUpdater(mockActualizer::actualize, numberOfCalls::incrementAndGet, 0);
        updater.update(0);
        Assert.assertEquals(numberOfCalls.get(), 1);
    }
}

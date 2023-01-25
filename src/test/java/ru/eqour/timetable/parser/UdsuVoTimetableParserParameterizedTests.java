package ru.eqour.timetable.parser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.parser.impl.UdsuVoTimetableParser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RunWith(Enclosed.class)
public class UdsuVoTimetableParserParameterizedTests {

    @RunWith(Parameterized.class)
    public static class OneWeekInOnePeriodParameterizedTests {

        private final LocalDate initialDate;
        private final int daysInPeriod;
        private final String expectedPeriod;

        public OneWeekInOnePeriodParameterizedTests(LocalDate initialDate, int daysInPeriod, String expectedPeriod) {
            this.initialDate = initialDate;
            this.daysInPeriod = daysInPeriod;
            this.expectedPeriod = expectedPeriod;
        }

        @Test
        public void test() throws IOException {
            TimetableParser parser = createParser(initialDate, daysInPeriod);
            List<Week> weeks = parser.parseTimetable(getClass().getResourceAsStream(getTimetablePath(0)));
            Assert.assertNotNull(weeks);
            Assert.assertEquals(1, weeks.size());
            Assert.assertEquals(expectedPeriod, weeks.get(0).period);
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { LocalDate.parse("2022-09-01"), 3, "01.09.2022-03.09.2022" },
                    { LocalDate.parse("2022-09-01"), 1, "01.09.2022-03.09.2022" },
                    { LocalDate.parse("2022-09-02"), 1, "01.09.2022-03.09.2022" },
                    { LocalDate.parse("2022-09-02"), 5, "01.09.2022-03.09.2022" },
                    { LocalDate.parse("2022-09-25"), 30, "03.10.2022-08.10.2022" },
                    { LocalDate.parse("2022-09-01"), 3, "01.09.2022-03.09.2022" },
                    { LocalDate.parse("2023-01-12"), 5, "16.01.2023-21.01.2023" },
            };
        }
    }

    @RunWith(Parameterized.class)
    public static class TwoWeeksInOnePeriodParameterizedTests {

        private final LocalDate initialDate;
        private final int daysInPeriod;
        private final String expectedPeriod1, expectedPeriod2;

        public TwoWeeksInOnePeriodParameterizedTests(LocalDate initialDate, int daysInPeriod,
                                                     String expectedPeriod1, String expectedPeriod2) {
            this.initialDate = initialDate;
            this.daysInPeriod = daysInPeriod;
            this.expectedPeriod1 = expectedPeriod1;
            this.expectedPeriod2 = expectedPeriod2;
        }

        @Test
        public void test() throws IOException {
            TimetableParser parser = createParser(initialDate, daysInPeriod);
            List<Week> weeks = parser.parseTimetable(getClass().getResourceAsStream(getTimetablePath(0)));
            Assert.assertNotNull(weeks);
            Assert.assertEquals(2, weeks.size());
            Assert.assertEquals(expectedPeriod1, weeks.get(0).period);
            Assert.assertEquals(expectedPeriod2, weeks.get(1).period);
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { LocalDate.parse("2022-09-03"), 30, "01.09.2022-03.09.2022", "03.10.2022-08.10.2022" },
                    { LocalDate.parse("2022-09-04"), 180, "03.10.2022-08.10.2022", "16.01.2023-21.01.2023" }
            };
        }
    }

    private static UdsuVoTimetableParser createParser(LocalDate date, int days) {
        UdsuVoTimetableParser parser = new UdsuVoTimetableParser(days);
        parser.setCurrentDate(date);
        return parser;
    }

    private static String getTimetablePath(@SuppressWarnings("SameParameterValue") int index) {
        return "/timetable-parser/udsu/udsu-" + index + ".xlsx";
    }
}

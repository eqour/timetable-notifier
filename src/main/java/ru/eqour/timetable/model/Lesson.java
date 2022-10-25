package ru.eqour.timetable.model;

import java.util.Objects;

public class Lesson {

    public String time;
    public String discipline;
    public String teacher;
    public String classroom;

    public Lesson() {
    }

    public Lesson(Lesson other) {
        time = other.time;
        discipline = other.discipline;
        teacher = other.teacher;
        classroom = other.classroom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(time, lesson.time) && Objects.equals(discipline, lesson.discipline) && Objects.equals(teacher, lesson.teacher) && Objects.equals(classroom, lesson.classroom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, discipline, teacher, classroom);
    }
}

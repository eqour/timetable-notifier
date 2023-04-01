package ru.eqour.timetable.sender.model;

/**
 * Сообщение.
 */
public class Message {

    private String subject;
    private String text;

    /**
     * Создаёт новый экземпляр класса {@code Message}.
     *
     * @param subject тема сообщения.
     * @param text текст сообщения.
     */
    public Message(String subject, String text) {
        this.subject = subject;
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

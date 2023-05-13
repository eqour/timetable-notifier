package ru.eqour.timetable.sender.model;

import java.util.Objects;

/**
 * Настройки отправителя сообщений по электронной почте.
 */
public class EmailSenderSettings {

    private String host;
    private int port;
    private String username;
    private String password;
    private String protocol;
    private String debug;

    /**
     * Создаёт новый экземпляр класса {@code EmailSenderSettings}.
     *
     * @param host хост сервера отправки сообщений.
     * @param port порт сервера отправки сообщений.
     * @param username имя пользователя.
     * @param password пароль.
     * @param protocol протокол взаимодействия с сервером.
     * @param debug режим отладки ("true" или "false").
     */
    public EmailSenderSettings(String host, int port, String username, String password, String protocol, String debug) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.protocol = protocol;
        this.debug = debug;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailSenderSettings settings = (EmailSenderSettings) o;
        return port == settings.port && host.equals(settings.host) && username.equals(settings.username) && password.equals(settings.password) && protocol.equals(settings.protocol) && debug.equals(settings.debug);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, username, password, protocol, debug);
    }
}

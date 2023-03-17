package ru.eqour.timetable.rest.utils.sender;

import org.springframework.stereotype.Component;
import ru.eqour.timetable.rest.exception.SendMessageException;

@Component
public class VkSender implements MessageSender {

    @Override
    public void sendMessage(String recipient, String title, String message) throws SendMessageException {
        System.out.println("[VK]:\t" + recipient + " => " + "'" + title + "': '" + message + "'");
    }
}

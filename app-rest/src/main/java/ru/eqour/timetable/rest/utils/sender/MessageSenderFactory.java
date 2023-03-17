package ru.eqour.timetable.rest.utils.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.eqour.timetable.rest.exception.MessageSenderNotFoundException;

@Component
public class MessageSenderFactory {

    private ApplicationContext context;

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public MessageSender getById(String id) throws MessageSenderNotFoundException {
        switch (id) {
            case "vk": return context.getBean(VkSender.class);
            case "telegram": return context.getBean(TelegramSender.class);
            default: throw new MessageSenderNotFoundException();
        }
    }
}

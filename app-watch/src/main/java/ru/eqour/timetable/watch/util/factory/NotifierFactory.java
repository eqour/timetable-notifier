package ru.eqour.timetable.watch.util.factory;

import ru.eqour.timetable.sender.MessageSender;
import ru.eqour.timetable.sender.impl.EmailSender;
import ru.eqour.timetable.sender.impl.TelegramSender;
import ru.eqour.timetable.sender.impl.VkSender;
import ru.eqour.timetable.watch.settings.Settings;
import ru.eqour.timetable.watch.model.Subscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * Фабрика классов, отвечающих за отправку уведомлений.
 */
public class NotifierFactory {

    /**
     * Создаёт список отправителей уведомлений для конкретного подписчика.
     *
     * @param subscriber подписчик.
     * @param settings настройки приложения.
     * @return список отправителей уведомлений для конкретного подписчика.
     */
    public static List<MessageSender> createNotifiersForSubscriber(Subscriber subscriber, Settings settings) {
        List<MessageSender> ans = new ArrayList<>();
        if (subscriber.vkId != null) {
            ans.add(new VkSender(settings.vkToken));
        }
        if (subscriber.telegramId != null) {
            ans.add(new TelegramSender(settings.telegramToken));
        }
        if (subscriber.email != null) {
             ans.add(new EmailSender(settings.emailSenderSettings));
        }
        return ans;
    }

    /**
     * Возвращает идентификатор получателя сообщений у подписчика для конкретного уведомителя.
     *
     * @param sender уведомитель.
     * @param subscriber подписчик.
     * @return идентификатор получателя сообщений.
     */
    public static String getRecipientIdForNotifier(MessageSender sender, Subscriber subscriber) {
        if (sender instanceof VkSender) {
            return subscriber.vkId;
        } else if (sender instanceof TelegramSender) {
            return subscriber.telegramId;
        } else if (sender instanceof EmailSender) {
            return subscriber.email;
        }
        throw new IllegalArgumentException();
    }
}

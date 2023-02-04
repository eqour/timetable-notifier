package ru.eqour.timetable.util.factory;

import ru.eqour.timetable.settings.Settings;
import ru.eqour.timetable.model.Subscriber;
import ru.eqour.timetable.notifier.Notifier;
import ru.eqour.timetable.notifier.TelegramNotifier;
import ru.eqour.timetable.notifier.VkNotifier;

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
    public static List<Notifier> createNotifiersForSubscriber(Subscriber subscriber, Settings settings) {
        List<Notifier> ans = new ArrayList<>();
        if (subscriber.vkId != null) {
            ans.add(new VkNotifier(settings.vkToken));
        }
        if (subscriber.telegramId != null) {
            ans.add(new TelegramNotifier(settings.telegramToken));
        }
        return ans;
    }

    /**
     * Возвращает идентификатор получателя сообщений у подписчика для конкретного уведомителя.
     *
     * @param notifier уведомитель.
     * @param subscriber подписчик.
     * @return идентификатор получателя сообщений.
     */
    public static String getRecipientIdForNotifier(Notifier notifier, Subscriber subscriber) {
        if (notifier instanceof VkNotifier) {
            return subscriber.vkId;
        } else if (notifier instanceof TelegramNotifier) {
            return subscriber.telegramId;
        }
        throw new IllegalArgumentException();
    }
}

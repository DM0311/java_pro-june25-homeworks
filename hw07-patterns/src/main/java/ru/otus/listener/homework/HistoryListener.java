package ru.otus.listener.homework;

import java.util.*;
import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {
    private Map<Long, Message> hisory = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        Message.Builder builder = msg.toBuilder();
        List<String> dataCopy = msg.getField13().getData().stream().map(String::valueOf).toList();
        var f13 = new ObjectForMessage();
        f13.setData(dataCopy);
        builder.field13(f13);
        hisory.put(msg.getId(), builder.build());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(hisory.get(id));
    }
}

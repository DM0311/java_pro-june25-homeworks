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
        var data = msg.getField13().getData();
        var dataCopy = Arrays.stream(Arrays.copyOfRange(data.toArray(new String[data.size()]), 0, data.size()))
                .toList();
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

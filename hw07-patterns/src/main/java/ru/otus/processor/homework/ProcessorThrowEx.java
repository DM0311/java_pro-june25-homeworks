package ru.otus.processor.homework;

import java.time.LocalDateTime;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorThrowEx implements Processor {
    @Override
    public Message process(Message message) {
        LocalDateTime now = LocalDateTime.now();
        if (now.getSecond() % 2 == 0) {
            throw new RuntimeException("Even second exception!");
        }
        return message;
    }
}

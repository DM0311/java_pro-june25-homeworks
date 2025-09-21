package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorUpperField10 implements ru.otus.processor.Processor {

    @Override
    public Message process(Message message) {
        return message.toBuilder().field4(message.getField10().toUpperCase()).build();
    }
}

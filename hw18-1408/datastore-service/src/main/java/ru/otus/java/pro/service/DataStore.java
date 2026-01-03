package ru.otus.java.pro.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.java.pro.domain.Message;

public interface DataStore {

    Mono<Message> saveMessage(Message message);

    Flux<Message> loadMessages(String roomId);

    Flux<Message> loadAllMessages();
}

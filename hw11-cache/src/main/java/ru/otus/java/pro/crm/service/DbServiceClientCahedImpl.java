package ru.otus.java.pro.crm.service;

import static ru.otus.java.pro.cachehw.Copy.copy;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.cachehw.HwCache;
import ru.otus.java.pro.core.repository.DataTemplate;
import ru.otus.java.pro.core.sessionmanager.TransactionManager;
import ru.otus.java.pro.crm.model.Client;

public class DbServiceClientCahedImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientCahedImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final HwCache<String, Client> cache;

    public DbServiceClientCahedImpl(
            TransactionManager transactionManager,
            DataTemplate<Client> clientDataTemplate,
            HwCache<String, Client> cache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                var savedClient = clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return savedClient;
            }
            var savedClient = clientDataTemplate.update(session, clientCloned);
            cache.put(String.valueOf(savedClient.getId()), savedClient);
            log.info("updated client: {}", copy(savedClient));
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cachedClient = cache.get(String.valueOf(id));
        if (cachedClient != null) {
            return Optional.of(copy(cachedClient));
        }
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            Client nwClient = clientOptional.get();
            cache.put(String.valueOf(nwClient.getId()), nwClient);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            for (Client cl : clientList) {
                cache.put(String.valueOf(cl.getId()), cl);
            }
            return clientList;
        });
    }
}

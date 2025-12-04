package ru.otus.java.pro.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.cachehw.HwCache;
import ru.otus.java.pro.cachehw.MyCache;
import ru.otus.java.pro.cachehw.MyKeyWrapper;
import ru.otus.java.pro.core.repository.DataTemplateHibernate;
import ru.otus.java.pro.core.repository.HibernateUtils;
import ru.otus.java.pro.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.java.pro.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.java.pro.crm.model.Address;
import ru.otus.java.pro.crm.model.Client;
import ru.otus.java.pro.crm.model.Phone;
import ru.otus.java.pro.crm.service.DbServiceClientCahedImpl;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Phone.class, Address.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        ///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        ///
        HwCache<MyKeyWrapper, Client> cache = new MyCache<>();
        cache.addListener((key, val, action) -> log.info("cache event: {} -> {}", action, key));
        var dbServiceClient = new DbServiceClientCahedImpl(transactionManager, clientTemplate, cache);

        dbServiceClient.saveClient(new Client("dbServiceFirst"));
        var client = dbServiceClient.saveClient(new Client("dbServiceSecond"));

        long t1 = System.nanoTime();
        var selectedClient1 = dbServiceClient
                .getClient(client.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));
        long t2 = System.nanoTime();
        log.info("First read time (empty cache): {} nanos, client: {}", t2 - t1, selectedClient1);
        ///

        long t3 = System.nanoTime();
        var clientSelected2 = dbServiceClient
                .getClient(client.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));
        long t4 = System.nanoTime();
        log.info("Second read time (from cache): {} nanos, client: {}", t4 - t3, clientSelected2);
    }
}

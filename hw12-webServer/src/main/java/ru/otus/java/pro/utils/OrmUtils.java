package ru.otus.java.pro.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.otus.java.pro.core.repository.DataTemplateHibernate;
import ru.otus.java.pro.core.repository.HibernateUtils;
import ru.otus.java.pro.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.java.pro.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.java.pro.crm.model.Client;

public class OrmUtils {
    private Configuration configuration;
    private SessionFactory sessionFactory;
    private TransactionManagerHibernate tm;

    public OrmUtils(String resource) {
        this.configuration = new Configuration().configure(resource);
    }

    public void createSessionFactory(Class<?>... annotatedClasses){
       this.sessionFactory =  HibernateUtils.buildSessionFactory(configuration, annotatedClasses);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void migrateDatabase(){
        new MigrationsExecutorFlyway(getDbUrl(), getDbUserName(), getDbPassword()).executeMigrations();
    }


    private String getDbUrl(){
        return configuration.getProperty("hibernate.connection.url");
    }

    private String getDbUserName(){
        return configuration.getProperty("hibernate.connection.username");
    }

    private String getDbPassword(){
        return configuration.getProperty("hibernate.connection.password");
    }
}

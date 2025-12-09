package ru.otus.java.pro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.URI;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.util.resource.PathResourceFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.otus.java.pro.core.repository.DataTemplateHibernate;
import ru.otus.java.pro.core.repository.HibernateUtils;
import ru.otus.java.pro.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.java.pro.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.java.pro.crm.model.Address;
import ru.otus.java.pro.crm.model.Client;
import ru.otus.java.pro.crm.model.Phone;
import ru.otus.java.pro.crm.service.DBServiceClient;
import ru.otus.java.pro.crm.service.DbServiceClientImpl;
import ru.otus.java.pro.helpers.FileSystemHelper;
import ru.otus.java.pro.server.UsersWebServer;
import ru.otus.java.pro.server.UsersWebServerBasicAuth;
import ru.otus.java.pro.services.TemplateProcessor;
import ru.otus.java.pro.services.TemplateProcessorImpl;

public class WebServer {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        SessionFactory sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Phone.class, Address.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        DBServiceClient dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        String hashLoginServiceConfigPath =
                FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        PathResourceFactory pathResourceFactory = new PathResourceFactory();
        Resource configResource = pathResourceFactory.newResource(URI.create(hashLoginServiceConfigPath));

        LoginService loginService = new HashLoginService(REALM_NAME, configResource);

        UsersWebServer usersWebServer =
                new UsersWebServerBasicAuth(WEB_SERVER_PORT, loginService, dbServiceClient, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}

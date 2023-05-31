package ru.otus.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import ru.otus.web.dao.InMemoryUserDao;
import ru.otus.web.dao.UserDao;
import ru.otus.web.helpers.FileSystemHelper;
import ru.otus.web.server.UsersWebServer;
import ru.otus.web.server.UsersWebServerWithBasicSecurity;
import ru.otus.web.services.TemplateProcessor;
import ru.otus.web.services.TemplateProcessorImpl;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithBasicSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME =
            "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    public static void main(String[] args) throws Exception {
        UserDao userDao = new InMemoryUserDao();
        Gson gson =
                new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor =
                new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath =
                FileSystemHelper.localFileNameOrResourceNameToFullPath(
                        HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService =
                new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);
        //LoginService loginService = new InMemoryLoginServiceImpl(userDao);

        UsersWebServer usersWebServer =
                new UsersWebServerWithBasicSecurity(WEB_SERVER_PORT,
                        loginService, userDao, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}

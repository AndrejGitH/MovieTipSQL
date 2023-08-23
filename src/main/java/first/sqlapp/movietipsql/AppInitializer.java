package first.sqlapp.movietipsql;

import java.io.InputStream;
import java.util.Properties;


//class for loading data from config.properties file with DB logins
public class AppInitializer {
    private String url;
    private String username;
    private String password;

    public AppInitializer() {
        loadConfig();
    }

    //Method for retrieving configuration data from config file
    private void loadConfig() {
        try {
            Properties properties = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
            if (inputStream == null) {
                throw new RuntimeException("config.properties not found in the classpath.");
            }
            properties.load(inputStream);

            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //method for initializing databasehandler from obtained config. data
    public DatabaseHandler initialize() {
        DatabaseHandler dbHandler = null;

        try {
            dbHandler = new DatabaseHandler(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dbHandler;
    }
}


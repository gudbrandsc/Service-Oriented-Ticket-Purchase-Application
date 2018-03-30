
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    private String userhost;
    private String eventhost;
    private String frontendhost;
    private String frontendport;
    private String userport;
    private String eventport;


    public PropertiesLoader() {
        loadProperties();

    }

    private void loadProperties() {
        Properties prop = new Properties();
        InputStream input = null;
        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);
            this.frontendhost = prop.getProperty("frontendhost");
            this.eventhost = prop.getProperty("eventhost");
            this.userhost = prop.getProperty("userhost");
            this.frontendport = prop.getProperty("frontendport");
            this.userport = prop.getProperty("userport");
            this.eventhost = prop.getProperty("eventport");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserhost() {
        return userhost;
    }

    public String getEventhost() {
        return eventhost;
    }

    public String getFrontendhost() {
        return frontendhost;
    }

    public String getFrontendport() {
        return frontendport;
    }

    public String getUserport() {
        return userport;
    }

    public String getEventport() {
        return eventport;
    }
}
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @Author Gudbrand Schistad
 * Class that starts the user service server, and maps all requests to the correct servlet.
 */
public class UserService {
    private static volatile int userid = 1;

    public static void main(String[] args) {

        PropertiesLoader properties = new PropertiesLoader();
        int port = Integer.parseInt(properties.getUserport());

        Server server = new Server(port);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        UserDataMap userDataMap = new UserDataMap();
        handler.addServletWithMapping(new ServletHolder(new UserServiceServlet(userDataMap, userid, properties)), "/*");
        System.out.println("Starting server on port " + port + "...");

        try {
            server.start();
            server.join();
            System.out.println("Exiting...");
        }
        catch (Exception ex) {
            System.out.println("Interrupted while running server.");
            System.exit(-1);
        }
    }
}

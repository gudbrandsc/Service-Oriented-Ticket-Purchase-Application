import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @Author Gudbrand Schistad, Omar Sharif
 * Class that starts the frontend service server, and maps all requests to the correct servlet.
 */
public class FrontendService {

    public static void main(String[] args) {
        PropertiesLoader properties = new PropertiesLoader();
        int port = Integer.parseInt(properties.getFrontendport());

        Server server = new Server(port);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(new ServletHolder(new GetEventsServlet(properties)), "/events");
        handler.addServletWithMapping(new ServletHolder(new EventServlet(properties)), "/events/*");
        handler.addServletWithMapping(new ServletHolder(new UserServlet(properties)), "/users/*");

        System.out.println("Starting server on port " + port + "...");

        try {
            server.start();
            server.join();
        }
        catch (Exception ex) {
            System.out.println("Interrupted while running server.");
            System.exit(-1);
        }
    }
}

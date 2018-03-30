import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 * @Author Gudbrand Schistad, Omar Sharif
 * Class that starts the frontend service server, and maps all requests to the correct servlet.
 */
public class FrontendService {
    private final static int PORT = 4400;

    public static void main(String[] args) {

        Server server = new Server(PORT);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(GetEventsServlet.class, "/events");
        handler.addServletWithMapping(EventServlet.class, "/events/*");
        handler.addServletWithMapping(UserServlet.class, "/users/*");

        System.out.println("Starting server on port " + PORT + "...");

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

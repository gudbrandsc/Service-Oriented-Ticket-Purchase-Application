
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class FrontendService {
    private static int PORT = 5051;

    public static void main(String[] args) {

        Server server = new Server(PORT);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(EventServlet.class, "/events");
        handler.addServletWithMapping(UserServlet.class, "/users/*");

        System.out.println("Starting server on port " + PORT + "...");

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

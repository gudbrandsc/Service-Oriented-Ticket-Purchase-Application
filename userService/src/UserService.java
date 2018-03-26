import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class UserService {
    private static int PORT = 5050;
    private static volatile int userid = 1;

    public static void main(String[] args) {

        Server server = new Server(PORT);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        UserDataMap userDataMap = new UserDataMap();
        handler.addServletWithMapping(new ServletHolder(new RedirectServlet(userDataMap, userid)), "/*");
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

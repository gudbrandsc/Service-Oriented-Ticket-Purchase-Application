import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;


public class UserService {
    protected static Logger log = LogManager.getLogger();
    private static int PORT = 8080;

    public static void main(String[] args) {

        Server server = new Server(PORT);
        ServletContextHandler context = new ServletContextHandler();

        context.addServlet(LoginUserServlet.class,     "/login");
        context.addServlet(LoginRegisterServlet.class, "/register");
        context.addServlet(HotelsDisplayServlet.class,  "/viewhotels");
        context.addServlet(AllReviewsServlet.class,  "/allreviews");
        context.addServlet(UserReviewsServlet.class,  "/myreviews");
        context.addServlet(AddReviewServlet.class,  "/addreview");
        context.addServlet(EditReviewServlet.class,  "/editreview");
        context.addServlet(HotelAttractionsServlet.class,  "/attractions");
        context.addServlet(LoginRedirectServlet.class, "/*");


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

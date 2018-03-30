// Omar Sharif, Event Server

import java.util.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * This class has the main method which uses jetty/servlets to start the server for the Event
 * Service. The port is 4450 and the node is mc09. When creating this class I referenced my
 * Project 6 from 601.
 * @author Omar Sharif
 */
public class EventMain {
    private static int PORT = 4450;

    public static void main(String[] args) throws Exception{
        Server server = new Server(PORT);
        ServletHandler handler = new ServletHandler();

        EventList events = new EventList();

        EventData event = new EventData(9542, "Rock Concert", 4);
        events.addToList(event);

        handler.addServletWithMapping(new ServletHolder(new EventServlet(events)), "/*");
        handler.addServletWithMapping(new ServletHolder(new ListServlet(events)), "/list");
        handler.addServletWithMapping(new ServletHolder(new CreateServlet(events)), "/create");
        handler.addServletWithMapping(new ServletHolder(new PurchaseServlet(events)), "/purchase/*");

        server.setHandler(handler);
        server.start();
        server.join();
    }

}

// Omar Sharif, Event Server

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class EventMain {
    private static int PORT = 7045;

    public static void main(String[] args) throws Exception{
        Server server = new Server(PORT);
        ServletHandler handler = new ServletHandler();

        handler.addServletWithMapping(new ServletHolder(new ListServlet()), "/list");

        server.setHandler(handler);

        server.start();
        server.join();
    }

}

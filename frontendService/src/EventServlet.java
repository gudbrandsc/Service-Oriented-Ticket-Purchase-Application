
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class EventServlet extends HttpServlet{
    private String userServiceURL;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
      //  String transferTicketPattern = "\\/([a-zA-Z1-9]*)\\/tickets\\/transfer";
      //  Pattern transfer = Pattern.compile(transferTicketPattern);
        //Matcher matchTransfer = transfer.matcher(pathInfo);




        System.out.println("with more");
    }
}

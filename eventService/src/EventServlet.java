import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.simple.JSONObject;

/**
 * This class reads in an event id, checks if it is valid, and returns eventid, event name,
 * user id, tickets available, and tickets purhased info in json format with proper status code.
 */
public class EventServlet extends HttpServlet{
    private EventList eventList;

    public EventServlet(EventList list){
        this.eventList = list;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        //response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter(); // going to print entire json file on the page

        String path = request.getPathInfo();
        //System.out.println(path);
        path = path.substring(1);
        int eventid = Integer.parseInt(path);

        if(eventList.eventExists(eventid)){ // Return 200 with proper message
            response.setStatus(HttpServletResponse.SC_OK);
            EventData event = eventList.getEvent(eventid);
            JSONObject object = validJSON(event);

            out.println(object);
        }
        else{ // return 400
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            //out.println("Event not found");
        }
    }

    public JSONObject validJSON(EventData event){
        JSONObject object = new JSONObject();

        object.put("eventid", event.getEventId());
        object.put("eventname", event.getEventName());
        object.put("userid", event.getUserId());
        object.put("avail", event.getTicketsAvailable());
        object.put("purchased", event.getTicketsPurchased());

        return object;
    }

}
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.simple.JSONObject;

public class EventServlet extends HttpServlet{
    private EventList eventList;

    public EventServlet(EventList list){
        this.eventList = list;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter(); // going to print entire json file on the page

        String path = request.getPathInfo();
        path = path.substring(1);
        String eventid = path;

        if(eventList.eventExists(eventid)){
            EventData event = eventList.getEvent(eventid);
            JSONObject object = validJSON(event);

            out.println(object);
        }
        else{
            out.println("Event not found");
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

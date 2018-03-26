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
        eventList = list;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter(); // going to print entire json file on the page

        String eventid = request.getParameter("eventid");
        System.out.println(eventid);

        JSONObject object = new JSONObject();
        object.put("eventid1", "12525");
        object.put("eventname", "Rock Concert");
        object.put("userid", "532");
        object.put("avail", "4");
        object.put("purchased", "356");
        out.println(object);
        /*
        if(eventid == null || !eventList.eventExists(eventid)){ // if hotelId is invalid
            //JSONObject object = invalidJSON();
            out.println("Event not found");
        }
        else{ // if eventid is valid
            EventData event = eventList.getEvent(eventid);

            JSONObject object = new JSONObject();

            out.println(object);
        }
        */
    }

    public JSONObject invalidJSON(){
        JSONObject object = new JSONObject();

        object.put("400", "Event not found");

        return object;
    }

    public JSONObject validJSON(EventData event){
        JSONObject object = new JSONObject();

        object.put("eventid1", "12525");
        object.put("eventname", "Rock Concert");
        object.put("userid", "532");
        object.put("avail", "4");
        object.put("purchased", "356");

        /*
        object.put("eventid", event.getEventId());
        object.put("eventname", event.getEventName());
        object.put("userid", event.getUserId());
        object.put("avail", event.getTicketsAvailable());
        object.put("purchased", event.getTicketsPurchased());
        */

        return object;
    }

}

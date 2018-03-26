import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ListServlet extends HttpServlet{
    private EventList eventList;

    public ListServlet(EventList list){
        eventList = list;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        JSONArray jsonArray = createJSONArray();

        out.println(jsonArray);
    }

    public JSONArray createJSONArray(){
        JSONArray array = new JSONArray();
        ArrayList<EventData> events = eventList.getList();

        for(EventData event: events){
            JSONObject object = new JSONObject();

            object.put("eventid", event.getEventId());
            object.put("eventname", event.getEventName());
            object.put("userid", event.getUserId());
            object.put("avail", event.getTicketsAvailable());
            object.put("purchased", event.getTicketsPurchased());

            array.add(object);
        }

        return array;
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

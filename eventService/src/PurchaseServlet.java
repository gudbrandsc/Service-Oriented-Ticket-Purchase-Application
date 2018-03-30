import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class handles the purchases of tickets, checks if eventid is valid, userid is valid,
 * and if tickets are able to be purchased. Either returns status code of 200 or 400.
 */
public class PurchaseServlet extends HttpServlet{
    private EventList eventList;
    private String hostAndPort = "mc08:4444";

    public PurchaseServlet(EventList list) {
        eventList = list;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        String path = request.getPathInfo();
        path = path.substring(1);
        int urlEventId = Integer.parseInt(path);

        BufferedReader jsonInput = request.getReader();
        StringBuilder builder = new StringBuilder();
        String line;

        // read one line of the post request message at a time
        while ((line = jsonInput.readLine()) != null) {
            builder.append(line);
        }

        // build string version of json object
        String jsonString = builder.toString();
        //System.out.println(jsonString);

        JSONParser parser = new JSONParser();
        JSONObject object;
        int userid = 0, eventid = 0, tickets = 0;

        try {
            object = (JSONObject) parser.parse(jsonString);
            userid = (int)Long.parseLong(object.get("userid").toString());
            eventid = (int) Long.parseLong(object.get("eventid").toString());
            tickets = (int)Long.parseLong(object.get("tickets").toString());
            System.out.println(userid);
            System.out.println(eventid);
            System.out.println(tickets);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(!eventList.eventExists(eventid) || !eventList.eventExists(urlEventId)){
            // if you try to purchase tickets for an event that doesn't exist
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // set status to 400
            System.out.println("event id not found");
        }
        else{ // subtract tickets, check if user id exists, if you get 200 back keep it, else
            // change the tickets back
            //System.out.println("event exists");
            EventData event = eventList.getEvent(eventid);
            boolean purchased = event.purchaseTicket(tickets);

            if(purchased){
                // if you can purchase tickets, subtract, and check if user id exists
                if(checkUseridExists(userid, hostAndPort)){
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else{
                    // if user doesn't exist, add tickets back to event
                    event.addTicketsBack(tickets);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    System.out.println("user doesnt exist");
                }
            }
            else{ // if you cannot purchase tickets
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                System.out.println("cant purchase tickets");
            }
        }

    }

    public boolean checkUseridExists(int userId, String hostAndPort) throws IOException{
        String url = "http://" + hostAndPort + "/";
        url = url.concat(String.valueOf(userId));

        URL objUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) objUrl.openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        return (responseCode == 200);
    }


}

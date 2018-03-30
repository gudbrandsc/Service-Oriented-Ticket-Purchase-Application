import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class handles the purchases of tickets, checks if eventid is valid, userid is valid,
 * and if enough tickets are available to be purchased. Either returns status code of 200 or 400.
 */
public class PurchaseServlet extends HttpServlet{
    private EventList eventList;
    private String userHOST = "mc08";
    private String userPort = "4444";

    public PurchaseServlet(EventList list) {
        eventList = list;
    }

    /**
     * This method processes a POST request sent from the front end to purchase a ticket.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
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

        JSONParser parser = new JSONParser();
        JSONObject object;
        int userid = 0, eventid = 0, tickets = 0;

        try {
            object = (JSONObject) parser.parse(jsonString);
            userid = (int)Long.parseLong(object.get("userid").toString());
            eventid = (int) Long.parseLong(object.get("eventid").toString());
            tickets = (int)Long.parseLong(object.get("tickets").toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // check if event exists
        if(!eventList.eventExists(eventid) || !eventList.eventExists(urlEventId)){
            // if you try to purchase tickets for an event that doesn't exist
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // set status to 400
        }
        else{ // subtract tickets, check if user id exists, if you get 200 back keep it, else
            // change the tickets back
            EventData event = eventList.getEvent(eventid);
            boolean purchased = event.purchaseTicket(tickets);

            if(purchased){
                // if you can purchase tickets, subtract, and check if user id exists
                if(checkUseridExists(eventid, userid, tickets)){
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else{
                    // if user doesn't exist, add tickets back to event
                    event.addTicketsBack(tickets);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
            else{ // if you cannot purchase tickets
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }

    }

    /**
     * This class takes in eventid, userid, and tickets as the parameter. It takes these parameters
     * and creates a JSONObject. It sends a POST request with this JSON object as part of the body
     * to the user service to verify if user id is valid. Returns true if it is, false if it
     * isn't. This is based on status code we get from user service.
     * @param eventId
     * @param userId
     * @param tickets
     * @return
     * @throws IOException
     */
    public boolean checkUseridExists(int eventId, int userId, int tickets) throws IOException{
        String url = "http://" + userHOST + ":" + userPort + "/";
        url = url.concat(String.valueOf(userId));
        url = url.concat("/tickets/add");

        URL objUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) objUrl.openConnection();

        connection.setRequestMethod("POST");
        // must be set to true if you want to send POST request
        connection.setDoOutput(true);
        // setting application/json
        connection.setRequestProperty("Content-type", "application/json");

        JSONObject jsonObject = createJSON(eventId, tickets);

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(jsonObject.toString());
        writer.flush();

        int responseCode = connection.getResponseCode();

        return (responseCode == 200);
    }

    public JSONObject createJSON(int eventid, int tickets){
        JSONObject object = new JSONObject();

        object.put("tickets", tickets);
        object.put("eventid", eventid);

        return object;
    }

}

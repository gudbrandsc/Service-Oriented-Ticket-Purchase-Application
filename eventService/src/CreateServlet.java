import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class processes a POST request to create a new event given a json file with userid, event
 * name, and tickets. It parses json to get userid, checks if userid is valid, then creates a new
 * event and adds it to list. Returns status code 200 or 400.
 */
public class CreateServlet extends HttpServlet {
    private EventList eventList;
    private String USERHOST = "mc08";
    private String USERPORT = "4444";

    public CreateServlet(EventList list) {
        eventList = list;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        BufferedReader jsonInput = request.getReader();
        StringBuilder builder = new StringBuilder();
        String line;

        while ((line = jsonInput.readLine()) != null) {
            builder.append(line);
        }

        String jsonString = builder.toString();

        JSONParser parser = new JSONParser();
        JSONObject object;

        int userid = 0, numtickets = 0;
        String eventname = "";

        try {
            object = (JSONObject) parser.parse(jsonString);
            userid = (int)Long.parseLong((object.get("userid").toString()));
            eventname = object.get("eventname").toString();
            numtickets = (int)Long.parseLong((object.get("numtickets").toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(numtickets < 0 || eventname.equals("")){ // check if number of tickets is below 0
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else {
            // checks if userid is valid
            int statusCode = checkUserId(userid);

            if (statusCode == 200) { // set status to 200, create a new event, and add it to list
                response.setStatus(HttpServletResponse.SC_OK);

                EventData event = new EventData(userid, eventname, numtickets);

                // output the json with eventid
                int eventId = event.getEventId();
                JSONObject object1 = new JSONObject();
                object1.put("eventid", eventId);
                out.println(object1);

                eventList.addToList(event);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    /**
     * This class forwards a GET request to the user service to check if userid exists, returns
     * status code.
     * @param userId
     * @return
     * @throws IOException
     */
    public int checkUserId(int userId) throws IOException{
        String url = "http://" + USERHOST + ":" + USERPORT + "/";
        url = url.concat(String.valueOf(userId));

        URL objUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) objUrl.openConnection();

        // set request method to GET
        connection.setRequestMethod("GET");

        int statusCode = connection.getResponseCode();

        return statusCode;
    }

}

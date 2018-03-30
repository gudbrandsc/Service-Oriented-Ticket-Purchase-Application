import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private String hostAndPort = "mc08:4444";

    public CreateServlet(EventList list) {
        eventList = list;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        BufferedReader jsonInput = request.getReader();
        StringBuilder builder = new StringBuilder();
        String line;

        while ((line = jsonInput.readLine()) != null) {
            builder.append(line);
        }

        String jsonString = builder.toString();
        System.out.println(jsonString);

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

        // checks if userid is valid
        int statusCode = checkUserId(userid, hostAndPort);
        System.out.println("Status code = " + statusCode);

        if(statusCode == 200){ // set status to 200, create a new event, and add it to list
            response.setStatus(HttpServletResponse.SC_OK);

            EventData event = new EventData(userid, eventname, numtickets);
            eventList.addToList(event);

        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * This class forwards a GET request to the user service to check if userid exists, returns
     * status code.
     * @param userId
     * @return
     * @throws IOException
     */
    public int checkUserId(int userId, String hostAndPort) throws IOException{
        //String url = "http://localhost:5050/";
        String url = "http://" + hostAndPort + "/";
        url = url.concat(String.valueOf(userId));
        System.out.println("Url = " + url);

        URL objUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) objUrl.openConnection();

        // optional default is GET
        connection.setRequestMethod("GET");

        //add request header
        int statusCode = connection.getResponseCode();

        // reading in json
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = reader.readLine()) != null){
            response.append(inputLine);
        }

        reader.close();

        return statusCode;
    }

}

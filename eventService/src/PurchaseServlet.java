import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PurchaseServlet extends HttpServlet{
    private EventList eventList;

    public PurchaseServlet(EventList list) {
        eventList = list;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        String path = request.getPathInfo();
        path = path.substring(1);
        //System.out.println(path);

        BufferedReader jsonInput = request.getReader();
        StringBuilder builder = new StringBuilder();
        String line;

        // read one line of the post request message at a time
        while ((line = jsonInput.readLine()) != null) {
            builder.append(line);
        }

        // build string version of json object
        String jsonString = builder.toString();
        System.out.println(jsonString);

        JSONParser parser = new JSONParser();
        JSONObject object;
        int userid = 0, eventid = 0, tickets = 0;

        try {
            //parser.parse(jsonString);
            object = (JSONObject) parser.parse(jsonString);
            userid = (int)object.get("userid");
            eventid = (int)object.get("eventid");
            tickets = (int)object.get("tickets");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(!eventList.eventExists(eventid)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // set status to 400

            EventData event = eventList.getEvent(eventid);
            String eventName = event.getEventName();

            EventData newEvent = new EventData(userid, eventName, tickets);
            eventList.addToList(newEvent);
        }
        else{ // set status to 200,
            response.setStatus(HttpServletResponse.SC_OK);


        }

        //System.out.println(userid);
        //System.out.println(eventid);
        //System.out.println(tickets);

        int statusCode = checkUserId(userid);
        //System.out.println("Status code = " + statusCode);
    }

    public int checkUserId(long userId) throws IOException{
        String url = "http://localhost:5050/1";
        URL objUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) objUrl.openConnection();

        // optional default is GET
        connection.setRequestMethod("GET");

        //add request header
        int responseCode = connection.getResponseCode();
        System.out.println("Sending 'GET' request to URL : "+url);
        System.out.println("Response Code : "+ responseCode);
        System.out.println("Response message = " + connection.getResponseMessage());

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }

        in.close();
        //print result
        System.out.println(response.toString());

        return responseCode;
    }

}

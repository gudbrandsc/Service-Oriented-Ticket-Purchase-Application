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

public class CreateServlet extends HttpServlet {
    private EventList eventList;

    public CreateServlet(EventList list) {
        eventList = list;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        //response.setStatus(HttpServletResponse.SC_OK);

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
        String userid = "", eventname = "", numtickets = "";

        try {
            //parser.parse(jsonString);
            object = (JSONObject) parser.parse(jsonString);
            userid = object.get("userid").toString();
            eventname = object.get("eventname").toString();
            numtickets = object.get("numtickets").toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(userid);
        System.out.println(eventname);
        System.out.println(numtickets);

        int statusCode = checkUserId(userid);
        System.out.println("Status code = " + statusCode);
    }

    /**
     * This class forwards a GET request to the user service to check if userid exists, returns
     * status code.
     * @param userId
     * @return
     * @throws IOException
     */
    public int checkUserId(String userId) throws IOException{
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

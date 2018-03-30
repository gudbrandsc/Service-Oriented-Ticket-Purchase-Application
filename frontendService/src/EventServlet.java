
import org.eclipse.jetty.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EventServlet extends HttpServlet{
    private final String EVENTHOST = "mc09";
    private final String EVENTPORT = "4450";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String eventid = req.getPathInfo().substring(1);

        PrintWriter printWriter = resp.getWriter();
        String transferTicketPattern = "([0-9]*)";
        Pattern transfer = Pattern.compile(transferTicketPattern);
        Matcher matchTransfer = transfer.matcher(eventid);

        JSONObject event;
        if(matchTransfer.matches()){
            event = sendGetRequest(EVENTHOST, EVENTPORT, matchTransfer.group(1), resp);
            printWriter.println(event);
        }else {
            resp.setStatus(HttpStatus.BAD_REQUEST_400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String regex = "\\/([0-9]*)\\/purchase\\/([0-9]*)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pathInfo);
        System.out.println("post path: " + pathInfo);

        if(pathInfo.equals("/create")){
            // Remove / from path
            String path = pathInfo.substring(1);
            sendPostRequest(EVENTHOST, EVENTPORT, path, resp, req);
        }else if(m.matches()){
            System.out.println("path match send post");
           int  eventid = Integer.parseInt(m.group(1));
           int  userid = Integer.parseInt(m.group(2));
           String path = "purchase/" + eventid;
           JSONObject object = new JSONObject();
           JSONObject reqObj = stringToJsonObject(requestToString(req));
           object.put("tickets", reqObj.get("tickets"));
           object.put("userid", userid);
           object.put("eventid", eventid);
           sendPostRequest(EVENTHOST, EVENTPORT, path, resp, object.toString());
        }else{

            resp.setStatus(HttpStatus.BAD_REQUEST_400);
        }
    }

    private String requestToString(HttpServletRequest request) throws IOException {
        StringBuffer sb = new StringBuffer();
        String line;

        BufferedReader in = request.getReader();

        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        String res = sb.toString();
        in.close();
        return res;
    }

    private JSONObject stringToJsonObject(String json){
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        try {
            obj = (JSONObject)parser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private String readInputStream(HttpURLConnection con) throws IOException {
        StringBuffer response = new StringBuffer();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }


    private JSONObject sendGetRequest(String host, String port, String path, HttpServletResponse resp) throws IOException {
        String url = "http://" + host + ":" + port + "/" + path;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        resp.setStatus(responseCode);
        System.out.println("got status: " + responseCode );
        //Remove this so you only read input if its status ok. Then move this over to the get --- may return connection
        if (responseCode == 200) {
            String response = readInputStream(con);
            System.out.println("Return json string: " + response + "\n");
            return stringToJsonObject(response);
        }
        return null;
    }


    private void sendPostRequest(String host, String port, String path, HttpServletResponse resp, HttpServletRequest req) throws IOException {
        String url = "http://" + host + ":" + port + "/" + path;
        System.out.println("Sent get request to: "+ url);

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type", "application/json");
        OutputStreamWriter wr =  new OutputStreamWriter(con.getOutputStream());
        wr.write(requestToString(req));
        wr.flush();
        System.out.println("resp: " + con.getResponseCode());

        resp.setStatus(con.getResponseCode());
    }
    private void sendPostRequest(String host, String port, String path, HttpServletResponse resp, String json) throws IOException {
        String url = "http://" + host + ":" + port + "/" + path;
        System.out.println("Sent post request with string url = "+ url);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type", "application/json");
        OutputStreamWriter wr =  new OutputStreamWriter(con.getOutputStream());
        wr.write(json);
        wr.flush();
        resp.setStatus(con.getResponseCode());
    }

}

import org.eclipse.jetty.http.HttpStatus;
import org.json.simple.JSONArray;
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
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserServlet extends HttpServlet{
    private final String HOST = "localhost";
    private final String USERPORT = "5050";
    private final String EVENTPORT = "7050";



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();
        JSONObject userObject = sendGetRequest(HOST, USERPORT, req.getPathInfo().substring(1), resp);
        if(userObject == null){
            //If user does not exist
            resp.setStatus(HttpStatus.BAD_REQUEST_400);
        }else {
            JSONObject json = new JSONObject();
            json.put("userid", userObject.get("userid"));
            json.put("username", userObject.get("username"));

            JSONArray eventarray = (JSONArray) userObject.get("tickets");
            JSONArray updatedEventarray = new JSONArray();

            Iterator<JSONObject> iterator = eventarray.iterator();
            while (iterator.hasNext()) {
                JSONObject res = iterator.next();
                long eventid = (long) res.get("eventid");

                JSONObject eventObject = sendGetRequest(HOST, EVENTPORT, String.valueOf(eventid), resp);
                if (eventObject != null) {
                    updatedEventarray.add(eventObject);
                }
            }
            json.put("tickets", updatedEventarray);
            resp.setContentType("application/json; charset=UTF-8");
            resp.setStatus(HttpStatus.OK_200);
            printWriter.println(json.toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        String transferTicketPattern = "\\/([a-zA-Z1-9]*)\\/tickets\\/transfer";
        Pattern transfer = Pattern.compile(transferTicketPattern);
        Matcher matchTransfer = transfer.matcher(pathInfo);

        if(pathInfo.equals("/create")){
            String path =  "create";
            sendPostRequest(HOST, USERPORT, path, resp, req);
        }else if(matchTransfer.matches()){
            String path =  matchTransfer.group(1) + "/tickets/transfer";
            sendPostRequest(HOST, USERPORT, path, resp, req);
        }else{
            resp.setStatus(HttpStatus.BAD_REQUEST_400);
        }
    }

    private JSONObject stringToJsonObject(HttpServletRequest request){
        JSONObject obj = null;
        try {
            BufferedReader in = request.getReader();
            String line;

            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            String res = sb.toString();
            in.close();

            JSONParser parser = new JSONParser();
            obj = (JSONObject)parser.parse(res);

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return obj;
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

        //Remove this so you only read input if its status ok. Then move this over to the get --- may return connection
        if (responseCode == 200) {
            String response = readInputStream(con);
            return stringToJsonObject(response.toString());
        }
        return null;
    }

    private void sendPostRequest(String host, String port, String path, HttpServletResponse resp, HttpServletRequest req) throws IOException {
        String url = "http://" + host + ":" + port + "/" + path;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type", "application/json");
        JSONObject object = stringToJsonObject(req);
        OutputStreamWriter wr =  new OutputStreamWriter(con.getOutputStream());
        wr.write(object.toString());
        wr.flush();
        resp.setStatus(con.getResponseCode());
    }

}


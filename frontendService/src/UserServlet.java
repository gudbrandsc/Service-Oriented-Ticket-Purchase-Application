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
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserServlet extends HttpServlet{
    private final String HOST = "localhost";
    private final String USERPORT = "5050";
    private final String EVENTPORT = "7050";



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();
        JSONObject userObject = sendGetRequest(HOST, USERPORT, req.getPathInfo().substring(1),resp);
        if(userObject == null){
            resp.setStatus(HttpStatus.BAD_REQUEST_400);
            printWriter.println("user do not exist");
        }else {
            JSONObject json = new JSONObject();
            json.put("userid", userObject.get("userid"));
            json.put("username", userObject.get("username"));

            JSONArray eventarray = (JSONArray) userObject.get("tickets");
            JSONArray updatedEventarray = new JSONArray();

            System.out.println(eventarray);
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
        PrintWriter printWriter = resp.getWriter();

        String pathInfo = req.getPathInfo();
        String transferTicketPattern = "\\/([a-zA-Z1-9]*)\\/tickets\\/transfer";
        Pattern transfer = Pattern.compile(transferTicketPattern);
        Matcher matchTransfer = transfer.matcher(pathInfo);

        if(pathInfo.equals("/create")){
            String url = "http://" + HOST + ":" + USERPORT + "/create";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty( "Content-type", "application/json");
            con.setRequestProperty("Content-type", "application/json");
            JSONObject object = getJsonObject(req);

            OutputStreamWriter wr =  new OutputStreamWriter(con.getOutputStream());
            wr.write(object.toString());
            wr.flush();
            int responseCode = con.getResponseCode();
            StringBuffer response = new StringBuffer();

            if(responseCode != 200){
                resp.setStatus(responseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }else {
                resp.setStatus(responseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
            printWriter.println(response.toString());

        }else if(matchTransfer.matches()){



            String url = "http://" + HOST + ":" + USERPORT + "/" + matchTransfer.group(1) + "/tickets/transfer";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty( "Content-type", "application/json");
            con.setRequestProperty("Content-type", "application/json");
            JSONObject object = getJsonObject(req);

            OutputStreamWriter wr =  new OutputStreamWriter(con.getOutputStream());
            wr.write(object.toString());
            wr.flush();

            int responseCode = con.getResponseCode();
            StringBuffer response = new StringBuffer();

            if(responseCode != 200){
                resp.setStatus(responseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }else {
                resp.setStatus(responseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
            printWriter.println(response.toString());
        }else{
            resp.setStatus(400);
            printWriter.println("Invalid request");
        }



    }

    private JSONObject getJsonObject(HttpServletRequest request){
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

    private JSONObject getJsonObject(String json){
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        try {
            obj = (JSONObject)parser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private JSONObject sendGetRequest(String host, String port, String pathInfo, HttpServletResponse resp) throws IOException {
        String url = "http://" + host + ":" + port + "/" + pathInfo;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        StringBuffer response = new StringBuffer();


        //Remove this so you only read input if its status ok. Then move this over to the get --- may return connection
        if (responseCode != 200) {
            resp.setStatus(responseCode);
           return null;
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            resp.setStatus(responseCode);
            in.close();
        }
        return getJsonObject(response.toString());
    }

}


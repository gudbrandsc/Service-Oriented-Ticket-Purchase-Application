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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserServlet extends HttpServlet{
    private final String HOST = "localhost";
    private final String USERPORT = "5050";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = "http://" + HOST + ":" + USERPORT + "/" + req.getPathInfo().substring(1);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        PrintWriter printWriter = resp.getWriter();
        StringBuffer response = new StringBuffer();

        if (responseCode != 200) {
            resp.setStatus(responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } else{
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            resp.setStatus(responseCode);
            in.close();
        }
        printWriter.println(response.toString());

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
        Map<String,String> jsonMap = new HashMap<String, String>();
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

}


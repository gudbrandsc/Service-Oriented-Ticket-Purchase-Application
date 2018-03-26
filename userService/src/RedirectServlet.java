import org.eclipse.jetty.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedirectServlet extends HttpServlet{
    private UserDataMap userDataMap;
    private static volatile int userid;

    public RedirectServlet(UserDataMap userDataMap, int userid) {
        this.userDataMap = userDataMap;
        this.userid = userid;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        PrintWriter out = response.getWriter();
        String isInt = "[0-9]+";
        String pathValue = request.getPathInfo().substring(1);
        Pattern pattern = Pattern.compile(isInt);
        if(pattern.matcher(pathValue).matches()) {
            int userId = Integer.parseInt(pathValue);
            if (userDataMap.checkIfUserExist(userId)) {
                User user = userDataMap.getUser(userId);
                String username = user.getUsername();
                JSONObject json = new JSONObject();
                JSONArray eventarray = new JSONArray();
                json.put("userid", userId);
                json.put("username", username);

                if (user.getNumEventsSize() > 0) {
                    for (int i : user.getEvents().keySet()) {
                        JSONObject item = new JSONObject();
                        item.put("eventid", i);
                        eventarray.add(item);
                    }
                }
                json.put("tickets", eventarray);
                out.println("User details");
                out.println(json);
                response.setStatus(HttpStatus.OK_200);
            } else {
                out.println("User not found");
                response.setStatus(HttpStatus.BAD_REQUEST_400);
            }
        }else{
            out.println("User not found");
            response.setStatus(HttpStatus.BAD_REQUEST_400);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter printWriter = response.getWriter();
        String addTicketPattern = "\\/([a-zA-Z1-9]*)\\/tickets\\/add";
        String transferTicketPattern = "\\/([a-zA-Z1-9]*)\\/tickets\\/transfer";
        String uri = request.getRequestURI();

        Pattern add = Pattern.compile(addTicketPattern);
        Pattern transfer = Pattern.compile(transferTicketPattern);

        Matcher matchAdd = add.matcher(uri);
        Matcher matchTransfer = transfer.matcher(uri);

        if (matchAdd.matches()) {
            JSONObject requestBody = getJsonObject(request);
            if(requestBody.containsKey("eventid") && requestBody.containsKey("tickets")){
                int userId = Integer.parseInt(matchAdd.group(1));
                int eventid = Integer.parseInt(requestBody.get("eventid").toString());
                int tickets = Integer.parseInt(requestBody.get("tickets").toString());
                userDataMap.getUser(userId).addTickets(eventid, tickets);
                response.setStatus(HttpStatus.OK_200);
                printWriter.println("Event tickets added");
            }else{
                response.setStatus(HttpStatus.BAD_REQUEST_400);
                printWriter.println("Tickets could not be added");
            }
        }else if(matchTransfer.matches()){
            JSONObject requestBody = getJsonObject(request);
            if(requestBody.containsKey("eventid") && requestBody.containsKey("tickets") && requestBody.containsKey("targetuser")){
                int userId = Integer.parseInt(matchTransfer.group(1));
                int eventid = Integer.parseInt(requestBody.get("eventid").toString());
                int tickets = Integer.parseInt(requestBody.get("tickets").toString());
                int targetuser = Integer.parseInt(requestBody.get("targetuser").toString());
                if(userDataMap.checkIfUserExist(targetuser) && userDataMap.checkIfUserExist(userId)) {
                    if (userDataMap.getUser(userId).validateNumTickets(eventid, tickets)) {
                        userDataMap.getUser(userId).removeTickets(eventid, tickets);
                        userDataMap.getUser(targetuser).addTickets(eventid, tickets);
                        response.setStatus(HttpStatus.OK_200);
                        printWriter.println("Event tickets transfered");

                    } else {
                        response.setStatus(HttpStatus.BAD_REQUEST_400);
                        printWriter.println("Tickets could not be transfered");
                    }
                }else {
                    response.setStatus(HttpStatus.BAD_REQUEST_400);
                    printWriter.println("Tickets could not be transfered");
                }
            }else {
                response.setStatus(HttpStatus.BAD_REQUEST_400);
                printWriter.println("Tickets could not be transfered");
            }
        }else if(request.getRequestURI().equals("/create")) {
            JSONObject requestBody = getJsonObject(request);
            if(requestBody.containsKey("username")){
                String username = requestBody.get("username").toString();
                User user = new User(userid, username);
                this.userDataMap.addUser(userid, user);
                response.setStatus(HttpStatus.OK_200);
                JSONObject respJSON = new JSONObject();
                respJSON.put("userid",userid);
                printWriter.println("User created\n");
                printWriter.println(respJSON.toString());
                this.userid++;
            } else{
                response.setStatus(HttpStatus.BAD_REQUEST_400);
            }
        }else{
            response.setStatus(HttpStatus.BAD_REQUEST_400);
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

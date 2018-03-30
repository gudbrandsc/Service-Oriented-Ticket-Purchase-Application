import org.eclipse.jetty.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserServiceServlet extends HttpServlet{
    private UserDataMap userDataMap;
    private static volatile int userid;

    public UserServiceServlet(UserDataMap userDataMap, int userid) {
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
                out.println(json);
                out.flush();
                response.setStatus(HttpStatus.OK_200);
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST_400);
            }
        }else{
            response.setStatus(HttpStatus.BAD_REQUEST_400);
        }
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter printWriter = response.getWriter();
        String addTicketPattern = "\\/([0-9]*)\\/tickets\\/add";
        String transferTicketPattern = "\\/([0-9]*)\\/tickets\\/transfer";
        String uri = request.getRequestURI();
        Pattern add = Pattern.compile(addTicketPattern);
        Pattern transfer = Pattern.compile(transferTicketPattern);

        Matcher matchAdd = add.matcher(uri);
        Matcher matchTransfer = transfer.matcher(uri);
        JSONObject requestBody = stringToJsonObject(requestToString(request));

        if (matchAdd.matches()) {
            if(requestBody.containsKey("eventid") && requestBody.containsKey("tickets")){
                int userId = Integer.parseInt(matchAdd.group(1));
                int eventid = Integer.parseInt(requestBody.get("eventid").toString());
                int tickets = Integer.parseInt(requestBody.get("tickets").toString());
                if(userDataMap.checkIfUserExist(userId)) {
                    userDataMap.getUser(userId).addTickets(eventid, tickets);
                    response.setStatus(HttpStatus.OK_200);
                }else{
                    response.setStatus(HttpStatus.BAD_REQUEST_400);
                }
            }else{
                response.setStatus(HttpStatus.BAD_REQUEST_400);
            }
        }else if(matchTransfer.matches()){
            if(requestBody.containsKey("eventid") && requestBody.containsKey("tickets") && requestBody.containsKey("targetuser")){
                int userId = Integer.parseInt(matchTransfer.group(1));
                int eventid = Integer.parseInt(requestBody.get("eventid").toString());
                int tickets = Integer.parseInt(requestBody.get("tickets").toString());
                int targetuser = Integer.parseInt(requestBody.get("targetuser").toString());
                if(userDataMap.checkIfUserExist(targetuser) && userDataMap.checkIfUserExist(userId)) {
                    if (transferTickets(eventid, userId, targetuser, tickets)) {
                        response.setStatus(HttpStatus.OK_200);
                    } else {
                        response.setStatus(HttpStatus.BAD_REQUEST_400);
                    }
                }else {
                    response.setStatus(HttpStatus.BAD_REQUEST_400);
                }
            }else {
                response.setStatus(HttpStatus.BAD_REQUEST_400);
            }
        }else if(request.getRequestURI().equals("/create")) {
            if(requestBody.containsKey("username")){
                String username = requestBody.get("username").toString();
                User user = new User(userid, username);
                this.userDataMap.addUser(userid, user);
                response.setStatus(HttpStatus.OK_200);
                JSONObject respJSON = new JSONObject();
                respJSON.put("userid", userid);
                printWriter.println(respJSON.toString());
                printWriter.flush();
                this.userid++;
            } else{
                response.setStatus(HttpStatus.BAD_REQUEST_400);
            }
        }else{
            response.setStatus(HttpStatus.BAD_REQUEST_400);
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

    private synchronized boolean transferTickets(int eventId, int userId, int targetUser, int numTickets){
        if (userDataMap.getUser(userId).validateNumTickets(eventId, numTickets)) {
            userDataMap.getUser(userId).removeTickets(eventId, numTickets);
            userDataMap.getUser(targetUser).addTickets(eventId, numTickets);
            return true;
        }
        return false;
    }
}

import org.eclipse.jetty.http.HttpStatus;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
/*
public class TransferThread extends Thread {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private JSONObject jsonObject;
    private UserDataMap userDataMap;
    private String userIdString;
    private PrintWriter printWriter;


    public TransferThread(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject, UserDataMap userDataMap, String userId, PrintWriter pw) {
        this.request = request;
        this.response = response;
        this.jsonObject = jsonObject;
        this.userDataMap = userDataMap;
        this.userIdString = userId;
        this.printWriter = pw;
    }


    public void run() {
        if (jsonObject.containsKey("eventid") && jsonObject.containsKey("tickets") && jsonObject.containsKey("targetuser")) {
            int userId = Integer.parseInt(userIdString);
            int eventid = Integer.parseInt(jsonObject.get("eventid").toString());
            int tickets = Integer.parseInt(jsonObject.get("tickets").toString());
            int targetuser = Integer.parseInt(jsonObject.get("targetuser").toString());
            if (userDataMap.checkIfUserExist(targetuser) && userDataMap.checkIfUserExist(userId)) {
                if (userDataMap.getUser(userId).validateNumTickets(eventid, tickets)) {
                    userDataMap.getUser(userId).removeTickets(eventid, tickets);
                    userDataMap.getUser(targetuser).addTickets(eventid, tickets);
                    response.setStatus(HttpStatus.OK_200);
                    printWriter.println("Event tickets transfered");
                } else {
                    response.setStatus(HttpStatus.BAD_REQUEST_400);
                    printWriter.println("Tickets could not be transfered");
                }
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST_400);
                printWriter.println("Tickets could not be transfered");
            }
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST_400);
            printWriter.println("Tickets could not be transfered");
        }
    }
}*/
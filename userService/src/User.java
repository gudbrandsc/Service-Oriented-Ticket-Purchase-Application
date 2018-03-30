import java.util.HashMap;
import java.util.Map;

public class User {
    private int userid;
    private String username;
    Map<Integer, Integer> events;

    public User(int userid, String username) {
        this.userid = userid;
        this.username = username;
        this.events = new HashMap<Integer,Integer>();
    }

    public boolean validateNumTickets(int eventid, int numTickets) {
        return (this.events.containsKey(eventid)) && (this.events.get(eventid) >= numTickets);
    }

    public String getUsername() {
        return this.username;
    }

    public void addTickets(int eventid, int numTickets) {
        if(this.events.containsKey(eventid)){
            int num = this.events.get(eventid) + numTickets;
            this.events.put(eventid, num);
        }else{
            this.events.put(eventid, numTickets);
        }
    }

    public void removeTickets(int eventid, int numTickets) {
        int num = this.events.get(eventid) - numTickets;
        if(num == 0){
            this.events.remove(eventid);
        }else{
            this.events.put(eventid, num);
        }
    }

    public Map<Integer,Integer> getEvents() {
        return this.events;
    }

    public int getNumEventsSize() {
        return this.events.size();
    }

}

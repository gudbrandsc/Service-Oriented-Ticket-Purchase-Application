import java.util.HashMap;
import java.util.Map;

/**
 * @Author Gudbrand Schistad
 * Class used to create a user object, that is stored in the user datamap.
 */
public class User {
    private int userid;
    private String username;
    private Map<Integer, Integer> events;

    /*** constructor*/
    public User(int userid, String username) {
        this.userid = userid;
        this.username = username;
        this.events = new HashMap<Integer,Integer>();
    }
    /**
     * Validates that the user has a event with the param eventid,
     * and that the number of tickets param is not greater than the number the user has
     * @param eventid Id of the event
     * @param numTickets number of tickets
     */
    public boolean validateNumTickets(int eventid, int numTickets) {
        return (this.events.containsKey(eventid)) && (this.events.get(eventid) >= numTickets);
    }
    /**
     * Get method to retrieve the username */
    public String getUsername() {
        return this.username;
    }

    /**
     * Method that adds tickets to an event.
     * Checks if the user already has any tickets for the event id.
     * If the user do then add the number of tickets to that event.
     * else put eventid and number of tickets.
     * @param eventid Id of the event
     * @param numTickets Number of tickets purchased/transferred
     */
    public void addTickets(int eventid, int numTickets) {
        if(this.events.containsKey(eventid)){
            int num = this.events.get(eventid) + numTickets;
            this.events.put(eventid, num);
        }else{
            this.events.put(eventid, numTickets);
        }
    }

    /**
     * Method that removes tickets from a event.
     * Uses validateNumTickets method to make sure that the number of tickets removed is
     * not greater than the number of tickets the user holds.
     * If the number of tickets is equal to zero then remove the event from the eventlist
     * @param eventid Id of the event
     * @param numTickets Number of tickets to remove
     */
    public void removeTickets(int eventid, int numTickets) {
        int num = this.events.get(eventid) - numTickets;
        if(num == 0){
            this.events.remove(eventid);
        }else{
            this.events.put(eventid, num);
        }
    }
    /**
     * Get method the get the events map
     * @return User hashmap of all events
     */
    public Map<Integer,Integer> getEvents() {
        return this.events;
    }

    /**
     * Method to get the number of elements in the hashmap
     * @return The size of events map
     */
    public int getNumEventsSize() {
        return this.events.size();
    }

}

import java.util.*;

/**
 * This class represents an Event object which has variables for userid, eventname, eventid,
 * available tickets, and purchased tickets
 */
public class EventData {
    private int userId;
    private String eventName;
    private int ticketsAvailable;
    private int ticketsPurchased;
    private int eventId;

    public EventData(int userid, String eventName1, int tickets){
        userId = userid;
        ticketsAvailable = tickets;
        eventName = eventName1;
        eventId = generateEventId();
        ticketsPurchased = 0;
    }

    public int getUserId(){
        return userId;
    }

    public String getEventName(){
        return eventName;
    }

    public int getTicketsAvailable(){
        return ticketsAvailable;
    }

    public int getTicketsPurchased(){
        return ticketsPurchased;
    }

    public int getEventId(){
        return eventId;
    }

    /**
     * This class uses a random number generator from to return an event id from 1-1000.
     * @return
     */
    public int generateEventId(){
        Random rand = new Random();
        int ret = rand.nextInt(1000) + 1;

        return ret;
    }

    /**
     * This class takes as a parameter the number of tickets the user wants to purchase. It will
     * return true if there are enough tickets available. Else it will return false. This method
     * is synchronized since if multiple threads can access it, they can possibly buy more tickets
     * than are available.
     * @param num
     * @return
     */
    public synchronized boolean purchaseTicket(int num){
        boolean ret = false;

        if(ticketsAvailable >= num){
            ticketsAvailable -= num;
            ticketsPurchased += num;
            ret = true;
        }

        return ret;
    }

    /**
     * This adds tickets back to tickets available and deducts from tickets purchased. This method
     * is also synchronized.
     * @param num
     */
    public synchronized void addTicketsBack(int num){
        ticketsAvailable += num;
        ticketsPurchased -= num;
    }

}

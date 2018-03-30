import java.util.*;

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

    // use random number generator for this
    public int generateEventId(){
        Random rand = new Random();
        int ret = rand.nextInt(1000) + 1;

        return ret;
    }

    // determine if purchase can be made, increment purchase, decrement available
    public boolean purchaseTicket(int num){
        boolean ret = false;

        if(ticketsAvailable >= num){
            ticketsAvailable -= num;
            ticketsPurchased += num;
            ret = true;
        }

        return ret;
    }

    public void addTicketsBack(int num){
        ticketsAvailable += num;
        ticketsPurchased -= num;
    }

}

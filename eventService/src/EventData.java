import java.util.*;

public class EventData {
    private String userId;
    private String eventName;
    private int ticketsAvailable;
    private int ticketsPurchased;
    private String eventId;

    public EventData(String user, String eventName1, int ticketsAvail){
        userId = user;
        ticketsAvailable = ticketsAvail;
        eventName = eventName1;
        //eventId = generateEventId();
        eventId = "9999";
        ticketsPurchased = 0;
    }

    public String getUserId(){
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

    public String getEventId(){
        return eventId;
    }

    // use random number generator for this
    public String generateEventId(){
        Random rand = new Random();
        int ret = rand.nextInt(1000) + 1;

        return String.valueOf(ret);
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
}

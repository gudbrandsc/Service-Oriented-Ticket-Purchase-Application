import java.util.*;

public class EventData {
    private String userId;
    private String eventName;
    private int ticketsAvailable;
    private int ticketsPurchased;
    private String eventId;

    public EventData(String userId, String eventName, int ticketsAvail){
        this.userId = userId;
        ticketsAvailable = ticketsAvail;
        this.eventName = eventName;
        eventId = generateEventId();
        ticketsPurchased = 450;
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

    // user random number generator for this
    public String generateEventId(){
        return "46219";
    }

    // determine if purchase can be made, increment purchase, decrement available
    public boolean purchaseTicket(int num){
        return true;
    }
}

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class is a threadsafe Arraylist that uses the concurrent reentrant read-write lock library.
 * It stores a list of Event objects.
 */
public class EventList {
    private ArrayList<EventData> eventsList;
    private ReentrantReadWriteLock lock;

    public EventList(){
        eventsList = new ArrayList<>();
        lock = new ReentrantReadWriteLock();
    }

    /**
     * This method adds an event object to the threadsafe arraylist.
     * @param event
     */
    public void addToList(EventData event){
        lock.writeLock().lock();
        eventsList.add(event);
        lock.writeLock().unlock();
    }

    /**
     * Returns an arraylist of event objects
     * @return
     */
    public ArrayList<EventData> getList(){
        ArrayList<EventData> ret = new ArrayList<>();

        lock.readLock().lock();
        for(EventData event: eventsList){
            ret.add(event);
        }
        lock.readLock().unlock();

        return ret;
    }

    /**
     * Returns an Event object with given event id.
     * @param eventId
     * @return
     */
    public EventData getEvent(int eventId){
        EventData ret = null;

        lock.readLock().lock();
        for(EventData event: eventsList){
            if(event.getEventId() == eventId){
                ret = event;
            }
        }
        lock.readLock().unlock();

        return ret;
    }

    /**
     * Checks if event with given event id exists
     * @param eventId
     * @return
     */
    public boolean eventExists(int eventId){
        boolean ret = false;

        lock.readLock().lock();
        for(EventData event: eventsList){
            if(eventId == event.getEventId()){
                ret = true;
            }
        }
        lock.readLock().unlock();

        return ret;
    }

}

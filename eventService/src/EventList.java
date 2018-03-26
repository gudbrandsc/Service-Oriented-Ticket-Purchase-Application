import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class is a threadsafe Arraylist that uses the concurrent reentrant read-write lock library.
 */
public class EventList {
    private ArrayList<EventData> eventsList;
    private ReentrantReadWriteLock lock;

    public EventList(){
        eventsList = new ArrayList<>();
        lock = new ReentrantReadWriteLock();
    }

    public void addToList(EventData event){
        lock.writeLock().lock();
        eventsList.add(event);
        lock.writeLock().unlock();
    }

    public ArrayList<EventData> getList(){
        ArrayList<EventData> ret = new ArrayList<>();

        lock.readLock().lock();
        for(EventData event: eventsList){
            ret.add(event);
        }
        lock.readLock().unlock();

        return ret;
    }

    public EventData getEvent(String eventId){
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

    public boolean eventExists(String eventId){
        boolean ret = false;

        lock.readLock().lock();
        for(EventData event: eventsList){
            if(event.getEventId() == eventId){
                ret = true;
            }
        }
        lock.readLock().unlock();

        return ret;
    }


}

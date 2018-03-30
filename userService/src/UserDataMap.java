import java.util.concurrent.ConcurrentHashMap;

public class UserDataMap {
    private ConcurrentHashMap<Integer, User> userData;

    public UserDataMap() {
        this.userData =  new ConcurrentHashMap<Integer, User>();
    }

    public void addUser(int userId, User user){
        this.userData.put(userId,user);
    }

    public User getUser(int userId) {
        return this.userData.get(userId);
    }

    public boolean checkIfUserExist(int userId){
        return this.userData.containsKey(userId);
    }
}

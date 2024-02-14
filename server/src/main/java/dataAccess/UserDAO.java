package dataAccess;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.ArrayList;
public class UserDAO {
    private static UserDAO instance = null;
    private ArrayList<UserData> userDB = new ArrayList<UserData>();

    public static synchronized UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }
    public UserData createUser(String username, String password, String email){
        return new UserData(username, password, email);
    }

    public boolean insertUser(UserData user){
        this.userDB.add(user);
        return true;
    }
    public boolean deleteUser(UserData user) throws DataAccessException{
        boolean removed = this.userDB.remove(user);
        return removed;
    }
    public boolean isEmpty(){
        return this.userDB.size() == 0;
    }
    public UserData getUser(String username) throws DataAccessException{
        for(UserData u: userDB){
            if(u.username().equals(username)){
                return u;
            }
        }
        return null;
    }
    public void clearUser() throws DataAccessException{
        userDB.clear();
    }
}

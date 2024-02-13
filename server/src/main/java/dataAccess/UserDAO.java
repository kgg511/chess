package dataAccess;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.ArrayList;
public class UserDAO {
    private static UserDAO instance = null;
    private ArrayList<UserData> UserDB = new ArrayList<UserData>();

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
        this.UserDB.add(user);
        System.out.println("SIZE:" + UserDB.size());
        return true;
    }

    public boolean deleteUser(UserData user) throws DataAccessException{
        boolean removed = this.UserDB.remove(user);
        return removed;
    }

    public UserData getUser(String username) throws DataAccessException{
        System.out.println("getUser function USERDAO the db length is " + this.UserDB.size());
        for(UserData u: UserDB){
            if(u.username().equals(username)){
                return u;
            }
        }
        return null;
    }

    //no update method currently
    public void clearUser() throws DataAccessException{
        UserDB.clear();
    }

}

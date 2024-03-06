package dataAccess;
import dataAccess.interfaces.UserDAOInterface;
import model.UserData;

import java.util.ArrayList;
public class UserDAO implements UserDAOInterface {
    private static UserDAO instance = null;
    public ArrayList<UserData> userDB = new ArrayList<UserData>();

    public static synchronized UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public boolean insertUser(UserData user){
        this.userDB.add(user);
        return true;
    }
    public UserData getUser(String username) throws DataAccessException{
        for(UserData u: userDB){
            if(u.username().equals(username)){
                return u;
            }
        }
        return null;
    }
}

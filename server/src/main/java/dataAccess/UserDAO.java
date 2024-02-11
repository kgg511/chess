package dataAccess;
import Model.Auth;
import Model.User;
import java.util.ArrayList;
public class UserDAO {
    //create user

    private ArrayList<User> UserDB = new ArrayList<User>();
    User createUser(String username, String password, String email){
        return new Model.User(username, password, email);
    }

    boolean insertUser(User user){
        this.UserDB.add(user);
        return true;
    }

    boolean deleteUser(User user){
        boolean removed = this.UserDB.remove(user);
        return removed;
    }

    User getUser(String username){
        for(User u: UserDB){
            if(u.getUsername().equals(username)){
                return u;
            }
        }
        return null;
    }

    //no update method currently
    void clearUser(){
        UserDB = new ArrayList<User>();
    }

}

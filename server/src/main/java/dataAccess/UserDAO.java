package dataAccess;
import model.UserData;
import java.util.ArrayList;
public class UserDAO {
    //create user

    private ArrayList<UserData> UserDB = new ArrayList<UserData>();
    public UserData createUser(String username, String password, String email){
        return new UserData(username, password, email);
    }

    public boolean insertUser(UserData user){
        this.UserDB.add(user);
        return true;
    }

    public boolean deleteUser(UserData user) throws DataAccessException{
        boolean removed = this.UserDB.remove(user);
        return removed;
    }

    public UserData getUser(String username) throws DataAccessException{
        for(UserData u: UserDB){
            if(u.username().equals(username)){
                return u;
            }
        }
        return null;
    }

    //no update method currently
    public void clearUser(){
        UserDB = new ArrayList<UserData>();
    }

}

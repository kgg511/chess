package dataAccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.util.ArrayList;

/**fetches from Auth DB*/
public class AuthDAO {
    private static AuthDAO instance = null;
    private ArrayList<AuthData> AuthDB = new ArrayList<AuthData>();
    //singleton
    public static synchronized AuthDAO getInstance() {
        if (instance == null) {
            instance = new AuthDAO();
        }
        return instance;
    }
    AuthData createAuth(String authToken, String username){
        return new model.AuthData(authToken, username);
    }
    public boolean insertAuth(AuthData auth){
        this.AuthDB.add(auth);
        System.out.println("AUTHDB LENGTH:" + this.AuthDB.size());
        return true;
    }

    public boolean deleteAuth(AuthData auth) throws DataAccessException{ //returns false if it didn't have anything to delete
       boolean removed = this.AuthDB.remove(auth);
        return removed;
    }

    public boolean deleteByToken(String authToken) throws DataAccessException{
        for(AuthData a: AuthDB){
            if(a.authToken().equals(authToken)){
                System.out.println("authToken has been removed");
                AuthDB.remove(a);
                return true;
            }
        }
        return false;
    }

    public AuthData getAuth(String username) throws DataAccessException{
        for(AuthData a: AuthDB){
            if(a.username().equals(username)){
                return a;
            }
        }
        return null; //do i have to manually throw the exception
    }

    public AuthData getAuthByToken(String authToken) throws DataAccessException{
        for(AuthData a: AuthDB){
            if(a.authToken().equals(authToken)){
                return a;
            }
        }
        return null;
    }

    public void clearAuth() throws DataAccessException {
        AuthDB = new ArrayList<AuthData>();
    }
}

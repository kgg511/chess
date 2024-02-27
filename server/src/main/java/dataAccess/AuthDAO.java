package dataAccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.util.ArrayList;

/**fetches from Auth DB*/
public class AuthDAO {
    private static AuthDAO instance = null;
    private ArrayList<AuthData> authDB = new ArrayList<AuthData>();
    //singleton
    public static synchronized AuthDAO getInstance() {
        if (instance == null) {
            instance = new AuthDAO();
        }
        return instance;
    }
    public boolean insertAuth(AuthData auth){
        this.authDB.add(auth);
        return true;
    }
    public boolean isEmpty(){
        return this.authDB.size() == 0;
    }
    public boolean deleteByToken(String authToken) throws DataAccessException{
        for(AuthData a: authDB){
            if(a.authToken().equals(authToken)){
                System.out.println("auth removed");
                authDB.remove(a);
                return true;
            }
        }
        return false;
    }
    public AuthData getAuth(String username) throws DataAccessException{
        for(AuthData a: authDB){
            if(a.username().equals(username)){
                return a;
            }
        }
        return null; //do i have to manually throw the exception
    }
    public AuthData getAuthByToken(String authToken) throws DataAccessException{
        for(AuthData a: authDB){
            if(a.authToken().equals(authToken)){
                return a;
            }
        }
        return null;
    }
    public void clearAuth() throws DataAccessException {
        authDB.clear();
    }
}

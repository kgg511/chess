package dataAccess;

import dataAccess.interfaces.AuthDAOInterface;
import model.AuthData;

import java.util.ArrayList;

/**fetches from Auth DB*/
public class AuthDAO implements AuthDAOInterface {
    private static AuthDAO instance = null;
    public ArrayList<AuthData> authDB = new ArrayList<AuthData>();
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
    public ArrayList<AuthData> getAuth(String username) throws DataAccessException{
        ArrayList<AuthData> auths = new ArrayList<>();
        for(AuthData a: authDB){
            if(a.username().equals(username)){
                auths.add(a);
            }
        }
        return auths;
        //return null; //do i have to manually throw the exception
    }
    public AuthData getAuthByToken(String authToken) throws DataAccessException{
        for(AuthData a: authDB){
            if(a.authToken().equals(authToken)){
                return a;
            }
        }
        return null;
    }
}

package dataAccess;

import model.AuthData;

import java.util.ArrayList;

/**fetches from Auth DB*/
public class AuthDAO {

    private ArrayList<AuthData> AuthDB = new ArrayList<AuthData>();
    AuthData createAuth(String authToken, String username){
        return new model.AuthData(authToken, username);
    }

    public boolean insertAuth(AuthData auth){
        this.AuthDB.add(auth);
        return true;
    }

    public boolean deleteAuth(AuthData auth) throws DataAccessException{ //returns false if it didn't have anything to delete
       boolean removed = this.AuthDB.remove(auth);
        return removed;
    }

    public boolean deleteByToken(String authToken) throws DataAccessException{
        for(AuthData a: AuthDB){
            if(a.authToken().equals(authToken)){
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

    public void clearAuth(){
        AuthDB = new ArrayList<AuthData>();
    }
}

package dataAccess;

import Model.Auth;

import java.util.ArrayList;

/**fetches from Auth DB*/
public class AuthDAO {

    private ArrayList<Auth> AuthDB = new ArrayList<Auth>();
    Auth createAuth(String authToken, String username){
        return new Model.Auth(authToken, username);
    }

    boolean insertAuth(Auth auth){
        this.AuthDB.add(auth);
        return true;
    }

    boolean deleteAuth(Auth auth){ //returns false if it didn't have anything to delete
       boolean removed = this.AuthDB.remove(auth);
        return removed;
    }

    boolean deleteByToken(String authToken){
        for(Auth a: AuthDB){
            if(a.getAuthToken().equals(authToken)){
                AuthDB.remove(a);
                return true;
            }
        }
        return false;
    }

    Auth getAuth(String username){
        for(Auth a: AuthDB){
            if(a.getUsername().equals(username)){
                return a;
            }
        }
        return null;
    }

    Auth getAuthByToken(String authToken){
        for(Auth a: AuthDB){
            if(a.getAuthToken().equals(authToken)){
                return a;
            }
        }
        return null;
    }

    void clearAuth(){
        AuthDB = new ArrayList<Auth>();
    }
}

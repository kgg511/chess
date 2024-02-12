import com.google.gson.Gson;

import model.*;
import service.LoginService;
import spark.*;
import dataAccess.*;
import Response.*;

public class Handler {
    //turns out the handlers will call the service possibly everything
    //all handlers, specifically the request part
    //convert the http request is mostly string
    //but the parameters to pass will be in json so convert to an object

    //ok it's going to turn it into a user object
    //{ "username":"", "password":"", "email":"" }
    private UserData RegisterHandler(Request req, Response res){
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        //return user;

        //
    }

    //{username, password}
    private String LoginHandler(Request req, Response res){

        if (req.body() == null || req.body().isEmpty()){
            res.status(400);
        }
        else{
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            LoginService service = new LoginService(new AuthDAO(), new GameDAO(), new UserDAO());
            UserData realUser = service.getUser(user.username());

            if(realUser == null){ //probably would have been caught by dataaccess
                throw error;
            }
            String authToken = service.createAuth(realUser.username());


            LoginResponse R = new LoginResponse(realUser.username(), authToken);
            res.status(200);
            return new Gson().toJson(R);

            //service code
        }
        return ""; //error case return just the error status? How to return specific error message?

    }

    //headers contain authorization: <authToken>
    private AuthData LogoutHandler(Request req, Response res){
        //TODO: error if no authtoken header

        String authToken = req.headers("authorization");
        if(authToken == null){
            throw error;
            res.status(400);
        }
        //return ;
    }

    //authToken header
    private AuthData ListGamesHandler(Request req, Response res){
        //TODO: error if no authtoken header
        String authToken = req.headers("authorization");
        if(authToken == null){
            throw error;
        }
    }

    private AuthData CreateGamesHandler(Request req, Response res){
        //TODO: error if no authtoken header
        String authToken = req.headers("authorization");
        if(authToken == null){
            throw error;
        }
        //create Game using gameName (apparently it knows which one it is)
        GameData game = new Gson().fromJson(req.body(), GameData.class);

    }

    //header authorization
    //body { "playerColor":"WHITE/BLACK", "gameID": 1234 }
    private AuthData JoinGamesHandler(Request req, Response res){
        //TODO: error if no authtoken header
        String authToken = req.headers("authorization");
        if(authToken == null){
            throw error;
        }
        GameData game = new Gson().fromJson(req.body(), GameData.class);

    }

    private Object clearDBHandler(Request req, Response res){

    }

    //None for clear data because there's no serialization stuff






}

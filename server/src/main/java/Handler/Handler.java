package Handler;

import com.google.gson.Gson;

import model.*;
import service.ClearService;
import service.LoginService;
import service.RegisterService;
import spark.*;
import dataAccess.*;
import Response.*;
import exception.ResponseException;
public class Handler {
    //handlers convert JSON -> Java and Java -> JSON

    //{ "username":"", "password":"", "email":"" }
    public String registerHandler(Request req, Response res){
        //TODO: how do I handle error of nothing passed in? Or, not everything?
        try{
            if (req.body() == null || req.body().isEmpty()){ //if didn't pass in stuff
                throw new ResponseException(400, "Error: bad request");
            }
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            RegisterService service = new RegisterService(new AuthDAO(), new GameDAO(), new UserDAO());
            RegisterResponse r = service.register(user.username(), user.password(), user.email());
            //[200] { "username":"", "authToken":"" }
            res.status(200);
            return new Gson().toJson(r);
        }
        catch (ResponseException e){
            return new Gson().toJson(exceptionHandler(e, req, res));
        }

    }

    //{username, password}
    public String loginHandler(Request req, Response res){
        if (req.body() == null || req.body().isEmpty()){
            res.status(400);
            return new Gson().toJson(new ExceptionResponse("Error: bad request"));
        }
        try{
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            LoginService service = new LoginService(new AuthDAO(), new GameDAO(), new UserDAO());
            LoginResponse r = service.login(user.username(), user.password());
            res.status(200);
            return new Gson().toJson(r);
        }
        catch (ResponseException e){
            return new Gson().toJson(exceptionHandler(e, req, res));
        }
    }

    //headers contain authorization: <authToken>
    public String logoutHandler(Request req, Response res){
        //TODO: error if no authtoken header

        String authToken = req.headers("authorization");
        if(authToken == null){
            //throw error;
            res.status(400);
        }
        return null;
    }

    //authToken header
    public String listGamesHandler(Request req, Response res){
        //TODO: error if no authtoken header
        String authToken = req.headers("authorization");
        if(authToken == null){
            //throw error;
        }

        return null;
    }

    public String createGamesHandler(Request req, Response res){
        //TODO: error if no authtoken header
        String authToken = req.headers("authorization");
        if(authToken == null){
            //throw error;
        }
        //create Game using gameName (apparently it knows which one it is)
        GameData game = new Gson().fromJson(req.body(), GameData.class);
        return null;
    }

    //header authorization
    //body { "playerColor":"WHITE/BLACK", "gameID": 1234 }
    public String joinGamesHandler(Request req, Response res){
        //TODO: error if no authtoken header
        String authToken = req.headers("authorization");
        if(authToken == null){
            //throw error;
        }
        GameData game = new Gson().fromJson(req.body(), GameData.class);
        return null;
    }

    public String clearDBHandler(Request req, Response res){
        ClearService clearService = new ClearService(new AuthDAO(), new GameDAO(), new UserDAO());
        clearService.clearDB();

        res.status(200);
        return new Gson().toJson(null);
    }

    //None for clear data because there's no serialization stuff



    public ExceptionResponse exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        return new ExceptionResponse(ex.getMessage());
    }


}

package Handler;

import com.google.gson.Gson;

import model.*;
import service.*;
import spark.*;
import dataAccess.*;
import Response.*;
import exception.ResponseException;

import java.util.List;

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
            RegisterService service = new RegisterService();
            RegisterResponse r = service.register(user.username(), user.password(), user.email());
            //[200] { "username":"", "authToken":"" }
            res.status(200);
            return new Gson().toJson(r);
        }
        catch (ResponseException e){
            return new Gson().toJson(exceptionHandler(e, req, res));
        }
        catch (DataAccessException e){
            return new Gson().toJson(exceptionHandler(new ResponseException(500, "Error: description"), req, res));
        }

    }

    //{username, password}
    public String loginHandler(Request req, Response res){
        try{
            if (req.body() == null || req.body().isEmpty()){ //if didn't pass in stuff
                throw new ResponseException(400, "Error: bad request");
            }
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            LoginService service = new LoginService();
            LoginResponse r = service.login(user.username(), user.password());
            res.status(200);
            return new Gson().toJson(r);
        }
        catch (ResponseException e){
            return new Gson().toJson(exceptionHandler(e, req, res));
        }
        catch (DataAccessException e){
            return new Gson().toJson(exceptionHandler(new ResponseException(500, "Error: description"), req, res));
        }
    }

    //headers contain authorization: <authToken>
    public String logoutHandler(Request req, Response res){
        //TODO: do i care if they pass in a body which is unnecessaru
        try{
            String authToken = req.headers("authorization"); //CASE: didn't pass in authToken
            if(authToken == null){
                throw new ResponseException(400, "Error: bad request");
            }
            LogoutService service = new LogoutService();
            LogoutResponse r = service.logout(authToken);
            res.status(200);
            return new Gson().toJson(r);

        }
        catch (ResponseException e){
            return new Gson().toJson(exceptionHandler(e, req, res));
        }
        catch (DataAccessException e ){
            return new Gson().toJson(exceptionHandler(new ResponseException(500, "Error: description"), req, res));
        }

    }

    //authToken header
    public String listGamesHandler(Request req, Response res){
        //TODO: error if no authtoken header
        try{
            String authToken = req.headers("authorization"); //CASE: didn't pass in authToken
            if(authToken == null){
                throw new ResponseException(400, "Error: bad request");
            }

            ListGamesService service = new ListGamesService();
            ListGamesResponse r = service.listGames(authToken);
            res.status(200);
            return new Gson().toJson(r);
        }
        catch (ResponseException e){
            return new Gson().toJson(exceptionHandler(e, req, res));
        }
        catch (DataAccessException e){
            return new Gson().toJson(exceptionHandler(new ResponseException(500, "Error: description"), req, res));
        }
    }

    //{ "gameName":"" }
    public String createGamesHandler(Request req, Response res){
        try{
            String authToken = req.headers("authorization"); //CASE: didn't pass in authToken
            if(authToken == null || req.body() == null || req.body().isEmpty()){
                throw new ResponseException(400, "Error: bad request");
            }
            GameData g = new Gson().fromJson(req.body(), GameData.class);
            CreateGameService service = new CreateGameService();
            CreateGameResponse r = service.createGame(authToken, g.gameName());
            res.status(200);
            return new Gson().toJson(r);
        }
        catch (ResponseException e){
            return new Gson().toJson(exceptionHandler(e, req, res));
        }
        catch (DataAccessException e){
            return new Gson().toJson(exceptionHandler(new ResponseException(500, "Error: description"), req, res));
        }
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
        ClearService clearService = new ClearService();
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

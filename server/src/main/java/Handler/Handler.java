package Handler;

import com.google.gson.Gson;

import model.*;
import service.*;
import spark.*;
import dataAccess.*;
import Response.*;
import exception.ResponseException;
import Request.*;

public class Handler {
    //handlers convert JSON -> Java and Java -> JSON
    public String registerHandler(Request req, Response res){
        try{
            checkBodyAuth(req, true, false, "");
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            RegisterService service = new RegisterService();
            RegisterResponse r = service.register(user.username(), user.password(), user.email());
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

    public String loginHandler(Request req, Response res){
        try{
            checkBodyAuth(req, true, false, "");
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

    public String logoutHandler(Request req, Response res){
        try{
            String authToken = req.headers("authorization"); //CASE: didn't pass in authToken
            checkBodyAuth(req, false, true, authToken);
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

    public String listGamesHandler(Request req, Response res){
        try{
            String authToken = req.headers("authorization"); //CASE: didn't pass in authToken
            checkBodyAuth(req, false, true, authToken);
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

    public String createGamesHandler(Request req, Response res){
        try{
            String authToken = req.headers("authorization"); //CASE: didn't pass in authToken
            checkBodyAuth(req, true, true, authToken);
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

    public String joinGameHandler(Request req, Response res){
        try{
            String authToken = req.headers("authorization"); //CASE: didn't pass in authToken
            checkBodyAuth(req, true, true, authToken);
            JoinGameRequest g = new Gson().fromJson(req.body(), JoinGameRequest.class);
            JoinGameService service = new JoinGameService();
            JoinGameResponse r = service.joinGame(authToken, g.playerColor(), g.gameID());
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

    public String clearDBHandler(Request req, Response res){
        try{
            ClearService clearService = new ClearService();
            clearService.clearDB();
            res.status(200);
            return "{}";
        }
        catch (ResponseException e){
            return new Gson().toJson(exceptionHandler(e, req, res));
        }
        catch (DataAccessException e) {
            return new Gson().toJson(exceptionHandler(new ResponseException(500, "Error: description"), req, res));
        }
    }

    public ExceptionResponse exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.statusCode());
        return new ExceptionResponse(ex.getMessage());
    }

    private void checkBodyAuth(Request req, boolean body, boolean auth, String authToken) throws ResponseException{
        if(body){
            if (req.body() == null || req.body().isEmpty()){ //if didn't pass in stuff
                throw new ResponseException(400, "Error: bad request");
            }
        }
        if(auth){
            if(authToken == null){
                throw new ResponseException(400, "Error: bad request");
            }
        }

    }
}

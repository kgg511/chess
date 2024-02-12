import com.google.gson.Gson;

import model.*;
import spark.*;
import java.util.Map;

public class RequestHandler {
    //turns out the handlers will call the service possibly everything
    //all handlers, specifically the request part
    //convert the http request is mostly string
    //but the parameters to pass will be in json so convert to an object

    //ok it's going to turn it into a user object
    //{ "username":"", "password":"", "email":"" }
    UserData RegisterHandler(Request req, Response res){
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        return user;
    }

    //{username, password}
    UserData LoginHandler(Request req, Response res){
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        return user;
    }

    //headers contain authorization: <authToken>
    AuthData LogoutHandler(Request req, Response res){
        //TODO: error if no authtoken header
        return new AuthData(req.headers("authorization"), null);
    }

    AuthData ListGamesHandler(Request req, Response res){
        //TODO: error if no authtoken header
        return new AuthData(req.headers("authorization"), null);
    }

    AuthData CreateGamesHandler(Request req, Response res){
        //TODO: error if no authtoken header
        return new AuthData(req.headers("authorization"), null);
    }

    AuthData JoinGamesHandler(Request req, Response res){
        //TODO: error if no authtoken header
        return new AuthData(req.headers("authorization"), null);
    }

    void 

    //None for clear data because there's no serialization stuff






}

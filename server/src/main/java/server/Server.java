package server;

import spark.*;
import java.nio.file.Paths;

import Handler;
public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        //TODO: what do I do about multiple endpoints having the same path?
        Spark.delete("/db", Handler::clearDBHandler);
        Spark.post("/user", Handler::RegisterHandler);
        Spark.post("/session", Handler::LoginHandler);
        Spark.delete("/session", Handler::LogoutHandler);
        Spark.get("/game", Handler::ListGamesHandler);
        Spark.post("/game", Handler::CreateGamesHandler);
        Spark.put("/game", Handler::JoinGamesHandler);

        //Spark.exception(ResponseException.class, this::exceptionHandler);
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

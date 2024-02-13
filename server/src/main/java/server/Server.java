package server;

import spark.*;

import Handler.Handler;
import exception.ResponseException;
public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Handler h = new Handler();
        Spark.delete("/db", h::clearDBHandler);
        Spark.post("/user", h::registerHandler);
        Spark.post("/session", h::loginHandler);
        Spark.delete("/session", h::logoutHandler);
        Spark.get("/game", h::listGamesHandler);
        Spark.post("/game", h::createGamesHandler);
        Spark.put("/game", h::joinGamesHandler);
        Spark.exception(ResponseException.class, h::exceptionHandler);
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public static void main(String[] args) {
        new Server().run(8080);
    }
}

package server;

import spark.*;

import Handler.Handler;
import webSocketServer.WebSocketHandler;
public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        //Websocket endpoint
        final WebSocketHandler webSocketHandler = new WebSocketHandler();
        Spark.webSocket("/connect", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Handler h = new Handler();
        Spark.delete("/db", h::clearDBHandler);
        Spark.post("/user", h::registerHandler);
        Spark.post("/session", h::loginHandler);
        Spark.delete("/session", h::logoutHandler);
        Spark.get("/game", h::listGamesHandler);
        Spark.post("/game", h::createGamesHandler);
        Spark.put("/game", h::joinGameHandler);
        //Spark.exception(ResponseException.class, h::exceptionHandler);
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

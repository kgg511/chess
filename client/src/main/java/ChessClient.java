import exception.ResponseException;

import java.util.Arrays;
import java.util.ArrayList;
public class ChessClient {

    //functions to take input and call stuff from the serverFacade
    private final ServerFacade server;
    private final String serverUrl;

    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }
    //String serverUrl, NotificationHandler notificationHandler
    //call the functions that then create the request

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);

            //if not logged in: help, quit, login, register
            if(state == state.SIGNEDOUT){
                return switch (cmd) {
                    case "help" -> help();
                    case "quit" -> "quit";
                    case "register" -> register(params);
                    case "login" -> login(params);
                    default -> help();
                };
            }
            //if logged in: help, logout, create game, list games, join game, join observer
            else if(state == state.SIGNEDIN){
                return switch (cmd) {
                    case "help" -> help();
                    case "logout" -> logout();
                    case "create" -> createGame(params);
                    case "list" -> listGames();
                    case "join" -> joinGame(params);
                    case "observe" -> observeGame(params);
                    default -> help();
                };
            }

        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
}

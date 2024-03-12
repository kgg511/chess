import exception.ResponseException;

import java.util.Arrays;
import java.util.ArrayList;
import model.*;
public class ChessClient {

    //functions to take input and call stuff from the serverFacade
    private final ServerFacade server;
    private final String serverUrl;

    private State state = State.SIGNEDOUT;

    //domain name , port number, will come in via cmdline?
    //localhost:80
    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }
    //String serverUrl, NotificationHandler notificationHandler
    //call the functions that then create the request

    public State getState(){ return this.state;}
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
                    case "quit" -> "quit";
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
        return "what did you do";
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - exit
                    help - with possible commands
                    """;
        }
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK|<empty>] - a game
                observe <ID> - a game
                logout - when you are done
                quit - quit playing chess
                help - see possible commands
                """;
    }

    //register <USERNAME> <PASSWORD> <EMAIL> - to create an account
    public String register(String... params) throws ResponseException{
        if(params.length >= 3){ //cmd, username, password, email
            Response.RegisterResponse response = server.register(params[0], params[1], params[2]);
            state = state.SIGNEDIN;
            return String.format("You registered as %s.", response.username());
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException{
        if(params.length >= 2){ //cmd, username, password
            Response.LoginResponse response = server.login(params[0], params[1]);
            state = state.SIGNEDIN; //hmmm
            return String.format("You signed in as %s.", response.username());
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String logout(String... params) throws ResponseException{
        if(state == state.SIGNEDOUT){throw new ResponseException(400, "you are already signed out");}
        Response.LogoutResponse response = server.logout();
        state = State.SIGNEDOUT; //hmmm
        return "User signed out";
    }

    public String createGame(String... params) throws ResponseException{
        //create <NAME> - a game
        if(params.length >= 1){ //create, name
            Response.CreateGameResponse response = server.createGame(params[0]);
            return String.format("Game, %s, created", params[0]);
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    //list - games
    public String listGames(String... params) throws ResponseException{

        ArrayList<GameData> games = server.listGames();
        return games.toString(); //TODO: reformat??
    }

    //join <ID> [WHITE|BLACK|<empty>] - a game
    public String joinGame(String... params) throws ResponseException{
        if(params.length >= 2){
            Response.JoinGameResponse response = server.joinGame(params[1], Integer.parseInt(params[0]));
            //draw game
            return "Successfully joined game";
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK|<empty>]");
    }

    //observe <ID> - a game
    public String observeGame(String... params) throws ResponseException{
        if(params.length >= 1){
            //Response.JoinGameResponse response = server.joinGame(params[1], Integer.parseInt(params[2]));
            return "Successfully joined game as an observer";
            //currently does nothign nice
        }
        throw new ResponseException(400, "Expected: <ID>");
    }






}

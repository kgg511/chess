package clientStuff;
import chess.ChessBoard;
import chess.ChessGame;
import clientStuff.webSocketClient.WebSocketCommunicator;
import exception.ResponseException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ArrayList;
import model.*;
import ui.DrawChessBoard;

import static ui.EscapeSequences.*;

public class ChessClientLoggedIn implements ChessClientInterface{
    public final ServerFacade server;
    private final String serverUrl;
    private final DrawChessBoard drawer = new DrawChessBoard();
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private WebSocketCommunicator ws;
    private State state = State.SIGNEDIN;
    private int gameID; //set when user join game

    public ChessClientLoggedIn(int port, String host, ServerFacade f) {
        this.serverUrl = host + ":" + port;
        if(f != null){server = f;}
        else{server = new ServerFacade(port, host);}
    }
    public WebSocketCommunicator getWS(){return this.ws;}
    public State getState(){ return this.state;}
    public int getGameID(){return gameID;}
    public ServerFacade getFacade(){return this.server;}
    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help"-> help();
                case "quit" -> "quit";
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        System.out.println(SET_TEXT_COLOR_BLUE);
        return """
            create <NAME> - a game
            list - games
            join <ID> [WHITE|BLACK] - a game
            observe <ID> - a game
            logout - when you are done
            quit - quit playing chess
            help - see possible commands
            """;
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
        String resultString = "";
        ArrayList<GameData> games = server.listGames();

        if(games.size() == 0){return "No games to display";}
        int number = 1;
        for(GameData game: games){
            resultString += game.noIDToString(number);
            resultString += "\n";
            number++;
        }
        resultString = resultString.substring(0, resultString.length()-1);
        return resultString;
    }

    //join <ID> [WHITE|BLACK] - a game
    public String joinGame(String... params) throws ResponseException{
        if(params.length >= 2){
            //translate game number to gameID
            ArrayList<GameData> games = server.listGames();
            GameData game = games.get(Integer.parseInt(params[0]) - 1); //convert to 0 indexing
            int id = game.gameID();
            Response.JoinGameResponse response = server.joinGame(params[1], id);
            gameID = id; //set gameID to joined gameID
            state = State.GAME;

            //get color
            int temp;
            ChessGame.TeamColor color = null;
            if(params[1].toLowerCase().equals("white")){
                color= ChessGame.TeamColor.WHITE;
                temp = 0;
            }
            else if(params[1].toLowerCase().equals("black")){
                color= ChessGame.TeamColor.BLACK;
                temp = 1;
            }
            else{throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK]");}

            //create WS and join
            ws = new WebSocketCommunicator(serverUrl);
            ws.role = temp;
            ws.joinPlayer(server.getAuthToken(), gameID, color);
            return "Successfully joined game";
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK]");
    }

    //observe <ID> - a game
    public String observeGame(String... params) throws ResponseException{
        if(params.length >= 1){
            ArrayList<GameData> games = server.listGames();
            GameData game = games.get(Integer.parseInt(params[0]) - 1); //convert to 0 indexing
            int id = game.gameID();
            Response.JoinGameResponse response = server.joinGame("", id);
            state = State.GAME;
            gameID = id;
            //create WS and join
            ws = new WebSocketCommunicator(serverUrl);
            ws.joinObserver(server.getAuthToken(), gameID);
            ws.role = -1;
            return "Successfully joined game as an observer";
        }
        throw new ResponseException(400, "Expected: <ID>");
    }
    public void setColor(){
        System.out.print(SET_TEXT_COLOR_WHITE);
    }
}


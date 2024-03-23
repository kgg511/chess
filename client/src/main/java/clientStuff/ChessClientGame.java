package clientStuff;

import exception.ResponseException;
import ui.DrawChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static ui.EscapeSequences.*;
import clientStuff.webSocketClient.WebSocketCommunicator;
//stuff using websocket does not need to return a string that is done by doMessage
public class ChessClientGame implements ChessClientInterface{
    public final ServerFacade server;
    private final String serverUrl;
    private final DrawChessBoard drawer = new DrawChessBoard();
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private State state = State.GAME;
    private WebSocketCommunicator ws;
    private int gameID; //this is GAMEID, just remember that the user typed in game number

    public ChessClientGame(int port, String host, ServerFacade f, int gameID){
        this.serverUrl = host + ":" + port;
        if(f != null){server = f;}
        else{server = new ServerFacade(port, host);}
        gameID = gameID;
    }

    @Override
    public int getGameID() {
        return gameID;
    }
    public void setColor(){System.out.print(SET_TEXT_COLOR_GREEN);}
    public ServerFacade getFacade(){return this.server;}
    public State getState(){return this.state;}
    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            String returnValue = "";
            switch (cmd) {
                case "help":
                    returnValue = help();
                    break;
                case "quit":
                    returnValue = "quit";
                    break;
                case "redraw":
                    redrawBoard(params);
                    break;
                case "leave":
                    leaveGame();
                    break;
                case "move":
                    makeMove(params);
                case "resign":
                    resignGame();
                    break;
                case "legal":
                    highlightLegal(params);
                    break;
                default:
                    returnValue = help();
                    break;
            };
            return returnValue;
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    public String help(){
        System.out.println(SET_TEXT_COLOR_BLUE);
        return """
            redraw - Redraw the chessboard
            leave - Exit game
            move <chessmove> - move piece at start position to end position 'EX: b4c4'
            resign - forfeit game
            legal <position> - highlight the legal moves for the selected piece
            quit - quit playing chess
            help - see possible commands
            """;
    }

    private void redrawBoard(String... params) throws ResponseException{
        //server
        if(params.length == 1){
            throw new ResponseException(111, "quiet");
        }

    }

    private void leaveGame() throws ResponseException{
        state = State.SIGNEDIN; //transition to logged in UI
        ws.leaveGame(server.getAuthToken(), gameID); //edits DB & removes connection on serverside
        ws = null; //no more websocket for you
    }

    private void makeMove(String... params) throws ResponseException{
        if(params.length >= 2){
            //TODO: verify move on front end
            //TODO: observers can't make moves!
            //TODO: ask about upgrade piece!

            ws.makeMove(server.getAuthToken(), gameID, params[0]);
        }
        else{
            throw new ResponseException(400, "Expected: <chessmove> 'EX: b4c4'");
        }
    }

    private void resignGame() throws ResponseException{
        ws.resignGame(server.getAuthToken(), gameID);
    }

    private void highlightLegal(String... params) throws ResponseException{ //https
        //we need to gameboard
        //we need a function to draw the board but highlight the certain squares
        //
        if(params.length >= 1){

        }
        else{
            throw new ResponseException(400, "Expected: <position>");
        }

    }

}


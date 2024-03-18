package clientStuff;

import exception.ResponseException;
import ui.DrawChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class ChessClientGame implements ChessClientInterface{
    public final ServerFacade server;
    private final String serverUrl;
    private final DrawChessBoard drawer = new DrawChessBoard();
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private State state = State.GAME;

    public ChessClientGame(int port, String host, ServerFacade f){
        this.serverUrl = host + ":" + port;
        if(f != null){server = f;}
        else{server = new ServerFacade(port, host);}
    }
    public void setColor(){System.out.print(SET_TEXT_COLOR_GREEN);}
    public ServerFacade getFacade(){return this.server;}
    public State getState(){return this.state;}
    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help"-> help();
                case "quit" -> "quit";
                case "redraw" -> redrawBoard();
                case "leave" -> leaveGame();
                case "move" -> makeMove(params);
                case "resign" -> resignGame();
                case "legal" -> highlightLegal(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    public String help(){
        System.out.println(SET_TEXT_COLOR_BLUE);
        return """
            redraw - Redraw the chessboard
            leave - Exit game
            move <start position> <end position> - move piece at start position to end position
            resign - forfeit game
            legal <position> - highlight the legal moves for the selected piece
            quit - quit playing chess
            help - see possible commands
            """;
    }

    private String redrawBoard(){}

    private String leaveGame(){}

    private String makeMove(String... params){} //TODO: is this chessmove object?

    private String resignGame(){
        
    }

    private String highlightLegal(String... params){}

}


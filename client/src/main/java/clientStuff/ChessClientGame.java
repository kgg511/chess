package clientStuff;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import model.GameData;
import ui.DrawChessBoard;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static ui.EscapeSequences.*;
import clientStuff.webSocketClient.WebSocketCommunicator;
import java.util.Scanner;
public class ChessClientGame implements ChessClientInterface{
    public final ServerFacade server;
    private final String serverUrl;
    private final DrawChessBoard drawer = new DrawChessBoard();
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private State state = State.GAME;
    private WebSocketCommunicator ws;
    private int gameID; //this is GAMEID, just remember that the user typed in game number
    public ChessClientGame(int port, String host, ServerFacade f, int gameID, WebSocketCommunicator ws) throws ResponseException{
        this.serverUrl = host + ":" + port;
        if(f != null){server = f;}
        else{server = new ServerFacade(port, host);}
        this.gameID = gameID;
        this.ws = ws;
        if(this.ws == null){System.out.println("CHESSCLIENTGAME RECIVED A NULL WS!");}
    }

    public WebSocketCommunicator getWS(){return ws;}
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
                    redrawBoard();
                    break;
                case "leave":
                    leaveGame();
                    break;
                case "move":
                    makeMove(params);
                    break;
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

    private void redrawBoard() throws ResponseException {
        //need to know if they are white, black, observer...pass in gid
        drawer.drawBoards(ws.role, getChessGame().getBoard(), out, false, null);
    }
    private chess.ChessGame getChessGame() throws ResponseException{
        ArrayList<GameData> games = server.listGames();
        GameData game = null;
        for(GameData g: games){if(g.gameID() == gameID){game=g;}}
        if(game == null){throw new ResponseException(400, "game associated with user does not exist");}
        return game.game();
    }

    private void leaveGame() throws ResponseException{
        state = State.SIGNEDIN; //transition to logged in UI
        ws.leaveGame(server.getAuthToken(), gameID); //edits DB & removes connection on serverside

    }
    private boolean canUpgrade(ChessMove move) throws ResponseException{
        chess.ChessGame g = getChessGame();
        // pawn in starting position
        // pawn moving to end row
        // move is valid
        if(!(g.getBoard().getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN)){
            return false;}
        if(!(move.getEndPosition().getRow() == 1 || move.getEndPosition().getRow() == 8)){
            return false;}

        for(ChessMove mov: g.validMoves(move.getStartPosition())){
            if(mov.getEndPosition().getRow() == move.getEndPosition().getRow() && mov.getEndPosition().getColumn() == move.getEndPosition().getColumn()){
                return true;
            }
        }
        return false;
    }

    private void makeMove(String... params) throws ResponseException{
        if(params.length >= 1){
            ChessMove m = convertMoveToCoords(params[0]);

            chess.ChessGame g = getChessGame();
            if(canUpgrade(m)){
                Scanner scanner = new Scanner(System.in);
                String upgrade = "";
                while(!upgrade.equals("ROOK") && !upgrade.equals("BISHOP") && !upgrade.equals("KNIGHT") && !upgrade.equals("QUEEN")){
                    System.out.println("Please specify which piece you would like to upgrade your pawn to: [ROOK|BISHOP|KNIGHT|QUEEN]");
                    upgrade = scanner.nextLine().toUpperCase();
                }
                ChessPiece.PieceType type = null;
                switch(upgrade){
                    case "ROOK": type = ChessPiece.PieceType.ROOK; break;
                    case "BISHOP": type = ChessPiece.PieceType.BISHOP; break;
                    case "KNIGHT": type = ChessPiece.PieceType.KNIGHT; break;
                    case "QUEEN": type = ChessPiece.PieceType.QUEEN; break;
                }
                m = new ChessMove(m.getStartPosition(), m.getEndPosition(), type);
            }

            ws.makeMove(server.getAuthToken(), gameID, m);
        }
        else{
            throw new ResponseException(400, "Expected: <chessmove> 'EX: b4c4'");
        }
    }

    private void resignGame() throws ResponseException{
        ws.resignGame(server.getAuthToken(), gameID);
    }
    private void highlightLegal(String... params) throws ResponseException{ //https
        if(params.length >= 1){ //'e6'
            chess.ChessGame game = getChessGame();
            String position = params[0];
            int col = position.charAt(0) - 'a' + 1; //letter gives column, convert to 1 indexing
            int row = Character.getNumericValue(position.charAt(1));
            chess.ChessPosition p = new chess.ChessPosition(row, col);
            Collection<ChessMove> moves = game.validMoves(p);
            if(moves == null){throw new ResponseException(400, "No possible moves from this position");}
            drawer.drawBoards(ws.role, game.getBoard(), out, true, moves);
        }
        else{
            throw new ResponseException(400, "Expected: <position>");
        }
    }

    private ChessMove convertMoveToCoords(String move){ //e6
        String start = move.substring(0, 2); //index 0-1
        String end = move.substring(2); //index 2 to the end

        int col1 = start.charAt(0) - 'a' + 1; //letter gives column, convert to 1 indexing
        int row1 = Character.getNumericValue(start.charAt(1));;

        int col2 = end.charAt(0) - 'a' + 1; //convert to 1 indexing
        int row2 = Character.getNumericValue(end.charAt(1));

        System.out.println(col1 + "," + row1 + " Move to " + col2 + "," + row2);

        ChessPosition p1 = new ChessPosition(row1, col1);
        ChessPosition p2 = new ChessPosition(row2, col2);

        return new ChessMove(p1,p2, null); //put piece there
    }

}


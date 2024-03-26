package webSocketMessages.userCommands;
import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private ChessMove move;

    public MakeMoveCommand(Integer gameID, ChessMove move, String authToken) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
        commandType = CommandType.MAKE_MOVE;
    }
    public ChessMove getMove(){return move;}
}

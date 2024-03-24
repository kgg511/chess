package webSocketMessages.userCommands;
import chess.ChessGame;
public class MakeMoveCommand extends UserGameCommand {
    private String move;

    public MakeMoveCommand(String authToken, int gameID, String move) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
        commandType = CommandType.MAKE_MOVE;
    }
    public String getMove(){return move;}
}

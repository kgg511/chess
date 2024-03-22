package webSocketMessages.userCommands;
import chess.ChessGame;
public class MakeMoveCommand extends UserGameCommand {
    private int gameID;
    private String move;
    protected CommandType commandType = CommandType.MAKE_MOVE;

    public MakeMoveCommand(String authToken, int gameID, String move) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
    }
    public int getGameID() {
        return gameID;
    }
    public String getMove(){return move;}
}

package webSocketMessages.userCommands;
import chess.ChessGame;
public class JoinPlayerCommand extends UserGameCommand{
    private ChessGame.TeamColor color;

    public JoinPlayerCommand(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameID = gameID;
        this.color = playerColor;
        commandType = CommandType.JOIN_PLAYER;

    }
    public ChessGame.TeamColor getColor(){return color;}
}

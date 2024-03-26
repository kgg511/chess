package webSocketMessages.userCommands;
import chess.ChessGame;
public class JoinPlayerCommand extends UserGameCommand{
    private ChessGame.TeamColor playerColor;

    public JoinPlayerCommand(Integer gameID, ChessGame.TeamColor playerColor, String authToken) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = playerColor;
        commandType = CommandType.JOIN_PLAYER;

    }
    public ChessGame.TeamColor getPlayerColor(){return playerColor;}
}

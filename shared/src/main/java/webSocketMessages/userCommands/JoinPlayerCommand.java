package webSocketMessages.userCommands;
import chess.ChessGame;
public class JoinPlayerCommand extends UserGameCommand{
    //mmm session, playerusername, gameid, color?
    private int gameID;
    private ChessGame.TeamColor color;

    protected CommandType commandType = CommandType.JOIN_PLAYER;

    public JoinPlayerCommand(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameID = gameID;
        this.color = playerColor;

    }
    public int getGameID(){return gameID;}
    public ChessGame.TeamColor getColor(){return color;}
}

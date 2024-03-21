package webSocketMessages.userCommands;
import chess.ChessGame;
public class JoinPlayerCommand extends UserGameCommand{
    //mmm session, playerusername, gameid, color?
    private int gameID;
    private ChessGame.TeamColor color;

    public JoinPlayerCommand(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameID = gameID;
        this.color = playerColor;

    }
    private int getGameID(){return gameID;}
    private ChessGame.TeamColor getColor(){return color;}
}

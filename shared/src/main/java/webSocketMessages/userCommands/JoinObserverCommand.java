package webSocketMessages.userCommands;
public class JoinObserverCommand extends UserGameCommand {
    private int gameID;
    protected CommandType commandType = CommandType.JOIN_OBSERVER;

    public JoinObserverCommand(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
    }
    public int getGameID() {
        return gameID;
    }
}
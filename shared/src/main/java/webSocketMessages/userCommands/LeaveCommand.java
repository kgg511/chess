package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
    private int gameID;
    protected CommandType commandType = CommandType.LEAVE;

    public LeaveCommand(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
    }
    private int getGameID() {
        return gameID;
    }
}
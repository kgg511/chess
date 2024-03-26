package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
    public LeaveCommand(Integer gameID, String authToken) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.LEAVE;
    }
}
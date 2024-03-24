package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {

    public ResignCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.RESIGN;
    }
}
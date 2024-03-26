package webSocketMessages.userCommands;
public class ResignCommand extends UserGameCommand {
    public ResignCommand(Integer gameID, String authToken) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.RESIGN;
    }
}
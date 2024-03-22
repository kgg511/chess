package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {
    private int gameID;
    protected CommandType commandType = CommandType.RESIGN;

    public ResignCommand(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
    }
    public int getGameID() {
        return gameID;
    }
}
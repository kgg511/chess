package webSocketMessages.userCommands;
public class JoinObserverCommand extends UserGameCommand {

    public JoinObserverCommand(Integer gameID, String authToken) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.JOIN_OBSERVER;
    }
}
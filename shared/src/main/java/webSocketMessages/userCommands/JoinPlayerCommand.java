package webSocketMessages.userCommands;

public class JoinPlayerCommand extends UserGameCommand{
    //mmm session, playerusername, gameid, color?
    public JoinPlayerCommand(String authToken) {
        super(authToken);

    }
}

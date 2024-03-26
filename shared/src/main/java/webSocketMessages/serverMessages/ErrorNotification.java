package webSocketMessages.serverMessages;

public class ErrorNotification extends ServerMessage{
    String errorMessage = "";
    public ErrorNotification(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage= errorMessage;
    }

    public String getMessage(){return "Error: " + errorMessage;} //message must include the word error
}

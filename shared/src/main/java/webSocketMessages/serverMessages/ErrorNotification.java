package webSocketMessages.serverMessages;

public class ErrorNotification extends ServerMessage{
    ServerMessageType serverMessageType = ServerMessageType.ERROR;
    String message = "";
    public ErrorNotification(String message) {
        super(ServerMessageType.ERROR);
        this.message = message;
    }

    public String getMessage(){return "Error: " + message;} //message must include the word error
}

package webSocketMessages.serverMessages;

public class MessageNotification extends ServerMessage{
    String message = "";
    public MessageNotification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage(){return message;}



}

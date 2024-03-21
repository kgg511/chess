package webSocketMessages.serverMessages;

public class MessageNotification extends ServerMessage{
    ServerMessageType serverMessageType = ServerMessageType.NOTIFICATION;

    String message = "";
    public MessageNotification(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    public String toString(){
        return message;
    }


}

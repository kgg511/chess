package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGameNotification extends ServerMessage{ //sends game to client
    ServerMessageType serverMessageType = ServerMessageType.LOAD_GAME;
    ChessGame message = null;
    public LoadGameNotification(ChessGame message) {
        super(ServerMessageType.LOAD_GAME);
        this.message = message;
    }
    public ChessGame getMessage(){return message;}

}

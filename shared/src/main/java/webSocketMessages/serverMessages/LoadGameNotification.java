package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGameNotification extends ServerMessage{ //sends game to client
    ChessGame message;
    public LoadGameNotification(ChessGame message) {
        super(ServerMessageType.LOAD_GAME);
        this.message = message;
    }
    public ChessGame getMessage(){return message;}

}

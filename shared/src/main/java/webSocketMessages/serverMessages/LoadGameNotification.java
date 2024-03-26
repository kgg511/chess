package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGameNotification extends ServerMessage{ //sends game to client
    ChessGame game;
    public LoadGameNotification(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
    public ChessGame getGame(){return this.game;}


}

package clientStuff.webSocketClient;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class WebSocketCommunicator extends Endpoint{
    Session session;
    DoMessage doMessage = new DoMessage();
    public int role;
    //we don't return strings, we send the messages to websocket then when we hear a response back
    //the repsonse is the output
    public WebSocketCommunicator(String url) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer(); //create websocket
            this.session = container.connectToServer(this, socketURI); //connect to websocket
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
                    System.out.println(msg.getServerMessageType());
                    switch (msg.getServerMessageType()) {
                        case LOAD_GAME:
                            //convert to load game, use the game
                            LoadGameNotification load = new Gson().fromJson(message, LoadGameNotification.class);
                            doMessage.drawGame(load.getGame(), role);
                            break;
                        case NOTIFICATION:
                            MessageNotification notification = new Gson().fromJson(message, MessageNotification.class);
                            doMessage.messageUser(notification.getMessage());
                            break;
                        case ERROR:
                            //IDK unpack the error and print it to the console I guess
                            ErrorNotification error = new Gson().fromJson(message, ErrorNotification.class);
                            doMessage.giveError(error.getMessage());
                            break;
                    }
                }
            });
        }
        catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    @Override
    public void onClose(Session session, CloseReason closeReason) {
        // Implement your logic when the WebSocket connection is closed
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println("WebSocket connection closed." + closeReason.toString());
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("WebSocket connection opened.");
        System.out.print(SET_TEXT_COLOR_GREEN);
    }

    public void leaveGame(String authToken, int gameID) throws ResponseException{
        try{
            LeaveCommand cmd = new LeaveCommand(gameID, authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) throws ResponseException{ //this sends the message to the websocket
        //yes this is in loggedinclient but it uses WS so it goes here
        try{
            JoinPlayerCommand cmd = new JoinPlayerCommand(gameID, playerColor, authToken); //send JOIN_PLAYER
            this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinObserver(String authToken, int gameID) throws ResponseException{ //this sends the message to the websocket
        //yes this is in loggedinclient but it uses WS so it goes here
        try{
            JoinObserverCommand cmd = new JoinObserverCommand(gameID,authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    public void makeMove(String authToken, int gameID, String move) throws ResponseException{ //actually send to websocket
        try{
            ChessMove m = convertMoveToCoords(move);
            MakeMoveCommand cmd = new MakeMoveCommand(gameID, m, authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    public void resignGame(String authToken, int gameID) throws ResponseException{
        try{
            ResignCommand cmd = new ResignCommand(gameID, authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }

    }

    private ChessMove convertMoveToCoords(String move){ //e6
        String start = move.substring(0, 2); //index 0-1
        String end = move.substring(2); //index 2 to the end

        int col1 = start.charAt(0) - 'a' + 1; //letter gives column, convert to 1 indexing
        int row1 = Character.getNumericValue(start.charAt(1));;

        int col2 = end.charAt(0) - 'a' + 1; //convert to 1 indexing
        int row2 = Character.getNumericValue(end.charAt(1));

        System.out.println(col1 + "," + row1 + " Move to " + col2 + "," + row2);

        ChessPosition p1 = new ChessPosition(row1, col1);
        ChessPosition p2 = new ChessPosition(row2, col2);

        return new ChessMove(p1,p2,null);
    }
}

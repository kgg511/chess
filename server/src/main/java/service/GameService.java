package service;

import dataAccess.DataAccessException;
import exception.ResponseException;
import chess.*;
import model.*;
import service.BaseService;
import webSocketMessages.serverMessages.LoadGameNotification;
import webSocketMessages.serverMessages.MessageNotification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketServer.GameConnectionManager;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.ArrayList;

public class GameService extends BaseService {
    private final GameConnectionManager connections;
    private Session session;

    public GameService(GameConnectionManager connections, Session sender) throws DataAccessException, ResponseException {
        super();
        this.connections = connections;
        this.session = sender;
    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor color) throws ResponseException, DataAccessException{
        //String authToken, Integer gameID, ChessGame.TeamColor playerColor
        //figure out username
        //you have to put in player Username
        //find the game in the database, put the player in there
        ArrayList<AuthData> data = getAuthDB().getAuthByToken(authToken);
        if(data.size() == 0){return;} //you can't
        String username = data.get(0).username();

        GameData g = getGameDB().getGameById(gameID);
        GameData updated = null;
        if(color == ChessGame.TeamColor.WHITE){ //updating white
            updated = new GameData(g.gameID(), username, g.blackUsername(), g.gameName(), g.game());
        }
        else{updated = new GameData(g.gameID(), g.whiteUsername(), username, g.gameName(), g.game());}

        boolean changed = getGameDB().updateGame(updated);

        //game service is going to return just what I need?
        //WAIT, this is ALREADY a service????
        //

    }
    public void joinObserver(){


    }

    private ChessMove convertMoveToCoords(String move){ //e6
        String start = move.substring(0, 2); //index 0-1
        String end = move.substring(2); //index 2 to the end

        int col1 = start.charAt(0) - 'a' + 1; //letter gives column, convert to 1 indexing
        int row1 = (int) start.charAt(1);

        int col2 = end.charAt(0) - 'a' + 1; //convert to 1 indexing
        int row2 = (int) end.charAt(1);

        System.out.println(col1 + "," + row1 + " " + col2 + "," + row2);

        ChessPosition p1 = new ChessPosition(row1, col1);
        ChessPosition p2 = new ChessPosition(row2, col2);

        return new ChessMove(p1,p2,null);

    }
    //ERROR HANDLING????
    public void makeMove(String authToken, int gameID, String move) throws dataAccess.DataAccessException, exception.ResponseException{
        try{
            ChessMove pMove = convertMoveToCoords(move);
            GameData data = getGameDB().getGameById(gameID);
            ChessGame game = data.game();

            //verify & make move, update game in database
            game.makeMove(pMove);
            GameData updated = new GameData(data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), game);
            getGameDB().updateGame(updated);

            //load game to all (including client)
            ServerMessage message = new LoadGameNotification(game);
            connections.sendToSession(session, message);
            connections.broadcast(gameID, session, message);

            //Notification to all but the client that move has been made
            String moveMessage = authToken + " made the move " + move;
            ServerMessage notification = new MessageNotification(moveMessage);
            connections.broadcast(gameID, session, notification);

        }
        catch(InvalidMoveException e){
            throw new ResponseException(400, "Not a valid chess move:" + e.toString());
        }
        catch(java.io.IOException e){
            throw new ResponseException(400, "Websocket error:" + e.toString());
        }

    }

    public void leaveGame(){}

    public void resignGame(){}





}

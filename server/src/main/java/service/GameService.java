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

import java.util.ArrayList;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class GameService extends BaseService {
    private final GameConnectionManager connections;
    private Session session;

    public GameService(GameConnectionManager connections, Session sender) throws DataAccessException, ResponseException {
        super();
        this.connections = connections;
        this.session = sender;
    }
    public String tokenToUsername(String authToken) throws DataAccessException, ResponseException{
        AuthData data = getAuthDB().getAuthByToken(authToken);
        if(data == null){throw new ResponseException(400, "AuthToken doesn't exist");}
        return data.username();
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
        if(color == WHITE){ //updating white
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

    private void doneCases(int gameID, ChessGame game, ChessGame.TeamColor colorOpposing) throws java.io.IOException{
        //after each move check if either side is in stalemate
        //you can't move yourself into check/checkmate so check opposing after move
        MessageNotification msg = null;
        if(game.checkStale(WHITE) || game.checkStale(ChessGame.TeamColor.BLACK)){
            msg = new MessageNotification("stalemate");
        }
        else if(game.isInCheck(colorOpposing)){
            msg = new MessageNotification(colorOpposing + " is in check!");
        }
        else if(game.isInCheckmate(colorOpposing)){
            msg = new MessageNotification(colorOpposing + " is in checkmate!");
        }
        connections.sendToSession(session, msg);
        connections.broadcast(gameID, session, msg);
    }
    //ERROR HANDLING????
    public void makeMove(String authToken, int gameID, String move) throws dataAccess.DataAccessException, exception.ResponseException{
        try{
            ChessMove pMove = convertMoveToCoords(move);
            GameData data = getGameDB().getGameById(gameID);
            ChessGame game = data.game();
            String username = tokenToUsername(authToken);
            ChessGame.TeamColor colorOpposing;

            if(data.whiteUsername().equals(username)){colorOpposing = BLACK;}
            else if(data.blackUsername().equals(username)){colorOpposing = WHITE;}
            else{throw new ResponseException(400, "you are not a player how did you get here");}

            //verify move
            if(game.getBoard().getPiece(pMove.getStartPosition()).getTeamColor() == colorOpposing){throw new ResponseException(400, "You can't move your opponent's pieces!");}
            game.makeMove(pMove);
            GameData updated = new GameData(data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), game);
            getGameDB().updateGame(updated);

            //load game to all (including client)
            ServerMessage message = new LoadGameNotification(game);
            connections.sendToSession(session, message);
            connections.broadcast(gameID, session, message);

            //Notification to all but the client that move has been made
            String moveMessage = username + " made the move " + move;
            ServerMessage notification = new MessageNotification(moveMessage);
            connections.broadcast(gameID, session, notification);

            doneCases(gameID, game, colorOpposing); //check stale/check/checkmate

        }
        catch(InvalidMoveException e){
            throw new ResponseException(400, "Not a valid chess move:" + e.toString());
        }
        catch(java.io.IOException e){
            throw new ResponseException(400, "Websocket error:" + e.toString());
        }

    }

    public void leaveGame(String authToken, int gameID){

        //remove client from game

        //remove websocket connection


    }

    public void resignGame(){}





}

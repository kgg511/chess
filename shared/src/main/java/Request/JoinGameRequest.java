package Request;

import chess.ChessGame;

public record JoinGameRequest(String playerColor, int gameID){

    public String toString(){
        return "joinGameRequest color: " + playerColor + " id: " + gameID;
    }
}

package model;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName,
                       chess.ChessGame game){


    public String toString(){
        return "gameid: " + gameID + ", white:" + whiteUsername + ", black:" + blackUsername + ", name:" + gameName;
    }
}



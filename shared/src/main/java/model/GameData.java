package model;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName,
                       chess.ChessGame game){


    public String toString(){
        return "Game ID: " + gameID + ", Name: " + gameName + ", white player: " + whiteUsername + ", black player: " + blackUsername;
    }

    public String noIDToString(int number){
        String white = whiteUsername;
        String black = blackUsername;
        if(white == null){white = "No player";}
        if(black == null){black = "No player";}
        return "#" + number + ", Name: " + gameName + ", white player: " + white + ", black player: " + black;
    }
}



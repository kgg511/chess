package model;

public record Game(int gameID, String whiteUsername, String blackUsername, String gameName,
                   chess.ChessGame game){}



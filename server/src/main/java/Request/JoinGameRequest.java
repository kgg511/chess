package Request;

import chess.ChessGame;

public record JoinGameRequest(ChessGame.TeamColor clientColor, int gameID){}

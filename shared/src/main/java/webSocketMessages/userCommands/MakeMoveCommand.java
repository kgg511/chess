package webSocketMessages.userCommands;
import chess.ChessMove;
public record MakeMoveCommand(int gameID, ChessMove move) {
}

package clientStuff.webSocketClient;

import chess.ChessGame;
import ui.DrawChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class DoMessage {
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final DrawChessBoard drawer = new DrawChessBoard();

    public void messageUser(String message){
        System.out.println(message);
    }

    public void giveError(String message) {
        System.out.print(SET_TEXT_COLOR_RED);
        System.out.println(message);
        System.out.print(SET_TEXT_COLOR_GREEN); //undo
    }
    public void drawGame(ChessGame game){
        System.out.println("yaya i got a load game");
        drawer.drawBoards(game.getBoard(), out, false, null);
    }

}

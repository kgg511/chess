package clientStuff.webSocketClient;

import chess.ChessGame;
import ui.DrawChessBoard;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class DoMessage {
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final DrawChessBoard drawer = new DrawChessBoard();

    public void messageUser(String message){
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println(message);
        System.out.print(SET_TEXT_COLOR_GREEN);
        System.out.print("\n" + "[GAME]" + ">>> ");
    }

    public void giveError(String message) {
        System.out.print(SET_TEXT_COLOR_RED);
        System.out.println(message);
        System.out.print(SET_TEXT_COLOR_GREEN); //undo
        System.out.print("\n" + "[GAME]" + ">>> ");
    }
    public void drawGame(ChessGame game, int role){
        drawer.drawBoards(role, game.getBoard(), out, false, null);
        System.out.print(SET_TEXT_COLOR_GREEN);
        if(game.getTeamTurn() == WHITE){System.out.println("White's Turn");}
        else if(game.getTeamTurn() == BLACK){System.out.println("Black's Turn");}
        System.out.print("[GAME]" + ">>> ");



    }

}

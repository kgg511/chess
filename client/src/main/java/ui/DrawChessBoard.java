package ui;
import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 2;
    private String[] headerLetters = {"h", "g", "f", "e", "d", "c", "b", "a"};
    private String[] headerLettersF = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private String[] headerNumbers = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private String[] headerNumbersF = {"8", "7", "6","5", "4", "3", "2", "1"};
    private static final int LINE_WIDTH_IN_CHARS = 1;

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var b = new ChessBoard();
        b.resetBoard();
        var draw = new DrawChessBoard();
        out.print(ERASE_SCREEN);
        draw.drawBoardRegular(b, out, false, null);
    }

    public void drawBoards(ChessBoard b, PrintStream out, boolean highlight, Collection<ChessMove> moves){
        drawBoardRegular(b, out, highlight, moves);
        out.println();
        drawBoardFlipped(b, out, highlight, moves);
    }

    public void drawBoardRegular(ChessBoard b, PrintStream out, boolean highlight, Collection<ChessMove> moves){
        boolean black = false;
        drawHeaders(out, headerLetters);
        for(int i = 0; i < BOARD_SIZE_IN_SQUARES; i++){
            drawRow(false, b, out, black, i, headerNumbers, highlight, moves);
            black = !black;
        }
        drawHeaders(out, headerLetters);
    }
    public void drawBoardFlipped(ChessBoard b, PrintStream out, boolean highlight, Collection<ChessMove> moves){
        boolean black = false;
        drawHeaders(out, headerLettersF);
        for(int i = BOARD_SIZE_IN_SQUARES - 1; i >= 0; i--){
            drawRow(true, b, out, black, i, headerNumbers, highlight, moves);
            black = !black;
        }
        drawHeaders(out, headerLettersF);
    }

    private static void drawHeaders(PrintStream out, String[] headers) {
        //out.print(SET_BG_COLOR_BLACK);
        out.print("  ");
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
            out.print(" ");
            drawHeader(out, headers[boardCol]);
            out.print(" ");
        }
        out.println();
    }
    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;
        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }
    private static void printHeaderText(PrintStream out, String headerText) {
        //out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(headerText);
    }

    private void drawRow(boolean flipped, ChessBoard board, PrintStream out, boolean isBlack, int row,
                         String[] header, boolean highlight, Collection<ChessMove> moves){
        boolean black = isBlack;
        boolean doHighlight = false;
        if(!flipped){
            for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; squareRow++) {
                for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES + 2; boardCol++) { //add two for headers}
                    doHighlight = yesHighlight(squareRow, boardCol, moves, highlight);
                    doSquare(board, out, black, row, header, squareRow, boardCol, doHighlight);
                    black = !black;
                }
                out.println();
            }
        }
        else if(flipped){
            for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; squareRow++) {
                for (int boardCol = BOARD_SIZE_IN_SQUARES + 1; boardCol >= 0; boardCol--) { //add two for headers}
                    doHighlight = yesHighlight(squareRow, boardCol, moves, highlight);
                    doSquare(board, out, black, row, header, squareRow, boardCol, doHighlight);
                    black = !black;
                }
                out.println();
            }
        }
    }

    private boolean yesHighlight(int row, int col, Collection<ChessMove> moves, boolean highlight){
        // returns true if square should be highlighted
        if(highlight){
            for(ChessMove move: moves){
                ChessPosition p = move.getEndPosition();
                if(p.getRow() - 1 == row && p.getColumn() - 1 == col){return true;}
            }
        }
        return false;
    }

    private void doSquare(ChessBoard board, PrintStream out, boolean isBlack, int row, String[] header,
                          int squareRow, int boardCol, boolean doHighlight) {
        boolean black = isBlack;
        if((boardCol == 0 || boardCol == BOARD_SIZE_IN_SQUARES + 1)){
            if((squareRow == SQUARE_SIZE_IN_CHARS / 2)){
                drawHeader(out, header[row]);
            }
            else{drawHeader(out, " ");}
        }
        else{
            setColor(out, black);
            if(doHighlight){setBlue(out);}
            if (squareRow == SQUARE_SIZE_IN_CHARS / 2) { //halfway down put it
                int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
                int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;
                out.print(EMPTY.repeat(prefixLength));

                ChessPosition p = new ChessPosition(row + 1, boardCol);
                ChessPiece piece = board.getPiece(p);
                if(piece != null){printPlayer(out, piece);}
                else{out.print(EMPTY);}
                out.print(EMPTY.repeat(suffixLength));
            }
            else {
                out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
            }
            out.print("\u001B[49m");
        }

    }
    private static void printPlayer(PrintStream out, ChessPiece piece) {
        ChessPiece.PieceType type = piece.getPieceType();
        String unicode = "";
        if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            out.print(SET_TEXT_COLOR_BLACK);
            switch (type) {
                case ChessPiece.PieceType.KING: unicode = BLACK_KING; break;
                case ChessPiece.PieceType.QUEEN: unicode = BLACK_QUEEN; break;
                case ChessPiece.PieceType.ROOK: unicode = BLACK_ROOK; break;
                case ChessPiece.PieceType.BISHOP: unicode = BLACK_BISHOP; break;
                case ChessPiece.PieceType.KNIGHT: unicode = BLACK_KNIGHT; break;
                case ChessPiece.PieceType.PAWN: unicode = BLACK_PAWN; break;
                default: unicode = ":O";
            }
        }
        else if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            out.print(SET_TEXT_COLOR_WHITE);
            switch (type) {
                case ChessPiece.PieceType.KING: unicode = WHITE_KING; break;
                case ChessPiece.PieceType.QUEEN: unicode = WHITE_QUEEN; break;
                case ChessPiece.PieceType.ROOK: unicode = WHITE_ROOK; break;
                case ChessPiece.PieceType.BISHOP: unicode = WHITE_BISHOP; break;
                case ChessPiece.PieceType.KNIGHT: unicode = WHITE_KNIGHT; break;
                case ChessPiece.PieceType.PAWN: unicode = WHITE_PAWN; break;
                default: unicode = ":O";
            }
        }
        out.print(unicode);
    }
    private static void setColor(PrintStream out, boolean black){
        if(black){setGrey(out);}
        else if(!black){setMagenta(out);}
    }
    private static void setGrey(PrintStream out) {out.print(SET_BG_COLOR_LIGHT_GREY);}
    private static void setMagenta(PrintStream out) {out.print(SET_BG_COLOR_MAGENTA);}
    private static void setBlue(PrintStream out) {out.print(SET_BG_COLOR_BLUE);}
}

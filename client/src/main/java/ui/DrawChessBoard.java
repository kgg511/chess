package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;



public class DrawChessBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;


    private static final int SQUARE_SIZE_IN_CHARS = 2;
    private String[] headerLetters = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private String[] headerNumbers = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private static final int LINE_WIDTH_IN_CHARS = 1;

    //If you use the chess piece characters:
    //Set your terminal font in IntelliJ to ‘Monospace’
    //File -> Settings -> Editor -> Color Scheme -> Console Font
    //The “Em Space” Unicode character \u2003 is the same width as the chess piece characters (same for the “Em Quad” character \u2001)
    //Use a combination of regular spaces and \u2003 (or \u2001) to make things align properly
//ChessBoard board

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var b = new ChessBoard();
        b.resetBoard();
        var draw = new DrawChessBoard();
        out.print(ERASE_SCREEN);
        draw.drawBoardRegular(b, out);
    }

    private static void drawHeaders(PrintStream out, String[] headers) {
        out.print(SET_BG_COLOR_BLACK);
        out.print("  ");
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
            out.print(" ");
            drawHeader(out, headers[boardCol]);
            out.print(" ");

            //out.print(EMPTY.repeat(1));

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
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(headerText);
    }

//indexes; 0,1,3

    public void drawBoardRegular(ChessBoard b, PrintStream out){
        boolean black = false;
        drawHeaders(out, headerLetters);
        for(int i = 0; i < 8; i++){
            drawRow(b, out, black, i, headerNumbers);
            black = !black;
        }
        //setBlack(out);
        drawHeaders(out, headerLetters);

    }
    private void drawRow(ChessBoard board, PrintStream out, boolean isBlack, int row, String[] header) {
        //squareRow represents thickness of the square
        //boardCol is the square we are on
        boolean black = isBlack;
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; squareRow++) {
            for (int boardCol = 0; boardCol < 10; boardCol++) { //add two for headers

                if((boardCol == 0 || boardCol == 9)){
                    if((squareRow == SQUARE_SIZE_IN_CHARS / 2)){
                        drawHeader(out, header[row]);
                    }
                    else{drawHeader(out, " ");}
                }
                else{
                    setColor(out, black);
                    black = !black; //flip color for next round
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
                    out.print(RESET_BG_COLOR);
                }
            }
            out.println();
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

    public void drawBoardFlipped(GameData game){
        return;
    }

    private static void setColor(PrintStream out, boolean black){
        if(black){setGrey(out);}
        else if(!black){setMagenta(out);}
    }
    private static void setGrey(PrintStream out) {out.print(SET_BG_COLOR_LIGHT_GREY);}
    private static void setMagenta(PrintStream out) {out.print(SET_BG_COLOR_MAGENTA);}



}

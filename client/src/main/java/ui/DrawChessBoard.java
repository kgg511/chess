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
    private static final int SQUARE_SIZE_IN_CHARS = 8;
    //chess.ChessBoard board;

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

//    private static void drawHeaders(PrintStream out) {
//        setBlack(out);
//
//        String[] headers = { "TIC", "TAC", "TOE" };
//        for (int boardCol = 0; boardCol < 8; ++boardCol) {
//            drawHeader(out, headers[boardCol]);
//
//            if (boardCol < 8- 1) {
//                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
//            }
//        }
//        out.println();
//    }


//indexes; 0,1,3

    public void drawBoardRegular(ChessBoard b, PrintStream out){
        boolean black = false;
        for(int i = 0; i < 8; i++){
            drawRow(b, out, black, i);
            black = !black;
        }

    }
    private void drawRow(ChessBoard board, PrintStream out, boolean isBlack, int row) {
        //squareRow represents thickness of the square
        //boardCol is the square we are on
        boolean black = isBlack;

        for (int squareRow = 0; squareRow < 3; squareRow++) {
            for (int boardCol = 0; boardCol < 8; boardCol++) {
                setColor(out, black);
                black = !black; //flip color for next round
                if (squareRow == SQUARE_SIZE_IN_CHARS / 2) { //halfway down put it
                    int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;
                    out.print(EMPTY.repeat(prefixLength));

                    ChessPosition p = new ChessPosition(row, boardCol);
                    ChessPiece piece = board.getPiece(p);
                    if(piece != null){printPlayer(out, piece);}
                    out.print(EMPTY.repeat(suffixLength));
                }
                else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
                }
            }
            out.println();
        }
    }

    private static void printPlayer(PrintStream out, ChessPiece piece) {
        ChessPiece.PieceType type = piece.getPieceType();
        String unicode = "";
        if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            switch (type) {
                case ChessPiece.PieceType.KING: unicode = BLACK_KING;
                case ChessPiece.PieceType.QUEEN: unicode = BLACK_QUEEN;
                case ChessPiece.PieceType.ROOK: unicode = BLACK_ROOK;
                case ChessPiece.PieceType.BISHOP: unicode = BLACK_BISHOP;
                case ChessPiece.PieceType.KNIGHT: unicode = BLACK_KNIGHT;
                case ChessPiece.PieceType.PAWN: unicode = BLACK_PAWN;
                default: unicode = ":O";
            }
        }
        else if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            switch (type) {
                case ChessPiece.PieceType.KING: unicode = WHITE_KING;
                case ChessPiece.PieceType.QUEEN: unicode = WHITE_QUEEN;
                case ChessPiece.PieceType.ROOK: unicode = WHITE_ROOK;
                case ChessPiece.PieceType.BISHOP: unicode = WHITE_BISHOP;
                case ChessPiece.PieceType.KNIGHT: unicode = WHITE_KNIGHT;
                case ChessPiece.PieceType.PAWN: unicode = WHITE_PAWN;
                default: unicode = ":O";
            }
        }
        out.print(unicode);
    }


    private static void printOnColor(PrintStream out, String backgroundColor, String text) {
        out.print(backgroundColor); // Set background color
        out.print(text); // Print the text or Unicode character
    }




    public void drawBoardFlipped(GameData game){
        return;
    }

    private static void setColor(PrintStream out, boolean black){
        if(black){setBlack(out);}
        else if(!black){setWhite(out);}
    }
    private static void setBlack(PrintStream out) { //set text and background black
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

}

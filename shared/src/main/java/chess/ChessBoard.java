package chess;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] grid;
    public ChessBoard() {
         //uncaptured pieces
        this.grid = new ChessPiece[8][8];
    }
    public void showBoard() {
        //ArrayList<ChessPiece> pieces
        System.out.println("It's time to print out the board! :)");
        //print the chess pieces
        for (int i = 0; i < this.grid.length; i++) {
            // Iterate through columns
            for (int j = 0; j < this.grid[i].length; j++) {
                System.out.print(this.grid[i][j].toString() + "POSITION:" + i + ","+ j);
            }
            // Move to the next line after printing each row
            System.out.println();
        }
    }
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if(position.getRow()-1 < 0 || position.getRow()-1 > 7 || position.getColumn()-1 < 0 || position.getColumn()-1 > 7){
            System.out.println("out of bounds");
            return;
        }
        System.out.println("ADDING" + piece.toString() + "at position " + position.toString());
        this.grid[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if(this.grid[position.getRow()-1][position.getColumn()-1] != null){
            return this.grid[position.getRow()-1][position.getColumn()-1];
        }
        System.out.println("PIECE NOT HERE");
        return null;
    }
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece w;
        ChessPiece b;
        ChessPosition pos_white;
        ChessPosition pos_black;
        for(int i = 0; i < 8; i++){ //pawns
            w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            pos_white = new ChessPosition(2, i+1);
            pos_black = new ChessPosition(7, i+1);
            addPiece(pos_white, w);
            addPiece(pos_black, b);
        }
        //king
        w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        pos_white = new ChessPosition(1, 5);
        pos_black = new ChessPosition(8, 5);
        addPiece(pos_white, w);
        addPiece(pos_black, b);

        //queen
        w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        pos_white = new ChessPosition(1, 4);
        pos_black = new ChessPosition(8, 4);
        addPiece(pos_white, w);
        addPiece(pos_black, b);

        //rook: 1,1 & 1,8 && 8,1 && 8,8
        for(int i = 1; i <= 8; i += 7){
            w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
            b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
            pos_white = new ChessPosition(1, i);
            pos_black = new ChessPosition(8, i);
            addPiece(pos_white, w);
            addPiece(pos_black, b);
        }

        //bishop: 1,3 && 8,3 && 1,6 && 8,6
        for(int i = 3; i <= 6; i += 3){
            w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
            b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
            pos_white = new ChessPosition(1, i);
            pos_black = new ChessPosition(8, i);
            addPiece(pos_white, w);
            addPiece(pos_black, b);
        }
        //horse
        for(int i = 2; i <= 7; i += 5){
            w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
            b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
            pos_white = new ChessPosition(1, i);
            pos_black = new ChessPosition(8, i);
            addPiece(pos_white, w);
            addPiece(pos_black, b);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(grid, that.grid);
    }
    @Override
    public int hashCode() {
        return Arrays.hashCode(grid);
    }
}

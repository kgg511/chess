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
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //should be 0-7 or 1-8
        if(position.getRow()-1 < 0 || position.getRow()-1 > 7 || position.getColumn()-1 < 0 || position.getColumn()-1 > 7){
            System.out.println("out of bounds");
            return;
        }

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
        //System.out.println("calling get piece on" + position.toString());
        if(this.grid[position.getRow()-1][position.getColumn()-1] != null){
            return this.grid[position.getRow()-1][position.getColumn()-1];
        }
        return null;
    }
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece w;
        ChessPiece b;
        ChessPosition posWhite;
        ChessPosition posBlack;
        for(int i = 0; i < 8; i++){ //pawns
            w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            posWhite = new ChessPosition(2, i+1);
            posBlack = new ChessPosition(7, i+1);
            addPiece(posWhite, w);
            addPiece(posBlack, b);
        }
        //king
        w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        posWhite = new ChessPosition(1, 5);
        posBlack = new ChessPosition(8, 5);
        addPiece(posWhite, w);
        addPiece(posBlack, b);

        //queen
        w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        posWhite = new ChessPosition(1, 4);
        posBlack = new ChessPosition(8, 4);
        addPiece(posWhite, w);
        addPiece(posBlack, b);

        //rook: 1,1 & 1,8 && 8,1 && 8,8
        for(int i = 1; i <= 8; i += 7){
            w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
            b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
            posWhite = new ChessPosition(1, i);
            posBlack = new ChessPosition(8, i);
            addPiece(posWhite, w);
            addPiece(posBlack, b);
        }

        //bishop: 1,3 && 8,3 && 1,6 && 8,6
        for(int i = 3; i <= 6; i += 3){
            w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
            b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
            posWhite = new ChessPosition(1, i);
            posBlack = new ChessPosition(8, i);
            addPiece(posWhite, w);
            addPiece(posBlack, b);
        }
        //horse
        for(int i = 2; i <= 7; i += 5){
            w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
            b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
            posWhite = new ChessPosition(1, i);
            posBlack = new ChessPosition(8, i);
            addPiece(posWhite, w);
            addPiece(posBlack, b);
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


    //ummmm w
    public String toString() {
        //ArrayList<ChessPiece> pieces
        String result = "";
        //print the chess pieces
        String line = "";
        for (int i = 0; i < this.grid.length; i++) {
            // Iterate through columns
            line = "";
            for (int j = 0; j < this.grid[i].length; j++) {
                if(this.grid[i][j] != null){
                    if(this.grid[i][j].getTeamColor() == ChessGame.TeamColor.WHITE){
                        line+="B";
                    }
                    else{
                        line+="W";
                    }
                    line +=  this.grid[i][j].getPieceType() + "(" + i + ","+ j + ")" + "*";
                }
                else{
                    line += " NULL(" + i + ","+ j + ")" + "*";
                }
            }
            result += line + "\n";
            // Move to the next line after printing each row
        }
        return result;
    }
}

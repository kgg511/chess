package chess;
import java.util.ArrayList;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ArrayList<ChessPiece> pieces;
    private ArrayList<ChessPosition> positions;
    public ChessBoard() {
         //uncaptured pieces
        this.pieces = new ArrayList<ChessPiece>();
        this.positions = new ArrayList<ChessPosition>();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.pieces.add(piece);
        this.positions.add(position);
    }

    public ArrayList<ChessPiece> getPieces(){
        return this.pieces;
    }
    public ArrayList<ChessPosition> getPositions(){
        return this.positions;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        //get the index of position, use that index to fetch from pieces
        System.out.println("get piece");
        int index = this.positions.indexOf(position);
        if(index == -1){
            System.out.println("No piece in this position");
            return null;
        }
        return this.pieces.get(index);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.pieces = new ArrayList<ChessPiece>();
        this.positions = new ArrayList<ChessPosition>();
    }
}

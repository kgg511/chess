package chess;

import javax.swing.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor color;
    private ChessPiece.PieceType type;
    private Collection<ChessMove> completeMoves;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
        this.completeMoves = new HashSet<>();
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.getPieceType() + " and I am " + this.getTeamColor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;

        //HashSet<ChessMove> set1 = new HashSet<>(completeMoves);
        //HashSet<ChessMove> set2 = new HashSet<>(that.completeMoves);

        return color == that.color && type == that.type && Objects.equals(this.completeMoves, completeMoves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type, completeMoves);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        //create a list of strings
        HashSet<ChessPosition> l = null;
        if(this.color == ChessGame.TeamColor.WHITE){
            //(ChessPiece.PieceType type, ChessPosition pos, ChessBoard board)

            MovesWhite m = new MovesWhite(this.type, myPosition, board);
            l = m.fillValid();
        }
        else{
            MovesBlack m = new MovesBlack(this.type, myPosition, board);
            l = m.fillValid();
        }

        ChessPiece.PieceType promotionP = null;
        ChessMove move = null;
        this.completeMoves = new HashSet<>();
        for(ChessPosition endPosition: l){ //go through the moves and make ChessMove objects
            if(this.type == PieceType.PAWN && (endPosition.getRow() == 8 || endPosition.getRow() == 1)){ //if its a pawn and end position is at end do it 4 times
                System.out.println("SPECIAL CASE PAWN");
                move = new ChessMove(myPosition, endPosition, PieceType.ROOK);
                this.completeMoves.add(move);
                move = new ChessMove(myPosition, endPosition, PieceType.KNIGHT);
                this.completeMoves.add(move);
                move = new ChessMove(myPosition, endPosition, PieceType.BISHOP);
                this.completeMoves.add(move);
                move = new ChessMove(myPosition, endPosition, PieceType.QUEEN);
                this.completeMoves.add(move);
            }
            else{
                move = new ChessMove(myPosition, endPosition, null);
                this.completeMoves.add(move);
            }
        }
        return this.completeMoves;
    }




}   //row 1-8, col 1-8, 1,1

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
    private Collection<ChessMove> complete_moves;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
        this.complete_moves = new ArrayList<>();
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
        return "I am a " + this.getPieceType() + " and I am " + this.getTeamColor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;

        HashSet<ChessMove> set1 = new HashSet<>(complete_moves);
        HashSet<ChessMove> set2 = new HashSet<>(that.complete_moves);

        return color == that.color && type == that.type && Objects.equals(set1, set2);
    }




    @Override
    public int hashCode() {
        return Objects.hash(color, type, complete_moves);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //how would you know what the promotion piece is? That is the player's choice????
        //create a list of strings
        int spaces;
        boolean forward = false;
        boolean backward = false;
        boolean forwardDiag = false;
        boolean backwardDiag = false;
        boolean leftRight = false;
        boolean horse = false;
        //bishop, knight, rook, pawn
        if(this.type == PieceType.KING){
            spaces = 1;
            forward = true;
            backward = true;
            forwardDiag = true;
            backwardDiag = true;
            leftRight = true;
        }
        else if(this.type == PieceType.QUEEN){
            spaces = 7;
            forward = true;
            backward = true;
            forwardDiag = true;
            backwardDiag = true;
            leftRight = true;
        }
        else if(this.type == PieceType.PAWN){ //if a pawn is in a certain row then it gets two moves
            spaces = 1; //one or two ehhhhh
            forward = true;
            forwardDiag = true;
        }
        else if(this.type == PieceType.ROOK){ //the forward/back
            spaces = 7;
            forward = true;
            backward = true;
            leftRight = true;
        }
        else if(this.type == PieceType.KNIGHT){ //horsey
            spaces = 1; //NA
            horse = true;
        }
        else if(this.type == PieceType.BISHOP){ //diag guy
            spaces = 7;
            forwardDiag = true;
            backwardDiag = true;
        }
        else{//just so it doesn't get mad
            System.out.println("the heck is this");
            spaces = 1;
        }
        Moves l = new Moves(spaces, forward, backward, forwardDiag, backwardDiag, leftRight, horse, myPosition, board, this.color, this.type);
        //int spaces, boolean forward, boolean backward, boolean forwardDiag, boolean backwardDiag, boolean leftRight, boolean horse,
        // ChessPosition position, ChessBoard board, ChessGame.TeamColor color, ChessPiece.PieceType type)

        ChessPiece.PieceType promotionP = null;
        System.out.println(this.toString());
        ChessMove move = null;
        for(ChessPosition end_position: l.getValidMoves()){ //go through the moves and make ChessMove objects
            System.out.println(end_position.toString());

            if(this.type == PieceType.PAWN && (end_position.getRow() == 7 || end_position.getRow() == 0)){ //if its a pawn and end position is at end do it 4 times
                System.out.println("SPECIAL CASE PAWN");
                move = new ChessMove(myPosition, end_position, PieceType.ROOK);
                this.complete_moves.add(move);
                move = new ChessMove(myPosition, end_position, PieceType.KNIGHT);
                this.complete_moves.add(move);
                move = new ChessMove(myPosition, end_position, PieceType.BISHOP);
                this.complete_moves.add(move);
                move = new ChessMove(myPosition, end_position, PieceType.QUEEN);
                this.complete_moves.add(move);
            }
            else{
                move = new ChessMove(myPosition, end_position, null);
                this.complete_moves.add(move);
            }
        }
        return this.complete_moves;
    }




}   //row 1-8, col 1-8, 1,1

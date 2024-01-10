package chess;

import javax.swing.*;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor color;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
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
            backwardDiag = true;
        }
        else if(this.type == PieceType.ROOK){ //the forward/back
            spaces = 1;
            forward = true;
            backward = true;
            forwardDiag = true;
            backwardDiag = true;
            leftRight = true;
            horse = false;
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


        for(ChessPosition end_position: l.getValid_moves()){
            if(this.type != PieceType.PAWN){
                //promotionPiece = null;
                break;
            }

            //ChessMove(ChessPosition startPosition, ChessPosition endPosition,
             //       ChessPiece.PieceType promotionPiece)
        }
        //arg if we have a pawn and the ending position is an edge then it gets promoted
        // the ending position
        Collection<ChessMove> moves = new ArrayList<>();
        return moves;
    }
    //the board knows about the pieces on it
    //myPosition is
}   //row 1-8, col 1-8, 1,1
//KING,
//        QUEEN,
//        BISHOP,
//        KNIGHT,
//        ROOK,
//        PAWN
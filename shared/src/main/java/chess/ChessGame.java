package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn = TeamColor.WHITE; //
    private ChessBoard board = null;

    public ChessGame() {
    }
    public ChessGame(TeamColor turn, ChessBoard board) {
        this.turn = turn;
        this.board = board;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece killed;
        System.out.println("validMoves");
        if(this.board.getPiece(startPosition) == null){
            return null;
        }
        ChessPiece piece_here = this.board.getPiece(startPosition);
        //if checkmate or stalemate, we are done
        Collection<ChessMove> all_moves = piece_here.pieceMoves(this.board, startPosition);
        Collection<ChessMove> remaining_moves = new HashSet<ChessMove>();
        if(isInCheckmate(piece_here.getTeamColor()) || isInStalemate(piece_here.getTeamColor())){
            return Collections.emptyList();
        }
        System.out.println("Not in checkmate or statelmeate");
        //go to a move where we are not in check
        System.out.println(this.board.toString());
        for(ChessMove m: all_moves){
            //System.out.println("How about " + m.getStartPosition() + "->" + m.getEndPosition());
            killed = doMove(this.board, m, piece_here);
            if(!isInCheck(piece_here.getTeamColor())){
                remaining_moves.add(m);
            }
            //(ChessBoard board, ChessMove m, ChessPiece p, ChessPiece killed)
            undoMove(this.board, m, piece_here, killed);
        }
        //remove all moves which put us into check
        return remaining_moves;

        //allmoves is based on the original board
        //how does moving a piece on it affect it
    }

    public ChessPiece doMove(ChessBoard board, ChessMove m, ChessPiece p){
        System.out.println("Domove" + m.getStartPosition() + " to " + m.getEndPosition());
        //make a move, undo the move
        //move: startpos, endpos, promotionp
        ChessPiece promo;
        ChessPiece killed = null; //save piece at destination
        //make move
        if(m.getPromotionPiece() != null){
            promo = new ChessPiece(p.getTeamColor(), m.getPromotionPiece());
            System.out.println("PROMO");
            board.addPiece(m.getEndPosition(), promo); //move piece
        }
        else{
            if(board.getPiece(m.getEndPosition()) != null){
                killed = board.getPiece(m.getEndPosition());
            }
            board.addPiece(m.getEndPosition(), p); //move piece
        }
        board.addPiece(m.getStartPosition(), null); //remove piece from start

        return killed;
    }

    public void undoMove(ChessBoard board, ChessMove m, ChessPiece p, ChessPiece killed){

        //undo move
        board.addPiece(m.getStartPosition(), p);
        board.addPiece(m.getEndPosition(), killed); //killed may be null but who cares
    }



    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        System.out.println("make move");
        if(this.board.getPiece(move.getStartPosition()) == null || (this.board.getPiece(move.getStartPosition()).getTeamColor() != turn)){
            System.out.println("Nothing to move or it is not this piece's turn");
            throw new InvalidMoveException();
        }

        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        System.out.println("finished validMoves");
        ChessPiece p = null;
        ChessGame.TeamColor color = null;
        if(moves.contains(move)){ //if not invalid move, make the move
            if(move.getPromotionPiece() != null){
                color = this.board.getPiece(move.getStartPosition()).getTeamColor();
                p = new ChessPiece(color,move.getPromotionPiece());
                this.board.addPiece(move.getEndPosition(), p);
            }
            else{
                this.board.addPiece(move.getEndPosition(), this.board.getPiece(move.getStartPosition())); //move the piece
            }
            this.board.addPiece(move.getStartPosition(), null); //remove the piece from original location
            setTurn(); //switch turns
        }
        else{
            throw new InvalidMoveException();
        }
    }

    public void setTurn(){
        if(turn == TeamColor.BLACK){
            turn = TeamColor.WHITE;
        }
        else if(turn == TeamColor.WHITE){
            turn = TeamColor.BLACK;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        System.out.println("is" + teamColor + " in check?");
        //they are in check if any of the other teams pieces valid moves contains the kings current position
        //loop through the grid, if they are other side look at their valid moves, if contain kings position return true
        ChessPosition p = null;
        ChessPiece piece = null;
        ChessPiece found_piece = null; //the piece at the end of the validmove..check if king
        Collection<ChessMove> possible_moves = null;
        for(int i = 1; i <= 8; i++){
            for(int k = 1; k <= 8; k++){
                p = new ChessPosition(i,k); //one indexing!
                piece = this.board.getPiece(p);
                if(piece != null && piece.getTeamColor() != teamColor){
                    //System.out.println("Where can the piece" + piece.getPieceType().toString()+ "at position " + p + "go?");
                    System.out.println("possible moves given we are in position" + p.toString());
                    System.out.println(this.board.toString());
                    possible_moves= null;
                    System.out.println("MOVE FOR " + this.board.getPiece(p) + p.toString());
                    possible_moves = piece.pieceMoves(this.board, p); //opponents possible moves;
                    //possible moves is ALWAYS the same
                    for(ChessMove move: possible_moves){
                        System.out.println(move.toString());
                    }
                    //p is supposed to be the pieces current position
                    for(ChessMove move: possible_moves){ //are any of these the kings position??
                        found_piece = this.board.getPiece(move.getEndPosition());
                        if(found_piece != null && found_piece.getPieceType() == ChessPiece.PieceType.KING && found_piece.getTeamColor() == teamColor){
                            System.out.println("we are in check");
                            return true; //if this piece can kill our king then its check
                        }
                    }
                    possible_moves = null;
                }
            }
        }
        return false;

        //VALID MOVES....would it be better to use the ones in this class?
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        System.out.println("is" + teamColor + "in checkmate?");
        //doMove(ChessBoard board, ChessMove m, ChessPiece p)
        //undoMove
        //must be inn check
        if(!isInCheck(teamColor)){
            return false;
        }
        //go through every possible valid move, if I am still in check after this move
        //continue
        //chesspiece has Collection<ChessMove> complete_moves
        ChessPiece piece;
        ChessPiece killed;
        Collection<ChessMove> m;
        for(int i = 1; i <= 8; i++){
            for(int k = 1; k <= 8; k++){
                ChessPosition p = new ChessPosition(i, k);
                piece = this.board.getPiece(p);
                if(piece != null && piece.getTeamColor() == teamColor){ //if there is a piece on OUR SIDE
                    m = piece.pieceMoves(board, p);
                    for(ChessMove move: m){
                        killed = doMove(this.board, move, piece);
                        if(!isInCheck(teamColor)){
                            undoMove(this.board, move, piece, killed);
                            System.out.println("Not in checkmate");
                            return false; //I made a move and am no longer in check

                        }
                        undoMove(this.board, move, piece, killed);
                    }
                }
            }
        }
        System.out.println("in checkmate");
        return true;
        //a giant list of all the valid_moves
        //pawn special case
        //'difference' b/t the set of valid_moves one side w another
        //you are in checkmate if at every move you could possibly do, you are still in check
        //soo...go through all of your pieces..go through all their validmoves
        //'make' the move,
        //when the other sides valid moves include the Kings position
        //and all
        //doMove(ChessBoard board, ChessMove m, ChessPiece p)
        //isInCheck(TeamColor teamColor)
        //pieceMoves(ChessBoard board, ChessPosition myPosition)
        //just pass in the
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        System.out.println("is " + teamColor + " in stalemate");
        //you are not in check, but you will be in check if no matter what move you make
        if(isInCheck(teamColor)){ //check != stalemate
            return false;
        }
        System.out.println("not in check");
        ChessPiece piece;
        ChessPiece killed;
        Collection<ChessMove> m;
        for(int i = 1; i <= 8; i++){
            for(int k = 1; k <= 8; k++){
                ChessPosition p = new ChessPosition(i, k);
                piece = this.board.getPiece(p);
                if(piece != null && piece.getTeamColor() == teamColor){ //if there is a piece on OUR SIDE
                    m = piece.pieceMoves(board, p);
                    for(ChessMove move: m){
                        killed = doMove(this.board, move, piece);
                        if(!isInCheck(teamColor)){
                            undoMove(this.board, move, piece, killed);
                            System.out.println("Not in stalemate");
                            return false;
                            //if I find even one move that takes me out of stalemate, return false
                        }
                        undoMove(this.board, move, piece, killed);
                    }
                }
            }
        }
        System.out.println("in stalemate");
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, board);
    }
}

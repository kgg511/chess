package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn = null;
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
        if(this.board.getPiece(startPosition) == null){
            return null;
        }
        //validMoves will not contain anything that would put the player in check
        Collection<ChessMove> all_moves = this.board.getPiece(startPosition).pieceMoves(this.board, startPosition);

        //if in stalemate then you have no valid_moves
        //
        //first check if in stalemate
        //now, go through all these moves and check if that move would
        return all_moves; //uhhh do I need to consider check/stalemate?
    }

    public void doMove(ChessBoard board, ChessMove m, ChessPiece p){
        //make a move, undo the move
        //move: startpos, endpos, promotionp
        ChessPiece promo;
        //make move
        if(m.getPromotionPiece() != null){
            promo = new ChessPiece(p.getTeamColor(), m.getPromotionPiece());
            board.addPiece(m.getEndPosition(), promo); //move piece
        }
        else{
            board.addPiece(m.getEndPosition(), p); //move piece
        }
        board.addPiece(m.getStartPosition(), null); //remove piece from start
    }

    public void undoMove(ChessBoard board, ChessMove m, ChessPiece p){
        //undo move
        board.addPiece(m.getStartPosition(), p);
        board.addPiece(m.getEndPosition(), null);
    }



    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> moves = validMoves(move.getStartPosition());
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
                this.board.addPiece(move.getStartPosition(), null); //remove the piece from original location
            }
        }
        else{
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //they are in check if any of the other teams pieces valid moves contains the kings current position
        //loop through the grid, if they are other side look at their valid moves, if contain kings position return true
        ChessPosition p = null;
        ChessPiece piece = null;
        ChessPiece found_piece = null; //the piece at the end of the validmove..check if king
        Collection<ChessMove> possible_moves = null;
        for(int i = 1; i <= 8; i++){
            for(int k = 1; k <= 8; i++){
                p = new ChessPosition(i,k); //one indexing!
                piece = this.board.getPiece(p);
                if(piece != null & piece.getTeamColor() != teamColor){
                    possible_moves = piece.pieceMoves(this.board, p); //opponents possible moves;
                    for(ChessMove move: possible_moves){ //are any of these the kings position???
                        found_piece = this.board.getPiece(move.getEndPosition());
                        if(found_piece.getPieceType() == ChessPiece.PieceType.KING && found_piece.getTeamColor() == teamColor){
                            return true; //if this piece can kill our king then its check
                        }
                    }
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
        Collection<ChessMove> m;
        for(int i = 1; i <= 8; i++){
            for(int k = 1; k <= 8; k++){
                ChessPosition p = new ChessPosition(i, k);
                piece = this.board.getPiece(p);
                if(piece != null && piece.getTeamColor() == teamColor){ //if there is a piece on OUR SIDE
                    m = piece.pieceMoves(board, p);
                    for(ChessMove move: m){
                        doMove(this.board, move, piece);
                        if(!isInCheck(teamColor)){
                            undoMove(this.board, move, piece);
                            return true;
                        }
                        undoMove(this.board, move, piece);
                    }
                }
            }
        }
        return false;
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
        //you are in stalemate if no matter what move you make
        //you are still in check
        //sooo...go through every single check piece on our side, check it's valid moves,
        //see if we would still be in check if we made those moves

        //stalemate is almost like check except you are not currently in check
        if(isInCheck(teamColor)){ //check != stalemate
            return false;
        }
        ChessPiece piece;
        Collection<ChessMove> m;
        for(int i = 1; i <= 8; i++){
            for(int k = 1; k <= 8; k++){
                ChessPosition p = new ChessPosition(i, k);
                piece = this.board.getPiece(p);
                if(piece != null && piece.getTeamColor() == teamColor){ //if there is a piece on OUR SIDE
                    m = piece.pieceMoves(board, p);
                    for(ChessMove move: m){
                        doMove(this.board, move, piece);
                        if(!isInCheck(teamColor)){
                            undoMove(this.board, move, piece);
                            return true;
                        }
                        undoMove(this.board, move, piece);
                    }
                }
            }
        }
        return false;
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
}

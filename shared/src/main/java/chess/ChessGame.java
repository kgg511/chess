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
        if(this.board.getPiece(startPosition) == null){
            return null;
        }
        ChessPiece pieceHere = this.board.getPiece(startPosition);
        //if checkmate or stalemate, we are done
        Collection<ChessMove> allMoves = pieceHere.pieceMoves(this.board, startPosition);
        Collection<ChessMove> remainingMoves = new HashSet<ChessMove>();
        if(isInCheckmate(pieceHere.getTeamColor()) || isInStalemate(pieceHere.getTeamColor())){
            return Collections.emptyList();
        }
        //go to a move where we are not in check
        for(ChessMove m: allMoves){
            //System.out.println("How about " + m.getStartPosition() + "->" + m.getEndPosition());
            killed = doMove(this.board, m, pieceHere);
            if(!isInCheck(pieceHere.getTeamColor())){
                remainingMoves.add(m);
            }
            //(ChessBoard board, ChessMove m, ChessPiece p, ChessPiece killed)
            undoMove(this.board, m, pieceHere, killed);
        }
        //remove all moves which put us into check
        //remainingMoves = remainingMoves;
        return remainingMoves;

        //allmoves is based on the original board
        //how does moving a piece on it affect it
    }

    public ChessPiece doMove(ChessBoard board, ChessMove m, ChessPiece p){
        //make a move, undo the move
        //move: startpos, endpos, promotionp
        ChessPiece promo;
        ChessPiece killed = null; //save piece at destination
        //make move
        if(m.getPromotionPiece() != null){
            promo = new ChessPiece(p.getTeamColor(), m.getPromotionPiece());
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
        if(this.board.getPiece(move.getStartPosition()) == null || (this.board.getPiece(move.getStartPosition()).getTeamColor() != turn)){
            System.out.println("Nothing to move or it is not this piece's turn");
            throw new InvalidMoveException();
        }
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
        //they are in check if any of the other teams pieces valid moves contains the kings current position
        //loop through the grid, if they are other side look at their valid moves, if contain kings position return true
        ChessPosition p = null;
        ChessPiece piece = null;
        ChessPiece foundPiece = null; //the piece at the end of the validmove..check if king
        Collection<ChessMove> possibleMoves = null;
        for(int i = 1; i <= 8; i++){
            for(int k = 1; k <= 8; k++){
                p = new ChessPosition(i,k); //one indexing!
                piece = this.board.getPiece(p);
                if(piece != null && piece.getTeamColor() != teamColor){
                    possibleMoves = piece.pieceMoves(this.board, p); //opponents possible moves;
                    for(ChessMove move: possibleMoves){ //are any of these the kings position??
                        foundPiece = this.board.getPiece(move.getEndPosition());
                        if(foundPiece != null && foundPiece.getPieceType() == ChessPiece.PieceType.KING && foundPiece.getTeamColor() == teamColor){
                            return true; //if this piece can kill our king then its check
                        }
                    }
                    possibleMoves = null;
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
        if(!isInCheck(teamColor)){
            return false;
        }
        return checkStale(teamColor);

    }

    public boolean checkStale(TeamColor teamColor){ //shared logic checkMate staleMate
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
                            return false; //I made a move and am no longer in check
                        }
                        undoMove(this.board, move, piece, killed);
                    }
                }
            }
        }
        System.out.println("in checkmate");
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //you are not in check, but you will be in check if no matter what move you make
        if(isInCheck(teamColor)){ //check != stalemate
            return false;
        }
        return checkStale(teamColor);
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

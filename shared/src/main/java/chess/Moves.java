package chess;

import java.util.ArrayList;
public class Moves {
    //TODO: make pieces unable to jump over other pieces
    //TODO: add the horsey
    private int spaces;
    private boolean forward;
    private boolean backward;
    private boolean forwardDiag;
    private boolean backwardDiag;

    private boolean leftRight; //no piece can go exclusively one

    private boolean horse;
    private ArrayList<ChessPosition> valid_moves;
    private ChessPosition position;
    private ChessBoard board;
    private ChessGame.TeamColor color; //passed in

    private ChessPiece.PieceType type;

    //= new ArrayList<>();
    //it's either 1,2, or 7 spaces, -1 for horse bc wth
    public Moves(int spaces, boolean forward, boolean backward, boolean forwardDiag, boolean backwardDiag, boolean leftRight, boolean horse, ChessPosition position, ChessBoard board, ChessGame.TeamColor color, ChessPiece.PieceType type){
        this.spaces = spaces;
        this.forward = forward;
        this.backward = backward;
        this.forwardDiag = forwardDiag;
        this.backwardDiag = backwardDiag;
        this.leftRight = leftRight;
        this.horse = horse;

        this.valid_moves = new ArrayList<ChessPosition>();
        this.position = position;
        this.board = board;
        this.color = color;
        this.type = type;
    } //assume it contains everything


    //functions: fd, bd, f, b, L
    //track: how many spaces
    ArrayList<ChessPosition> getValidMoves(){
        //call the appropriate function to fill it
        if(this.forward){forward();}
        if(this.backward){backward();}
        if(this.forwardDiag){forward_diag();}
        if(this.backwardDiag){backward_diag();}
        if(this.leftRight){left(); right();}
        if(horse){horse();}
        return this.valid_moves;
    }

    void forward(){
        //if a pawn is in a certain row then it gets two moves
        int temp_spaces = this.spaces;
        if(this.type == ChessPiece.PieceType.PAWN && this.position.getRow() == 2){ //only white side
            temp_spaces = 2;
        }
        for(int i = this.position.getRow() + 1; i < this.position.getRow() + temp_spaces; i++){
            if(out_of_bounds(i)){break;}
            ChessPosition p = new ChessPosition(i, this.position.getColumn());
            if (this.board.getPiece(p) == null || ((this.board.getPiece(p).getTeamColor() != this.color) && (this.type != ChessPiece.PieceType.PAWN))){
                this.valid_moves.add(p);
                if(this.board.getPiece(p) != null){
                    break;
                }
            }
        }
    }

    boolean out_of_bounds(int num){ //num represents position, NOT INDEX
        return num > 8 || num < 1;
    }
    void backward(){ //for king/queen/rook, NOT pawn
        //go forward spaces number of spaces
        for(int i = this.position.getRow() - 1; i >= this.position.getRow() - this.spaces; i--){
            if(out_of_bounds(i)){break;}
            ChessPosition p = new ChessPosition(i, this.position.getColumn());
            if (this.board.getPiece(p) == null || (this.board.getPiece(p).getTeamColor() != this.color)){
                this.valid_moves.add(p);
                if(this.board.getPiece(p) != null){
                    break;
                }
            }
            //we are going up rows
        }
    }

    void right(){ //for king/queen/rook
        //go forward spaces number of spaces
        for(int i = this.position.getColumn() + 1; i < this.position.getColumn() + this.spaces; i++){
            if(out_of_bounds(i)){break;}
            ChessPosition p = new ChessPosition(this.position.getRow(), i);;
            if (this.board.getPiece(p) == null || (this.board.getPiece(p).getTeamColor() != this.color)){
                this.valid_moves.add(p);
                if(this.board.getPiece(p) != null){
                    break;
                }
            }
            //we are going up rows
        }
    }

    void left(){ //for king/queen/rook
        //go forward spaces number of spaces
        for(int i = this.position.getColumn() - 1; i < this.position.getColumn() - this.spaces; i--){
            if(out_of_bounds(i)){break;}
            ChessPosition p = new ChessPosition(this.position.getRow(), i);
            if (this.board.getPiece(p) == null || (this.board.getPiece(p).getTeamColor() != this.color)){
                this.valid_moves.add(p);
                if(this.board.getPiece(p) != null){
                    break;
                }
            }
        }
    }

    void forward_diag(){ //now both column and rows will change
        //if it is a pawn, there must be something there
        //left diag: row increments, col decreases
        System.out.println("time to go forward diag");
        int j = this.position.getColumn() - 1;
        for(int i = this.position.getRow() + 1; i < this.position.getRow() + this.spaces; i++){
            if(out_of_bounds(i) || out_of_bounds(j)){break;}
            ChessPosition p = new ChessPosition(i, j);
            if(this.type == ChessPiece.PieceType.PAWN){ //case:PAWN
                if((this.board.getPiece(p).getTeamColor() != this.color)){
                    this.valid_moves.add(p);
                    if(this.board.getPiece(p) != null){
                        break;
                    }
                }
            }
            else if (this.board.getPiece(p) == null || (this.board.getPiece(p).getTeamColor() != this.color)){ //case: all others
                this.valid_moves.add(p);
                if(this.board.getPiece(p) != null){
                    break;
                }
            }
            j -= 1; //col decrease also
        }
        //right diag: row increment, col increment
        j = this.position.getColumn() + 1;
        for(int i = this.position.getRow() + 1; i < this.position.getRow() + this.spaces; i++){
            if(out_of_bounds(i) || out_of_bounds(j)){break;}
            ChessPosition p = new ChessPosition(i, j);
            if(this.type == ChessPiece.PieceType.PAWN){ //case:PAWN
                if((this.board.getPiece(p).getTeamColor() != this.color)){
                    this.valid_moves.add(p);
                    if(this.board.getPiece(p) != null){
                        break;
                    }
                }

            }
            else if (this.board.getPiece(p) == null || (this.board.getPiece(p).getTeamColor() != this.color)){ //case: all others
                this.valid_moves.add(p);
                if(this.board.getPiece(p) != null){
                    break;
                }
            }
            j += 1; //col increment also
        }
    }
    void backward_diag(){ //now both column and rows will change
        System.out.println("time to go backward diag" + this.position.getColumn()  + "" + this.position.getRow() );
        //left back diag: row decrements, col decreases

        int j = this.position.getColumn() - 1;
        for(int i = this.position.getRow() - 1; i >= this.position.getRow() - this.spaces; i--){
            System.out.println("backward diag" + i + " " + j);
            if(out_of_bounds(i) || out_of_bounds(j)){break;}
            ChessPosition p = new ChessPosition(i, j);
            if (this.board.getPiece(p) == null || (this.board.getPiece(p).getTeamColor() != this.color)){
                this.valid_moves.add(p);
                if(this.board.getPiece(p) != null){
                    break;
                }
            }
            j -= 1; //col decrease also
        }
        //right diag: row increment, col increment
        j = this.position.getColumn() + 1;
        for(int i = this.position.getRow() - 1; i >= this.position.getRow() - this.spaces; i--){
            if(out_of_bounds(i) || out_of_bounds(j)){break;}
            ChessPosition p = new ChessPosition(i, j);
            if (this.board.getPiece(p) == null || (this.board.getPiece(p).getTeamColor() != this.color)){
                this.valid_moves.add(p);
                if(this.board.getPiece(p) != null){
                    break;
                }
            }
            j += 1; //col increment also
        }
    }

    void horse(){
        System.out.println("horsey has not been coded yet");  //8 moves
        //does horsey moves
        int r = this.position.getRow();
        int c = this.position.getColumn();
        //move 2, move 1

        if(in_bounds(r,c)){ //up right
            ChessPosition p = new ChessPosition(r+2, c+1);
            this.valid_moves.add(p);
        }
        if(in_bounds(r,c)){ //up left
            ChessPosition p = new ChessPosition(r+2, c-1);
            this.valid_moves.add(p);
        }
        if(in_bounds(r,c)){ //down
            ChessPosition p = new ChessPosition(r-2, c+1);
            this.valid_moves.add(p);
        }
        if(in_bounds(r,c)){ //down
            ChessPosition p = new ChessPosition(r-2, c-1);
            this.valid_moves.add(p);
        }
        if(in_bounds(r,c)){ //right
            ChessPosition p = new ChessPosition(r+1, c+2);
            this.valid_moves.add(p);
        }
        if(in_bounds(r,c)){ //right
            ChessPosition p = new ChessPosition(r-1, c+2);
            this.valid_moves.add(p);
        }
        if(in_bounds(r,c)){ //down
            ChessPosition p = new ChessPosition(r-1, c-2);
            this.valid_moves.add(p);
        }
        if(in_bounds(r,c)){ //down
            ChessPosition p = new ChessPosition(r+1, c-2);
            this.valid_moves.add(p);
        }
    }

    boolean in_bounds(int r, int c){ //this is 1 to 8
        return r < 1 || c < 1 || r > 8 || c > 8;

    }



    //no one but horse can jump
    //aka, if you encounter an opponent, you can add it, but you have to stop right after


}
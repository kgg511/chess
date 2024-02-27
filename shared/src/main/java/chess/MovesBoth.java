package chess;

import java.util.HashSet;
public class MovesBoth {
    private ChessPiece.PieceType type;
    private boolean pawn = false;
    private ChessPosition pos;
    private ChessBoard board;

    private HashSet<ChessPosition> valid;
    private int spaces;

    private boolean white;

    private ChessGame.TeamColor color;

    MovesBoth(ChessPiece.PieceType type, ChessPosition pos, ChessBoard board, boolean white, ChessGame.TeamColor color){
        this.type = type;
        this.pos = pos;
        this.board = board;
        this.valid = new HashSet<ChessPosition>(); //valid end positions for the piece
        this.white = white;
        if(type == ChessPiece.PieceType.PAWN){
            this.pawn = true;
        }
        this.color = color;

    }
    public HashSet<ChessPosition> fillValid(){
        if(type == ChessPiece.PieceType.KING){
            this.spaces = 1;
            this.forward();
            this.backward();
            this.leftRight();
            this.forwardDiag();
            this.backwardDiag();
        }
        else if(type == ChessPiece.PieceType.QUEEN){
            this.spaces = 7;
            this.forward();
            this.backward();
            this.leftRight();
            this.forwardDiag();
            this.backwardDiag();

        }
        else if(type == ChessPiece.PieceType.BISHOP){
            this.spaces = 7;
            this.forwardDiag();
            this.backwardDiag();
        }
        else if(type == ChessPiece.PieceType.KNIGHT){
            this.horse();
        }
        else if(type == ChessPiece.PieceType.ROOK){
            this.spaces = 7;
            this.forward();
            this.backward();
            this.leftRight();
        }
        else if(type == ChessPiece.PieceType.PAWN){
            this.spaces = 1;
            this.forward();
            this.forwardDiag();
        }
        return this.valid;
    }
    public boolean checkMove(ChessPosition p, boolean pawnKill){
        //if pawnKill then allow for killing
        if(!inBounds(p)){return true;} //go to the next loop by returning early
        if(board.getPiece(p) == null){
            if(pawnKill && pawn){return true;} //if diagonal then pawn can't kill forward
            valid.add(p);
        }

        else if(board.getPiece(p) != null){ //yes IF we are not a pawn and its the other side
            if(board.getPiece(p).getTeamColor() != this.color){
                if(pawnKill && this.pawn){System.out.println("I am a pawn and ready to kill");}
                if(pawnKill || !this.pawn){ //pawn can only kill on forward diag
                    valid.add(p);
                }
            }
            return false; //break if this returns false
        }
        return true;
    }

    public boolean forwardInside(int i){
        ChessPosition p;
        p = new ChessPosition(i, this.pos.getColumn());
        return checkMove(p, false);
    }
    public void forward(){ //move up rows
        //pawn can move 2, pawn cannot kill
        int temp = this.spaces;
        if(this.pawn && (this.pos.getRow() == 2 || this.pos.getRow() == 7)){temp = 2;} //row two means first move
        if(white){
            for(int i = this.pos.getRow() + 1; i <= this.pos.getRow() + temp; i++){
                if(!forwardInside(i)){break;}
            }
        }
        else{
            for(int i = this.pos.getRow() - 1; i >= this.pos.getRow() - temp; i--){
                if(!forwardInside(i)){break;}
            }
        }
    }

    public boolean backwardInside(int i){
        ChessPosition p;
        p = new ChessPosition(i, this.pos.getColumn());
        return checkMove(p, false);
    }
    public void backward(){
        if(white){
            for(int i = this.pos.getRow() - 1; i >= this.pos.getRow() - spaces; i--){
                if(!backwardInside(i)){break;}
            }
        }
        else{
            for(int i = this.pos.getRow() + 1; i <= this.pos.getRow() + spaces; i++){
                if(!backwardInside(i)){break;}
            }
        }
    }


    //one of the few functions that is the exact same for both colors
    public void leftRight(){ //row same, col change
        //LEFT
        ChessPosition p;
        for(int i = this.pos.getColumn() - 1; i >= this.pos.getColumn() - spaces; i--){
            p = new ChessPosition(this.pos.getRow(), i);
            if(!checkMove(p, false)){break;}
        }

        //RIGHT
        for(int i = this.pos.getColumn() + 1; i <= this.pos.getColumn() + spaces; i++){
            p = new ChessPosition(this.pos.getRow(), i);
            if(!checkMove(p, false)){break;}
        }
    }


    public boolean forwardDiagLeftInside(int i, int j){
        ChessPosition p;
        p = new ChessPosition(i, j);
        return checkMove(p, true);
    }
    public boolean forwardDiagRightInside(int i, int j){
        ChessPosition p;
        p = new ChessPosition(i, j);
        return checkMove(p, true);
    }

    public void forwardDiag(){
        //i for row, j for col
        if(white){
            //forward left
            int j = this.pos.getColumn() - 1;
            for(int i = this.pos.getRow() + 1; i <= this.pos.getRow() + spaces; i++){
                if(!forwardDiagLeftInside(i,j)){break;};
                j -= 1;
            }
            //forward right
            j = this.pos.getColumn() + 1;
            for(int i = this.pos.getRow() + 1; i <= this.pos.getRow() + spaces; i++){
                if(!forwardDiagRightInside(i,j)){break;};
                j += 1;
            }
        }

        else{
            int j = this.pos.getColumn() - 1;
            for(int i = this.pos.getRow() - 1; i >= this.pos.getRow() - spaces; i--){
                if(!forwardDiagLeftInside(i,j)){break;};
                j -= 1;
            }
            j = this.pos.getColumn() + 1;
            for(int i = this.pos.getRow() - 1; i >= this.pos.getRow() - spaces; i--){
                if(!forwardDiagRightInside(i,j)){break;};
                j += 1;
            }
        }
    }


    public boolean backwardDiagLeftInside(int i, int j){
        ChessPosition p;
        p = new ChessPosition(i, j);
        return checkMove(p, false);
    }



    public boolean backwardDiagRightInside(int i, int j){
        ChessPosition p;
        p = new ChessPosition(i, j);
        return checkMove(p, false);

    }
    public void backwardDiag(){ //no pawn stuff
        ChessPosition p;
        if(white){
            //backward left
            int j = this.pos.getColumn() - 1;
            for(int i = this.pos.getRow() - 1; i >= this.pos.getRow() - spaces; i--){
                if(!backwardDiagLeftInside(i, j)){break;}
                j -= 1;
            }
            //backward right
            j = this.pos.getColumn() + 1;
            for(int i = this.pos.getRow() - 1; i >= this.pos.getRow() - spaces; i--){
                if(!backwardDiagRightInside(i, j)){break;}
                j += 1;
            }
        }
        else{
            int j = this.pos.getColumn() - 1;
            for(int i = this.pos.getRow() + 1; i <= this.pos.getRow() + spaces; i++){
                if(!backwardDiagLeftInside(i, j)){break;}
                j -= 1;
            }
            j = this.pos.getColumn() + 1;
            for(int i = this.pos.getRow() + 1; i <= this.pos.getRow() + spaces; i++){
                if(!backwardDiagRightInside(i, j)){break;}
                j += 1;
            }
        }

    }

    public void checkHorse(ChessPosition p){
        if(inBounds(p)){
            if(this.board.getPiece(p) == null){
                valid.add(p);
            }
            else{
                if(this.board.getPiece(p).getTeamColor() != this.color){
                    valid.add(p); //kilzz
                }
            }
        }
    }
    public void horse(){
        int r = this.pos.getRow();
        int c = this.pos.getColumn();
        // r+2, c-1
        ChessPosition p = new ChessPosition(r+2, c-1);
        checkHorse(p);
        // r+2, c+1
        p = new ChessPosition(r+2, c+1);
        checkHorse(p);
        // r-2, c+1
        p = new ChessPosition(r-2, c+1);
        checkHorse(p);
        //r-2, c-1
        p = new ChessPosition(r-2, c-1);
        checkHorse(p);
        //r+1, c+2
        p = new ChessPosition(r+1, c+2);
        checkHorse(p);
        //r-1, c+2
        p = new ChessPosition(r-1, c+2);
        checkHorse(p);
        //r+1, c-2
        p = new ChessPosition(r+1, c-2);
        checkHorse(p);
        //r-1, c-2
        p = new ChessPosition(r-1, c-2);
        checkHorse(p);
    }


    public boolean inBounds(ChessPosition pos){
        return pos.getColumn() <= 8 && pos.getColumn() >= 1 && pos.getRow() >= 1 && pos.getRow() <=8;
    }
}

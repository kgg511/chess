package chess;

import java.util.HashSet;

public class MovesWhite {
    private ChessPiece.PieceType type;
    private boolean pawn = false;
    private ChessPosition pos;
    private ChessBoard board;

    private HashSet<ChessPosition> valid;
    private int spaces;

    MovesWhite(ChessPiece.PieceType type, ChessPosition pos, ChessBoard board){
        this.type = type;
        this.pos = pos;
        this.board = board;
        this.valid = new HashSet<ChessPosition>(); //valid end positions for the piece
        if(type == ChessPiece.PieceType.PAWN){
            this.pawn = true;
        }

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

    public void forward(){ //move up rows
        //pawn can move 2, pawn cannot kill
        //
        ChessPosition p;
        int temp = this.spaces;
        if(this.pawn && this.pos.getRow() == 2){temp = 2;} //row two means first move

        for(int i = this.pos.getRow() + 1; i <= this.pos.getRow() + temp; i++){
            p = new ChessPosition(i, this.pos.getColumn());
            if(!in_bounds(p)){continue;}

            if(board.getPiece(p) == null){
                valid.add(p);
            }
            else if(board.getPiece(p) != null){ //yes IF we are not a pawn and its the other side
                if(board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE && !this.pawn){
                    valid.add(p);
                }
                break; //we found a piece so we are done
            }
        }

    }

    public void backward(){
        ChessPosition p;
        for(int i = this.pos.getRow() - 1; i >= this.pos.getRow() - spaces; i--){
            p = new ChessPosition(i, this.pos.getColumn());
            if(!in_bounds(p)){continue;}

            if(board.getPiece(p) == null){
                valid.add(p);
            }
            else if(board.getPiece(p) != null){ //yes IF we are not a pawn and its the other side
                if(board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p);
                }
                break; //we found a piece so we are done
            }
        }
    }

    public void leftRight(){ //row same, col change
        //LEFT
        ChessPosition p;
        for(int i = this.pos.getColumn() - 1; i >= this.pos.getColumn() - spaces; i--){
            p = new ChessPosition(this.pos.getRow(), i);
            if(!in_bounds(p)){continue;}

            if(board.getPiece(p) == null){
                valid.add(p);
            }
            else if(board.getPiece(p) != null){ //someone there nad its not our side
                if(board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p);
                }
                break; //we found a piece so we are done
            }
        }

        //RIGHT
        for(int i = this.pos.getColumn() + 1; i <= this.pos.getColumn() + spaces; i++){
            p = new ChessPosition(this.pos.getRow(), i);
            if(!in_bounds(p)){continue;}

            if(board.getPiece(p) == null){
                valid.add(p);
            }
            else if(board.getPiece(p) != null){ //someone there nad its not our side
                if(board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p);
                }
                break; //we found a piece so we are done
            }
        }
    }


    public void forwardDiag(){
        //i for row, j for col
        ChessPosition p;
        //forward left
        int j = this.pos.getColumn() - 1;
        for(int i = this.pos.getRow() + 1; i <= this.pos.getRow() + spaces; i++){
            p = new ChessPosition(i, j);
            if(!in_bounds(p)){continue;}

            if(board.getPiece(p) == null){
                if(!this.pawn){ //pawn can only go diag IF there's someone to kill
                    valid.add(p);
                }
            }
            else if(board.getPiece(p) != null){ //someone there nad its not our side
                if(board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p);
                }
                break; //we found a piece so we are done
            }
            j -= 1;
        }
        //forward right
        j = this.pos.getColumn() + 1;
        for(int i = this.pos.getRow() + 1; i <= this.pos.getRow() + spaces; i++){
            p = new ChessPosition(i, j);
            if(!in_bounds(p)){continue;}

            if(board.getPiece(p) == null){
                if(!this.pawn){ //pawn can only go diag IF there's someone to kill
                    valid.add(p);
                }
            }
            else if(board.getPiece(p) != null){ //someone there nad its not our side
                if(board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p);
                }
                break; //we found a piece so we are done
            }
            j += 1;
        }


    }

    public void backwardDiag(){ //no pawn stuff
        //i for row, j for col
        ChessPosition p;

        //backward left
        int j = this.pos.getColumn() - 1;
        for(int i = this.pos.getRow() - 1; i >= this.pos.getRow() - spaces; i--){
            p = new ChessPosition(i, j);
            if(!in_bounds(p)){continue;}

            if(board.getPiece(p) == null){
                valid.add(p);
            }
            else if(board.getPiece(p) != null){ //someone there nad its not our side
                if(board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p);
                }
                break; //we found a piece so we are done
            }
            j -= 1;
        }

        //backward right
        j = this.pos.getColumn() + 1;
        for(int i = this.pos.getRow() - 1; i >= this.pos.getRow() - spaces; i--){
            p = new ChessPosition(i, j);
            if(!in_bounds(p)){continue;}

            if(board.getPiece(p) == null){
                valid.add(p);
            }
            else if(board.getPiece(p) != null){ //someone there nad its not our side
                if(board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p);
                }
                break; //we found a piece so we are done
            }
            j += 1;
        }
    }

    public void horse(){
        //8 moves, lets work them out on paper
        int r = this.pos.getRow();
        int c = this.pos.getColumn();

        //if in bounds && if there is someone there they are not our color
        // r+2, c-1
        ChessPosition p = new ChessPosition(r+2, c-1);
        if(in_bounds(p)){
            if(this.board.getPiece(p) == null){
                valid.add(p);
            }
            else{
                if(this.board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p); //kilzz
                }
            }
        }

        // r+2, c+1
        p = new ChessPosition(r+2, c+1);
        if(in_bounds(p)){
            if(this.board.getPiece(p) == null){
                valid.add(p);
            }
            else{
                if(this.board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p); //kilzz
                }
            }
        }

        // r-2, c+1
        p = new ChessPosition(r-2, c+1);
        if(in_bounds(p)){
            if(this.board.getPiece(p) == null){
                valid.add(p);
            }
            else{
                if(this.board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p); //kilzz
                }
            }
        }

        //r-2, c-1
        p = new ChessPosition(r-2, c-1);
        if(in_bounds(p)){
            if(this.board.getPiece(p) == null){
                valid.add(p);
            }
            else{
                if(this.board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p); //kilzz
                }
            }
        }

        //r+1, c+2
        p = new ChessPosition(r+1, c+2);
        if(in_bounds(p)){
            if(this.board.getPiece(p) == null){
                valid.add(p);
            }
            else{
                if(this.board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p); //kilzz
                }
            }
        }

        //r-1, c+2
        p = new ChessPosition(r-1, c+2);
        if(in_bounds(p)){
            if(this.board.getPiece(p) == null){
                valid.add(p);
            }
            else{
                if(this.board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p); //kilzz
                }
            }
        }

        //r+1, c-2
        p = new ChessPosition(r+1, c-2);
        if(in_bounds(p)){
            if(this.board.getPiece(p) == null){
                valid.add(p);
            }
            else{
                if(this.board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p); //kilzz
                }
            }
        }

        //r-1, c-2
        p = new ChessPosition(r-1, c-2);
        if(in_bounds(p)){
            if(this.board.getPiece(p) == null){
                valid.add(p);
            }
            else{
                if(this.board.getPiece(p).getTeamColor() != ChessGame.TeamColor.WHITE){
                    valid.add(p); //kilzz
                }
            }
        }
    }

    public boolean in_bounds(ChessPosition pos){
        return pos.getColumn() <= 8 && pos.getColumn() >= 1 && pos.getRow() >= 1 && pos.getRow() <=8;
    }




}

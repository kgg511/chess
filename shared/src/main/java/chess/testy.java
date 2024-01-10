package chess;

public class testy {
    //once I test and get actual stuff working then we commit again
    public static void main(String[] args) {
        // Your testing code goes here
        System.out.println("Testing the main method.");

        ChessBoard b = new ChessBoard();
        ChessPiece p;
        ChessPosition pos;
        for(int i = 0; i < 8; i++){
            p = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            pos = new ChessPosition(2, i + 1);
            b.addPiece(pos, p);
        }

        //king
        p = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        pos = new ChessPosition(1, 5);
        b.addPiece(pos, p);

        //queen
        p = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        pos = new ChessPosition(1, 4);
        b.addPiece(pos, p);

        //rook
        p = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        pos = new ChessPosition(1, 1);
        b.addPiece(pos, p);
        p = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        pos = new ChessPosition(1, 8);
        b.addPiece(pos, p);

        //bishop
        p = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        pos = new ChessPosition(1, 6);
        b.addPiece(pos, p);
        p = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        pos = new ChessPosition(1, 3);
        b.addPiece(pos, p);

        //horse
        p = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        pos = new ChessPosition(1, 7);
        b.addPiece(pos, p);
        p = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        pos = new ChessPosition(1, 2);
        b.addPiece(pos, p);


        b.getPiece()

        }



    }

}

package chess;

public class testy {
    //once I test and get actual stuff working then we commit again
    public static void main(String[] args) {
        // Your testing code goes here
        System.out.println("Testing the main method.");

        ChessBoard b = new ChessBoard();

        b.showBoard();


        //what is in our board?
        /*System.out.println("whats on the board");
        for(int i = 0; i < b.getPieces().size(); i++){
            System.out.println("" + b.getPieces().get(i) + "" + b.getPositions().get(i));
        }

         */

        //pos = new ChessPosition(1, 6); //this is the bishop
        //ChessPiece c= b.getPiece(pos);
        //System.out.println(c.toString());

    }

}

package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean equals(ChessPosition o) {
        if (this == o) return true;
        if(o.getColumn() == this.getColumn() && o.getRow() == this.getRow()){
            return true;
        }
        return false

    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }
}

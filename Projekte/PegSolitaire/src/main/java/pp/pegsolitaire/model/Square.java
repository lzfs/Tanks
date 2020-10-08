package pp.pegsolitaire.model;

import java.util.Objects;

/**
 * A class representing a single square.
 */
public class Square {
    private final int x;
    private final int y;
    private SquareState state;
    private boolean selected;

    /**
     * Creates a new square
     *
     * @param x     x coordinate of the square in the game map
     * @param y     y coordinate of the square in the game map
     * @param state the initial state of the new square
     */
    public Square(int x, int y, SquareState state) {
        this.x = x;
        this.y = y;
        this.state = state;
        selected = false;
    }

    /**
     * Returns the current state of this square
     *
     * @return current state
     */
    public SquareState getState() {
        return state;
    }

    /**
     * Sets the current state of this square
     *
     * @param state current state
     */
    public void setState(SquareState state) {
        this.state = state;
    }

    /**
     * Returns the x coordinate of this square
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of this square
     */
    public int getY() {
        return y;
    }

    /**
     * Returns whether this square is accessible, i.e., if its state is not
     * {@link pp.pegsolitaire.model.SquareState#Inaccessible}
     */
    public boolean isAccessible() {
        return state != SquareState.Inaccessible;
    }

    /**
     * Returns whether this square is occupied, i.e., if its state is
     * {@link pp.pegsolitaire.model.SquareState#Occupied}
     */
    public boolean isOccupied() {
        return state == SquareState.Occupied;
    }

    /**
     * Returns whether this square is empty, i.e., its state is
     * {@link pp.pegsolitaire.model.SquareState#Empty}
     */
    public boolean isEmpty() {
        return state == SquareState.Empty;
    }

    /**
     * Returns whether this square is empty and reachable, i.e., its state is
     * {@link pp.pegsolitaire.model.SquareState#EmptyReachable}
     */
    public boolean isEmptyReachable() {
        return state == SquareState.EmptyReachable;
    }

    /**
     * Returns a string representation of this square.
     */
    @Override
    public String toString() {
        return state.getSymbol();
    }

    /**
     * Creates the hash value of this Square
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y, state);
    }

    /**
     * Checks whether the argument is equal to this square.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof Square)) return false;
        final Square sq = (Square) other;
        return sq.getX() == this.getX()
                && sq.getY() == this.getY()
                && sq.getState() == this.getState();
    }

    /**
     * Returns whether this square is selected in the game model.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Selects or deselects this square in the game model depending on the argument.
     */
    void setSelected(boolean sel) {
        selected = sel;
    }
}

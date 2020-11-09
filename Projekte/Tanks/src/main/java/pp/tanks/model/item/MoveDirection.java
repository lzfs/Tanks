package pp.tanks.model.item;

import pp.util.DoubleVec;

/**
 * Enumeration of all possible directions a Tank can drive to
 */
public enum MoveDirection {
    UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0), LEFTUP(-1, 1), RIGHTUP(1, 1), LEFTDOWN(-1, -1), RIGHTDOWN(1, -1);

    private int x;
    private int y;

    MoveDirection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public DoubleVec getVec() {
        return new DoubleVec(this.x, this.y);
    }
}

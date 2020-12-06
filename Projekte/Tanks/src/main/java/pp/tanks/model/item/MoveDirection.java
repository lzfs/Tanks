package pp.tanks.model.item;

import pp.util.DoubleVec;

/**
 * Enumeration of all possible directions a Tank can drive to
 */
public enum MoveDirection {
    UP(0, -1, 90), DOWN(0, 1, 90), LEFT(-1, 0, 0), RIGHT(1, 0, 0), LEFT_UP(-1, -1, 45), RIGHT_UP(1, -1, 135), LEFT_DOWN(-1, 1, 135), RIGHT_DOWN(1, 1, 45), STAY(0, 0, 0);

    private final int x;
    private final int y;
    private final double rotation;

    MoveDirection(int x, int y, double rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    /**
     * @return x-coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return y-coordinate
     */
    public int getY() {
        return this.y;
    }

    /**
     * @return current rotation
     */
    public double getRotation() {
        return this.rotation;
    }

    /**
     * @return vector as DoubleVec
     */
    public DoubleVec getVec() {
        return new DoubleVec(this.x, this.y);
    }
}

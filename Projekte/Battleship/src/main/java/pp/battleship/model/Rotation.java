package pp.battleship.model;

import pp.util.IntVec;

import java.io.Serializable;

/**
 * represents the rotation of a ship and brings minor functionality
 */
public enum Rotation implements Serializable {
    UP(){
        @Override
        public IntVec next(IntVec pos) {
            return new IntVec(pos.x, pos.y - 1);
        }

        @Override
        public Rotation rotate() {
            return RIGHT;
        }
    },
    RIGHT(){
        @Override
        public IntVec next(IntVec pos) {
            return new IntVec(pos.x + 1, pos.y);
        }

        @Override
        public Rotation rotate() {
            return DOWN;
        }
    },
    DOWN(){
        @Override
        public IntVec next(IntVec pos) {
            return new IntVec(pos.x, pos.y + 1);
        }

        @Override
        public Rotation rotate() {
            return LEFT;
        }
    },
    LEFT(){
        @Override
        public IntVec next(IntVec pos) {
            return new IntVec(pos.x - 1, pos.y);
        }

        @Override
        public Rotation rotate() {
            return UP;
        }
    };

    /**
     * Returns the next position to pos according to the rotation
     *
     * @param pos   position to find the next position to
     * @return      next position
     */
    public abstract IntVec next(IntVec pos);

    /**
     * Rotates clockwise and returns the next rotation
     *
     * @return      next rotation
     */
    public abstract Rotation rotate();
}

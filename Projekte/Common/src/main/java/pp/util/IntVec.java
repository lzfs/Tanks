package pp.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class implementing immutable 2D int vectors
 */
public class IntVec implements WithDistance<IntVec>, Serializable {
    public final int x;
    public final int y;

    public static final IntVec NULL = new IntVec(0, 0);

    /**
     * Creates a new vector with the specified coordinates
     */
    public IntVec(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Converts a IntVec into a DoubleVec
     */
    public DoubleVec toFloatVec() {
        return new DoubleVec(x, y);
    }

    /**
     * Returns the length of this vector.
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Adds another vector to this vector.
     *
     * @return the sum of this vector and the argument.
     */
    public IntVec add(IntVec other) {
        if (x == 0 && y == 0) return other;
        if (other.x == 0 && other.y == 0) return this;
        return new IntVec(x + other.x, y + other.y);
    }

    /**
     * Subtracts another vector from this one.
     *
     * @return the difference of this vector and the argument.
     */
    public IntVec sub(IntVec other) {
        if (other.x == 0 && other.y == 0) return this;
        return new IntVec(x - other.x, y - other.y);
    }

    /**
     * Returns the Euclidean distance between this vector and the argument.
     */
    public double distance(IntVec other) {
        final int dx = x - other.x;
        final int dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Checks whether the argument is equal to this vector.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntVec)) return false;
        IntVec intVec = (IntVec) o;
        return x == intVec.x && y == intVec.y;
    }

    /**
     * Returns a hash value value of this vector.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Returns a string representation of this vector.
     */
    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}

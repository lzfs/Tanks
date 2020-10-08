package pp.util;

import java.io.Serializable;

/**
 * Class implementing immutable 2D double vectors
 */
public class DoubleVec implements WithDistance<DoubleVec>, Serializable {

    public final double x;
    public final double y;

    /**
     * The null vector.
     */
    public static final DoubleVec NULL = new DoubleVec(0., 0.);

    /**
     * Returns the vector specified by polar coordinates
     *
     * @param length length of the vector
     * @param degree angle of the vector with the x-axis, specified in degrees (not radians!)
     */
    public static DoubleVec polar(double length, double degree) {
        final double rot = Math.toRadians(degree);
        return new DoubleVec(length * Math.cos(rot), length * Math.sin(rot));
    }

    /**
     * Creates a new vector with the specified coordinates.
     */
    public DoubleVec(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a vector with integer coordinates that is the closest one to this vector.
     */
    public IntVec toIntVec() {
        final int x1 = Math.toIntExact(Math.round(x));
        final int y1 = Math.toIntExact(Math.round(y));
        return new IntVec(x1, y1);
    }

    /**
     * Adds another vector to this vector.
     *
     * @return the sum of this vector and the argument.
     */
    public DoubleVec add(DoubleVec v) {
        return new DoubleVec(x + v.x, y + v.y);
    }

    /**
     * Subtracts another vector from this one.
     *
     * @return the difference of this vector and the argument.
     */
    public DoubleVec sub(DoubleVec v) {
        return new DoubleVec(x - v.x, y - v.y);
    }

    /**
     * Computes the scalar product of this vector with the specified one
     */
    public double scalarProduct(DoubleVec that) {
        return this.x * that.x + this.y * that.y;
    }

    /**
     * Multiplies this vector with a scalar
     *
     * @param f a scalar
     * @return this vector multiplied with the argument
     */
    public DoubleVec mult(double f) {
        return new DoubleVec(x * f, y * f);
    }

    /**
     * Divides this vector by a scalar
     *
     * @param f a scalar
     * @return this vector divided by the argument
     */
    public DoubleVec div(double f) {
        return new DoubleVec(x / f, y / f);
    }

    /**
     * Returns the length of the vector
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Returns the angle - in degrees - of this vector with the x axis in the range from
     * -180 to +180 degrees.
     */
    public double angle() {
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns a copy of this vector normalized to length 1.
     *
     * @return a normalized copy of this vector
     */
    public DoubleVec normalize() {
        return div(length());
    }

    /**
     * Returns the Euclidean distance between this vector and the specified one.
     *
     * @param v vector
     * @return Euclidean distance between this and v
     */
    public double distance(DoubleVec v) {
        return Math.sqrt(distanceSq(v));
    }

    /**
     * Returns the square of the Euclidean distance between this vector and the specified one.
     *
     * @param v vector
     * @return square of the Euclidean distance between this and v
     */
    public double distanceSq(DoubleVec v) {
        final double dx = x - v.x;
        final double dy = y - v.y;
        return dx * dx + dy * dy;
    }

    /**
     * Compares this object against the specified object. The result is true if and only if the argument is
     * not null and is a FloatVec object that represents a vector with the same x value as this vector and the same
     * y value as this vector. For this purpose, two double values are considered to be the same if and only if the
     * method {@linkplain Double#doubleToLongBits(double)} returns the identical int value when applied to each.
     *
     * @param obj the object to be compared
     * @return true if the objects represent the same vector; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof DoubleVec)) return false;
        final DoubleVec other = (DoubleVec) obj;
        return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x) &&
                Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
    }

    /**
     * Returns the Euclidean distance between this vector and the argument.
     */
    @Override
    public int hashCode() {
        return (int) (31L * Double.doubleToLongBits(x) + Double.doubleToLongBits(y));
    }

    /**
     * Returns a string representation of this vector.
     */
    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }
}

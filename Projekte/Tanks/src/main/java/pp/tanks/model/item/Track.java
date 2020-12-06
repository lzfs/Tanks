package pp.tanks.model.item;

import pp.util.DoubleVec;

public class Track {

    private DoubleVec vec;
    private double rotation;
    private TrackIntensity intensity;

    public Track(DoubleVec vec, double rotation, TrackIntensity intensity) {
        this.vec = vec;
        this.rotation = rotation;
        this.intensity = intensity;
    }

    public DoubleVec getVec() {
        return vec;
    }

    public double getRotation() {
        return rotation;
    }

    public TrackIntensity getintensity() {
        return intensity;
    }
}

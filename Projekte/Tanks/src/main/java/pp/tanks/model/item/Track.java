package pp.tanks.model.item;

import pp.util.DoubleVec;

public class Track {

    private DoubleVec vec;
    private double rotation;

    public Track(DoubleVec vec, double rotation) {
        this.vec=vec;
        this.rotation=rotation;
    }

    public DoubleVec getVec() {
        return vec;
    }

    public double getRotation() {
        return rotation;
    }
}

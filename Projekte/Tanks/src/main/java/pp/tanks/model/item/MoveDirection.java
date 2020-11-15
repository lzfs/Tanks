package pp.tanks.model.item;

import pp.util.DoubleVec;

/**
 * Enumeration of all possible directions a Tank can drive to
 */
public enum MoveDirection {
    UP(0, -1,90), DOWN(0, 1,90), LEFT(-1, 0,0), RIGHT(1, 0,0), LEFTUP(-1, -1,45), RIGHTUP(1, -1,135), LEFTDOWN(-1, 1,135), RIGHTDOWN(1, 1,45);

    private int x;
    private int y;
    private double rotation;

    MoveDirection(int x, int y,double rotation) {
        this.x = x;
        this.y = y;
        this.rotation =rotation;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public double getRotation() {return this.rotation;}

    public DoubleVec getVec() {
        return new DoubleVec(this.x, this.y);
    }

    /*
    public MoveDirection getEq(MoveDirection dir){
        if( dir == UP || dir==DOWN){

        }
    }

     */
}

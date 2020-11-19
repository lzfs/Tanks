package pp.tanks.message.data;

import pp.tanks.model.item.MoveDirection;
import pp.util.DoubleVec;

/**
 * Represents the data of a tank that is sent to the server
 */
public class TankData extends Data {
    private MoveDirection moveDir;
    private DoubleVec turretDir;
    private boolean move;
    private double rotation;
    private int lifePoints;

    public TankData(DoubleVec pos, int id, int lifePoints, MoveDirection moveDir, double rotation, DoubleVec turretDir) {
        super(pos, id);
        this.moveDir = moveDir;
        this.turretDir = turretDir;
        this.move = false;
        this.rotation = rotation;
        this.lifePoints = lifePoints;
        /*
        {
            super(pos, id);
            this.moveDir = MoveDirection.RIGHT;
            this.turretDir = new DoubleVec(0, 0);
            this.move = false;
            this.rotation = 0;
            this.lifePoints = lifePoints;
        }
         */
    }

    /**
     * creates a similar copy of the current TankData-class for working processes
     *
     * @return returns the copy
     */
    public TankData mkCopy() { return new TankData(this.getPos(), this.getId(), this.lifePoints, this.moveDir, this.rotation, this.turretDir);}

    /**
     * checks if the tank is moving
     *
     * @return move
     */
    public boolean isMoving() {
        return move;
    }

    /**
     * @return current move-direction
     */
    public MoveDirection getMoveDir() {
        return moveDir;
    }

    /**
     * @return current turret-direction
     */
    public DoubleVec getTurretDir() {
        return turretDir;
    }

    /**
     * @return current rotation
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * updates the life points of the tank
     *
     * @param lifePoints
     */
    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    /**
     * @return current number of lifepoints
     */
    public int getLifePoints() {
        return lifePoints;
    }

    /**
     * updates the move direction of the tank
     *
     * @param moveDir
     */
    public void setMoveDir(MoveDirection moveDir) {
        this.moveDir = moveDir;
    }

    /**
     * updates the rotation of the tank
     *
     * @param rotation
     */
    public void setRotation(double rotation) {
        this.rotation = (rotation + 360) % 360;
    }

    /**
     * updates the turret direction of the tank
     *
     * @param turretDir
     */
    public void setTurretDir(DoubleVec turretDir) {
        this.turretDir = turretDir;
    }

    /**
     * updates the movement of the tank
     *
     * @param move
     */
    public void setMove(boolean move) {
        this.move = move;
    }
}

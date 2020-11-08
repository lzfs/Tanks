package pp.tanks.message.data;

import pp.tanks.model.item.MoveDirection;
import pp.util.DoubleVec;

/**
 * Represents the data of a tank that is sent to the server
 */
public class TankData extends Data{
    private MoveDirection moveDir;
    private DoubleVec turretDir;
    private boolean move;
    private double rotation;
    private int lifepoints;

    public TankData(DoubleVec pos, int id, int lifepoints) {
        super(pos, id);
        this.moveDir = MoveDirection.RIGHT;
        this.turretDir = new DoubleVec(0,0);
        this.move = false;
        this.rotation = 0;
        this.lifepoints = lifepoints;
    }

    /**
     * checks if the tank is moving
     * @return move
     */
    public boolean isMoving() {
        return move;
    }

    public MoveDirection getMoveDir() {
        return moveDir;
    }

    public DoubleVec getTurretDir() {
        return turretDir;
    }

    public double getRotation() {
        return rotation;
    }

    /**
     * updates the lifepoints of the tank
     * @param lifepoints
     */
    public void setLifepoints(int lifepoints) {
        this.lifepoints = lifepoints;
    }


    public int getLifepoints() {
        return lifepoints;
    }

    /**
     * updates the move direction of the tank
     * @param moveDir
     */
    public void setMoveDir(MoveDirection moveDir) {
        this.moveDir = moveDir;
    }

    /**
     * updates the rotation of the tank
     * @param rotation
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    /**
     * updates the turret direction of the tank
     * @param turretDir
     */
    public void setTurretDir(DoubleVec turretDir) {
        this.turretDir = turretDir;
    }

    /**
     * updates the movement of the tank
     * @param move
     */
    public void setMove(boolean move) {
        this.move = move;
    }
}

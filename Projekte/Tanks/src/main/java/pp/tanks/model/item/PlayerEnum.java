package pp.tanks.model.item;

/**
 * Enumeration of the players that can join the game
 */
public enum PlayerEnum {
    PLAYER1(0, 1000), PLAYER2(1, 2000);

    private int tankID;
    private int projectileID;

    PlayerEnum(int tankID, int projectileID) {
        this.tankID = tankID;
        this.projectileID = projectileID;
    }

    public int getTankID(PlayerEnum player) {
        return player.tankID;
    }

    public int getProjectileID(PlayerEnum player) {
        return player.projectileID;
    }
}

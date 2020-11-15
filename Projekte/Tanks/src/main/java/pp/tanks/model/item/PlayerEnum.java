package pp.tanks.model.item;

/**
 * Enumeration of the players that can join the game
 */
public enum PlayerEnum {
    PLAYER1(0, 1000),
    PLAYER2(1, 2000),
    SERVER(-1, -1000);

    public final int tankID;
    public final int projectileID;

    PlayerEnum(int tankID, int projectileID) {
        this.tankID = tankID;
        this.projectileID = projectileID;
    }

    public static PlayerEnum getPlayer(int id) {
        if (id == 0) return PLAYER1;
        else return PLAYER2;
    }

    public int getEnemyID() {
        if (this == PLAYER1) return 1;
        else return 0;
    }
}

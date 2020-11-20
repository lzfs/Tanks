package pp.tanks.server;

/**
 * Enumeration for GameMode-Types
 */
public enum GameMode {
    /**
     * used for singleplayer
     */
    SINGLEPLAYER,
    /**
     * used for multiplayer
     */
    MULTIPLAYER,
    /**
     * used for tutorial and debug
     */
    TUTORIAL;

    public static int getBlockIdStart (GameMode mode,int mapCounter) {
        if (mode == SINGLEPLAYER) {
            if (mapCounter == 1) {
                return  3;
            } else {
                return 4;
            }
        }
        else if (mode == MULTIPLAYER) {
            return 2;
        }
        else {
            if (mapCounter == 0){
                return 2;
            } else if (mapCounter == 3) {
                return 1;
            }
        }
        throw new IllegalArgumentException("wrong");
    }
}

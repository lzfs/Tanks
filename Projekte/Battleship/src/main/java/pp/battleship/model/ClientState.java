package pp.battleship.model;

/**
 * States to determine the controls a client can see
 */
public enum ClientState {
    WAIT, SELECT_SHIP, PLACE_SHIP, ALL_PLACED, INVALID_PLACEMENT, SHOOT, WON, LOST
}

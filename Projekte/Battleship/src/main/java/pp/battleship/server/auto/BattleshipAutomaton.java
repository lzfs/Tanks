package pp.battleship.server.auto;

import pp.battleship.model.ClientState;
import pp.battleship.server.BattleshipServer;
import pp.battleship.server.Player;

import java.util.logging.Logger;

/**
 * The automaton containing the game logic for battleships
 */
public class BattleshipAutomaton extends BattleshipStatemachine {
    static final Logger LOGGER = Logger.getLogger(BattleshipAutomaton.class.getName());
    final BattleshipServer bs;
    private Player activePlayer;

    final BattleshipState lobbyState = new LobbyState(this);
    final BattleshipState waitingForPlayer2 = new WaitingForPlayer2(this);
    final BattleshipState setupState = new SetupState(this);
    final BattleshipState playState = new PlayState(this);
    final BattleshipState gameOver = new GameOver(this);

    /**
     * Creates a new automaton
     *
     * @param bs the server that holds the automaton
     */
    public BattleshipAutomaton(BattleshipServer bs) {
        this.bs = bs;
        entry();
    }

    @Override
    public BattleshipAutomaton containingState() {
        return null;
    }

    @Override
    BattleshipAutomaton getAuto() {
        return this;
    }

    Player getActivePlayer() {
        return activePlayer;
    }

    void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * Sets the client state to determine the controls
     *
     * @param p        the target player
     * @param infoText info text for the player
     * @param state    control state
     */
    void setClientState(Player p, String infoText, ClientState state) {
        p.setState(state);
        p.setInfoText(infoText);
        bs.sendMap(p);
    }

    /**
     * Returns the initial state for the BattleshipAutomaton
     *
     * @return initial state
     */
    @Override
    public BattleshipState init() { return lobbyState; }
}

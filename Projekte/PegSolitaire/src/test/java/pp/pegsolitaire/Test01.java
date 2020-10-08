package pp.pegsolitaire;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pp.pegsolitaire.model.Cross;
import pp.pegsolitaire.model.Square;
import pp.pegsolitaire.model.SquareState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Testklasse fuer PegSolitaire
 */

public class Test01 {

    private Cross cross;

    /**
     * Initialisierung der Testumgebung
     */
    @BeforeEach
    public void setUp() {
        cross = new Cross();
    }

    /**
     * Testet ob ein nach einem Zug der uebersprungene Stein entfernt wurde
     * erfolgreich
     */
    @Test
    public void testRun() {
        // aktiver Stein auf Position (4,2)
        Square selected = cross.getSquare(4, 2);
        cross.setSelectedSquare(selected);

        // Zielposisition des Zuges
        Square aim = cross.getSquare(4, 4);

        // Ausfuehrung des Zuges
        aim.getState().handleClickEvent(cross, aim);

        // Pruefung ob uebersprungener Stein entfernt wurde, d.h. State = EmptySquareState
        assertSame(SquareState.Empty,
                   cross.getSquare(4, 3).getState(),
                   "knight should habe been removed");
    }

    /**
     * Beispiel positiver Test der Gleichheit selbst definierter Klassen mit assertEquals
     */
    @Test
    public void testEqual() {
        Square sq1 = new Square(1, 1, SquareState.Empty);
        Square sq2 = new Square(1, 1, SquareState.Empty);

        assertEquals(sq1, sq2, "squares should have been equal");
    }
}

package pp.tanks;

import pp.util.StringProperty;

public enum TanksStringProperty implements StringProperty {
    hintText("Drücke ESC, um zum Menü zu schalten"),
    remainingLivesText("Verbleibende Leben: ");

    TanksStringProperty(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    private final String defaultValue;
}


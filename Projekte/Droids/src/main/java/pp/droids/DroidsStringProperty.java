package pp.droids;

import pp.util.StringProperty;

public enum DroidsStringProperty implements StringProperty {
    hintText("Drücke ESC, um zum Menü zu schalten"),
    remainingLivesText("Verbleibende Leben: ");

    DroidsStringProperty(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    private final String defaultValue;
}

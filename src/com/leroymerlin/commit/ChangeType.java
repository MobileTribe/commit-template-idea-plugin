package com.leroymerlin.commit;

/**
 * From https://github.com/commitizen/conventional-commit-types
 *
 * @author Damien Arrachequesne
 */
public enum ChangeType {

    FEAT("feat", ""),
    FIX("fix", ""),
    DOCS("docs", ""),
    STYLE("style", ""),
    REFACTOR("refactor", ""),
    PERF("perf", ""),
    TEST("test", ""),
    BUILD("build", ""),
    CI("ci", ""),
    CHORE("chore", ""),
    REVERT("revert", "");

    private final String title;
    private String i18n;

    ChangeType(String title, String i18n) {
        this.title = title;
        this.i18n = i18n;
    }

    public void setI18n(String i18n) {
        this.i18n = i18n;
    }

    public String label(boolean english) {
        return english ? title : i18n;
    }

    public static ChangeType lookup(String value) {
        for (ChangeType type : ChangeType.values()) {
            if (type.title.equalsIgnoreCase(value) || type.i18n.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("ChangeType not found: " + value);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.name(), this.title);
    }
}

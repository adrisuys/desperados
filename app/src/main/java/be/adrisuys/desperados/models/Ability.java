package be.adrisuys.desperados.models;

public enum Ability {
    BRAIN(2),
    UGLY(3),
    BAD(4),
    LADY(5),
    BOSS(6)
    ;

    private int value;

    Ability(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

package desktop.model.primitives;

import desktop.exception.UnsupportedGameTypeException;

public enum GameType {
    POKER("Poker"),N_PLUS("N+"),N_PRODUCT("N*");

    private String name;

    GameType(String s) {
        this.name = s;
    }

    public String getName() {
        return name;
    }

    public static GameType get(String name) throws UnsupportedGameTypeException {
        for (GameType g: GameType.values()) {
            if (g.getName().equals(name)) {
                return g;
            }
        }
        throw new UnsupportedGameTypeException("No game named " + name);
    }
}

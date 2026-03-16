package com.pao.laboratory03.enums;

public enum Priority {

    LOW(1, "green") {
        @Override
        public String getEmoji() { return "🟢"; }
    },
    MEDIUM(2, "yellow") {
        @Override
        public String getEmoji() { return "🟡"; }
    },
    HIGH(3, "orange") {
        @Override
        public String getEmoji() { return "🟠"; }
    },
    CRITICAL(4, "red") {
        @Override
        public String getEmoji() { return "🔴"; }
    };

    // 2. Câmpurile private
    private final int level;
    private final String color;

    // 3. Constructorul (este implicit private la enum-uri)
    Priority(int level, String color) {
        this.level = level;
        this.color = color;
    }

    // 4. Getterii
    public int getLevel() {
        return level;
    }

    public String getColor() {
        return color;
    }

    // 5. Metoda abstractă (șablonul pe care îl respectă constantele de mai sus)
    public abstract String getEmoji();
}
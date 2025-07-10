package bbTan.my_baseball_all_star.domain;

import java.util.Arrays;

public enum Mode {
    RANDOM;

    public static boolean isValidPosition(String parameter) {
        return Arrays.stream(Mode.values())
                .anyMatch(position -> position.name().equalsIgnoreCase(parameter));
    }
}

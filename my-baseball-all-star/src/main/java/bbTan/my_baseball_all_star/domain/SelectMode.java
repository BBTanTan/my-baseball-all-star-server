package bbTan.my_baseball_all_star.domain;

import java.util.Arrays;

public enum SelectMode {
    RANDOM;

    public static boolean isValidSelectMode(String inputMode) {
        return Arrays.stream(SelectMode.values())
                .anyMatch(position -> position.name().equalsIgnoreCase(inputMode));
    }
}

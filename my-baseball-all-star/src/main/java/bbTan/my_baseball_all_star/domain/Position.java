package bbTan.my_baseball_all_star.domain;

import lombok.Getter;

@Getter
public enum Position {

    STARTING_PITCHER(1, "선발 투수"),
    MIDDLE_PITCHER(2, "중간 투수"),
    CLOSER_PITCHER(3, "마무리 투수"),
    CATCHER(4, "포수"),
    FIRST_BASE(5, "1루수"),
    SECOND_BASE(6, "2루수"),
    THIRD_BASE(7, "3루수"),
    SHORTSTOP(8, "유격수"),
    OUT_FIELD(9, "외야수"),
    DESIGNATED_HITTER(10, "지명타자");

    private final int id;
    private final String name;

    Position(int id, String name) {
        this.id = id;
        this.name = name;
    }
}


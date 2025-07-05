package bbTan.my_baseball_all_star.domain;

import lombok.Getter;

@Getter
public enum Club {

    SAMSUNG_LIONS(1, "삼성 라이온즈"),
    LOTTE_GIANTS(2, "롯데 자이언츠"),
    LG_TWINS(3, "LG 트윈스"),
    HANWHA_EAGLES(4, "한화 이글스"),
    DOOSAN_BEARS(5, "두산 베어스"),
    KIA_TIGERS(6, "KIA 타이거즈"),
    KIWOOM_HEROES(7, "키움 히어로즈"),
    NC_DINOS(8, "NC 다이노스"),
    KT_WIZ(9, "KT WIZ"),
    SSG_LANDERS(10, "SSG 랜더스");

    private final int id;
    private final String name;

    Club(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

package bbTan.my_baseball_all_star.fixture;

import bbTan.my_baseball_all_star.domain.Team;

public class TeamFixture {

    public static Team TEAM1() {
        return new Team("팀1");
    }

    public static Team TEAM2() {
        return new Team("팀2");
    }
}

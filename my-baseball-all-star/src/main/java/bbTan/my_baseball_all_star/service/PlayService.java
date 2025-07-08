package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.TeamRoaster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlayService {

    @Transactional
    public List<Integer> soloPlay(TeamRoaster home, TeamRoaster away) {
        Integer homeTeamScore = TeamScoreCalculator.calculate(home);
        Integer awayTeamScore = TeamScoreCalculator.calculate(away);
        return List.of(homeTeamScore, awayTeamScore);
    }
}

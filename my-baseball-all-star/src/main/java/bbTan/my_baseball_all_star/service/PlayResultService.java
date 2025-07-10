package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.PlayResult;
import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.domain.TeamRoaster;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import bbTan.my_baseball_all_star.repository.PlayResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlayResultService {

    private final PlayResultRepository playResultRepository;

    @Transactional
    public void saveResult(Team home, TeamRoaster away, List<Integer> playResultScore) {
        if (playResultScore.size() != 2) {
            throw  new AllStarException(ExceptionCode.INVALID_RESULT_SCORE_SIZE);
        }

        PlayResult playResult = new PlayResult(home, away.getName(), playResultScore.get(0), playResultScore.get(1));
        playResultRepository.save(playResult);
    }

    @Transactional(readOnly = true)
    public List<PlayResult> readByTeam(Team team) {
        return playResultRepository.findByTeamId(team.getId());
    }
}

package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.domain.TeamRoaster;
import bbTan.my_baseball_all_star.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public Team readById(Long id) {
        return teamRepository.getById(id);
    }

    @Transactional
    public void recordMatchResult(Long id, boolean win) {
        if (win) {
            teamRepository.incrementTotalAndWinCount(id);
            return;
        }
        teamRepository.incrementTotalCount(id);
    }

    @Transactional
    public Team create(TeamRoaster teamRoaster) {
        return teamRepository.save(new Team(teamRoaster.getName()));
    }
}

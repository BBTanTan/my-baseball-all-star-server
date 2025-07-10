package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.PlayShare;
import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import bbTan.my_baseball_all_star.repository.PlayShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PlayShareService {

    private final PlayShareRepository playShareRepository;

    @Transactional
    public String createShareUrl(Team team, String password) {
        PlayShare playShare = playShareRepository.save(new PlayShare(team, password));
        return playShare.getUrl();
    }

    @Transactional(readOnly = true)
    public Team readTeamByUrl(String teamUrl) {
        Optional<PlayShare> playShare = playShareRepository.findByUrl(teamUrl);
        if (playShare.isEmpty()) {
            throw new AllStarException(ExceptionCode.TEAM_URL_NOT_FOUND);
        }
        return playShare.get().getTeam();
    }

    @Transactional(readOnly = true)
    public Team readTeamByOwner(Long teamId, String password) {
        Optional<PlayShare> playShare = playShareRepository.findFirstByTeamId(teamId);
        if (playShare.isEmpty()) {
            throw new AllStarException(ExceptionCode.TEAM_SHARE_NOT_FOUND);
        }
        PlayShare share = playShare.get();
        validatePlaySharePassword(password, share);
        return share.getTeam();
    }

    private void validatePlaySharePassword(String password, PlayShare playShare) {
        if (!password.equals(playShare.getPassword())) {
            throw new AllStarException(ExceptionCode.INVALID_PLAY_SHARE_PASSWORD);
        }
    }
}

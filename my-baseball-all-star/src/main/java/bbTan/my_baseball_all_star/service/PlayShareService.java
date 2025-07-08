package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.PlayShare;
import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.repository.PlayShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PlayShareService {

    private final PlayShareRepository playShareRepository;

    @Transactional
    public String createShareUrl(Team team, String password) {
        PlayShare playShare = playShareRepository.save(new PlayShare(team, password));
        return playShare.getUrl();
    }
}

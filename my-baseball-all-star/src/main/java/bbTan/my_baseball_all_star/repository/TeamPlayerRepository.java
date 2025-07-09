package bbTan.my_baseball_all_star.repository;

import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.TeamPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamPlayerRepository extends JpaRepository<TeamPlayer, Long> {
    List<TeamPlayer> findByTeamId(Long teamId);
}

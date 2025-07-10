package bbTan.my_baseball_all_star.repository;

import bbTan.my_baseball_all_star.domain.PlayResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlayResultRepository extends JpaRepository<PlayResult, Long> {
    List<PlayResult> findByTeamId(Long teamId);
}

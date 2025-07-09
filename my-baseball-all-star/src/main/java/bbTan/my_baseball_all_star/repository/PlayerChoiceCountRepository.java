package bbTan.my_baseball_all_star.repository;


import bbTan.my_baseball_all_star.domain.PlayerChoiceCount;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlayerChoiceCountRepository extends JpaRepository<PlayerChoiceCount, Long> {

    Optional<PlayerChoiceCount> findByPlayerId(Long playerId);

    default PlayerChoiceCount getByPlayerId(Long playerId) {
        return findByPlayerId(playerId).orElseThrow(() -> new AllStarException(ExceptionCode.PLAYER_CHOICE_COUNT_NOT_FOUND));
    }
}

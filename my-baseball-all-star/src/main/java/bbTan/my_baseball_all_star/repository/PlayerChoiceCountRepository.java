package bbTan.my_baseball_all_star.repository;


import bbTan.my_baseball_all_star.domain.PlayerChoiceCount;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerChoiceCountRepository extends JpaRepository<PlayerChoiceCount, Long> {

    default PlayerChoiceCount getById(Long playerId) {
        return findById(playerId).orElseThrow(() -> new AllStarException(ExceptionCode.PLAYER_CHOICE_COUNT_NOT_FOUND));
    }
}

package bbTan.my_baseball_all_star.repository;

import bbTan.my_baseball_all_star.domain.Player;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Player p SET p.score = :score WHERE p.id = :id")
    @Transactional
    int updateScoreById(@Param("id") Long id, @Param("score") Double score);
}

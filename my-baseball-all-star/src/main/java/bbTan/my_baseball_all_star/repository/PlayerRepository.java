package bbTan.my_baseball_all_star.repository;

import bbTan.my_baseball_all_star.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {

}

package bbTan.my_baseball_all_star.repository;

import bbTan.my_baseball_all_star.domain.PlayShare;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlayShareRepository extends JpaRepository<PlayShare, Long> {

    Optional<PlayShare> findByUrl(String url);
}

package bbTan.my_baseball_all_star.repository;


import bbTan.my_baseball_all_star.domain.Team;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    default Team getById(Long id) {
        return findById(id).orElseThrow(() -> new AllStarException(ExceptionCode.TEAM_NOT_FOUND));
    }

    @Transactional
    @Modifying
    @Query("UPDATE Team t SET t.totalPlayCount = t.totalPlayCount + 1, t.winPlayCount = t.winPlayCount + 1 WHERE t.id = :id")
    void incrementTotalAndWinCount(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Team t SET t.totalPlayCount = t.totalPlayCount + 1 WHERE t.id = :id")
    void incrementTotalCount(@Param("id") Long id);
}

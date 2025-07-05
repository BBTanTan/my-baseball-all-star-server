package bbTan.my_baseball_all_star;

import bbTan.my_baseball_all_star.JpaAuditingTest.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {
}

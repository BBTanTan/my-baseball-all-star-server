package bbTan.my_baseball_all_star;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JpaAuditingTest extends IntegrationTestSupport {

    @Autowired
    private TestRepository testRepository;

    @DisplayName("JPA Auditing 성공")
    @Test
    void jpaAuditing() {
        // given
        TestEntity testEntity = new TestEntity("테스트");

        // when
        TestEntity savedEntity = testRepository.save(testEntity);

        // then
        assertAll(
                () -> assertThat(savedEntity.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now()),
                () -> assertThat(savedEntity.getModifiedAt()).isBeforeOrEqualTo(LocalDateTime.now())
        );
    }

    @DisplayName("JPA Auditing 성공 : Entity 값 변경 시 수정 시간이 modifiedAt에 반영된다.")
    @Test
    void jpaAuditing_modifyEntity() {
        // given
        TestEntity testEntity = new TestEntity("테스트");
        TestEntity savedEntity = testRepository.save(testEntity);

        // when
        savedEntity.setName("이름");
        TestEntity updatedEntity = testRepository.save(savedEntity);

        // then
        assertAll(
                () -> assertThat(savedEntity.getCreatedAt()).isEqualTo(updatedEntity.getCreatedAt()),
                () -> assertThat(savedEntity.getModifiedAt()).isBefore(updatedEntity.getModifiedAt())
        );
    }

    @Table(name = "test_entity")
    @Entity
    @EntityListeners(AuditingEntityListener.class)
    static class TestEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @CreatedDate
        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @LastModifiedDate
        @Column(nullable = false)
        private LocalDateTime modifiedAt;

        protected TestEntity() {
        }

        public TestEntity(String name) {
            this.name = name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getModifiedAt() {
            return modifiedAt;
        }
    }
}

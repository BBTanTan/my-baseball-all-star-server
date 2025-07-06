package bbTan.my_baseball_all_star.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class PlayResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    private String opposingTeamName;

    private Integer ownTeamScore;

    private Integer opposingTeam;

    @CreatedDate
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayResult that = (PlayResult) o;
        return Objects.equals(id, that.id) && Objects.equals(team, that.team)
                && Objects.equals(opposingTeamName, that.opposingTeamName) && Objects.equals(
                ownTeamScore, that.ownTeamScore) && Objects.equals(opposingTeam, that.opposingTeam)
                && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, team, opposingTeamName, ownTeamScore, opposingTeam, createdAt);
    }

    @Override
    public String toString() {
        return "PlayResult{" +
                "id=" + id +
                ", team=" + team +
                ", opposingTeamName='" + opposingTeamName + '\'' +
                ", ownTeamScore=" + ownTeamScore +
                ", opposingTeam=" + opposingTeam +
                ", createdAt=" + createdAt +
                '}';
    }
}

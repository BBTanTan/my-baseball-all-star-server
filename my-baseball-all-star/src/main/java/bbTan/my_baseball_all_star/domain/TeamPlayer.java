package bbTan.my_baseball_all_star.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class TeamPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    private Player player;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TeamPlayer that = (TeamPlayer) o;
        return Objects.equals(id, that.id) && Objects.equals(team, that.team)
                && Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, team, player);
    }

    @Override
    public String toString() {
        return "TeamPlayer{" +
                "id=" + id +
                ", team=" + team +
                ", player=" + player +
                '}';
    }
}

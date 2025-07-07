package bbTan.my_baseball_all_star.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class PlayerChoiceCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    private Long count = 0L;

    @Version
    private Long version;

    public PlayerChoiceCount(Player player) {
        this.player = player;
    }

    public void increase() {
        this.count++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerChoiceCount that = (PlayerChoiceCount) o;
        return Objects.equals(Id, that.Id) && Objects.equals(player, that.player)
                && Objects.equals(count, that.count) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, player, count, version);
    }

    @Override
    public String toString() {
        return "PlayerChoiceCount{" +
                "Id=" + Id +
                ", player=" + player +
                ", count=" + count +
                ", version=" + version +
                '}';
    }
}

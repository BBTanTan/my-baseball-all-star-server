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
public class PlayShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    private String url;

    private String ownerPassword;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayShare playShare = (PlayShare) o;
        return Objects.equals(id, playShare.id) && Objects.equals(team, playShare.team)
                && Objects.equals(url, playShare.url) && Objects.equals(ownerPassword,
                playShare.ownerPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, team, url, ownerPassword);
    }

    @Override
    public String toString() {
        return "PlayShare{" +
                "id=" + id +
                ", team=" + team +
                ", url='" + url + '\'' +
                ", ownerPassword='" + ownerPassword + '\'' +
                '}';
    }
}

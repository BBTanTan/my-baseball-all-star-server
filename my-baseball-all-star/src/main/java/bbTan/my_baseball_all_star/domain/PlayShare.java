package bbTan.my_baseball_all_star.domain;

import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class PlayShare {

    private static final int MIN_PASSWORD_LENGTH = 4;
    private static final int MAX_PASSWORD_LENGTH = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    private String url;

    private String password;

    public PlayShare(Team team, String password) {
        this.team = team;
        this.url = UUID.randomUUID().toString();
        validatePassword(password);
        this.password = password;
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new AllStarException(ExceptionCode.INVALID_PASSWORD);
        }
    }

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
                && Objects.equals(url, playShare.url) && Objects.equals(password, playShare.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, team, url, password);
    }

    @Override
    public String toString() {
        return "PlayShare{" +
                "id=" + id +
                ", team=" + team +
                ", url='" + url + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

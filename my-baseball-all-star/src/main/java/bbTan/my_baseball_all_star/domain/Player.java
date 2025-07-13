package bbTan.my_baseball_all_star.domain;

import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Club club;

    @Enumerated(EnumType.STRING)
    private Position position;

    private LocalDate dateOfBirth;

    private Double score;

    private String profileUrl;

    public Player(String name, Club club, Position position, LocalDate dateOfBirth, Double score, String profileUrl) {
        this.name = name;
        this.club = club;
        this.position = position;
        this.dateOfBirth = dateOfBirth;
        this.score = score;
        this.profileUrl = profileUrl;
    }

    public void updateScore(Double score) {
        if (score < 0 || score > 100) {
            throw new AllStarException(ExceptionCode.INVALID_PLAYER_SCORE);
        }
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return Objects.equals(id, player.id) && Objects.equals(name, player.name) && club == player.club
                && position == player.position && Objects.equals(dateOfBirth, player.dateOfBirth)
                && Objects.equals(score, player.score) && Objects.equals(profileUrl, player.profileUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, club, position, dateOfBirth, score, profileUrl);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", club=" + club +
                ", position=" + position +
                ", dateOfBirth=" + dateOfBirth +
                ", score=" + score +
                ", profileURL" + profileUrl +
                '}';
    }
}

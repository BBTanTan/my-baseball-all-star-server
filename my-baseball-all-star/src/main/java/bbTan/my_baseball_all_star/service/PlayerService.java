package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.controller.dto.response.PlayerResponse;
import bbTan.my_baseball_all_star.domain.Player;
import bbTan.my_baseball_all_star.domain.Position;
import bbTan.my_baseball_all_star.domain.TeamRoaster;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import bbTan.my_baseball_all_star.repository.PlayerRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Transactional(readOnly = true)
    public List<Player> readPlayers(List<Long> playerIds) {
        return playerRepository.findAllById(playerIds);
    }

    @Transactional(readOnly = true)
    public List<PlayerResponse> readAllPlayers() {
        return playerRepository.findAll().stream()
                .map(PlayerResponse::fromEntity)
                .toList();
    }

    //각 포지션마다 선수 랜덤 추출
    public List<Player> randomPlayerSelection() {
        Map<Position, List<Player>> groupedPlayersByPosition = playerRepository.findAll().stream()
                .collect(Collectors.groupingBy(Player::getPosition));

        return buildRandomTeam(groupedPlayersByPosition);
    }

    private List<Player> buildRandomTeam(Map<Position, List<Player>> playerPositionMap) {
        List<Player> team = new ArrayList<>();

        for (Position position : Position.values()) {
            List<Player> candidates = playerPositionMap.get(position);
            if (candidates == null || candidates.isEmpty()) {
                throw new IllegalArgumentException("No candidates for position: " + position);
            }

            if (position == Position.OUT_FIELD) {
                team.addAll(pickRandom(candidates, 3)); // 외야수 3명 추출
            } else {
                Player player = pickRandom(candidates, 1).get(0); // 1명 추출
                team.add(player);
            }
        }

        return team;
    }

    private List<Player> pickRandom(List<Player> source, int count) {
        if (source.size() < count) {
            throw new AllStarException(ExceptionCode.INSUFFICIENT_CANDIDATES);
        }

        List<Player> copy = new ArrayList<>(source);
        Collections.shuffle(copy);
        return copy.subList(0, count);
    }

}

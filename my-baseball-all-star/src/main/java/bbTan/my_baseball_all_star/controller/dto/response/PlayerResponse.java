package bbTan.my_baseball_all_star.controller.dto.response;

import bbTan.my_baseball_all_star.domain.Player;

public record PlayerResponse (long id, String name, String dateOfBirth,
                              String club, String position, String profileUrl) {
    public static PlayerResponse fromEntity(Player player) {
        return new PlayerResponse(player.getId(), player.getName(), player.getDateOfBirth().toString(),
                player.getClub().getName(), player.getPosition().getName(), player.getProfileUrl());
    }
}

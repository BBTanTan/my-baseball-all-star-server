package bbTan.my_baseball_all_star.controller.dto.response;

import java.util.List;

public record PositionGroupResponse(String position, List<PlayerResponse> players) {}

package bbTan.my_baseball_all_star.controller;

import bbTan.my_baseball_all_star.controller.dto.request.FriendPlayCreateRequest;
import bbTan.my_baseball_all_star.controller.dto.request.FriendPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.SoloPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.TeamPlayResultRequest;
import bbTan.my_baseball_all_star.controller.dto.response.FriendPlayCreateResponse;
import bbTan.my_baseball_all_star.controller.dto.response.FriendPlayTeamResponse;
import bbTan.my_baseball_all_star.controller.dto.response.PlayResultResponse;
import bbTan.my_baseball_all_star.controller.dto.response.PositionGroupResponse;
import bbTan.my_baseball_all_star.controller.dto.response.RandomTeamPlayerResponse;
import bbTan.my_baseball_all_star.controller.dto.response.TeamPlayResultResponse;
import bbTan.my_baseball_all_star.service.AllStarFacadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AllStarController {

    private final AllStarFacadeService allStarService;

    @PostMapping("/plays/solo")
    public ResponseEntity<PlayResultResponse> soloPlay(@Valid @RequestBody SoloPlayRequest request) {
        return ResponseEntity.ok(allStarService.soloPlay(request));
    }

    @GetMapping("/players")
    public ResponseEntity<List<PositionGroupResponse>> getAllPlayers() {
        return ResponseEntity.ok(allStarService.findAllPlayers());
    }

    @PostMapping("/plays/friend")
    public ResponseEntity<PlayResultResponse> friendPlay(@Valid @RequestBody FriendPlayRequest request) {
        return ResponseEntity.ok(allStarService.friendPlay(request));
    }

    @PostMapping("/teams")
    public ResponseEntity<FriendPlayCreateResponse> createFriendPlay(@Valid @RequestBody FriendPlayCreateRequest request) {
        return ResponseEntity.ok(allStarService.createFriendPlay(request));
    }

    @GetMapping("/teams")
    public ResponseEntity<RandomTeamPlayerResponse> getRandomTeams(@RequestParam(value = "mode") String mode) {
        return ResponseEntity.ok(allStarService.makeRandomTeamRoaster(mode));
    }

    @GetMapping("/teams/{team-uuid}")
    public ResponseEntity<FriendPlayTeamResponse> readFriendPlayTeam(@PathVariable("team-uuid") String teamUrl) {
        return ResponseEntity.ok(allStarService.readFriendPlayTeam(teamUrl));
    }

    @PostMapping("/play-results/{team-id}")
    public ResponseEntity<TeamPlayResultResponse> readTeamPlayResults(@PathVariable("team-id") Long teamId,
                                                                      @Valid @RequestBody TeamPlayResultRequest request) {
        return ResponseEntity.ok(allStarService.readTeamPlayResults(teamId, request));
    }
}

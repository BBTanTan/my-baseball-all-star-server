package bbTan.my_baseball_all_star.controller;

import bbTan.my_baseball_all_star.controller.dto.request.FriendPlayCreateRequest;
import bbTan.my_baseball_all_star.controller.dto.request.FriendPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.request.SoloPlayRequest;
import bbTan.my_baseball_all_star.controller.dto.response.FriendPlayCreateResponse;
import bbTan.my_baseball_all_star.controller.dto.response.PlayResultResponse;
import bbTan.my_baseball_all_star.service.AllStarFacadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AllStarController {

    private final AllStarFacadeService allStarService;

    @PostMapping("/plays/solo")
    public ResponseEntity<PlayResultResponse> soloPlay(@Valid @RequestBody SoloPlayRequest request) {
        return ResponseEntity.ok(allStarService.soloPlay(request));
    }

    @PostMapping("/plays/friend")
    public ResponseEntity<PlayResultResponse> friendPlay(@Valid @RequestBody FriendPlayRequest request) {
        return ResponseEntity.ok(allStarService.friendPlay(request));
    }

    @PostMapping("/teams")
    public ResponseEntity<FriendPlayCreateResponse> createFriendPlay(@Valid @RequestBody FriendPlayCreateRequest request) {
        return ResponseEntity.ok(allStarService.createFriendPlay(request));
    }
}

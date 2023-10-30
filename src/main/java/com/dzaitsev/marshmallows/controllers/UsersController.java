package com.dzaitsev.marshmallows.controllers;

import com.dzaitsev.marshmallows.dto.UserRole;
import com.dzaitsev.marshmallows.dto.auth.ChangePasswordRequest;
import com.dzaitsev.marshmallows.dto.auth.SaveMyInfoRequest;
import com.dzaitsev.marshmallows.dto.request.AcceptInviteRequest;
import com.dzaitsev.marshmallows.dto.request.AddDeliverymanRequest;
import com.dzaitsev.marshmallows.dto.response.AssociatedUsersResponse;
import com.dzaitsev.marshmallows.dto.response.UserInfoResponse;
import com.dzaitsev.marshmallows.service.AuthenticationService;
import com.dzaitsev.marshmallows.service.UserService;
import com.dzaitsev.marshmallows.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final AuthenticationService authenticationService;

    private final VerificationService verificationService;

    private final UserService userService;


    @GetMapping("/my")
    public ResponseEntity<UserInfoResponse> getMyInfo() {
        return ResponseEntity.ok(new UserInfoResponse(authenticationService.getUserFromContext()));
    }


    @PostMapping("/save")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> saveMyInfo(@RequestBody SaveMyInfoRequest saveMyInfoRequest) {
        userService.saveMyInfo(saveMyInfoRequest);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        verificationService.changePassword(changePasswordRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-associated-users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AssociatedUsersResponse> getAssociatedUser(@RequestParam("role") UserRole role) {
        return ResponseEntity.ok(new AssociatedUsersResponse(userService.getAssociatedUser(role)));
    }

    @PostMapping("add-deliveryman")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<AssociatedUsersResponse> addDeliveryman(@RequestBody AddDeliverymanRequest request) {
        userService.addDeliveryman(request.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("accept-invite-request")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<AssociatedUsersResponse> acceptInviteRequest(@RequestBody AcceptInviteRequest request) {
        userService.acceptInviteRequest(request.getRequestId());
        return ResponseEntity.noContent().build();
    }
}
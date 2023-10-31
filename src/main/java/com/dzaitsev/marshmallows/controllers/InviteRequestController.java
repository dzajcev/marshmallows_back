package com.dzaitsev.marshmallows.controllers;

import com.dzaitsev.marshmallows.dto.InviterRequestDirection;
import com.dzaitsev.marshmallows.dto.request.AcceptInviteRequest;
import com.dzaitsev.marshmallows.dto.request.AddInviteRequestRequest;
import com.dzaitsev.marshmallows.dto.response.DeliverymenResponse;
import com.dzaitsev.marshmallows.dto.response.InviteRequestsResponse;
import com.dzaitsev.marshmallows.service.InviteRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invite-request")
@RequiredArgsConstructor
public class InviteRequestController {

    private final InviteRequestService inviteRequestService;

    @GetMapping(value = "/get-deliverymen", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DeliverymenResponse getDeliverymen() {
        return new DeliverymenResponse(inviteRequestService.getDeliverymen());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addInviteRequest(@RequestBody AddInviteRequestRequest request) {
        inviteRequestService.addInviteRequest(request.getLogin());
    }

    @PostMapping(value = "accept-invite-request", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptInviteRequest(@RequestBody AcceptInviteRequest request) {
        inviteRequestService.acceptInviteRequest(request.getRequestId());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public InviteRequestsResponse getInviteRequests(@RequestParam("direction") InviterRequestDirection direction,
                                                    @RequestParam(value = "is-accepted", required = false) Boolean accepted) {
        return new InviteRequestsResponse(inviteRequestService.getInviteRequests(direction, accepted));
    }

    @DeleteMapping("/{requestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInviteRequest(@PathVariable("requestId") Integer direction) {
        inviteRequestService.deleteInviteRequest(direction);
    }

}

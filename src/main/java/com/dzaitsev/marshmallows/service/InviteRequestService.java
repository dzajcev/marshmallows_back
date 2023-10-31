package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.InviteRequest;
import com.dzaitsev.marshmallows.dto.InviterRequestDirection;
import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.dto.UserRole;

import java.util.Collection;
import java.util.List;

public interface InviteRequestService {
    Collection<User> getDeliverymen();

    void addInviteRequest(String login);

    void acceptInviteRequest(Integer requestId);

    List<InviteRequest> getInviteRequests(InviterRequestDirection direction, Boolean accepted);

    void deleteInviteRequest(Integer requestId);
}

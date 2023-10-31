package com.dzaitsev.marshmallows.dto.response;

import com.dzaitsev.marshmallows.dto.InviteRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InviteRequestsResponse {
    private List<InviteRequest> requests;
}

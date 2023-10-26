package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.dto.auth.*;

public interface AuthenticationService {
    JwtSignUpResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);



    User getUserFromContext();


}
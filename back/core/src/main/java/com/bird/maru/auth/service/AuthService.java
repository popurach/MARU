package com.bird.maru.auth.service;

import com.bird.maru.domain.model.type.CustomUserDetails;
import java.util.Map;

public interface AuthService {

    Map<String, String> generateToken(CustomUserDetails member);

}

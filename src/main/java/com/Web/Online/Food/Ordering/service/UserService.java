package com.Web.Online.Food.Ordering.service;

import com.Web.Online.Food.Ordering.config.JwtProvider;
import com.Web.Online.Food.Ordering.model.User;
import com.Web.Online.Food.Ordering.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    public User findUserByJwtToken (String jwt) throws Exception {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        return user;
    }

    public User findUserByEmail (String email) throws Exception {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new Exception("User not found");
        }
        return user;
    }
}

package com.Web.Online.Food.Ordering.service;

import com.Web.Online.Food.Ordering.config.JwtProvider;
import com.Web.Online.Food.Ordering.model.Cart;
import com.Web.Online.Food.Ordering.model.USER_ROLE;
import com.Web.Online.Food.Ordering.model.User;
import com.Web.Online.Food.Ordering.repository.CartRepository;
import com.Web.Online.Food.Ordering.repository.UserRepository;
import com.Web.Online.Food.Ordering.request.LoginRequest;
import com.Web.Online.Food.Ordering.response.AuthResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtProvider jwtProvider;
    private CustomerUserDetailsService customerUserDetailsService;
    private CartRepository cartRepository;

    public AuthService( UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, CustomerUserDetailsService customerUserDetailsService, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.customerUserDetailsService = customerUserDetailsService;
        this.cartRepository = cartRepository;
    }

    public AuthResponse signUp (User user) throws Exception {

        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            throw new Exception("Email is already used with another account");
        }

        User createdUser = new User();
        createdUser.setEmail(user.getEmail());
        createdUser.setFullName(user.getFullName());
        createdUser.setRole(user.getRole());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(createdUser);

        Cart cart = new Cart();
        cart.setCustomer(savedUser);
        cartRepository.save(cart);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register success");
        authResponse.setRole(savedUser.getRole());

        return authResponse;
    }

    public AuthResponse login(LoginRequest req) throws Exception {
        String username = req.getEmail();
        String password = req.getPassword();

        Authentication authentication = authenticate (username, password);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login success");
        Collection<? extends GrantedAuthority> authorites = authentication.getAuthorities();

        String role = authorites.isEmpty()? null: authorites.iterator().next().getAuthority();
        authResponse.setRole(USER_ROLE.valueOf(role));

        return authResponse;


    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid Username ");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}

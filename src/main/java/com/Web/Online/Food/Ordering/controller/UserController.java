package com.Web.Online.Food.Ordering.controller;

import com.Web.Online.Food.Ordering.model.Address;
import com.Web.Online.Food.Ordering.model.User;
import com.Web.Online.Food.Ordering.request.PasswordChangeRequest;
import com.Web.Online.Food.Ordering.request.UpdateUserRequest;
import com.Web.Online.Food.Ordering.response.AddressResponse;
import com.Web.Online.Food.Ordering.response.AuthResponse;
import com.Web.Online.Food.Ordering.response.Message;
import com.Web.Online.Food.Ordering.service.AuthService;
import com.Web.Online.Food.Ordering.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> findUserByJwtToken (@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> updateUser (@RequestHeader("Authorization") String jwt,
                                            @RequestBody UpdateUserRequest updateUserRequest
    ) throws Exception {
        User user = userService.updateUser(jwt, updateUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new Message("Updated Successfully"));
    }

    @GetMapping("/address")
    public ResponseEntity<List<AddressResponse>> findAddressByJwtToken (@RequestHeader("Authorization") String jwt) throws Exception {
        List<Address> addresses = userService.findAddressByUser(jwt);
        List<AddressResponse> addressList = userService.mapAddressesToResponse(addresses);
        return ResponseEntity.status(HttpStatus.OK).body(addressList);
    }

    @PutMapping("/address")
    public ResponseEntity<List<AddressResponse>> addAddress (@RequestHeader("Authorization") String jwt,
                                                     @RequestBody Address addAddress) throws Exception {
            List<Address> addresses = userService.addAddress(jwt, addAddress);
        List<AddressResponse> addressList = userService.mapAddressesToResponse(addresses);
        return ResponseEntity.status(HttpStatus.OK).body(addressList);
    }

    @DeleteMapping("/address")
    public ResponseEntity<List<AddressResponse>> deleteAddress (@RequestHeader("Authorization") String jwt,
                                                  @RequestBody Long id ) throws Exception {
        List<Address> addresses = userService.deleteAddress(jwt, id);
        List<AddressResponse> addressList = userService.mapAddressesToResponse(addresses);

        return ResponseEntity.status(HttpStatus.OK).body(addressList);
    }

    @PutMapping("/passwordChange")
    public ResponseEntity<AuthResponse> changePassword (@RequestHeader("Authorization") String jwt,
                                                   @RequestBody PasswordChangeRequest passwordChangeRequest
                                                   ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        AuthResponse authResponse = authService.changePassword(user, passwordChangeRequest);
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

}

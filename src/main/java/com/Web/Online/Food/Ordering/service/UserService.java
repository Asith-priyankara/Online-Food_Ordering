package com.Web.Online.Food.Ordering.service;

import com.Web.Online.Food.Ordering.config.JwtProvider;
import com.Web.Online.Food.Ordering.model.Address;
import com.Web.Online.Food.Ordering.model.User;
import com.Web.Online.Food.Ordering.repository.AddressRepository;
import com.Web.Online.Food.Ordering.repository.UserRepository;
import com.Web.Online.Food.Ordering.request.UpdateUserRequest;
import com.Web.Online.Food.Ordering.response.AddressResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, JwtProvider jwtProvider, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.addressRepository = addressRepository;
    }

    public User findUserByJwtToken (String jwt) throws Exception {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        return user;
    }

    public User updateUser (String jwt, UpdateUserRequest updateUserRequest) throws Exception {
        User user = findUserByJwtToken(jwt);
        if (updateUserRequest.getFullName() != null) {
            user.setFullName(updateUserRequest.getFullName());
        }
        if (updateUserRequest.getPhone() != null) {
            user.setPhone(updateUserRequest.getPhone());
        }
        return userRepository.save(user);
    }

    public List<Address> findAddressByUser (String jwt) throws Exception {
        User user = findUserByJwtToken(jwt);
        List<Address> addressList = addressRepository.findByUserId(user.getId());
        if (addressList.isEmpty()) {
            return new ArrayList<>();
        }
        return addressList;
    }

    public List<Address> addAddress (String jwt, Address address) throws Exception {
        User user = findUserByJwtToken(jwt);
        if (address != null) {
            Address newAddress = new Address();
            newAddress.setUser(user);
            newAddress.setStreet(address.getStreet());
            newAddress.setCity(address.getCity());
            newAddress.setState(address.getState());
            newAddress.setZipCode(address.getZipCode());
            addressRepository.save(newAddress);
        }
        return findAddressByUser(jwt);
    }

    public List<Address> deleteAddress(String jwt, Long addressId) throws Exception {
        List<Address> addressList = findAddressByUser(jwt);
        for (Address address : addressList) {
            if (addressId.equals(address.getId())) {
                addressRepository.delete(address);
            }
        }
        return findAddressByUser(jwt);
    }

    public List<AddressResponse> mapAddressesToResponse(List<Address> addresses) {
        List<AddressResponse> addressList = addresses.stream().map(address -> new AddressResponse(address.getId(),address.getStreet(), address.getCity(), address.getState(), address.getZipCode())).collect(Collectors.toList());
        return addressList;
    }

    public boolean changePassword (String jwt, UpdateUserRequest updateUserRequest) throws Exception {
        User user = findUserByJwtToken(jwt);
        return true;
    }

    public User findUserByEmail (String email) throws Exception {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new Exception("User not found");
        }
        return user;
    }

}

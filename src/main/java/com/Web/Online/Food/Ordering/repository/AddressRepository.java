package com.Web.Online.Food.Ordering.repository;

import com.Web.Online.Food.Ordering.model.Address;
import com.Web.Online.Food.Ordering.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    public List<Address> findByUserId(Long userId);
}

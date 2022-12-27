package com.example.demo1.repositories;

import com.example.demo1.entities.Address;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends CrudRepository<Address, Long> {

    List<Address> findAllByUserGuid(UUID guid);
}

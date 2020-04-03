package com.prs.kalendar.kalendarserv.dao;

import com.prs.kalendar.kalendarserv.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    Users findByUsername(String username);
    Users findByEmailId(String emailId);
}

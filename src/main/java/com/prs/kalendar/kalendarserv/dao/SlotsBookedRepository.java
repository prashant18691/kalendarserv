package com.prs.kalendar.kalendarserv.dao;


import com.prs.kalendar.kalendarserv.entity.SlotsBooked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository
@Transactional
public interface SlotsBookedRepository extends JpaRepository<SlotsBooked, UUID> {

}

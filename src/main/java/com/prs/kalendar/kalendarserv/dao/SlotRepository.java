package com.prs.kalendar.kalendarserv.dao;

import com.prs.kalendar.kalendarserv.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository
@Transactional
public interface SlotRepository extends JpaRepository<Slot, UUID> {
    @Query("select s from Slot s where s.slotId=:id and s.isBooked='N'")
    public Slot findFreeSlotsBySlotId(@Param("id") UUID slotId);

}

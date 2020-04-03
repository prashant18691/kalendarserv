package com.prs.kalendar.kalendarserv.entity;

import com.prs.kalendar.kalendarserv.util.CommonUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class Slot {

    @Id
    @Column(name = "slot_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID slotId;
    @Column(name = "slot_date_time", updatable = false, nullable = false)
    private Timestamp slotDateTime;

    public Slot() {
    }

    public Slot(String dateTime) {
        this.slotDateTime = CommonUtils.strToTimeStamp(dateTime);
    }

    public UUID getSlotId() {
        return slotId;
    }

    public void setSlotId(UUID slotId) {
        this.slotId = slotId;
    }

    public Timestamp getSlotDateTime() {
        return slotDateTime;
    }

    public void setSlotDateTime(Timestamp slotDateTime) {
        this.slotDateTime = slotDateTime;
    }
}

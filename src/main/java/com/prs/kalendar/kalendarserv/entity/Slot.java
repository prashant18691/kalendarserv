package com.prs.kalendar.kalendarserv.entity;

import com.prs.kalendar.kalendarserv.util.CommonUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Slot {

    @Id
    @Column(name = "slot_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID slotId;
    @Column(name = "start_date_time", updatable = false, nullable = false)
    private Timestamp startDateTime;
    @Column(name = "end_date_time", updatable = false, nullable = false)
    private Timestamp endDateTime;
    @Column(name = "is_booked", updatable = true, nullable = false)
    private char isBooked = 'N';

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users users;

    public Slot() {
    }

    public Slot(String dateTime) {
        if (!CommonUtils.checkPastDateTime(dateTime)) {
            this.startDateTime = CommonUtils.strToTimeStamp(dateTime);
            this.endDateTime = CommonUtils.getEndDateTime(this.startDateTime);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return Objects.equals(startDateTime, slot.startDateTime) &&
                Objects.equals(endDateTime, slot.endDateTime) &&
                Objects.equals(users, slot.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDateTime, endDateTime, users);
    }

    public UUID getSlotId() {
        return slotId;
    }

    public void setSlotId(UUID slotId) {
        this.slotId = slotId;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
    }

    public char getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(char isBooked) {
        this.isBooked = isBooked;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}

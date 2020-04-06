package com.prs.kalendar.kalendarserv.entity;

import com.prs.kalendar.kalendarserv.model.SlotVO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Users {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(name = "username", updatable = false, nullable = false)
    private String username;
    @Column(name = "password", updatable = false, nullable = false)
    private String password;
    @Column(name = "first_name", updatable = false, nullable = false)
    private String firstName;
    @Column(name = "last_name", updatable = false, nullable = true)
    private String lastName;
    @Pattern(regexp = "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9]"
            + "(?:[A-Za-z0-9-]*[A-Za-z0-9])?")
    @Column(name = "email_id", updatable = true, nullable = false)
    private String emailId;
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<Slot> slots = new HashSet<>();

    @OneToMany(mappedBy = "users")
    private Set<SlotsBooked> slotsBookeds = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Set<Slot> getSlots() {
        return slots;
    }

    public void setSlots(Set<Slot> slots) {
        this.slots = slots;
    }

    public Set<SlotsBooked> getSlotsBookeds() {
        return slotsBookeds;
    }

    public void setSlotsBookeds(Set<SlotsBooked> slotsBookeds) {
        this.slotsBookeds = slotsBookeds;
    }

    public void addSlots(Set<SlotVO> slotVOs) {
        for(SlotVO slotVO : slotVOs){
            Slot slot = new Slot(slotVO.getSlotDateTime());
            slots.add(slot);
            slot.setUsers(this);
        }
    }
}

package com.prs.kalendar.kalendarserv.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class SlotsBooked {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID bookId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "slot_id", unique = true)
    private Slot slot;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users users;

    public SlotsBooked() {
    }

    public SlotsBooked(Slot slot,Users users) {
        this.slot = slot;
        this.users = users;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}

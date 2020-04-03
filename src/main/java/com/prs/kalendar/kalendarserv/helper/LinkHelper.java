package com.prs.kalendar.kalendarserv.helper;

import com.prs.kalendar.kalendarserv.controller.SlotsController;
import com.prs.kalendar.kalendarserv.controller.UserController;
import org.springframework.hateoas.Link;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class LinkHelper {

    public static Link getUserIdLink(UUID id){
        return linkTo(UserController.class).slash("id").slash(id).withSelfRel();
    }

    public static Link getEmailLink(String email){
        return linkTo(UserController.class).slash("email").slash(email).withSelfRel();
    }

    public static Link getaddSlotsByIdLink(UUID id){
        return linkTo(SlotsController.class).slash("id").slash(id).withRel("add_slots_by_user_id");
    }

    public static Link getaddSlotsByEmailLink(String email){
        return linkTo(SlotsController.class).slash("email").slash(email).withRel("add_slots_by_email");
    }

    public static Link getBookSlotsLink(UUID slotId){
        return linkTo(SlotsController.class).slash(slotId).withRel("book_slots");
    }
}

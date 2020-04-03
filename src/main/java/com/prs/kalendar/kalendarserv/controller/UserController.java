package com.prs.kalendar.kalendarserv.controller;

import com.prs.kalendar.kalendarserv.model.SlotVO;
import com.prs.kalendar.kalendarserv.model.UserVO;
import com.prs.kalendar.kalendarserv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserVO> registerUser(@Valid @RequestBody UserVO userVO){
        UserVO savedUser = userService.save(userVO);
        Link link = linkTo(UserController.class).slash(savedUser.getEmailId()).withSelfRel();
        userVO.add(link);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("users").path("/{emailId}").buildAndExpand(savedUser.getEmailId()).toUri();
        return ResponseEntity.created(uri).body(savedUser);
    }

    @PostMapping("/{emailId}")
    public ResponseEntity<UserVO> addSlotsForUser(@PathVariable ("emailId") String emailId, @RequestBody Set<@Valid SlotVO> slotVOS){
        UserVO userVO = userService.addSlotsToUser(emailId, slotVOS);
        Link userlink = linkTo(UserController.class).slash(userVO.getEmailId()).withSelfRel();
        userVO.add(userlink);
        return ResponseEntity.ok().body(userVO);
    }

    @GetMapping("/{emailId}")
    public ResponseEntity<UserVO> getUserById(@PathVariable ("emailId") String emailId){
        UserVO userVO = userService.findByEmailId(emailId);
        Link selfLink = linkTo(UserController.class).slash(userVO.getEmailId()).withSelfRel();
        userVO.add(selfLink);
        return ResponseEntity.ok(userVO);
    }


   /* @GetMapping("/{userId}/slots/{slotId}")
    public ResponseEntity<SlotVO> getSlotsById(@PathVariable("userId") UUID userId,
                                                       @PathVariable("slotId") UUID slotId){
        UserVO userVO = userService.findByUserId(userId);
        Optional<SlotVO> slotOpt = userVO.getSlotVOs().stream().filter(slotVO -> slotId.equals
                (slotVO.getSlotId())).findAny();
        if (!slotOpt.isPresent()){

        }
        return ResponseEntity.ok(slotVOs.);
    }*/
}

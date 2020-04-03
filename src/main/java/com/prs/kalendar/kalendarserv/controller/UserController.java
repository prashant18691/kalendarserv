package com.prs.kalendar.kalendarserv.controller;

import com.prs.kalendar.kalendarserv.model.UserVO;
import com.prs.kalendar.kalendarserv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

import static com.prs.kalendar.kalendarserv.helper.LinkHelper.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserVO> registerUser(@Valid @RequestBody UserVO userVO){
        UserVO savedUser = userService.save(userVO);
        Link idLink = getUserIdLink(userVO.getId());
        Link emailIdLink = getEmailLink(userVO.getEmailId());
        Link addSlotsLinkById = getaddSlotsByIdLink(userVO.getId());
        Link addSlotsLinkByEmail = getaddSlotsByEmailLink(userVO.getEmailId());
        userVO.add(idLink);
        userVO.add(emailIdLink);
        userVO.add(addSlotsLinkById);
        userVO.add(addSlotsLinkByEmail);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("users").path("id/{id}").buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(uri).body(savedUser);
    }

    @GetMapping("/email/{emailId}")
    public ResponseEntity<UserVO> getUserByEmailId(@PathVariable ("emailId") String emailId){
        UserVO userVO = userService.findByEmailId(emailId);
        Link idLink = getUserIdLink(userVO.getId());
        userVO.add(idLink);
        return ResponseEntity.ok(userVO);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserVO> getUserById(@PathVariable ("id") UUID id){
        UserVO userVO = userService.findByUserId(id);
        Link emailIdLink = getEmailLink(userVO.getEmailId());
        userVO.add(emailIdLink);
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

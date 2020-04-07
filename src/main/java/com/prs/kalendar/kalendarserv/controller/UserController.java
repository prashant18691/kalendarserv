package com.prs.kalendar.kalendarserv.controller;

import com.prs.kalendar.kalendarserv.model.UserVO;
import com.prs.kalendar.kalendarserv.service.UserService;
import com.prs.kalendar.kalendarserv.util.CommonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.net.URI;
import java.util.UUID;

import static com.prs.kalendar.kalendarserv.helper.LinkHelper.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/users")
public class UserController {

    private final static Log logger = LogFactory.getLog(SlotsController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserVO> registerUser(@Valid @RequestBody UserVO userVO){
        logger.info("UserController :: registerUser : start");
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
        logger.info("UserController :: registerUser : end");
        return ResponseEntity.created(uri).body(savedUser);
    }

    @GetMapping("/email/{emailId}")
    public ResponseEntity<UserVO> getUserByEmailId(@PathVariable ("emailId") @NotBlank @Pattern(regexp = CommonUtils.EMAIL_REGEX) String emailId){
        logger.info("UserController :: getUserByEmailId : start");
        UserVO userVO = userService.findByEmailId(emailId);
        Link idLink = getUserIdLink(userVO.getId());
        userVO.add(idLink);
        logger.info("UserController :: getUserByEmailId : end");
        return ResponseEntity.ok(userVO);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserVO> getUserById(@PathVariable ("id") @NotNull UUID id){
        logger.info("UserController :: getUserById : start");
        UserVO userVO = userService.findByUserId(id);
        Link emailIdLink = getEmailLink(userVO.getEmailId());
        userVO.add(emailIdLink);
        logger.info("UserController :: getUserById : end");
        return ResponseEntity.ok(userVO);
    }

}

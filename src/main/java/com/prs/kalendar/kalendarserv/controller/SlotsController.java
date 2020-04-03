package com.prs.kalendar.kalendarserv.controller;

import com.prs.kalendar.kalendarserv.model.SlotVO;
import com.prs.kalendar.kalendarserv.model.UserVO;
import com.prs.kalendar.kalendarserv.service.SlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

import static com.prs.kalendar.kalendarserv.helper.LinkHelper.getEmailLink;
import static com.prs.kalendar.kalendarserv.helper.LinkHelper.getUserIdLink;

@RestController
@RequestMapping("/slots")
public class SlotsController {

    @Autowired
    SlotsService slotsService;

    @PostMapping("email/{emailId}")
    public ResponseEntity<UserVO> addSlotsForUser(@PathVariable("emailId") String emailId, @RequestBody Set<@Valid SlotVO> slotVOS){
        UserVO userVO = slotsService.addSlotsToUserByEmail(emailId, slotVOS);
        Link idLink = getUserIdLink(userVO.getId());
        Link emailIdLink = getEmailLink(emailId);
        userVO.add(idLink);
        userVO.add(emailIdLink);
        return ResponseEntity.ok().body(userVO);
    }

    @PostMapping("id/{id}")
    public ResponseEntity<UserVO> addSlotsForUser(@PathVariable ("id") UUID id, @RequestBody Set<@Valid SlotVO> slotVOS){
        UserVO userVO = slotsService.addSlotsToUserByUserId(id, slotVOS);
        Link idLink = getUserIdLink(userVO.getId());
        Link emailIdLink = getEmailLink(userVO.getEmailId());
        userVO.add(idLink);
        userVO.add(emailIdLink);
        return ResponseEntity.ok().body(userVO);
    }
}

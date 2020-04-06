package com.prs.kalendar.kalendarserv.controller;

import com.prs.kalendar.kalendarserv.model.SlotBookedVO;
import com.prs.kalendar.kalendarserv.model.SlotVO;
import com.prs.kalendar.kalendarserv.model.UserVO;
import com.prs.kalendar.kalendarserv.request.BookSlotRequest;
import com.prs.kalendar.kalendarserv.service.SlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;
import java.util.UUID;

import static com.prs.kalendar.kalendarserv.helper.LinkHelper.getEmailLink;
import static com.prs.kalendar.kalendarserv.helper.LinkHelper.getUserIdLink;
import static com.prs.kalendar.kalendarserv.util.CommonUtils.*;

@RestController
@RequestMapping("/slots")
public class SlotsController {

    @Autowired
    SlotsService slotsService;

    @PostMapping("email/{emailId}")
    public ResponseEntity<UserVO> addSlotsForUser(@PathVariable("emailId") @NotBlank @Pattern(regexp = EMAIL_REGEX) String emailId, @RequestBody Set<@Valid SlotVO> slotVOS){
        UserVO userVO = slotsService.addSlotsToUserByEmail(emailId, slotVOS);
        Link idLink = getUserIdLink(userVO.getId());
        Link emailIdLink = getEmailLink(emailId);
        userVO.add(idLink);
        userVO.add(emailIdLink);
        return ResponseEntity.ok().body(userVO);
    }

    @PostMapping("/{slotId}")
    public ResponseEntity<SlotBookedVO> bookSlots(@PathVariable ("slotId") @NotNull UUID slotId, @RequestBody @Valid BookSlotRequest bookSlotRequest){
        SlotBookedVO slotBookedVO = slotsService.bookSlots(slotId, bookSlotRequest);
        return ResponseEntity.ok().body(slotBookedVO);
    }

    @GetMapping("email/{email}/date/{date}")
    public ResponseEntity<Set<SlotVO>> findSlotsByEmailAndDate(@PathVariable ("email") @NotBlank
                                                                  @Pattern(regexp = EMAIL_REGEX) String email,
                                                              @PathVariable ("date") @NotBlank
                                                              @Pattern(regexp = DATE_REGEX) String date){
        Set<SlotVO> slotsByEmailAndDate = null;
        if (checkPastDate(date)){
            slotsByEmailAndDate = slotsService.findSlotsByEmailAndDate(email, date);
        }
        return ResponseEntity.ok().body(slotsByEmailAndDate);
    }
}

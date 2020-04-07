package com.prs.kalendar.kalendarserv.controller;

import com.prs.kalendar.kalendarserv.googlecalsync.GoogleNotifications;
import com.prs.kalendar.kalendarserv.model.SlotBookedVO;
import com.prs.kalendar.kalendarserv.model.SlotVO;
import com.prs.kalendar.kalendarserv.model.UserVO;
import com.prs.kalendar.kalendarserv.request.BookSlotRequest;
import com.prs.kalendar.kalendarserv.service.SlotsService;
import com.prs.kalendar.kalendarserv.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private final static Log logger = LogFactory.getLog(SlotsController.class);

    @Autowired
    SlotsService slotsService;

    @Autowired
    GoogleNotifications googleNotifications;

    @PostMapping("email/{emailId}")
    public ResponseEntity<UserVO> addSlotsForUser(@PathVariable("emailId") @NotBlank @Pattern(regexp = EMAIL_REGEX) String emailId, @RequestBody Set<@Valid SlotVO> slotVOS){
        logger.info("SlotsController :: addSlotsForUser : start");
        UserVO userVO = slotsService.addSlotsToUserByEmail(emailId, slotVOS);
        Link idLink = getUserIdLink(userVO.getId());
        Link emailIdLink = getEmailLink(emailId);
        userVO.add(idLink);
        userVO.add(emailIdLink);
        logger.info("SlotsController :: addSlotsForUser : end");
        return ResponseEntity.ok().body(userVO);
    }

    @PostMapping("/{slotId}")
    public ResponseEntity<SlotBookedVO> bookSlots(@PathVariable ("slotId") @NotNull UUID slotId, @RequestBody @Valid BookSlotRequest bookSlotRequest){
        logger.info("SlotsController :: bookSlots : start");
        SlotBookedVO slotBookedVO = slotsService.bookSlots(slotId, bookSlotRequest);
        googleNotifications.addAuthLink(slotBookedVO);
        logger.info("SlotsController :: bookSlots : end");
        return ResponseEntity.ok().body(slotBookedVO);
    }

    @GetMapping("email/{email}/date/{date}")
    public ResponseEntity<Set<SlotVO>> findSlotsByEmailAndDate(@PathVariable ("email") @NotBlank
                                                                  @Pattern(regexp = EMAIL_REGEX) String email,
                                                              @PathVariable ("date") @NotBlank
                                                              @Pattern(regexp = DATE_REGEX) String date){
        logger.info("SlotsController :: findSlotsByEmailAndDate : start");
        Set<SlotVO> slotsByEmailAndDate = null;
        if (checkPastDate(date)){
            slotsByEmailAndDate = slotsService.findSlotsByEmailAndDate(email, date);
        }
        logger.info("SlotsController :: findSlotsByEmailAndDate : end");
        return ResponseEntity.ok().body(slotsByEmailAndDate);
    }
}

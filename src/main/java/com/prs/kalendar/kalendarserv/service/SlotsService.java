package com.prs.kalendar.kalendarserv.service;

import com.prs.kalendar.kalendarserv.dao.SlotRepository;
import com.prs.kalendar.kalendarserv.dao.SlotsBookedRepository;
import com.prs.kalendar.kalendarserv.dao.UserRepository;
import com.prs.kalendar.kalendarserv.entity.Slot;
import com.prs.kalendar.kalendarserv.entity.SlotsBooked;
import com.prs.kalendar.kalendarserv.entity.Users;
import com.prs.kalendar.kalendarserv.exception.custom.SlotBookingException;
import com.prs.kalendar.kalendarserv.exception.custom.SlotExistsException;
import com.prs.kalendar.kalendarserv.exception.custom.SlotNotAvailableException;
import com.prs.kalendar.kalendarserv.exception.custom.UserNotFoundException;
import com.prs.kalendar.kalendarserv.model.SlotBookedVO;
import com.prs.kalendar.kalendarserv.model.SlotVO;
import com.prs.kalendar.kalendarserv.model.UserVO;
import com.prs.kalendar.kalendarserv.request.BookSlotRequest;
import com.prs.kalendar.kalendarserv.util.CommonUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.prs.kalendar.kalendarserv.helper.CopyHelper.copySlotsBookedToSlotBookedVO;
import static com.prs.kalendar.kalendarserv.helper.CopyHelper.copySlotsToVO;

@Service
public class SlotsService {

    @Autowired
    SlotRepository slotRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SlotsBookedRepository slotsBookedRepository;

    public UserVO addSlotsToUserByEmail(String emailId, Set<SlotVO> slotVOs){
        Users user = userRepository.findByEmailId(emailId);
        if (user==null){
            throw new UserNotFoundException("No user found with email Id: "+emailId);
        }
        UserVO userVO = null;
        try {
            userVO = saveSlots(slotVOs, user);
        }
        catch (DataIntegrityViolationException e){
            throw new SlotExistsException("Entered slots are already available for user with email id "+emailId);
        }
        return userVO;
    }

    public UserVO addSlotsToUserByUserId(UUID id, Set<SlotVO> slotVOs){
        Optional<Users> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()){
            throw new UserNotFoundException("No user found with user id: "+id);
        }
        Users user = userOpt.get();
        UserVO userVO = null;
        try {
            userVO = saveSlots(slotVOs, user);
        }
        catch (DataIntegrityViolationException e){
            throw new SlotExistsException("Entered slots are already available for user with email id "+id);
        }
        return userVO;
    }

    public Set<SlotVO> findSlotsByEmailAndDate(String email, String date){
        Users users = userRepository.findByEmailId(email);
        Set<Slot> slots = users.getSlots();
        slots = slots.stream().filter(k -> k.getIsBooked() == 'N' &&
                        DateUtils.isSameDay(CommonUtils.getTimeStampToDate(k.getStartDateTime()),CommonUtils.getDateFromStr(date))
                ).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(slots))
            throw new SlotNotAvailableException("No slots are available for given search criteria");
        Set<SlotVO> slotVOS = new HashSet<>();
        copySlotsToVO(slots,slotVOS);
        return slotVOS;
    }

    public SlotBookedVO bookSlots(UUID slotId, BookSlotRequest bookSlotRequest){
        Slot slot = slotRepository.findFreeSlotsBySlotId(slotId);
        if (slot==null){
            throw new SlotNotAvailableException("Selected slot is not available for booking");
        }
        Users users = userRepository.findByEmailId(bookSlotRequest.getEmail());
        if (users==null){
            throw new UserNotFoundException("Selected user not found");
        }
        slot.setIsBooked('Y');
        SlotsBooked slotsBooked = new SlotsBooked(slot, users);
        final SlotBookedVO slotBookedVO;
        try {
            slotsBookedRepository.save(slotsBooked);
            slotBookedVO = new SlotBookedVO();
            copySlotsBookedToSlotBookedVO(slotsBooked,slotBookedVO);
//            new Thread(()-> GoogleNotifications.send(slotBookedVO)).start();
        }
        catch (DataIntegrityViolationException e){
            throw new SlotBookingException("Error while booking slot : "+slotId);
        }
        return slotBookedVO;
    }



    private UserVO saveSlots(Set<SlotVO> slotVOs, Users user) {
        Set<Slot> slots = user.getSlots();
        if (CollectionUtils.isEmpty(slots)) {
            slots = new HashSet<>();
            user.setSlots(slots);
        }
        user.addSlots(slotVOs);
        userRepository.save(user);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO,"slots");
        if (CollectionUtils.isEmpty(user.getSlots()))
            return userVO;
        Set<SlotVO> slotSet = new HashSet<>();
        userVO.setSlots(slotSet);
        copySlotsToVO(slots,slotSet);
        return userVO;
    }
}

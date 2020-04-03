package com.prs.kalendar.kalendarserv.service;

import com.prs.kalendar.kalendarserv.dao.UserRepository;
import com.prs.kalendar.kalendarserv.entity.Slot;
import com.prs.kalendar.kalendarserv.entity.Users;
import com.prs.kalendar.kalendarserv.exception.custom.SlotExistsException;
import com.prs.kalendar.kalendarserv.exception.custom.UserNotFoundException;
import com.prs.kalendar.kalendarserv.model.SlotVO;
import com.prs.kalendar.kalendarserv.model.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.prs.kalendar.kalendarserv.helper.CopyHelper.copySlotsToVO;
import static com.prs.kalendar.kalendarserv.helper.CopyHelper.copySlotsVOtoSlots;

@Service
public class SlotsService {
    @Autowired
    UserRepository userRepository;

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

    private UserVO saveSlots(Set<SlotVO> slotVOs, Users user) {
        Set<Slot> slots = user.getSlots();
        if (CollectionUtils.isEmpty(slots))
            slots = new HashSet<>();
        user.setSlots(slots);
        copySlotsVOtoSlots(slotVOs, slots);
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

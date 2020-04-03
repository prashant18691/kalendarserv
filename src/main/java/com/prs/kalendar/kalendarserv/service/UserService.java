package com.prs.kalendar.kalendarserv.service;

import com.prs.kalendar.kalendarserv.dao.UserRepository;
import com.prs.kalendar.kalendarserv.entity.Slot;
import com.prs.kalendar.kalendarserv.entity.Users;
import com.prs.kalendar.kalendarserv.exception.SlotExistsException;
import com.prs.kalendar.kalendarserv.exception.UserExistsException;
import com.prs.kalendar.kalendarserv.exception.UserNotFoundException;
import com.prs.kalendar.kalendarserv.model.SlotVO;
import com.prs.kalendar.kalendarserv.model.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public UserVO save(UserVO userVO)
    {
        Users user = new Users();
        BeanUtils.copyProperties(userVO,user);
        user.setPassword(passwordEncoder.encode(user.getEmailId()));
        try {
            userRepository.save(user);
        }
        catch (DataIntegrityViolationException e){
            throw new UserExistsException("A user with email id "+userVO.getEmailId()+" already present");
        }
        userVO.setId(user.getId());
        Set<SlotVO> slotSet = new HashSet<>();
        copySlotsToVO(user.getSlots(),slotSet);
        userVO.setSlotVOs(slotSet);
        return userVO;
    }

    public List<UserVO> getAllUsers(){
        List<Users> userList = userRepository.findAll();
        List<UserVO> userVOList = new ArrayList<>();
        UserVO userVO = null;
        Set<SlotVO> slotSet = null;
        for (Users user:userList){
            userVO = new UserVO();
            slotSet = new HashSet<>();
            BeanUtils.copyProperties(user,userVO);
            userVO.setSlotVOs(slotSet);
            copySlotsToVO(user.getSlots(),slotSet);
            userVOList.add(userVO);
        }
        return userVOList;
    }

    public UserVO findByUserId(UUID userId){
        Optional<Users> user = userRepository.findById(userId);
        if (!user.isPresent()){
            throw new UserNotFoundException("No user found with id: "+userId);
        }
        UserVO userVO = new UserVO();
        Set<SlotVO> slotSet = new HashSet<>();
        BeanUtils.copyProperties(user.get(),userVO);
        userVO.setSlotVOs(slotSet);
        copySlotsToVO(user.get().getSlots(),slotSet);
        return userVO;
    }

    public UserVO findByEmailId(String emailId){
        Users user = userRepository.findByEmailId(emailId);
        if (user==null){
            throw new UserNotFoundException("No user found with email id: "+emailId);
        }
        UserVO userVO = new UserVO();
        Set<SlotVO> slotSet = new HashSet<>();
        BeanUtils.copyProperties(user,userVO);
        userVO.setSlotVOs(slotSet);
        copySlotsToVO(user.getSlots(),slotSet);
        return userVO;
    }

    public UserVO addSlotsToUser(String emailId, Set<SlotVO> slotVOs){
        Users user = userRepository.findByEmailId(emailId);
        if (user==null){
            throw new UserNotFoundException("No user found with email Id: "+emailId);
        }
        Set<Slot> slots = user.getSlots();
        if (CollectionUtils.isEmpty(slots))
            slots = new HashSet<>();
        user.setSlots(slots);
        copySlotsVOtoSlots(slotVOs, slots);
        try {
            userRepository.save(user);
        }
        catch (DataIntegrityViolationException e){
            throw new SlotExistsException("Entered slots are already available for user with email id "+emailId);
        }
        UserVO userVO = new UserVO();
        Set<SlotVO> slotSet = new HashSet<>();
        userVO.setSlotVOs(slotSet);

        BeanUtils.copyProperties(user,userVO,"slots");
        copySlotsToVO(slots,slotSet);
        return userVO;
    }

    private void copySlotsVOtoSlots(Set<SlotVO> slotVOs, Set<Slot> slots) {
        for(SlotVO slotVO : slotVOs){
            slots.add(new Slot(slotVO.getSlotDateTime()));
        }
    }

    private void copySlotsToVO(Set<Slot> slots, Set<SlotVO> slotSet) {
        for (Slot eachSlot: slots){
            SlotVO slotVO = new SlotVO(eachSlot.getSlotId(),eachSlot.getSlotDateTime());
            slotSet.add(slotVO);
        }
    }
}

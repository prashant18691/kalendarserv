package com.prs.kalendar.kalendarserv.service;

import com.prs.kalendar.kalendarserv.dao.UserRepository;
import com.prs.kalendar.kalendarserv.entity.Users;
import com.prs.kalendar.kalendarserv.exception.custom.UserExistsException;
import com.prs.kalendar.kalendarserv.exception.custom.UserNotFoundException;
import com.prs.kalendar.kalendarserv.model.SlotVO;
import com.prs.kalendar.kalendarserv.model.UserVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.prs.kalendar.kalendarserv.helper.CopyHelper.copySlotsToVO;

@Service
public class UserService {


    private final static Log logger = LogFactory.getLog(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public UserVO save(UserVO userVO)
    {
        logger.info("UserService :: save : start");
        Users user = new Users();
        BeanUtils.copyProperties(userVO,user,"slots");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        }
        catch (DataIntegrityViolationException e){
            logger.warn("A user with entered email id exists");
            throw new UserExistsException("A user with email id "+userVO.getEmailId()+" already present");
        }
        userVO.setId(user.getId());
        logger.info("UserService :: save : end");
        return userVO;
    }

    public List<UserVO> getAllUsers(){
        logger.info("UserService :: getAllUsers : start");
        List<Users> userList = userRepository.findAll();
        List<UserVO> userVOList = new ArrayList<>();
        UserVO userVO = null;
        Set<SlotVO> slotSet = null;
        for (Users user:userList){
            userVO = new UserVO();
            BeanUtils.copyProperties(user,userVO,"slots");
            if (CollectionUtils.isEmpty(user.getSlots()))
                continue;
            slotSet = new HashSet<>();
            userVO.setSlots(slotSet);
            copySlotsToVO(user.getSlots(),slotSet);
            userVOList.add(userVO);
        }
        logger.info("UserService :: getAllUsers : end");
        return userVOList;
    }

    public UserVO findByUserId(UUID userId){
        logger.info("UserService :: findByUserId : start");
        Optional<Users> user = userRepository.findById(userId);
        if (!user.isPresent()){
            logger.warn("No user found");
            throw new UserNotFoundException("No user found with id: "+userId);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user.get(),userVO,"slots");
        if (CollectionUtils.isEmpty(user.get().getSlots()))
            return userVO;
        Set<SlotVO> slotSet = new HashSet<>();
        userVO.setSlots(slotSet);
        copySlotsToVO(user.get().getSlots(),slotSet);
        logger.info("UserService :: findByUserId : end");
        return userVO;
    }

    public UserVO findByEmailId(String emailId){
        logger.info("UserService :: findByEmailId : start");
        Users user = userRepository.findByEmailId(emailId);
        if (user==null){
            logger.warn("No user found");
            throw new UserNotFoundException("No user found with email id: "+emailId);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO,"slots");
        if (CollectionUtils.isEmpty(user.getSlots()))
            return userVO;
        Set<SlotVO> slotSet = new HashSet<>();
        userVO.setSlots(slotSet);
        copySlotsToVO(user.getSlots(),slotSet);
        logger.info("UserService :: findByEmailId : end");
        return userVO;
    }

}

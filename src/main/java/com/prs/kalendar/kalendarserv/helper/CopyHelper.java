package com.prs.kalendar.kalendarserv.helper;

import com.prs.kalendar.kalendarserv.entity.Slot;
import com.prs.kalendar.kalendarserv.entity.SlotsBooked;
import com.prs.kalendar.kalendarserv.model.SlotBookedVO;
import com.prs.kalendar.kalendarserv.model.SlotVO;

import java.util.Arrays;
import java.util.Set;

import static com.prs.kalendar.kalendarserv.util.CommonUtils.timeStampToStr;

public class CopyHelper {



    public static void copySlotsVOtoSlots(Set<SlotVO> slotVOs, Set<Slot> slots) {
        for(SlotVO slotVO : slotVOs){
            slots.add(new Slot(slotVO.getSlotDateTime()));
        }
    }

    public static void copySlotsToVO(Set<Slot> slots, Set<SlotVO> slotSet) {
        for (Slot eachSlot: slots){
            SlotVO slotVO = new SlotVO(eachSlot.getSlotId(),eachSlot.getStartDateTime());
            if (eachSlot.getIsBooked()=='N')//show book link only when it is unbooked
                slotVO.add(LinkHelper.getBookSlotsLink(eachSlot.getSlotId()));
            slotSet.add(slotVO);
        }
    }

    public static void copySlotsBookedToSlotBookedVO(SlotsBooked slotsBooked, SlotBookedVO slotBookedVO) {
        slotBookedVO.setBookId(slotsBooked.getBookId());
        slotBookedVO.setStartDateTime(timeStampToStr(slotsBooked.getSlot().getStartDateTime()));
        slotBookedVO.setEndDateTime(timeStampToStr(slotsBooked.getSlot().getEndDateTime()));
        slotBookedVO.setAttendees(Arrays.asList(slotsBooked.getUsers().getEmailId(),slotsBooked.getSlot().getUsers().getEmailId()));
    }
}

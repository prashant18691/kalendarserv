package com.prs.kalendar.kalendarserv.helper;

import com.prs.kalendar.kalendarserv.entity.Slot;
import com.prs.kalendar.kalendarserv.model.SlotVO;

import java.util.Set;

public class CopyHelper {



    public static void copySlotsVOtoSlots(Set<SlotVO> slotVOs, Set<Slot> slots) {
        for(SlotVO slotVO : slotVOs){
            slots.add(new Slot(slotVO.getSlotDateTime()));
        }
    }

    public static void copySlotsToVO(Set<Slot> slots, Set<SlotVO> slotSet) {
        for (Slot eachSlot: slots){
            SlotVO slotVO = new SlotVO(eachSlot.getSlotId(),eachSlot.getSlotDateTime());
            slotVO.add(LinkHelper.getBookSlotsLink(eachSlot.getSlotId()));
            slotSet.add(slotVO);
        }
    }
}

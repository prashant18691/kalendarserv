package com.prs.kalendar.kalendarserv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.prs.kalendar.kalendarserv.util.CommonUtils;
import com.prs.kalendar.kalendarserv.validators.PresentOrFuture;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Validated
@JsonPropertyOrder({
        "slot_id",
        "slot_date_time"
})
public class SlotVO extends RepresentationModel<SlotVO> {
    @JsonProperty("slot_id")
    private UUID slotId;
    @JsonProperty("slot_date_time")
    @NotBlank
    @PresentOrFuture
    @Pattern(regexp = "^((0[1-9])|([12][0-9])|(3[01]))/((0[1-9])|1[012])/\\d\\d\\d\\d ((((0[1-9])|(1[0-9])|(2[0-3])):[0-5][0-9])|24:00)$")
    private String slotDateTime;

    public SlotVO() {
    }
    public SlotVO(UUID id,Timestamp dateTime) {
        this.slotId=id;
        this.slotDateTime= CommonUtils.timeStampToStr(dateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SlotVO slotVO = (SlotVO) o;
        return Objects.equals(slotId, slotVO.slotId) &&
                Objects.equals(slotDateTime, slotVO.slotDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), slotId, slotDateTime);
    }

    public UUID getSlotId() {
        return slotId;
    }

    public void setSlotId(UUID slotId) {
        this.slotId = slotId;
    }

    public String getSlotDateTime() {
        return slotDateTime;
    }

    public void setSlotDateTime(String slotDateTime) {
            this.slotDateTime=slotDateTime;
    }
}

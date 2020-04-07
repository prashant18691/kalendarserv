package com.prs.kalendar.kalendarserv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.UUID;
@JsonPropertyOrder({
        "book_id",
        "start_date_time",
        "end_date_time",
        "attendees",
        "add_to_calendar"
})
public class SlotBookedVO extends RepresentationModel<SlotBookedVO> {
    @JsonProperty("book_id")
    private UUID bookId;
    @JsonProperty("start_date_time")
    private String startDateTime;
    @JsonProperty("end_date_time")
    private String endDateTime;
    @JsonProperty("attendees")
    private List<String> attendees;
    @JsonProperty("add_to_calendar")
    private String addToCalendar;

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public List<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<String> attendees) {
        this.attendees = attendees;
    }

    public String getAddToCalendar() {
        return addToCalendar;
    }

    public void setAddToCalendar(String addToCalendar) {
        this.addToCalendar = addToCalendar;
    }
}

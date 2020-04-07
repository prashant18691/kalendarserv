package com.prs.kalendar.kalendarserv.googlecalsync;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.prs.kalendar.kalendarserv.exception.custom.GoogleAuthorizationException;
import com.prs.kalendar.kalendarserv.model.SlotBookedVO;
import com.prs.kalendar.kalendarserv.util.CommonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
public class GoogleNotifications {

    private final static Log logger = LogFactory.getLog(GoogleNotifications.class);
    private static final String APPLICATION_NAME = "Kalendar";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static com.google.api.services.calendar.Calendar client;
    private static Map<String,Object> cache = new HashMap<>();

    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
    Credential credential;

    @Value("${google.client.client-id}")
    private String clientId;
    @Value("${google.client.client-secret}")
    private String clientSecret;
    @Value("${google.client.redirectUri}")
    private String redirectURI;

    private  String authorize() throws Exception {
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null) {
            GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                    Collections.singleton(CalendarScopes.CALENDAR_EVENTS)).setAccessType("offline").build();
        }
        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
//        System.out.println("cal authorizationUrl->" + authorizationUrl);
        return authorizationUrl.build();
    }


    public void addAuthLink(SlotBookedVO slotBookedVO) {
        logger.info("GoogleNotifications :: authorize : start");
        try {
            cache.put("SlotBookedVO",slotBookedVO);
            slotBookedVO.setAddToCalendar(authorize());
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw new GoogleAuthorizationException("Exception while creating google authorization link");
        }
        logger.info("GoogleNotifications :: authorize : end");
    }

    @RequestMapping(value = "/Callback", method = RequestMethod.GET, params = "code")
    public ResponseEntity<String> oauth2Callback(@RequestParam(value = "code") String code) {
        logger.info("GoogleNotifications :: oauth2Callback : start");
        String message;
        try {
            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
            credential = flow.createAndStoreCredential(response, "userID");
            client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
            Event event = createEvent();
            if (event==null){
               message = "No event found add to calendar";
            }
            else {
                addEvent(event);
                message = "Event added to Calendar";
            }
        } catch (Exception e) {
            logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.");
            message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.";
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    private static Event createEvent() {
        if (cache.isEmpty() || !cache.containsKey("SlotBookedVO")) {
            logger.warn("No event found add to calendar");
            return null;
        }
        SlotBookedVO slotBookedVO = (SlotBookedVO) cache.remove("SlotBookedVO");
        Event event = new Event()
                .setSummary(APPLICATION_NAME+" :: "+slotBookedVO.getBookId());

        DateTime startDateTime = CommonUtils.convertStrToIsoDateFormat(slotBookedVO.getStartDateTime());

        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Kolkata");
        event.setStart(start);
        logger.info("start date time : "+start.toString());
        DateTime endDateTime = CommonUtils.convertStrToIsoDateFormat(slotBookedVO.getEndDateTime());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Kolkata");
        event.setEnd(end);
        logger.info("end date time : "+end.toString());

        EventAttendee[] attendees = null;
        if (!CollectionUtils.isEmpty(slotBookedVO.getAttendees())){
            attendees = new EventAttendee[]{
                new EventAttendee().setEmail(slotBookedVO.getAttendees().get(0)),
                        new EventAttendee().setEmail(slotBookedVO.getAttendees().get(1)),
            };
        }
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);
        return event;
    }

    private static void addEvent(Event event) throws IOException {
        if (event!=null)
            client.events().insert("primary", event).execute();
    }

}

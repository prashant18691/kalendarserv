package com.prs.kalendar.kalendarserv.googlecalsync;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.prs.kalendar.kalendarserv.model.SlotBookedVO;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;

public class GoogleNotifications {

    private static HttpTransport httpTransport;

    private static FileDataStoreFactory dataStoreFactory;

    private static final String APPLICATION_NAME = "Kalendar";


    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.home"), ".store/calendar_sample");


    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static com.google.api.services.calendar.Calendar client;

    static {
        try {

            // initialize the transport
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            // initialize the data store factory
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

            // authorization
            Credential credential = authorize();

            // set up global Calendar instance
            client = new com.google.api.services.calendar.Calendar.Builder(
                    httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /** Authorizes the installed application to access user's protected data. */
    private static Credential authorize() throws Exception {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(GoogleNotifications.class.getResourceAsStream("/client_secrets.json")));

        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(CalendarScopes.CALENDAR_EVENTS)).setDataStoreFactory(dataStoreFactory)
                .build();
        LocalServerReceiver.Builder builder =  new LocalServerReceiver.Builder();
        LocalServerReceiver localServerReceiver = builder.setHost("localhost").setPort(8080).build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, localServerReceiver).authorize("user");
    }

    public static void send(SlotBookedVO slotBookedVO) {
        try {
            Event event = createEvent(slotBookedVO);
            addEvent(event);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    private static Event createEvent(SlotBookedVO slotBookedVO) {

        Event event = new Event()
                .setSummary("Google I/O 2015")
                .setLocation("800 Howard St., San Francisco, CA 94103");

        DateTime startDateTime = new DateTime("2020-04-07T09:00:00+05:30");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Kolkata");
        event.setStart(start);

        DateTime endDateTime = new DateTime("2020-04-07T10:00:00+05:30");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Kolkata");
        event.setEnd(end);

        /*String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));*/

        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail("prashant18691@gmail.com"),
                new EventAttendee().setEmail("vibhor.sunny007@gmail.com"),
        };
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
        Event result = client.events().insert("primary", event).execute();
    }

}

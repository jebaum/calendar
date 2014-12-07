package yamlcal.calendar;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import static spark.Spark.*;

import com.fasterxml.jackson.core.JsonProcessingException; // thrown by ObjectMapper.writeValueAsString()
import com.fasterxml.jackson.databind.ObjectMapper;

import yamlcal.type.Event;
import yamlcal.type.Title;
import yamlcal.type.Location;
import yamlcal.type.Description;
import yamlcal.type.Category;
import yamlcal.type.StartTime;
import yamlcal.type.EndTime;
// import yamlcal.type.DueDate;

import org.yaml.snakeyaml.Yaml;

public class Server {

    // TODO: there are a handful of magic values through the current codebase
    // API endpoints, status codes, jackson annotations, etc. fix this
    public static void main(String[] args) throws JsonProcessingException, FileNotFoundException, Exception {

        // http://yaml-online-parser.appspot.com/
        InputStream input = new FileInputStream(new File("calendar.yaml"));
        Yaml calReader    = new Yaml();

        @SuppressWarnings("unchecked")
        Map<String, List<Map<String, String>>> eventList = (Map<String, List<Map<String, String>>>) calReader.load(input);

        // TODO: duplicate keys to the map may cause problems? two different events with the same name?
        List<Event> calendarList = new ArrayList<Event>();
        for (String eventTitleString : eventList.keySet()) {
            Title eventTitle = new Title(eventTitleString);

            List<Map<String, String>> eventAttributeList = eventList.get(eventTitleString);
            Map<String, String> eventAttributeMap        = new HashMap<String, String>();
            for (Map<String, String> eventAttribute : eventAttributeList) {
                eventAttributeMap.putAll(eventAttribute);
            }

            Location eventLocation       = new Location(eventAttributeMap.get("location"));
            Description eventDescription = new Description(eventAttributeMap.get("description"));
            Category eventCategory       = new Category(eventAttributeMap.get("category"));

            String eventStartTimeString = eventAttributeMap.get("start");
            StartTime eventStartTime    = null;
            // TODO: need a more robust way to parse dates, or at least need to allow for not specifying time?
            if (eventStartTimeString != null) {
                DateFormat formatter = new SimpleDateFormat("MM/dd/yy h:mm a");
                Date date            = (Date)formatter.parse(eventStartTimeString);
                eventStartTime       = new StartTime(date);
            }

            String eventEndTimeString = eventAttributeMap.get("end");
            EndTime eventEndTime      = null;
            if (eventStartTime != null) {
                DateFormat formatter = new SimpleDateFormat("MM/dd/yy h:mm a");
                Date date            = (Date)formatter.parse(eventEndTimeString);
                eventEndTime         = new EndTime(date);
            }

            Event someEvent = new Event(eventTitle, eventLocation, eventDescription, eventCategory, eventStartTime, eventEndTime);
            calendarList.add(someEvent);
            System.out.println("Event: " + someEvent + " added to calendar list");
        }


        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(calendarList));

        /*
         * BEGIN ENDPOINTS
         */

        get("/date_start/:start/date_end/:end", (request, response) -> {
            String getResponse;
            long rangeStart, rangeEnd;
            try {
                rangeStart = Long.parseLong(request.params(":start"), 10);
                rangeEnd   = Long.parseLong(request.params(":end"), 10);
                System.out.println("start: " + rangeStart + ", end: " + rangeEnd);
            } catch (NumberFormatException e) {
                rangeStart = -1;
                rangeEnd   = -1;
                System.out.println("Passed time parameters were bad");
                e.printStackTrace();
            }

            for (Event e : calendarList) {
                if (e.getStartTime().valueOf() != null) {
                    long msTimestamp = e.getStartTime().valueOf().getTime();
                    msTimestamp /= 1000;
                    if (msTimestamp >= rangeStart && msTimestamp <= rangeEnd) {
                        // TODO: need to build up json output
                        System.out.println(e.getTitle());
                    }
                }
            }

            try {
                getResponse = mapper.writeValueAsString(calendarList);
                response.status(201);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                response.status(500);
                getResponse = "wat";
            }
            return getResponse;
        });

        post("/", (request, response) -> {
            // TODO should events received via post be added to some "master" calendarList, e.g. the existing one?
            // should they be immediately written to the calendar file?
            System.out.println("POST:\n" + request.body());
            return "Thanks for the post!\n" + request.body() + "\n";
        });

    }
}

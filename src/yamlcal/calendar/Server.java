package yamlcal.calendar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

import com.fasterxml.jackson.core.JsonProcessingException; // thrown by ObjectMapper.writeValueAsString()
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import yamlcal.type.Category;
import yamlcal.type.Description;
import yamlcal.type.EndTime;
import yamlcal.type.Event;
import yamlcal.type.Location;
import yamlcal.type.StartTime;
import yamlcal.type.Title;
// import yamlcal.type.DueDate;

import org.yaml.snakeyaml.Yaml;

public class Server {
    private static final String fileName = "calendar.yaml";

    // TODO: there are a handful of magic values through the current codebase
    // API endpoints, status codes, jackson annotations, etc. fix this
    private static List<Event> readCalendarFile()
        throws JsonProcessingException, FileNotFoundException, ParseException {
        // create objects to parse calendar file
        InputStream input = new FileInputStream(new File(fileName));
        Yaml calReader    = new Yaml();

        // read in the calendar file as a java object
        // NOTE: unchecked type conversion error is a false positive, results from how snakeyaml is designed
        @SuppressWarnings("unchecked")
        Map<String, List<Map<String, String>>> eventList = (Map<String, List<Map<String, String>>>) calReader.load(input);


        // create the list that we will populate with calendar Events
        // TODO: duplicate keys to the map may cause problems? two different events with the same name?
        // TODO: add recur handling?
        List<Event> calendarList = new ArrayList<Event>();

        // go through each event in the calendar file, using the event title as the "index"
        for (String eventTitleString : eventList.keySet()) {
            Title eventTitle = new Title(eventTitleString);

            // the actual structure of the parsed calendar file, as seen above, is triply nested, so this is needed to extract the data
            // each attribute of an event (with the exception of titles, which are keys), e.g. location, description, category, etc, is in its own one element map
            // the key of the map is the attribute name
            // eventAttributeList is a list of these maps that we'll go through in order to determine the attribute values
            List<Map<String, String>> eventAttributeList = eventList.get(eventTitleString);
            Map<String, String> eventAttributeMap        = new HashMap<String, String>();
            for (Map<String, String> eventAttribute : eventAttributeList) {
                eventAttributeMap.putAll(eventAttribute); // gather all attributes into a single map to make things easier
            }

            // pure text attributes that don't have to be parsed
            Location eventLocation       = new Location(eventAttributeMap.get("location"));
            Description eventDescription = new Description(eventAttributeMap.get("description"));
            Category eventCategory       = new Category(eventAttributeMap.get("category"));

            // get the raw text of the time, which can be in one of two different formats
            String eventStartTimeString = eventAttributeMap.get("start");
            StartTime eventStartTime    = null;
            if (eventStartTimeString != null) { // it's possible that no start time was specified, in which case we won't include it in our calendarList
                DateFormat formatter = new SimpleDateFormat("MM/dd/yy h:mm a");
                Date date;
                try {
                    date = (Date)formatter.parse(eventStartTimeString);
                } catch (ParseException e) {
                    formatter = new SimpleDateFormat("MM/dd/yy");
                    date      = (Date)formatter.parse(eventStartTimeString);
                }
                eventStartTime = new StartTime(date);
            }

            // same code for end time
            // NOTE: it bothers me that this is virtually identical code, and the way I'm using try-catch is smelly
            // unfortunately I couldn't think of a better design, and this Works for valid calendar files, so it'll do for now
            String eventEndTimeString = eventAttributeMap.get("end");
            EndTime eventEndTime      = null;
            if (eventStartTime != null) {
                DateFormat formatter = new SimpleDateFormat("MM/dd/yy h:mm a");
                Date date;
                try {
                    date = (Date)formatter.parse(eventEndTimeString);
                } catch (ParseException e) {
                    formatter = new SimpleDateFormat("MM/dd/yy");
                    date      = (Date)formatter.parse(eventEndTimeString);
                }
                eventEndTime = new EndTime(date);
            }

            // now that we've gotten all the data for the event, create the Event object and add it to our calendar list
            Event someEvent = new Event(eventTitle, eventLocation, eventDescription, eventCategory, eventStartTime, eventEndTime);
            calendarList.add(someEvent);
            System.err.println("Event: " + someEvent + " added to calendar list");
        }

        return calendarList;
    }
    public static void main(String[] args) throws JsonProcessingException, FileNotFoundException, Exception {

        List<Event> calendarList = readCalendarFile();

        ObjectMapper mapper = new ObjectMapper();
        System.err.println(mapper.writeValueAsString(calendarList));

        /*
         * BEGIN ENDPOINTS
         */

        // GET / should return the web ui's index.html, but I've abandoned hope on that for now
        get ("/", (request, response) -> {
            response.redirect(":4568");
            return response;
        });

        // GET all of the calendar events in the calendar file
        get("/all", (request, response) -> {
            String getResponse;
            try {
                getResponse = mapper.writeValueAsString(calendarList);
                response.status(201);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                response.status(500);
                getResponse = "Something went wrong.";
            }

            return getResponse;
        });

        // GET api for getting calendar data based on a date range
        get("/date_start/:start/date_end/:end", (request, response) -> {
            String getResponse;

            // attempt to get the range
            long rangeStart, rangeEnd;
            try {
                rangeStart = Long.parseLong(request.params(":start"), 10);
                rangeEnd   = Long.parseLong(request.params(":end"), 10);
                System.err.println("start: " + rangeStart + ", end: " + rangeEnd);
            } catch (NumberFormatException e) {
                rangeStart = -1;
                rangeEnd   = -1;
                System.err.println("Passed time parameters were bad");
                e.printStackTrace();
            }

            // go through the calendar list and add events to the response object if they're in the range
            List<Event> responseList = new ArrayList<Event>();
            for (Event e : calendarList) {
                if (e.getStartTime().valueOf() != null) {
                    long msTimestamp = e.getStartTime().valueOf().getTime();
                    msTimestamp /= 1000;
                    if (msTimestamp >= rangeStart && msTimestamp <= rangeEnd) {
                        System.err.println(e.getTitle());
                        responseList.add(e);
                    }
                }
            }

            // attempt to create and send the response
            try {
                getResponse = mapper.writeValueAsString(responseList);
                response.status(201);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                response.status(500);
                getResponse = "Something went wrong.";
            }
            return getResponse;
        });

        // POST endpoint for updating an existing event
        // must receive JSON of exactly two events, the first is the full contents of the event to be deleted
        // the second is the contents of the new event
        post("/update", (request, response) -> {
            System.err.println("POST:\n" + request.body());
            List<Event> eventUpdate;
            try {
                eventUpdate = mapper.readValue(request.body(), new TypeReference<List<Event>>() {});
            } catch (IOException  e) {  // JsonProcessingException is also caught by IOException
                e.printStackTrace();
                eventUpdate = null;
            }

            String responseString;
            if (eventUpdate.size() > 2) {
                responseString = "Invalid POST - must contain two events";
            } else {
                for (Event e : calendarList) {
                    if (eventUpdate.get(0) == e) {
                        calendarList.remove(e);
                    }
                }
                calendarList.add(eventUpdate.get(1));
                responseString = "success";
            }

            return responseString;
        });


        // POST endpoint for receiving updates to the entire calendar file
        // ASSUMPTION: the entire contents of the calendar file will be sent in every POST
        // this simplifies the code, and the requests are very small and infrequent, so it's a good tradeoff
        post("/", (request, response) -> {
            System.err.println("POST:\n" + request.body());

            List<Event> postList; // TODO: should I just use the calendarList from above instead?
            try {
                postList = mapper.readValue(request.body(), new TypeReference<List<Event>>() {});
            } catch (IOException  e) {  // JsonProcessingException is also caught by IOException
                e.printStackTrace();
                postList = null;
            }


            // generate calendar file content
            String calendarFile = "---\n";
            for (Event e : postList) {
                // TODO: problem with this code - how to handle all day events?
                DateFormat formatter = new SimpleDateFormat("MM/dd/yy h:mm a");
                String startTimeString = formatter.format(e.getStartTime().valueOf());
                String endTimeString = formatter.format(e.getEndTime().valueOf());

                // TODO: if something isn't defined, leave it out of the file?
                // if for instance, somebody doesn't specify a category, i think it will be an empty string
                calendarFile += "\n";
                calendarFile +=  e.getTitle().valueOf() + ":\n";
                calendarFile += "  - location: "    + e.getLocation().valueOf() + "\n";
                calendarFile += "  - description: " + e.getDescription().valueOf() + "\n";
                calendarFile += "  - category: "    + e.getCategory().valueOf() + "\n";
                calendarFile += "  - start: "       + startTimeString + "\n";
                calendarFile += "  - end: "         + endTimeString + "\n";
            }

            File file = new File("calendar.yaml");
            try {
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(calendarFile);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Thanks for the post!\n" + request.body() + "\n\n";
        });

    }
}

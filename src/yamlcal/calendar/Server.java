package yamlcal.calendar;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import static spark.Spark.*;

import com.fasterxml.jackson.core.JsonProcessingException; // thrown by ObjectMapper.writeValueAsString()
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import yamlcal.type.Event;
import yamlcal.type.Title;
import yamlcal.type.Location;
import yamlcal.type.Description;
import yamlcal.type.Category;
import yamlcal.type.StartTime;
import yamlcal.type.EndTime;
import yamlcal.type.DueDate;

public class Server {

    // TODO: there are a handful of magic values through the current codebase
    // API endpoints, status codes, jackson annotations, etc. fix this
    public static void main(String[] args) throws JsonProcessingException {
        Title mytitle             = new Title("this is my title");
        Location mylocation       = new Location("here i am");
        Description mydescription = new Description("sup");
        Category mycategory       = new Category("important");
        StartTime mystart         = new StartTime(1414000000000l);
        EndTime myend             = new EndTime(1414005000000l);

        Title mytitle2             = new Title("another title");
        Location mylocation2       = new Location("nowhere");
        Description mydescription2 = new Description("things are weird");
        Category mycategory2       = new Category("unimportant");
        StartTime mystart2         = new StartTime(1414010000000l);
        EndTime myend2             = new EndTime(1414013000000l);

        Event myevent  = new Event(mytitle, mylocation, mydescription, mycategory, mystart, myend);
        Event myevent2 = new Event(mytitle2, mylocation2, mydescription2, mycategory2, mystart2, myend2);

        List<Event> calendarList = new ArrayList<Event>();
        calendarList.add(myevent);
        calendarList.add(myevent2);

        ObjectMapper mapper = new ObjectMapper();

        /*
         * List<Event> calendarList2;
         * try {
         *     calendarList2 = mapper.readValue(mapper.writeValueAsString(calendarList), new TypeReference<List<Event>>(){});
         * } catch (JsonProcessingException e) {
         *     e.printStackTrace();
         *     calendarList2 = null;
         * } catch (IOException e) {
         *     e.printStackTrace();
         *     calendarList2 = null;
         * }
         */

        /*
         * BEGIN ENDPOINTS
         */
        get("/date_start/:start/date_end/:end", (request, response) -> {
            String getResponse;
            System.out.println("Eventually this will return the events in between "
                            + request.params(":start") + " and " + request.params(":end"));
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
            System.out.println("POST:\n" + request.body());
            return "Thanks for the post!\n" + request.body() + "\n";
        });

    }
}

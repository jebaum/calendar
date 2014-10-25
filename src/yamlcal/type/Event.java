package yamlcal.type;

import java.util.Date;

import yamlcal.type.Title;
import yamlcal.type.Location;
import yamlcal.type.Description;
import yamlcal.type.Category;
import yamlcal.type.StartTime;
import yamlcal.type.EndTime;


public class Event {
    private final Title title;
    private final Location location;
    private final Description description;
    private final Category category;
    private final StartTime startTime;
    private final EndTime endTime;

    public Event(Title title, Location location, Description description, Category category, StartTime startTime, EndTime endTime) {
        this.title        = new Title(title.valueOf());
        this.location     = new Location(location.valueOf());
        this.description  = new Description(description.valueOf());
        this.category     = new Category(category.valueOf());
        this.startTime    = new StartTime(startTime.getMilliseconds());
        this.endTime      = new EndTime(endTime.getMilliseconds());
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {"       + NEW_LINE);
        result.append(" Title: "       + this.title.valueOf()       + NEW_LINE);
        result.append(" Location: "    + this.location.valueOf()    + NEW_LINE);
        result.append(" Description: " + this.description.valueOf() + NEW_LINE);
        result.append(" Category: "    + this.category.valueOf()    + NEW_LINE);
        result.append(" StartTime: "   + this.startTime.valueOf()   + NEW_LINE);
        result.append(" EndTime: "     + this.endTime.valueOf()     + NEW_LINE);
        result.append("}");

        return result.toString();
    }

    public final Title getTitle() {
        return this.title;
    }

    public final Location getLocation() {
        return this.location;
    }

    public final Description getDescription() {
        return this.description;
    }

    public final Category getCategory() {
        return this.category;
    }

    public final StartTime getStartTime() {
        return this.startTime;
    }

    public final EndTime getEndTime() {
        return this.endTime;
    }
}

package yamlcal.type;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import yamlcal.type.Title;
import yamlcal.type.Location;
import yamlcal.type.Description;
import yamlcal.type.Category;
import yamlcal.type.StartTime;
import yamlcal.type.EndTime;


public class Event {
    @JsonUnwrapped
    public Title title;
    @JsonUnwrapped
    public final Location location;
    @JsonUnwrapped
    public final Description description;
    @JsonUnwrapped
    public final Category category;
    @JsonUnwrapped
    public final StartTime startTime;
    @JsonUnwrapped
    public final EndTime endTime;

    public Event(Title title, Location location, Description description, Category category, StartTime startTime, EndTime endTime) {
        this.title       = new Title(title.valueOf());
        this.location    = new Location(location.valueOf());
        this.description = new Description(description.valueOf());
        this.category    = new Category(category.valueOf());
        this.startTime   = new StartTime(startTime == null ? null : startTime.valueOf());
        this.endTime     = new EndTime(endTime == null ? null : endTime.valueOf());
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

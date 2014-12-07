package yamlcal.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class StartTime {
    private final Date date;

    @JsonCreator
    public StartTime(@JsonProperty("startTime") Date date) {
        this.date = date;
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        if (this.date != null) {
            result.append(" Date: " + this.date.toString()   + NEW_LINE);
        } else {
            result.append(" Date: null" + NEW_LINE);
        }

        result.append("}");

        return result.toString();
    }

    public final Date valueOf() {
        return this.date;
    }

    @JsonProperty("startTime")
    public final Long getTime() {
        if (this.date == null) {
            return null;
        } else {
            return this.date.getTime();
        }
    }
}

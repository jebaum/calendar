package yamlcal.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class StartTime {
    private final Date date;
    @JsonProperty("startTime")
    private final long timeStamp;

    @JsonCreator
    public StartTime(@JsonProperty("startTime") long timeStamp) {
        this.date      = new Date(timeStamp);
        this.timeStamp = timeStamp;
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" Date: " + this.date.toString()   + NEW_LINE);
        result.append(" Timestamp (ms): " + this.timeStamp + NEW_LINE);
        result.append("}");

        return result.toString();
    }

    public final Date valueOf() {
        return this.date;
    }

    @JsonProperty("startTime")
    public final long getMilliseconds() {
        return this.timeStamp;
    }
}

package yamlcal.type;

import java.util.Date;

public class StartTime {
    private final Date startTime;
    private final long milliseconds;

    public StartTime(long startTime) {
        this.startTime    = new Date(startTime);
        this.milliseconds = startTime;
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" StartTime: " + this.startTime.toString()   + NEW_LINE);
        result.append("}");

        return result.toString();
    }

    public final Date valueOf() {
        return this.startTime;
    }

    public final long getMilliseconds() {
        return this.milliseconds;
    }
}

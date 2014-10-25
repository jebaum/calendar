package yamlcal.type;

import java.util.Date;

public class EndTime {
    private final Date endTime;
    private final long milliseconds;

    public EndTime(long endTime) {
        this.endTime      = new Date(endTime);
        this.milliseconds = endTime;
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" EndTime: " + this.endTime.toString() + NEW_LINE);
        result.append("}");

        return result.toString();
    }

    public final Date valueOf() {
        return this.endTime;
    }

    public final long getMilliseconds() {
        return this.milliseconds;
    }
}

package yamlcal.type;

import java.util.Date;

public class DueDate {
    private final Date dueDate;
    private final long milliseconds;

    public DueDate(long dueDate) {
        this.dueDate      = new Date(dueDate);
        this.milliseconds = dueDate;
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" DueDate: " + this.dueDate.toString() + NEW_LINE);
        result.append("}");

        return result.toString();
    }

    public final Date valueOf() {
        return this.dueDate;
    }

    public final long getMilliseconds() {
        return this.milliseconds;
    }
}

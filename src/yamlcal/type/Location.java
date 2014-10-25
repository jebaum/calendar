package yamlcal.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
    private String location;

    public Location(String location) {
        this.location = location;
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" Location: " + this.location + NEW_LINE);
        result.append("}");

        return result.toString();
    }

    @JsonProperty("location")
    public final String valueOf() {
        return this.location;
    }
}

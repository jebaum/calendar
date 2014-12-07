package yamlcal.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Description {
    private String description;

    public Description(String description) {
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" Description: " + this.description + NEW_LINE);
        result.append("}");

        return result.toString();
    }

    @JsonProperty("description")
    public final String valueOf() {
        return this.description;
    }
}

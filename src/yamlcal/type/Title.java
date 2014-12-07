package yamlcal.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Title {
    private final String title;

    public Title(String title) {
        if (title == null) {
            this.title = "";
        } else {
            this.title = title;
        }
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" Title: " + this.title + NEW_LINE);
        result.append("}");

        return result.toString();
    }

    @JsonProperty("title")
    public final String valueOf() {
        return this.title;
    }
}

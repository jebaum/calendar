package yamlcal.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Category {
    private String category;

    public Category(String category) {
        if (category == null) {
            this.category = "";
        } else {
            this.category = category;
        }
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" Category: " + this.category + NEW_LINE);
        result.append("}");

        return result.toString();
    }

    @JsonProperty("category")
    public final String valueOf() {
        return this.category;
    }
}

package yamlcal.type;

public class Category {
    private String category;

    public Category(String category) {
        this.category = category;
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" Category: " + this.category + NEW_LINE);
        result.append("}");

        return result.toString();
    }

    public final String valueOf() {
        return this.category;
    }
}

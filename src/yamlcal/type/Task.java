package yamlcal.type;

import java.util.Date;

import yamlcal.type.Title;
import yamlcal.type.Description;
import yamlcal.type.DueDate;
import yamlcal.type.Category;


public class Task {
    private final Title title;
    private final Description description;
    private final DueDate dueDate;
    private final Category category;

    public Task(Title title, Description description, DueDate dueDate, Category category) {
        this.title        = new Title(title.valueOf());
        this.description  = new Description(description.valueOf());
        this.dueDate      = new DueDate(dueDate.getMilliseconds());
        this.category     = new Category(category.valueOf());
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {"       + NEW_LINE);
        result.append(" Title: "       + this.title.valueOf()       + NEW_LINE);
        result.append(" Description: " + this.description.valueOf() + NEW_LINE);
        result.append(" DueDate: "     + this.dueDate.valueOf()     + NEW_LINE);
        result.append(" Category: "    + this.category.valueOf()    + NEW_LINE);
        result.append("}");

        return result.toString();
    }

    public final Title getTitle() {
        return this.title;
    }

    public final Description getDescription() {
        return this.description;
    }

    public final DueDate getdueDate() {
        return this.dueDate;
    }

    public final Category getCategory() {
        return this.category;
    }
}

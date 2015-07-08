package javang;

import java.util.ArrayList;
import java.util.List;

public class JavaNGMenu extends JavaNGMenuItem {
    private List<JavaNGMenuItem> mItems;

    public JavaNGMenu(String text) {
        super(text);
        mItems = new ArrayList<>();
    }

    public JavaNGMenu(String text, List<JavaNGMenuItem> items) {
        super(text);
        mItems = items;
    }

    public JavaNGMenu(String text, JavaNGMenuItem... items) {
        this(text);
        for (JavaNGMenuItem item : items) {
            addItem(item);
        }
    }

    public List<JavaNGMenuItem> getItems() {
        return mItems;
    }

    public void setItems(List<JavaNGMenuItem> items) {
        mItems = items;
    }

    public void addItem(JavaNGMenuItem item) {
        mItems.add(item);
    }
}

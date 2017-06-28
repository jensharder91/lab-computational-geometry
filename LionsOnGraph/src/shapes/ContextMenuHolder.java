package shapes;


import javafx.scene.control.ContextMenu;

public class ContextMenuHolder {
    private static ContextMenu singleton;


    private ContextMenuHolder() {
        // disable constructor
    }

    public static final ContextMenu getFreshContextMenu() {
        if (singleton != null) {
            singleton.hide();
        }

        singleton = new ContextMenu();
        return singleton;
    }
}

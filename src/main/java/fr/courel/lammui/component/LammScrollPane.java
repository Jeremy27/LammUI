package fr.courel.lammui.component;

import javax.swing.*;
import java.awt.*;

/**
 * ScrollPane avec scrollbars fines {@link LammScrollBarUI}.
 */
public class LammScrollPane extends JScrollPane {

    public LammScrollPane(Component view) {
        super(view);
        init();
    }

    public LammScrollPane() {
        super();
        init();
    }

    private void init() {
        setBorder(null);
        setOpaque(false);
        getViewport().setOpaque(false);
        getVerticalScrollBar().setUnitIncrement(16);
        getHorizontalScrollBar().setUnitIncrement(16);
        getVerticalScrollBar().setOpaque(false);
        getHorizontalScrollBar().setOpaque(false);
        getVerticalScrollBar().setUI(new LammScrollBarUI());
        getHorizontalScrollBar().setUI(new LammScrollBarUI());
    }
}

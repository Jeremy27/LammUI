package fr.courel.lammui.fx.component;

import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class LammTreeFx<T> extends TreeView<T> {

    public LammTreeFx() {
        super();
        init();
    }

    public LammTreeFx(TreeItem<T> root) {
        super(root);
        init();
    }

    private void init() {
        getStyleClass().add("lamm-tree");
        setCellFactory(tv -> new LammTreeCell<>());
    }

    private static class LammTreeCell<T> extends TreeCell<T> {

        private TreeItem<T> trackedItem;
        private final ChangeListener<Boolean> expandedListener = (obs, ov, nv) -> refreshIcon();

        LammTreeCell() {
            treeItemProperty().addListener((obs, oldItem, newItem) -> {
                if (trackedItem != null) {
                    trackedItem.expandedProperty().removeListener(expandedListener);
                }
                trackedItem = newItem;
                if (trackedItem != null) {
                    trackedItem.expandedProperty().addListener(expandedListener);
                }
                refreshIcon();
            });
        }

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.toString());
                refreshIcon();
            }
        }

        private void refreshIcon() {
            if (isEmpty() || getItem() == null) {
                setGraphic(null);
                return;
            }
            var ti = getTreeItem();
            Node icon;
            if (ti == null || ti.isLeaf()) {
                icon = LammTreeIcons.file();
            } else if (ti.isExpanded()) {
                icon = LammTreeIcons.folderOpen();
            } else {
                icon = LammTreeIcons.folder();
            }
            setGraphic(icon);
        }
    }
}

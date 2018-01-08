package ui.managerui.checkui;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import ui.managerui.common.treeTableRelated.ChooseColumn;
import ui.managerui.common.treeTableRelated.MyTreeTableBorderPane;
import ui.managerui.common.treeTableRelated.SearchableStringColumn;
import ui.util.ButtonCell;
import ui.util.NodeAnimation;
import ui.util.NodeHolder;
import util.ReceiptState;
import vo.promotionVO.PromotionVO;
import vo.receiptVO.ReceiptVO;

import java.util.List;
import java.util.Set;

public class CheckTable extends MyTreeTableBorderPane<ReceiptVO> {
    public CheckTable(Set<ReceiptVO> chosenItems, StringProperty keywordProperty, List<ReceiptVO> keywordFilterList) {
        JFXTreeTableColumn<ReceiptVO, Boolean> choose = new ChooseColumn<>(chosenItems);
        JFXTreeTableColumn<ReceiptVO, String> idColumn = new SearchableStringColumn<>("编号", 200, keywordProperty, keywordFilterList, ReceiptVO::getId);
        JFXTreeTableColumn<ReceiptVO, String> lastModifiedTimeColumn = new SearchableStringColumn<>("提交时间", 100, keywordProperty, keywordFilterList, p -> p.getLastModifiedTime().toLocalDate().toString());

        JFXTreeTableColumn<ReceiptVO, String> stateColumn = new JFXTreeTableColumn<>("状态");
        stateColumn.setPrefWidth(100);
        stateColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getReceiptState().name()));
        stateColumn.setCellFactory(param -> new ButtonCell<ReceiptVO>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setButtonStyle(ReceiptState.color.get(item));
                }
            }
        });

        myTreeTable.setRowFactory(tableView -> {
            JFXTreeTableRow<ReceiptVO> row = new JFXTreeTableRow<>();
            row.setPrefHeight(55);
            row.setStyle("-fx-border-color: rgb(233,237,239); -fx-border-width: 0.3;");
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    row.getTreeItem().getValue().getDetailPane();
                }
            });
            row.selectedProperty().addListener(e -> {
                if (row.isSelected()) {
                    row.toFront();
                } else {
                    row.setEffect(null);
                }
            });

            return row;
        });

        myTreeTable.getColumns().addAll(choose, idColumn, lastModifiedTimeColumn, stateColumn);
    }
}

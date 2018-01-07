package ui.managerui.promotionui.promotionDetailPane;

import blService.promotionblService.PromotionblService;
import businesslogic.promotionbl.PromotionFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRippler;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import exceptions.ItemNotFoundException;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import ui.managerui.common.MyBoardController;
import ui.managerui.common.MyOneButtonDialog;
import ui.managerui.common.MyTwoButtonDialog;
import ui.managerui.common.Save;
import ui.managerui.promotionui.GoodsTreeTable;
import ui.util.BoardController;
import ui.util.GetTask;
import ui.util.Refreshable;
import util.PromotionState;
import vo.promotionVO.PromotionGoodsItemVO;
import vo.promotionVO.PromotionVO;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public abstract class PromotionDetailPane<T extends PromotionVO> extends Refreshable {
    @FXML
    protected GoodsTreeTable goodsTreeTable;
    @FXML
    protected Label idLabel;
    @FXML
    protected JFXButton modify, reset, save, saveAsDraft;
    @FXML
    protected MaterialDesignIconView pen;
    @FXML
    protected JFXDatePicker beginTimePicker, endTimePicker;
    @FXML
    protected TextArea commentArea; // TODO 设字数限制
    @FXML
    protected JFXRippler add;

    protected T promotionVO;
    protected PromotionblService<T> promotionblService;
    protected BooleanProperty modifyState = new SimpleBooleanProperty(true);

    protected abstract String getURL();

    // TODO 现在这样就是必须第一次new的时候call完这个就call refresh。虽然感觉这样不好唉
    protected PromotionDetailPane(T promotionVO) {
        initialize();
        this.promotionVO = promotionVO;

//        modifyState.set(false); // 这句不知道什么就会报错

        saveAsDraft.setVisible(false);
        reset.setLayoutX(saveAsDraft.getLayoutX());
    }

    protected abstract Class<? extends PromotionblService<T>> getServiceClass();

    protected PromotionDetailPane() {
        initialize();
    }

    protected void initialize() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getURL()));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        modifyState.addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                modify.setBackground(new Background(new BackgroundFill(Color.valueOf("#03A9F4"), modify.getBackground().getFills().get(0).getRadii(), null)));
                pen.setFill(Paint.valueOf("#FFFFFF"));
            } else {
                modify.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, modify.getBackground().getFills().get(0).getRadii(), null)));
                pen.setFill(Paint.valueOf("#000000"));
            }
        }));
        beginTimePicker.disableProperty().bind(modifyState.not());
        endTimePicker.disableProperty().bind(modifyState.not());
        commentArea.disableProperty().bind(modifyState.not());
        add.visibleProperty().bind(modifyState);
        save.visibleProperty().bind(modifyState);
        reset.visibleProperty().bind(modifyState);
    }

    protected boolean validate() {
        return true;
    }

    protected void updatePromotionVO() {
        promotionVO.setBeginTime(LocalDateTime.of(beginTimePicker.getValue(), LocalTime.MIN));
        promotionVO.setEndTime(LocalDateTime.of(endTimePicker.getValue(), LocalTime.MIN));
        promotionVO.setComment(commentArea.getText());
    }

    @FXML
    private void save() {
        if (validate()) {
            promotionVO.setPromotionState(PromotionState.SAVED);
            saveTask();
        }
    }

    @FXML
    private void saveAsDraft() {
        if (validate()) {
            saveTask();
        }
    }

    protected void saveTask() {
        MyBoardController boardController = MyBoardController.getMyBoardController();
        boardController.Loading();

        updatePromotionVO();

        StringProperty prompt = new SimpleStringProperty(); // 为了避免lambda的final限制。
        new Thread(new GetTask(() -> {
            new MyTwoButtonDialog("保存成功", boardController::goBack).show(); // 这个和delete不一样
        }, new MyTwoButtonDialog(prompt.get(), boardController::goBack), woid -> {
            try {
                promotionblService.update(promotionVO);
                return true;
            } catch (ItemNotFoundException e) {
                e.printStackTrace();
                prompt.set("策略不存在，是否返回单据列表");
                return false;
            } catch (RemoteException e) {
                e.printStackTrace();
                prompt.set("连接错误");
                return false;
            }
        })).start();
    }

    @FXML
    protected void addGift() { // TODO
        goodsTreeTable.add(new PromotionGoodsItemVO("id5113", "商品A", 23, new SimpleIntegerProperty(3)));
    }

    @FXML
    private void modify() {
        modifyState.set(!modifyState.get());
    }

    @FXML
    private void delete() {
        new MyTwoButtonDialog("请确认删除", this::deleteTask).show();
    }

    private void deleteTask() {
        MyBoardController boardController = MyBoardController.getMyBoardController();
        boardController.Loading();

        StringProperty prompt = new SimpleStringProperty(); // 为了避免lambda的final限制。
        new Thread(new GetTask(() -> {
            new MyOneButtonDialog("删除成功", boardController::goBack).show();
        }, new MyTwoButtonDialog(prompt.get(), boardController::goBack), woid -> {
            try {
                promotionblService.delete(promotionVO);
                return true;
            } catch (ItemNotFoundException e) {
                e.printStackTrace();
                prompt.set("策略不存在，是否返回单据列表");
                return false;
            } catch (RemoteException e) {
                e.printStackTrace();
                prompt.set("连接错误");
                return false;
            }
        })).start();
    }

    @FXML
    protected void reset() {
        idLabel.setText(promotionVO.getId());
        if (promotionVO.getBeginTime() != null) {
            beginTimePicker.setValue(promotionVO.getBeginTime().toLocalDate());
        }
        if (promotionVO.getEndTime() != null) {
            endTimePicker.setValue(promotionVO.getEndTime().toLocalDate());
        }
        commentArea.setText(promotionVO.getComment());
    }

    @Override
    public void refresh(boolean toSwitch) {
        MyBoardController myBoardController = MyBoardController.getMyBoardController();
        myBoardController.Loading();

        MyTwoButtonDialog dialog = new MyTwoButtonDialog("连接错误", () -> refresh(false), myBoardController::Ret);

        GetTask task = new GetTask(() -> {
            myBoardController.switchTo(this); // 感觉这个分两次来明显就是为了可以有等待…
        }, dialog, woid -> {
            try {
                if (promotionblService == null) { // 这说明肯定是第一次
                    promotionblService = PromotionFactory.getService(getServiceClass());
                    if (promotionVO == null) {
                        promotionVO = promotionblService.getNew();
                    }
                    reset();
                }
                return true; // TODO 如果不是第一次refresh目前就是什么都不做，但是好像是希望去数据库刷新
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
        new Thread(task).start();
    }
}

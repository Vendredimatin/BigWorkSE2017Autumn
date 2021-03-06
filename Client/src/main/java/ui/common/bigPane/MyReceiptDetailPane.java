package ui.common.bigPane;

import blService.checkblService.CheckInfo;
import blService.genericblService.ReceiptblService;
import businesslogic.blServiceFactory.MyblServiceFactory;
import com.jfoenix.controls.JFXButton;
import exceptions.ItemNotFoundException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import ui.common.BoardController;
import ui.common.dialog.MyOneButtonDialog;
import ui.common.dialog.MyTwoButtonDialog;
import ui.util.GetTask;
import ui.util.RefreshablePane;
import ui.util.UserInfomation;
import util.ReceiptState;
import util.UserCategory;
import vo.receiptVO.ReceiptVO;

import java.io.IOException;
import java.rmi.RemoteException;

public abstract class MyReceiptDetailPane<TV extends ReceiptVO> extends RefreshablePane {
    @FXML
    protected JFXButton modify, reset, save, saveAsDraft, delete, approve, reject, redCredit, redCreditCopy;
    @FXML
    protected Label idLabel;




    protected ReceiptblService<TV> receiptblService;
    protected CheckInfo<TV> checkInfo; // TODO 最好提子类
    protected TV receiptVO;
    protected BooleanProperty modifyState = new SimpleBooleanProperty(true);

    /**
     * Constructors related
     */
    public MyReceiptDetailPane() {
        initiate();
    }

    public MyReceiptDetailPane(TV receiptVO) {
        this.receiptVO = receiptVO;
        initiate();
    }

    protected void initiate() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getURL()));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        approve.setVisible(false);
        reject.setVisible(false);
        redCredit.setVisible(false);
        redCreditCopy.setVisible(false);

        if (receiptVO != null) {
            // 默认是草稿的状态，如果执行到下面说明不是。
            if (receiptVO.getReceiptState() != ReceiptState.DRAFT) {
                saveAsDraft.setVisible(false);
                reset.setLayoutX(saveAsDraft.getLayoutX());
            }

            // 如果不是总经理，提交后的单据只能看。如果是审批通过的或者就只能看
            if (receiptVO.getReceiptState() == ReceiptState.APPROVED
                    || UserInfomation.usertype != UserCategory.GeneralManager && receiptVO.getReceiptState() == ReceiptState.PENDING) {
                modify.setVisible(false); // 这句还是个问题
                reset.setVisible(false);
                save.setVisible(false);
                saveAsDraft.setVisible(false);
                delete.setVisible(false);
            }
            if (receiptVO.getReceiptState() == ReceiptState.APPROVED //&& SomeParameter.isRedCreditable
                    && UserInfomation.usertype == UserCategory.Accountant) {
                redCredit.setVisible(true);
                redCreditCopy.setVisible(true);
                save.setVisible(false);
                saveAsDraft.setVisible(false);
                reset.setVisible(false);
            }

            if (UserInfomation.usertype == UserCategory.GeneralManager && receiptVO.getReceiptState() == ReceiptState.PENDING) { // 如果是总经理肯定只能是pending的
                reset.setVisible(false);
                save.setVisible(false);
                saveAsDraft.setVisible(false);
                delete.setVisible(false);

                // modify, reset, save, saveAsDraft, delete;

                approve.setVisible(true);
                reject.setVisible(true);
            }
        }
    }

    /**
     * abstract methods
     */
    protected abstract String getURL();

    protected abstract Class<? extends ReceiptblService<TV>> getServiceClass();

    /**
     * to be overriden
     */

    protected boolean validate() {
        return true;
    }

    protected void updateReceiptVO() {
    }

    protected void setRedCredit(TV redCreditVO) {
        redCreditVO.setReceiptState(receiptVO.getReceiptState());
        redCreditVO.setOperatorId(receiptVO.getOperatorId());
    }

    @FXML
    protected void reset() {
        idLabel.setText(receiptVO.getId());
    }

    /**
     * FXML
     */

    @FXML
    protected void save() { // 这里的save相当于提交，但是不想改名了…
        if (validate()) {
            receiptVO.setReceiptState(ReceiptState.PENDING);
            saveTask();
        } else {
            new MyOneButtonDialog("不合法数据").show();
        }
    }

    @FXML
    private void saveAsDraft() {
        if (validate()) {
            saveTask();
        } else {
            new MyOneButtonDialog("不合法数据").show();
        }
    }

    protected void saveTask() {
        BoardController boardController = BoardController.getBoardController();
        boardController.Loading();

        updateReceiptVO();

        StringProperty prompt = new SimpleStringProperty();
        new Thread(new GetTask(() -> {
            new MyTwoButtonDialog("保存成功", boardController::goBack).show();
        }, new MyTwoButtonDialog(prompt.get(), boardController::goBack), woid -> {
            try {
                receiptblService.update(receiptVO);
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
    private void delete() {
        new MyTwoButtonDialog("请确认删除", () -> {
            BoardController boardController = BoardController.getBoardController();
            boardController.Loading();

            StringProperty prompt = new SimpleStringProperty(); // 为了避免lambda的final限制。
            new Thread(new GetTask(() -> {
                new MyOneButtonDialog("删除成功", boardController::goBack).show();
            }, new MyTwoButtonDialog(prompt.get(), boardController::goBack), woid -> {
                try {
                    receiptblService.delete(receiptVO);
                    return true;
                } catch (ItemNotFoundException e) {
                    e.printStackTrace();
                    prompt.set("单据不存在，是否返回单据列表");
                    return false;
                } catch (RemoteException e) {
                    e.printStackTrace();
                    prompt.set("连接错误");
                    return false;
                }
            })).start();
        }).show();
    }

    @FXML
    private void approve() { // approve的时候顺便update单据
        if (validate()) {
            System.out.println("MyReceiptDetailPane approve called");
            receiptVO.setReceiptState(ReceiptState.APPROVED); // TODO 这个其实不应该在这里set的

            BoardController myBoardController = BoardController.getBoardController();
            myBoardController.Loading();

            updateReceiptVO();

            MyTwoButtonDialog dialog = new MyTwoButtonDialog("连接错误", () -> refresh(false), myBoardController::Ret);

            new Thread(new GetTask(() -> {
                new MyOneButtonDialog("审批通过", myBoardController::goBack).show();
            }, dialog, woid -> {
                try {
                    checkInfo.approve(receiptVO);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            })).start();
        } else {
            new MyOneButtonDialog("不合法数据").show();
        }
    }

    @FXML
    private void reject() { // reject就不需要update数据
        if (validate()) {
            receiptVO.setReceiptState(ReceiptState.REJECTED);

            BoardController myBoardController = BoardController.getBoardController();
            myBoardController.Loading();

            MyTwoButtonDialog dialog = new MyTwoButtonDialog("连接错误", () -> refresh(false), myBoardController::Ret);

            new Thread(new GetTask(() -> {
                new MyOneButtonDialog("驳回单据", myBoardController::goBack).show();
            }, dialog, woid -> {
                try {
                    checkInfo.reject(receiptVO);
                    System.out.println(" reject success");
                    return true;
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return false;
                }
            })).start();
        } else {
            new MyOneButtonDialog("不合法数据").show();
        }
    }

    @FXML
    private void modify() {
        modifyState.set(!modifyState.get());
    }

    @FXML
    private void clickRedCredit() {
        BoardController myBoardController = BoardController.getBoardController();
        myBoardController.Loading();

        updateReceiptVO();

        MyTwoButtonDialog dialog = new MyTwoButtonDialog("连接错误", () -> refresh(false), myBoardController::Ret);

        new Thread(new GetTask(() -> {
            new MyOneButtonDialog("红冲成功", myBoardController::goBack).show();
        }, dialog, woid -> {
            try {
                TV redCreditVO = receiptblService.getNew();
                setRedCredit(redCreditVO);
                receiptblService.update(redCreditVO);
                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        })).start();

    }

    @FXML
    protected void clickRedCreditCopy() {
        BoardController myBoardController = BoardController.getBoardController();
        myBoardController.Loading();

        updateReceiptVO();

        MyTwoButtonDialog dialog = new MyTwoButtonDialog("连接错误", () -> refresh(false), myBoardController::Ret);

        new Thread(new GetTask(() -> {
            new MyOneButtonDialog("红冲成功", myBoardController::goBack).show();
        }, dialog, woid -> {
            try {
                TV redCreditVO = receiptblService.getNew();
                setRedCredit(redCreditVO);
                receiptblService.update(redCreditVO);

                receiptVO = redCreditVO; //
                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        })).start();
    }

    /**
     * refresh
     */
    @Override
    public void refresh(boolean toSwitch) {
        BoardController myBoardController = BoardController.getBoardController();
        myBoardController.Loading();

        MyTwoButtonDialog dialog = new MyTwoButtonDialog("连接错误", () -> refresh(false), myBoardController::Ret);

        GetTask task = new GetTask(() -> {
            myBoardController.switchTo(this); // 感觉这个分两次来明显就是为了可以有等待…
            reset();
        }, dialog, woid -> {
            try {
                if (receiptblService == null) { // 这说明肯定是第一次
                    receiptblService = MyblServiceFactory.getService(getServiceClass());
                    if (receiptVO == null) {
                        receiptVO = receiptblService.getNew();
                        receiptVO.setOperatorId(UserInfomation.userid);
                    }
                    if (checkInfo == null) {
                        checkInfo = receiptVO.getService();
                    }
                } else {
                    receiptVO = receiptblService.selectByMold(receiptVO);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
        new Thread(task).start();
    }

}

package ui.accountantui;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import ui.util.PaneFactory;
import vo.ListGoodsItemVO;
import vo.billReceiptVO.TransferItemVO;

import static ui.util.ValidatorDecorator.NumberValid;

public class TransferItemPane extends AnchorPane{

    TransferItemVO transferItemVO;

    StackPane mainpane;

    ObservableList<TransferItemVO> observableList;

    @FXML
    JFXTextField accountID;

    @FXML
    JFXTextField price;

    @FXML
    JFXTextField comment;

    JFXDialog jfxDialog;

    public TransferItemPane(TransferItemVO transferItemVO, ObservableList<TransferItemVO> observableList) {
        super();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/accountantui/TransferItemPane.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.transferItemVO = transferItemVO;
        accountID.setText(""+transferItemVO.getAccountID());
        price.setText(""+transferItemVO.getSum());
        comment.setText(transferItemVO.getComment());


        accountID.setOnKeyPressed(t->{
            if(t.getCode()==KeyCode.ENTER){
                transferItemVO.setAccountID(Integer.parseInt(accountID.getText()));
                accountID.setText(""+transferItemVO.getAccountID());
            }
        });

        price.setOnKeyPressed(t->{
            if(t.getCode()==KeyCode.ENTER){
                transferItemVO.setSum(Double.parseDouble(price.getText()));
                price.setText(""+transferItemVO.getSum());
            }
        });

        comment.setOnKeyPressed(t->{
            if(t.getCode()==KeyCode.ENTER){
                transferItemVO.setComment(comment.getText());
                comment.setText(transferItemVO.getComment());
            }
        });

        this.mainpane = PaneFactory.getMainPane();
        this.observableList=observableList;



    }

    public void setDialog(JFXDialog dialog) {
        this.jfxDialog = dialog;
    }




    @FXML
    public void save() {
        if(price.validate()){
            //listGoodsItemVO.setGoodsNum(Integer.parseInt(num.getText()));
            //sum.setText(""+listGoodsItemVO.getSum());
            //listGoodsItemVO.setComment(comment.getText());
            transferItemVO.setAccountID(Integer.parseInt(accountID.getText()));
            transferItemVO.setSum(Double.parseDouble(price.getText()));
            transferItemVO.setComment(comment.getText());
            //accountID.setText(""+transferItemVO.getAccountID());
            //price.setText(""+transferItemVO.getSum());
            //comment.setText(transferItemVO.getComment());
            jfxDialog.close();

        }
    }

    @FXML
    public void delete() {
        observableList.remove(this.transferItemVO);
        jfxDialog.close();
    }

}

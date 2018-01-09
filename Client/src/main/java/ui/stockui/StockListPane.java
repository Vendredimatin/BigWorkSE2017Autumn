package ui.stockui;

import blService.blServiceFactory.ServiceFactory_Stub;
import blService.stockblService.StockblService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.BorderPane;
import ui.util.*;
import vo.receiptVO.StockReceiptListVO;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author: Lin Yuchao
 * @Description: listpane  注意所有见面跳转职责都分给自己，要跳到谁，由后者自己改，因为刷新也是这样
 * @ModifyBy: Lin Yuchao
**/

public class StockListPane extends ReceiptListPane<StockReceiptListVO> {


    /**
    * 这里的变量要有searchcondition和blservice还有关键字（这个也可以提到父类）
    **/

    StockblService stockblService;

    SimpleBooleanProperty isPur = new SimpleBooleanProperty();

    SimpleStringProperty match = new SimpleStringProperty("");

 //   static StockSearchVO stockSearchVO = new StockSearchVO();




    /**
     * @Author: Lin Yuchao
     * @Attention
     * @Param: isPur  是否是进货单
     * @Return:
    **/
    public StockListPane(boolean isPur) throws Exception {
        super("/stockui/stocklistpane.fxml");
        this.stockblService = ServiceFactory_Stub.getService(StockblService.class.getName());
        this.isPur.set(isPur);

        receiptTreeTable = new StockTreeTable();
        receiptTreeTable.setPrefSize(600, 435);
        receiptTreeTable.keywordProperty().bind(match);
        borderpane.setTop(new BorderPane(receiptTreeTable));
/*

        for (ReceiptState receiptState : ReceiptState.values()) {
            stockSearchVO.getReceiptStates().add(receiptState);
        }
        StockFilterPane slp = new StockFilterPane(filterPopOver, stockSearchVO);
        filterPopOver.setDetachable(false);
        filterPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        filterPopOver.setContentNode(slp);
        filter.setOnMouseClicked(e -> filterPopOver.show(filter));*/
    }


    /**
    * 将职责分配到treetable里面去删除
    **/
    @Override
    public void deleteList() {
        DoubleButtonDialog doubleButtonDialog = new DoubleButtonDialog(mainpane,"Delete","sabi","Yes","No");
        doubleButtonDialog.setButtonOne(()->{receiptTreeTable.delete(pagination); });
        doubleButtonDialog.setButtonTwo(()->{});
        doubleButtonDialog.show();
    }



    /**
    * 关键字搜索
    **/
    @Override
    public void search() {
        if (!searchField.getText().equals("")) {
            match.setValue(searchField.getText().toLowerCase());
            Set<StockReceiptListVO> hashSet;
            hashSet = set.stream().filter(
                    s -> s.getReceiptState().name().contains(match.get()) ||
                            s.getId().contains(match.get())||
                            s.getMemberName().contains(match.get())||
                            s.getStockName().contains(match.get())
            ).collect(Collectors.toSet());
            receiptTreeTable.setReceipts(hashSet);
            pagination.setPageCount(receiptTreeTable.getObservableList().size() / receiptTreeTable.getRowsPerPage() + 1);
            receiptTreeTable.createPage(0);
            borderpane.setBottom(pagination);
            switchPane(false);
        }
    }

    /**
    * 新建单据
    **/
    @Override
    public void add() {
        StockReceiptPane stockReceiptPane = new StockReceiptPane(isPur.get());
    }

    @Override
    public void refresh(boolean toSwitch) {
        boardController.Loading();
        try {
            /**
            * 两个button的dialog，可以传入runnable实现你想要的东西
            **/
            DoubleButtonDialog buttonDialog =
                    new DoubleButtonDialog(mainpane, "Wrong", "sabi", "Last", "Ret");
            buttonDialog.setButtonTwo(() -> boardController.Ret());
            buttonDialog.setButtonTwo(() -> refresh(false));
            /**
            * 这个predicate主要是把方法传到task里面去，我想不出有什么能够传进去又能return值的函数式就用这个了
            **/
            Predicate<Integer> p = (s) -> {
      /*          if ((set = stockblService.search(stockSearchVO, isPur.get())) != null) {
                    System.out.println(set.size());
                    return true;
                }*/
                return false;
            };
            /**
            *  详见gettask
            **/
            GetTask getTask =
                    new GetTask(() -> {
                        receiptTreeTable.setReceipts(set);
                        pagination.setPageCount(receiptTreeTable.getObservableList().size() / receiptTreeTable.getRowsPerPage() + 1);
                        receiptTreeTable.createPage(0);
                        borderpane.setBottom(pagination);
                        switchPane(toSwitch);
                    }, buttonDialog, p);

            new Thread(getTask).start();
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

}


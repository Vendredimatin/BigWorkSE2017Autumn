package ui.inventoryui.goodsclassificationui;

import blService.goodsClassificationblService.GoodsClassificationblService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import exception.ExistException;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.CheckTreeView;
import ui.util.BoardController;
import ui.util.PaneFactory;
import vo.inventoryVO.GoodsClassificationVO;

import java.rmi.RemoteException;
import java.util.*;

public class GoodsClassificationTreeView extends CheckTreeView<String>{
    private BoardController boardController;
    private StackPane mainpane;
    private BorderPane borderPane;
    private GoodsClassificationblService goodsClassificationblService;
    private List<GoodsClassificationVO> list;
    private CheckBoxTreeItem<String> root = new CheckBoxTreeItem<>("root");
    private CheckBoxTreeItem<String> chosenItem;
    private JFXButton addGood = new JFXButton("增加商品");
    private JFXButton add = new JFXButton("增加");
    private JFXButton delete = new JFXButton("删除");
    private JFXButton modify = new JFXButton("修改");

    public GoodsClassificationTreeView() {
        super();
        root.setExpanded(true);
        root.setSelected(true);

    }

    public Node getPane(){
        root = buildTree();
        root.setExpanded(true);
        this.setRoot(root);
        this.setEditable(true);
        /*this.setCellFactory((TreeView<String> p) ->
                new TextFieldTreeCellImpl());*/

        borderPane = new BorderPane();

        borderPane.setCenter(this);

        String selected1;
        this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.getCheckModel().getCheckedItems().addListener(new ListChangeListener<TreeItem<String>>() {
            @Override public void onChanged(ListChangeListener.Change<? extends TreeItem<String>> change) {
                System.out.println("hehehe2");


                delete.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        deleteItem(change.getList());
                    }
                });

                add.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        addItem(change.getList());
                    }
                });

                modify.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        modifyItem(change.getList());

                    }
                });

     }
        });


        HBox buttons = new HBox();
        buttons.getChildren().addAll(add,delete,modify);

        borderPane.setRight(buttons);

        return borderPane;
    }

    public CheckBoxTreeItem<String> getTreeRoot(){
        return root;
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setMainpane(StackPane mainpane) {
        this.mainpane = mainpane;
    }

    public void setGoodsClassificationblService(GoodsClassificationblService goodsClassificationblService) {
        this.goodsClassificationblService = goodsClassificationblService;
    }

    public void setGoodsClassificationVO(List<GoodsClassificationVO> list) {
        this.list = list;
    }

    protected void deleteItem(ObservableList<? extends TreeItem<String>> list){
        CheckBoxTreeItem<String> son = (CheckBoxTreeItem<String>) list.get(0);

        CheckBoxTreeItem<String> parent =(CheckBoxTreeItem<String>)  son.getParent();

        //连同changeList中的数据也要删掉，否则会报错
        parent.getChildren().remove(son);
        list.remove(son);
    }

    protected void addItem(ObservableList<? extends TreeItem<String>> list){
        CheckBoxTreeItem<String> item = (CheckBoxTreeItem<String>) list.get(0);

        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setPrefWidth(220.0);
        jfxDialogLayout.setHeading(new Label("GoodsClassificationName"));
        JFXTextField name = new JFXTextField();
        jfxDialogLayout.setBody(name);
        JFXButton save = new JFXButton("Save");
        JFXDialog dialog = new JFXDialog(mainpane, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);

        save.setOnAction(e -> {
            String classifyName = name.getText();

            CheckBoxTreeItem<String> son = new CheckBoxTreeItem<>(classifyName);
            item.getChildren().add(son);

            try {
                System.out.println(classifyName+" "+item.getValue());
                addGoodsClassificationVO(classifyName,item.getValue());
            } catch (ExistException e1) {
                e1.printStackTrace();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

            dialog.close();
        });
        jfxDialogLayout.setActions(save);
        dialog.show();


    }

    /**
     * 传入子分类名和父分类名，更新数据库
     * @param sonName
     * @param fatherName
     * @throws ExistException
     */
    private void addGoodsClassificationVO(String sonName, String fatherName) throws ExistException, RemoteException {
        System.out.println(list.toString());
        for (GoodsClassificationVO vo:list) {
            System.out.println(vo.getName());
            if (vo.getName().equals(fatherName)) {
                List<String> childernId = vo.getChildrenId();
                childernId.add(sonName);

                String classifyId = vo.getFatherID() + "/" + Integer.toString(childernId.size());
                GoodsClassificationVO goodsClassificationVO = new GoodsClassificationVO(classifyId,sonName,vo.getFatherID());
                goodsClassificationblService.addGoodsClassification(goodsClassificationVO);
            }
        }
    }

    protected void modifyItem(ObservableList<? extends TreeItem<String>> list){
        CheckBoxTreeItem<String> item = (CheckBoxTreeItem<String>) list.get(0);

        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setPrefWidth(220.0);
        jfxDialogLayout.setHeading(new Label("GoodsClassificationName"));
        JFXTextField name = new JFXTextField();
        name.setText(item.getValue());
        name.setPrefWidth(210);
        jfxDialogLayout.setBody(name);
        JFXButton save = new JFXButton("Save");
        JFXDialog dialog = new JFXDialog(mainpane, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);

        String oldName = item.getValue();

        save.setOnAction(e -> {
            String classifyName = name.getText();
            item.setValue(classifyName);
            dialog.close();

            updateGoodsClassificationVO(classifyName,oldName);
        });
        jfxDialogLayout.setActions(save);
        dialog.show();

    }


    /**
     * 修改分类信息时，更新数据库
     * @param newName
     * @param oldName
     */
    private void updateGoodsClassificationVO(String newName, String oldName) {
        for (GoodsClassificationVO vo:list) {
            if(vo.getName().equals(oldName)){
                vo.setName(newName);
                //goodsClassificationblService.updateGoodsClassification(vo);
                break;
            }
        }

    }

    private CheckBoxTreeItem<String> buildTree(){
        Queue<GoodsClassificationVO> que = new LinkedList<>();
        que.offer(list.get(0));

        root = new CheckBoxTreeItem<>(list.get(0).getID());
        CheckBoxTreeItem<String> item = root;

        int index = 1;
        int size = list.size();
        while(!que.isEmpty()){
            GoodsClassificationVO father = que.poll();
            String fatherId = father.getID();

           if (index != 1) {
               item = getCheckItem(root, fatherId);
           }

            for (;index < size; index++){
                GoodsClassificationVO son = list.get(index);
                if (son.getFatherID() == fatherId){
                    item.getChildren().add(new CheckBoxTreeItem<>(son.getID()));
                    que.offer(son);
                }else {
                    break;
                }
            }
        }

        return root;
    }

    //递归调用获取fatherId对应的节点
    private CheckBoxTreeItem<String> getCheckItem(CheckBoxTreeItem<String> currentRoot,String fatherId){
        for(Iterator<TreeItem<String>> iterator = currentRoot.getChildren().iterator(); iterator.hasNext();){

            CheckBoxTreeItem tmp = (CheckBoxTreeItem<String>) iterator.next();

            if(tmp.getValue().equals(fatherId))
                return tmp;
            else {
                tmp = getCheckItem(tmp, fatherId);
                if(tmp != null)
                    return tmp;
            }
        }
        return null;
    }

   /* private final class TextFieldTreeCellImpl extends CheckBoxTreeCell<String> {

        private TextField textField;

        public TextFieldTreeCellImpl() {
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText((String) getItem());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnKeyReleased((KeyEvent t) -> {
                if (t.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }*/

}
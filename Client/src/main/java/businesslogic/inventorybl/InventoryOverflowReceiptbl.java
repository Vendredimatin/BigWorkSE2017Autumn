package businesslogic.inventorybl;

import blService.inventoryblService.InventoryOverflowReceiptblService;
import businesslogic.checkbl.Receiptbl;
import businesslogic.goodsbl.goodsUpdate.GoodsSalesUpdate;
import blService.goodsblService.GoodsSalesUpdateInfo;
import po.receiptPO.InventoryOverflowReceiptPO;
import util.ResultMessage;
import vo.inventoryVO.inventoryReceiptVO.InventoryOverflowReceiptVO;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class InventoryOverflowReceiptbl extends Receiptbl<InventoryOverflowReceiptVO,InventoryOverflowReceiptPO> implements InventoryOverflowReceiptblService {
    GoodsSalesUpdateInfo info;

    public InventoryOverflowReceiptbl() throws RemoteException, NotBoundException, MalformedURLException {
        super(InventoryOverflowReceiptVO.class,InventoryOverflowReceiptPO.class);
        info = new GoodsSalesUpdate();
    }




 /*   @Override
    public ResultMessage approve(InventoryReceiptVO receiptVO) throws RemoteException {
        InventoryReceiptPO receiptPO = receiptVO.toPO();
        receiptPO.setReceiptState(ReceiptState.APPROVED);
        receiptDataService.update(receiptPO);

        InventoryReceiptGoodsItemPO[] goodsList = receiptPO.getGoodsList();

        List<GoodsPO> poList = new ArrayList<>();

        for (InventoryReceiptGoodsItemPO item: goodsList) {
            GoodsPO po = new GoodsPO(item.getId(),null,null,null,item.getFactNumber(),
                    -1,-1,-1,-1,-1);
            poList.add(po);
        }

        info.goodsUpdate(poList);

        return ResultMessage.SUCCESS;
        return null;
    }*/



    @Override
    public ResultMessage approve(InventoryOverflowReceiptVO receiptVO) throws RemoteException {
        return null;
    }
}

package businesslogic.inventorybl;

import blService.inventoryblService.InventoryShowblService;
import po.InventoryDamageReceiptPO;
import po.InventoryGiftReceiptPO;
import po.InventoryOverflowReceiptPO;
import po.InventoryWarningReceiptPO;
import vo.InventoryCheckVO;
import vo.InventoryViewVO;

import java.util.List;

public class InventoryShowblController implements InventoryShowblService{
    @Override
    public InventoryViewVO InventoryView(String beginDate, String endDate) {
        return null;
    }

    @Override
    public InventoryCheckVO InventoryCheck() {
        return null;
    }

    @Override
    public List<InventoryGiftReceiptPO> showGiftReceiptOfApproved() {
        return null;
    }

    @Override
    public List<InventoryOverflowReceiptPO> showOverflowReceiptOfApproved() {
        return null;
    }

    @Override
    public List<InventoryDamageReceiptPO> showDamageReceiptOfApproved() {
        return null;
    }

    @Override
    public List<InventoryWarningReceiptPO> showWarningReceiptOfApproved() {
        return null;
    }

    @Override
    public List<InventoryGiftReceiptPO> showGiftReceiptOfPendding() {
        return null;
    }

    @Override
    public List<InventoryOverflowReceiptPO> showOverflowReceiptOfPendding() {
        return null;
    }

    @Override
    public List<InventoryDamageReceiptPO> showDamageReceiptOfPendding() {
        return null;
    }

    @Override
    public List<InventoryWarningReceiptPO> showWarningReceiptOfPendding() {
        return null;
    }

    @Override
    public List<InventoryGiftReceiptPO> showGiftReceiptOfDarft() {
        return null;
    }

    @Override
    public List<InventoryOverflowReceiptPO> showOverflowReceiptOfDarft() {
        return null;
    }

    @Override
    public List<InventoryDamageReceiptPO> showDamageReceiptOfDarft() {
        return null;
    }

    @Override
    public List<InventoryWarningReceiptPO> showWarningReceiptOfDarft() {
        return null;
    }
}
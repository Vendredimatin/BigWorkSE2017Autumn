package vo.receiptVO;

import util.ReceiptState;

import java.time.LocalDateTime;

public class SalesSellReceiptVO extends ReceiptVO {
    public SalesSellReceiptVO(String id, int operatorId, LocalDateTime createTime, LocalDateTime lastModifiedTime, ReceiptState receiptState) {
        super(id, operatorId, createTime, lastModifiedTime, receiptState);
    }
}
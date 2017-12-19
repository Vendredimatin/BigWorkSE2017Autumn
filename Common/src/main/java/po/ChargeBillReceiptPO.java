package po;

import po.receiptPO.ReceiptPO;
import util.ReceiptState;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ChargeBillReceiptPO extends ReceiptPO {

    private String clerkName;
    private int clientID;
    private TransferItemPO[] transferList;
    private double sum;


    public ChargeBillReceiptPO(int dayId, int operatorId, LocalDateTime createTime, LocalDateTime lastModifiedTime, ReceiptState receiptState,String clerkName, int retailerID, TransferItemPO[] transferList, double sum) {
        super(dayId, operatorId, createTime, lastModifiedTime, receiptState);
        this.clerkName = clerkName;
        this.clientID = clientID;
        this.transferList = transferList;
        this.sum = sum;
    }


    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public TransferItemPO[] getTransferList() {
        return transferList;
    }

    public void setTransferList(TransferItemPO[] transferList) {
        this.transferList = transferList;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
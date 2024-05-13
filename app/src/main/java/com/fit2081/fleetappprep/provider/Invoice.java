package com.fit2081.fleetappprep.provider;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName="invoices")
public class Invoice {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "Id")
    private int Id;

    @ColumnInfo(name = "invoiceId")
    private String invoiceId;

    @ColumnInfo(name = "issuerName")
    private String issuerName;

    @ColumnInfo(name = "buyerId")
    private String buyerId;

    @ColumnInfo(name = "buyerName")
    private String buyerName;

    @ColumnInfo(name = "buyerAddress")
    private String buyerAddress;

    @ColumnInfo(name="isPaid")
    private Boolean isPaid;

    @ColumnInfo(name="items")
    private ArrayList<Item> items;

    @ColumnInfo(name = "totalCost")
    private int total;

    public Invoice(String invoiceId,String issuerName,String buyerId,String buyerName,String buyerAddress, Boolean isPaid, ArrayList<Item> items, int total) {
        this.invoiceId = invoiceId;
        this.issuerName = issuerName;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.buyerAddress = buyerAddress;
        this.isPaid = isPaid;
        this.items = items;
        this.total = total;
    }

    public int getId() {
        return Id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getIssuerName() {
        return issuerName;
    }
    public String getBuyerId() {
        return buyerId;
    }
    public String getBuyerName() {
        return buyerName;
    }
    public String getBuyerAddress() {
        return buyerAddress;
    }
    public Boolean getPaid() {
        return isPaid;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
    public int getTotal() {
        return total;
    }

    public void setId(int Id) {
        this.Id = Id;
    }






}

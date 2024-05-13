package com.fit2081.fleetappprep.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

//defines database operations
@Dao
public interface InvoiceDao {
    @Query("select * from invoices")
    LiveData<List<Invoice>> getAllInvoices();

    @Query("select * from invoices where invoiceId=:name")
    List<Invoice> getInvoice(String name);

    @Insert
    void addInvoice(Invoice invoice);

    @Query("delete from invoices where id= :invoiceId")
    int deleteInvoiceById(int invoiceId);

    @Query("delete FROM invoices")
    void deleteAllInvoices();
}

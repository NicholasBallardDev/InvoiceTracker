package com.fit2081.fleetappprep.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

//allows access to multiple sources
public class InvoiceRepository {
    private InvoiceDao mInvoiceDao;
    private LiveData<List<Invoice>> mAllInvoice;

    InvoiceRepository(Application application){
        InvoiceDatabase db = InvoiceDatabase.getDatabase(application);
        mInvoiceDao = db.invoiceDao();
        mAllInvoice = mInvoiceDao.getAllInvoices();
    }

    LiveData<List<Invoice>> getAllInvoices() {
        return mAllInvoice;
    }

    void insert(Invoice invoice) {
        InvoiceDatabase.databaseWriteExecutor.execute(() -> mInvoiceDao.addInvoice(invoice));
    }

    void deleteAll(){
        InvoiceDatabase.databaseWriteExecutor.execute(()->{
            mInvoiceDao.deleteAllInvoices();
        });
    }

    int deleteInvoice(int id){
        InvoiceDatabase.databaseWriteExecutor.execute(()->{
            mInvoiceDao.deleteInvoiceById(id);
        });
        return 1;
    }
}

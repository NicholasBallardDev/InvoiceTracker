package com.fit2081.fleetappprep.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

//provides data for the UI
//retrieves the data for the fragment
public class InvoiceViewModel extends AndroidViewModel {
    private InvoiceRepository mRepository;
    private LiveData<List<Invoice>> mAllInvoices;

    public InvoiceViewModel(@NonNull Application application) {
        super(application);
        mRepository = new InvoiceRepository(application);
        mAllInvoices = mRepository.getAllInvoices();
    }

    public LiveData<List<Invoice>> getmAllInvoices() {
        return mAllInvoices;
    }

    public void insert(Invoice invoice) {
        mRepository.insert(invoice);
    }
    public void deleteAll(){
        mRepository.deleteAll();
    }

    public int deleteInvoice(int id){
        return mRepository.deleteInvoice(id);
    }
}

package com.fit2081.fleetappprep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class ViewInvoicesActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoices);
        getSupportFragmentManager().beginTransaction().replace(R.id.invoiceList_frame_layout, new InvoiceFragment()).commit();
    }


}
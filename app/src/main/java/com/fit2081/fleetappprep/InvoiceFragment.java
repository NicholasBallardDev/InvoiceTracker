package com.fit2081.fleetappprep;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.fleetappprep.provider.Invoice;
import com.fit2081.fleetappprep.provider.InvoiceViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class InvoiceFragment extends Fragment implements iDeleteInvoice, invoiceRecyclerViewAdapter.CallbackListener {
    InvoiceViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.invoice_recycler_id);
        invoiceRecyclerViewAdapter adapter = new invoiceRecyclerViewAdapter(this, this);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel=new ViewModelProvider(this).get(InvoiceViewModel.class);

        viewModel.getmAllInvoices().observe(getViewLifecycleOwner(), (newInvoices)->{
            adapter.setData((ArrayList<Invoice>) newInvoices);
            adapter.notifyDataSetChanged();
        });

        return view;
    }


    @Override
    public void onDeleteInvoiceById(int id) {
        viewModel.deleteInvoice(id);
    }

    @Override
    public boolean onItemClicked() {
        Snackbar.make(getActivity().findViewById(R.id.invoice_screen),"Deleted", Snackbar.LENGTH_SHORT).show();

        return true;
    }
}
package com.fit2081.fleetappprep;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.fleetappprep.provider.Invoice;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class invoiceRecyclerViewAdapter extends RecyclerView.Adapter<invoiceRecyclerViewAdapter.ViewHolder> {

    ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();
    iDeleteInvoice instance;
    CallbackListener listener;

    public invoiceRecyclerViewAdapter(iDeleteInvoice anInstance, CallbackListener listener){
        instance=anInstance;
        this.listener = listener;
    }

    public void setData(ArrayList<Invoice> invoiceList){
        this.invoiceList = invoiceList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflates recycler view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_recycler_row, parent, false); //CardView inflated as RecyclerView list item
        Log.d("week6App","onCreateViewHolder");
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //prompts textViews to change on the recycler rows
        String invoiceId = invoiceList.get(position).getInvoiceId();
        String issuer = invoiceList.get(position).getIssuerName();
        String buyer = invoiceList.get(position).getBuyerName();
        String total = String.valueOf(invoiceList.get(position).getTotal());
        holder.invoiceIdTv.setText(invoiceId);
        holder.issuerTv.setText(issuer);
        holder.buyerTv.setText(buyer);
        holder.totalTv.setText(total);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener.onItemClicked()){
                    instance.onDeleteInvoiceById(invoiceList.get(holder.getAdapterPosition()).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        //assigning the views in the recycler view to variables

        public TextView invoiceIdTv, issuerTv, buyerTv,totalTv;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            invoiceIdTv = itemView.findViewById(R.id.invoice_id_id);
            issuerTv = itemView.findViewById(R.id.issuer_id);
            buyerTv = itemView.findViewById(R.id.buyer_id);
            totalTv = itemView.findViewById(R.id.total_id);
            view = itemView;
        }
    }

    public interface CallbackListener{
        boolean onItemClicked();
    }
}

package com.android.watersprinkle;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CustRecycleAdapter extends RecyclerView.Adapter<CustRecycleAdapter.ArtistViewHolder> {

    private Context mCtx;
    private List<Customer> artistList;
    private RecycleItemClickListener clickListener;

    public CustRecycleAdapter(Context mCtx, List<Customer> artistList) {
        this.mCtx = mCtx;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.customer_single_layout, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Customer customer = artistList.get(position);
        holder.textViewName.setText(customer.custName);
        holder.textViewAddress.setText(customer.custAddress);

  /*  OrderDate od =arrayDateList.get(position);
        String dt=od.date;
        if(!dt.equals("")){
            holder.chk.setVisibility(View.VISIBLE);
            holder.chk.setChecked(true);
        }else{
            holder.chk.setVisibility(View.INVISIBLE);
            holder.chk.setChecked(false);
        }
    */ }

    @Override
    public int getItemCount() {
        return artistList.size() ;
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewName, textViewAddress;
        //CheckBox chk;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.cust_single_name);
            textViewAddress = itemView.findViewById(R.id.cust_single_add);
          //  chk=itemView.findViewById(R.id.chkcust);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onClick(view, getAdapterPosition());
        }
    }
    public void setClickListener(RecycleItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}
package com.android.watersprinkle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
        final Customer customer = artistList.get(position);
        holder.textViewName.setText(customer.custName);
        holder.textViewAddress.setText(customer.custAddress);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(mCtx,CustProfileActivity.class);
                i.putExtra("custKey",customer.getUid());
                mCtx.startActivity(i);
            }
        });
   }

    @Override
    public int getItemCount() {
        return artistList.size() ;
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewName, textViewAddress;
        ImageButton img;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.cust_single_name);
            textViewAddress = itemView.findViewById(R.id.cust_single_add);
            img=itemView.findViewById(R.id.custprofile);
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
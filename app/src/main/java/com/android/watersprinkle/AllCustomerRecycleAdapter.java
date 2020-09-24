package com.android.watersprinkle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllCustomerRecycleAdapter extends RecyclerView.Adapter<AllCustomerRecycleAdapter.ArtistViewHolder>{

    private Context mCtx;
    private List<Customer> artistList;
    private RecycleItemClickListener clickListener;

    public AllCustomerRecycleAdapter(Context mCtx, List<Customer> artistList) {
        this.mCtx = mCtx;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public AllCustomerRecycleAdapter.ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.allcustomer_single_layout, parent, false);
        return new AllCustomerRecycleAdapter.ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllCustomerRecycleAdapter.ArtistViewHolder holder, int position) {
        Customer customer = artistList.get(position);
        holder.textViewName.setText(customer.custName);
        holder.textViewTimeStamp.setText(customer.timestamp);
        Picasso.get().load(customer.image).placeholder(R.drawable.defavatar).into(holder.userImageView);
 }

    @Override
    public int getItemCount() {
        return artistList.size() ;
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewName,textViewTimeStamp;
        CircleImageView userImageView;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.user_single_name);
            userImageView = (CircleImageView) itemView.findViewById(R.id.user_single_image);
            textViewTimeStamp = itemView.findViewById(R.id.user_single_status);
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

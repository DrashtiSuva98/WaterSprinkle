package com.android.watersprinkle;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportAdapterRecycle extends RecyclerView.Adapter<ReportAdapterRecycle.ArtistViewHolder>{

    private Context mCtx;
    private List<Customer> artistList;
    private RecycleItemClickListener clickListener;

    public ReportAdapterRecycle(Context mCtx, List<Customer> artistList) {
        this.mCtx = mCtx;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public ReportAdapterRecycle.ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.reportlayout, parent, false);
        return new ReportAdapterRecycle.ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapterRecycle.ArtistViewHolder holder, int position) {
        Customer customer = artistList.get(position);
        String inputPattern = "yyyy-MMM-dd";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(customer.date);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.textdate.setText(str);
        holder.txttype.setText(customer.bottleType);
        holder.txtltr.setText(customer.liter);
       holder.textprice.setText(String.valueOf(customer.bottlePrice));
        holder.txtqty.setText(String.valueOf(customer.quantity));
        holder.txttotal.setText(String.valueOf(customer.total));
       }

    @Override
    public int getItemCount() {
        return artistList.size() ;
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textdate,txttype,textprice,txtqty,txttotal,txtltr;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            textdate = itemView.findViewById(R.id.rptdate);
            txttype = itemView.findViewById(R.id.rpttype);
            txtltr=itemView.findViewById(R.id.rptltr);
            textprice = itemView.findViewById(R.id.rptprice);
            txtqty = itemView.findViewById(R.id.rptqty);
            txttotal = itemView.findViewById(R.id.rpttotal);
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

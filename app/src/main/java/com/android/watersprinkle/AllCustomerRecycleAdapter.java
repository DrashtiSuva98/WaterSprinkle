package com.android.watersprinkle;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
        final Customer customer = artistList.get(position);
        holder.textViewName.setText(customer.custName);
        holder.textViewTimeStamp.setText(customer.timestamp);
        Picasso.get().load(customer.image).placeholder(R.drawable.defavatar).into(holder.userImageView);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean installed = appInstalledOrNot("com.whatsapp");
                if (installed){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+91"+customer.custMobile));
                    mCtx.startActivity(intent);
                }else {
                    Toast.makeText(mCtx, "Whats app not installed on your device", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return artistList.size() ;
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewName,textViewTimeStamp;
        CircleImageView userImageView;
        ImageButton img;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.user_single_name);
            userImageView = (CircleImageView) itemView.findViewById(R.id.user_single_image);
            textViewTimeStamp = itemView.findViewById(R.id.user_single_status);
            img=itemView.findViewById(R.id.chat);
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

    private boolean appInstalledOrNot(String url){
        PackageManager packageManager = mCtx.getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return app_installed;
    }
}

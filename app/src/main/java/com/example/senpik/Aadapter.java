package com.example.senpik;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiConfiguration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class Aadapter extends RecyclerView.Adapter<Aadapter.ImageViewHolder> implements View.OnClickListener {

    private Context context;
    private List<LoadImage> uploads;
    private AdapterView.OnItemClickListener mlistener;
    DatabaseReference reference;
    public Aadapter(Context context, List<LoadImage> uploads) {
        this.context = context;
        this.uploads = uploads;
    }

    public Aadapter(List<LoadImage> uploads) {
        this.uploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        LoadImage loadImage=uploads.get(position);
        holder.name.setText(loadImage.getName());


        final OkHttpClient client = new OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();

        final Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();

        picasso.load(loadImage.getImageUri())
                .placeholder(R.drawable.load)
                .fit()
                .centerCrop()
                .into(holder.imageView);
        //Picasso.setSingletonInstance(picasso);

        //Picasso.with(context)


    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    @Override
    public void onClick(View view) {


    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView imageView;

        public ImageViewHolder(@NonNull final View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name_lay);
            imageView=itemView.findViewById(R.id.image_lay);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position=getAdapterPosition();

                    String url=uploads.get(position).getImageUri();

                    Intent intent=new Intent(view.getContext(),Display.class);
                    intent.putExtra("Ref",url);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    public interface OnItemClickedListener
    {
        void OnItemClicked(int position);
    }
    public void setOnItemClickedListener(OnItemClickedListener listener){

        mlistener= (AdapterView.OnItemClickListener) listener;
    }

}

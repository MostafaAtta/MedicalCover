package com.atta.medicalcover;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ui.fragments.NewLabTestFragment;
import com.atta.medicalcover.ui.fragments.NewRadiologyFragment;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Bitmap> bitmaps;
    private NewLabTestFragment newLabTestFragment;
    private NewRadiologyFragment newRadiologyFragment;

    public ImagesAdapter(Context mContext, List<Bitmap> bitmaps,
                         NewLabTestFragment newLabTestFragment,
                         NewRadiologyFragment newRadiologyFragment) {
        this.mContext = mContext;
        this.bitmaps = bitmaps;
        this.newLabTestFragment = newLabTestFragment;
        this.newRadiologyFragment = newRadiologyFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.images_list_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        final Bitmap bitmap = bitmaps.get(i) ;

        holder.imageView.setImageBitmap(bitmap);

        holder.closeImg.setOnClickListener(v -> {
            bitmaps.remove(i);
            if (newLabTestFragment != null){

                newLabTestFragment.removeImage(i);
            }else {

                newRadiologyFragment.removeImage(i);
            }
            notifyDataSetChanged();
        });

    }


    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView, closeImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.images_recycler_item);
            closeImg = itemView.findViewById(R.id.close_img);
        }
    }
}

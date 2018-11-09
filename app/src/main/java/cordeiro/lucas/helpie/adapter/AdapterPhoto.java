package cordeiro.lucas.helpie.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import cordeiro.lucas.helpie.R;
import cordeiro.lucas.helpie.model.Photo;

public class AdapterPhoto extends RecyclerView.Adapter<AdapterPhoto.MyViewHolder> {

    private List<Photo> photos;
    private Fragment fragment;

    public AdapterPhoto(List<Photo> photos, Fragment fragment) {
        this.photos = photos;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_photo, viewGroup, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        Photo photo = photos.get(i);

        myViewHolder.progressBar.setVisibility(View.VISIBLE);
        myViewHolder.txtTitle.setText(photo.getTitle());
        Glide.with(fragment).load(photo.getThumbnailUrl()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                myViewHolder.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                myViewHolder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(myViewHolder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView txtTitle;
        private ImageView imgPhoto;
        private ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitlePhoto);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            progressBar = itemView.findViewById(R.id.progressBarPhotoAdapter);
        }
    }
}

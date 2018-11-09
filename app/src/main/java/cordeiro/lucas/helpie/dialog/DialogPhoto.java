package cordeiro.lucas.helpie.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import cordeiro.lucas.helpie.R;
import cordeiro.lucas.helpie.model.Photo;

public class DialogPhoto extends Dialog {

    private Photo photo;
    private Activity activity;
    private ProgressBar progressBar;

    public static final String TAG = "PHOTO_DIALOG";

    public DialogPhoto(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_photo);

        progressBar = findViewById(R.id.progressBarDialogPhoto);
        ImageView imageView = findViewById(R.id.imgPhotoDialog);
        ImageButton imageButtonClose = findViewById(R.id.imageButtonClose);
        TextView txtTilte = findViewById(R.id.txtTitleDialogPhoto);
        txtTilte.setText(photo.getTitle());

        Log.d(TAG, "Url: "+photo.getUrl());
        Glide.with(activity).load(photo.getUrl()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(getContext(), "Falha: "+e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "Falha: "+e.getMessage());
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.d(TAG, "onResourceReady");
                progressBar.setVisibility(View.GONE);
                return false;
            }

        }).into(imageView);

        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPhoto.this.dismiss();
            }
        });
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}

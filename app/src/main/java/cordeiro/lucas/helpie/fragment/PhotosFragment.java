package cordeiro.lucas.helpie.fragment;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import cordeiro.lucas.helpie.R;
import cordeiro.lucas.helpie.adapter.AdapterPhoto;
import cordeiro.lucas.helpie.adapter.AdapterUser;
import cordeiro.lucas.helpie.clickListener.RecyclerItemClickListener;
import cordeiro.lucas.helpie.dialog.DialogPhoto;
import cordeiro.lucas.helpie.model.Photo;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Photo> photos;
    private AdapterPhoto adapter;

    public static final String TAG = "PHOTOS_FRAGMENT";

    public PhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPhotos);

        //Recycler View
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Recycler View Click
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                DialogPhoto dialogPhoto = new DialogPhoto(getActivity());
                                dialogPhoto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogPhoto.setPhoto(photos.get(position));
                                dialogPhoto.show();

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        carregarPhotos();
    }

    private void carregarPhotos() {
        photos = new ArrayList<>();
        adapter = new AdapterPhoto(photos, this);
        recyclerView.setAdapter(adapter);

        for(int i=0;i<10;i++){
            Photo photo = new Photo();
            photo.setId(i);
            photos.add(photo);
        }
        Photo photo = new Photo();
        photo.setId(11);
        photo.setUrl("https://via.placeholder.com/150/24f355");
        photos.add(photo);

        Photo photo2 = new Photo();
        photo2.setId(12);
        photo2.setUrl("http://static1.conquistesuavida.com.br/ingredients/5/54/52/05/@/24682--ingredient_detail_ingredient-2.png");
        photos.add(photo2);
        adapter.notifyDataSetChanged();
    }

}

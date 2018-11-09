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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cordeiro.lucas.helpie.R;
import cordeiro.lucas.helpie.adapter.AdapterPhoto;
import cordeiro.lucas.helpie.adapter.AdapterUser;
import cordeiro.lucas.helpie.api.DataService;
import cordeiro.lucas.helpie.clickListener.RecyclerItemClickListener;
import cordeiro.lucas.helpie.dialog.DialogPhoto;
import cordeiro.lucas.helpie.model.Photo;
import cordeiro.lucas.helpie.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Photo> photos;
    private AdapterPhoto adapter;
    private ProgressBar progressBar;


    private Retrofit retrofit;
    private Call<List<Photo>> call;

    public static final String TAG = "PHOTOS_FRAGMENT";

    public PhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPhotos);
        progressBar = view.findViewById(R.id.progressBarPhotos);

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

    private void configurarRetrofit() {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        carregarPhotos();
    }

    private void carregarPhotos() {
        progressBar.setVisibility(View.VISIBLE);
        DataService recuperarPhotos = retrofit.create(DataService.class);
        call = recuperarPhotos.recuperarPhotos();

        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    photos = response.body();

                    Log.d(TAG, "Size: " + photos.size() + " Title: " + photos.get(0).getTitle());

                    //Adapter RecyclerView
                    adapter = new AdapterPhoto(photos, PhotosFragment.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else
                    Toast.makeText(getContext(), "Falha ao recuperar", Toast.LENGTH_LONG).show();

                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (!t.getMessage().equals("Canceled"))
                    Toast.makeText(getContext(), "Falha: "+t.getMessage(),Toast.LENGTH_LONG).show();
                Log.d(TAG,"Falha: "+t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if(call == null)
            configurarRetrofit();
        else if(!call.isExecuted()) {
            try {
                call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if(call.isExecuted())
            call.cancel();
    }


}

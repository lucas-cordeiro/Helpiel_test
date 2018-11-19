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
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Photo> photos;
    private AdapterPhoto adapter;
    private ProgressBar progressBar;


    private CompositeDisposable disposables = new CompositeDisposable();
    private Retrofit retrofit;
    private Observable<List<Photo>> observable;
    private Observer<List<Photo>> observer;

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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        definirObservable();
    }

    private void definirObservable(){
        progressBar.setVisibility(View.VISIBLE);
        DataService recuperarPhotos = retrofit.create(DataService.class);
        observable = recuperarPhotos.recuperarPhotos();
        observer = new Observer<List<Photo>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(List<Photo> photosList) {
                photos = photosList;

                Log.d(TAG, "Size: " + photos.size() + " Title: " + photos.get(0).getTitle());

                //Adapter RecyclerView
                adapter = new AdapterPhoto(photos, PhotosFragment.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "Falha: "+e.getMessage(),Toast.LENGTH_LONG).show();
                Log.d(TAG,"Falha: "+e.getMessage());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                progressBar.setVisibility(View.GONE);
            }
        };

        carregarPhotos();
    }

    private void carregarPhotos() {
        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (observable == null)
            configurarRetrofit();
        else
            carregarPhotos();
    }

    @Override
    public void onStop() {
        super.onStop();
        disposables.clear();
    }


}

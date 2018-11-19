package cordeiro.lucas.helpie.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import cordeiro.lucas.helpie.R;
import cordeiro.lucas.helpie.adapter.AdapterPost;
import cordeiro.lucas.helpie.api.DataService;
import cordeiro.lucas.helpie.model.Post;
import cordeiro.lucas.helpie.model.User;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsUserFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Post> posts;
    private AdapterPost adapter;
    private ProgressBar progressBar;
    private User user;


    private final CompositeDisposable disposables = new CompositeDisposable();
    private Retrofit retrofit;
    private Observable<List<Post>> observable;
    private Observer<List<Post>> observer;

    public static final String TAG = "POST_FRAGMENT";

    public PostsUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posts_user, container, false);

        if(getArguments()!=null) {
            user = (User) getArguments().getSerializable("user");
            Log.d(TAG, "User: "+user.getName());
        }else{
            Log.d(TAG, "getArguments() == null");
        }

        recyclerView = view.findViewById(R.id.recyclerViewPosts);
        progressBar = view.findViewById(R.id.progressBarPosts);

        //Recycler View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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
        DataService postService = retrofit.create(DataService.class);
        observable = postService.recuperarPostsObservable(String.valueOf(user.getId()));
        observer = new Observer<List<Post>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(List<Post> postsList) {
                posts = postsList;

                Log.d(TAG,"Size: "+posts.size()+" Title: "+posts.get(0).getTitle());

                //Adapter RecyclerView
                adapter = new AdapterPost(posts);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "Falha: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "Falha: " + e.getMessage());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                progressBar.setVisibility(View.GONE);
            }
        };

        carregarPosts();
    }


    private void carregarPosts() {

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
            carregarPosts();
    }

    @Override
    public void onStop() {
        super.onStop();
        disposables.clear();
    }

}

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cordeiro.lucas.helpie.R;
import cordeiro.lucas.helpie.adapter.AdapterPost;
import cordeiro.lucas.helpie.api.DataService;
import cordeiro.lucas.helpie.model.Post;
import cordeiro.lucas.helpie.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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

    private Retrofit retrofit;
    private Call<List<Post>> call;

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
                .build();
        carregarPosts();
    }

    private void carregarPosts() {
        progressBar.setVisibility(View.VISIBLE);

        DataService postService = retrofit.create(DataService.class);
        call = postService.recuperarPosts(String.valueOf(user.getId()));

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()){
                    posts = response.body();
                    Log.d(TAG,"Size: "+posts.size()+" Title: "+posts.get(0).getTitle());

                    //Adapter RecyclerView
                    adapter = new AdapterPost(posts);
                    recyclerView.setAdapter(adapter);

                }else{
                    Toast.makeText(getContext(), "Falha ao recuperar",Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                if (!t.getMessage().equals("Canceled"))
                    Toast.makeText(getContext(), "Falha: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "Falha: " + t.getMessage());
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

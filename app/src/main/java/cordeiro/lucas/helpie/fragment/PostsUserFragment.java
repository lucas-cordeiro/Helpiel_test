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

import java.util.ArrayList;
import java.util.List;

import cordeiro.lucas.helpie.R;
import cordeiro.lucas.helpie.adapter.AdapterPost;
import cordeiro.lucas.helpie.model.Post;
import cordeiro.lucas.helpie.model.User;
import retrofit2.Call;
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

        carregarPosts();

        return view;
    }
    

    private void carregarPosts() {
        progressBar.setVisibility(View.VISIBLE);

        posts = new ArrayList<>();
        adapter = new AdapterPost(posts);
        recyclerView.setAdapter(adapter);


        for(int i=0;i<10;i++){
            Post post = new Post();
            post.setId(i);
            posts.add(post);
        }

        progressBar.setVisibility(View.GONE);
    }

}

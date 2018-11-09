package cordeiro.lucas.helpie.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cordeiro.lucas.helpie.R;
import cordeiro.lucas.helpie.activity.MainActivity;
import cordeiro.lucas.helpie.adapter.AdapterUser;
import cordeiro.lucas.helpie.api.DataService;
import cordeiro.lucas.helpie.clickListener.RecyclerItemClickListener;
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
public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<User> users;
    private AdapterUser adapter;
    private ProgressBar progressBar;

    private Retrofit retrofit;
    private Call<List<User>> call;

    public static final String TAG = "USERS_FRAGMENT";

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        progressBar = view.findViewById(R.id.progressBarUsers);

        //Recycler View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
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
                               User user = users.get(position);

                               PostsUserFragment fragment = new PostsUserFragment();
                               Bundle bundle = new Bundle();
                               bundle.putSerializable("user", user);
                               fragment.setArguments(bundle);

                               FragmentManager fragmentManager = getFragmentManager();
                               FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                               fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                               fragmentTransaction.replace(R.id.viewPager, fragment);
                               fragmentTransaction.commit();

                               ((MainActivity)getActivity()).setPosts(true);
                           }

                           @Override
                           public void onLongItemClick(View view, int position) {

                           }

                           @Override
                           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                           }
                       })
        );


        return view;
    }

    private void configurarRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        carregarUsers();
    }

    private void carregarUsers() {
        progressBar.setVisibility(View.VISIBLE);

        DataService userService = retrofit.create(DataService.class);
        call = userService.recuperarUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    users = response.body();

                    List<User> usersOrder = orderUsersByName(users);
                    Log.d(TAG,"Size: "+usersOrder.size()+" Name: "+usersOrder.get(0).getCompanyName());

                    //Adapter RecyclerView
                    adapter = new AdapterUser(usersOrder);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(getContext(), "Falha ao recuperar",Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                if (!t.getMessage().equals("Canceled"))
                Toast.makeText(getContext(), "Falha: "+t.getMessage(),Toast.LENGTH_LONG).show();
                Log.d(TAG,"Falha: "+t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private List<User> orderUsersByName(List<User> users){
        List<String> names = new ArrayList<>();

        for(User user : users){
            names.add(user.getName());
        }
        Collections.sort(names);

        List<User> usersOrder = new ArrayList<>();

        for(String name : names){
            for(User user : users){
                if(name.equals(user.getName())){
                    usersOrder.add(user);
                    break;
                }
            }
        }

        return usersOrder;
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

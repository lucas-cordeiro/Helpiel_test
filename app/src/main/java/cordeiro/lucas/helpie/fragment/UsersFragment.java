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
import cordeiro.lucas.helpie.adapter.AdapterUser;
import cordeiro.lucas.helpie.api.DataService;
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

    private static final String TAG = "USERS_FRAGMENT";

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

        configurarRetrofit();


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

        final DataService userService = retrofit.create(DataService.class);
        call = userService.recuperarUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    users = response.body();
                    Log.d(TAG,"Size: "+users.size()+" Name: "+users.get(0).getCompanyName());

                    //Adapter RecyclerView
                    adapter = new AdapterUser(users);
                    recyclerView.setAdapter(adapter);
                    //adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getContext(), "Falha ao recuperar",Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getContext(), "Falha: "+t.getMessage(),Toast.LENGTH_LONG).show();
                Log.d(TAG,"Falha: "+t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if(!call.isExecuted()) {
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

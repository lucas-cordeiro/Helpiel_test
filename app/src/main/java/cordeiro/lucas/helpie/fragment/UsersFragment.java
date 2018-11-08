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
import cordeiro.lucas.helpie.adapter.AdapterUser;
import cordeiro.lucas.helpie.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<User> users;
    private AdapterUser adapter;
    private ProgressBar progressBar;

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

        //Adapter RecyclerView
        users = new ArrayList<>();
        adapter = new AdapterUser(users);
        recyclerView.setAdapter(adapter);

        carregarUsers();

        return view;
    }

    private void carregarUsers() {
        progressBar.setVisibility(View.VISIBLE);

        for(int i=0;i<10;i++){
            User user = new User();
            user.setId(i);
            users.add(user);
            Log.d("User","i: "+i);
        }
        adapter.notifyDataSetChanged();

        progressBar.setVisibility(View.GONE);
    }

}

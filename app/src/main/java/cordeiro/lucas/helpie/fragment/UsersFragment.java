package cordeiro.lucas.helpie.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cordeiro.lucas.helpie.R;
import cordeiro.lucas.helpie.activity.MainActivity;
import cordeiro.lucas.helpie.adapter.AdapterUser;
import cordeiro.lucas.helpie.api.DataService;
import cordeiro.lucas.helpie.clickListener.RecyclerItemClickListener;
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
public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<User> users;
    private AdapterUser adapter;
    private ProgressBar progressBar;


    private final CompositeDisposable disposables = new CompositeDisposable();
    private Retrofit retrofit;
    private Observable<List<User>> observable;
    private Observer<List<User>> observer;

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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        definirObservable();
    }

    private void definirObservable() {
        progressBar.setVisibility(View.VISIBLE);

        DataService userService = retrofit.create(DataService.class);
        observable = userService.recuperarPostsObservable();
        observer = new Observer<List<User>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(List<User> usersList) {
                users = usersList;

                List<User> usersOrder = orderUsersByName(users);
                Log.d(TAG,"Size: "+usersOrder.size()+" Name: "+usersOrder.get(0).getCompanyName());

                //Adapter RecyclerView
                adapter = new AdapterUser(usersOrder);
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

        carregarUsers();
    }

    private void carregarUsers(){
       observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
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
        if (observable == null)
            configurarRetrofit();
        else
            carregarUsers();
    }

    @Override
    public void onStop() {
        super.onStop();
        disposables.clear();
    }
}

package cordeiro.lucas.helpie.activity;

import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import cordeiro.lucas.helpie.R;
import cordeiro.lucas.helpie.fragment.PhotosFragment;
import cordeiro.lucas.helpie.fragment.UsersFragment;

public class MainActivity extends AppCompatActivity {

    private boolean posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configurar BottomNavigationView
        configurarBottomNavigationView();
    }

    @Override
    public void onBackPressed() {
        if(posts){
            posts = false;
            carregarFragmentPadrao();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Carregar Fragment Padrão
        carregarFragmentPadrao();

        //Configura item selecionado
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    private void configurarBottomNavigationView() {

        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_navigation);

        //Configurações iniciais
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.setItemHorizontalTranslationEnabled(true);
        bottomNavigationViewEx.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_SELECTED);
        bottomNavigationViewEx.setTextVisibility(true);

        //Habilitar navegação
        habilitarNavagacao(bottomNavigationViewEx);

    }

    private void habilitarNavagacao(BottomNavigationViewEx viewEx){
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                posts = false;

                switch (menuItem.getItemId()){
                    case R.id.ic_users:
                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                        fragmentTransaction.replace(R.id.viewPager, new UsersFragment());
                        fragmentTransaction.commit();
                        return true;
                    case R.id.ic_photos:
                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        fragmentTransaction.replace(R.id.viewPager, new PhotosFragment());
                        fragmentTransaction.commit();
                        return true;
                }
                return false;
            }
        });
    }

    private void carregarFragmentPadrao() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.viewPager, new UsersFragment());
        fragmentTransaction.commit();
    }

    public void setPosts(boolean posts) {
        this.posts = posts;
    }
}

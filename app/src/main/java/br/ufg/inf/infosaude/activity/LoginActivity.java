package br.ufg.inf.infosaude.activity;

import android.os.Bundle;

import br.ufg.inf.infosaude.R;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

    }

    @Override
    protected void onStart() {
        super.onStart();
//
//        Fragment fragment = new LoginFragment();
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.content_frame, fragment);
//        ft.commit();

    }
}

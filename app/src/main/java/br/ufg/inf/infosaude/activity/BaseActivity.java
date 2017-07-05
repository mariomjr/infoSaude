package br.ufg.inf.infosaude.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import br.ufg.inf.infosaude.R;
import br.ufg.inf.infosaude.fragment.LoginFragment;
import br.ufg.inf.infosaude.fragment.MapFragment;
import br.ufg.inf.infosaude.fragment.RegisterFragment;
import br.ufg.inf.infosaude.utils.SessionManager;

/**
 * Created by astr1x on 03/07/17.
 */

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    protected SessionManager session;
    protected NavigationView navigationView;
    protected TextView tvNomeUsuario;
    protected TextView tvEmailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        hideItem();
        setaNomeUsuarioHeader();
        setTitle(R.string.app_name);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.map_fragment) {
            launchMapIntent();
        } else if (id == R.id.login_fragment) {
            launchLogIntent();
        } else if (id == R.id.register_fragment) {
            launchRegisterIntent();
        } else if (id == R.id.favorite_fragment) {
            launchFavoriteIntent();
        } else if (id == R.id.logout) {
            logout();
            Toast.makeText(this, R.string.logout_sucesso, Toast.LENGTH_LONG).show();
            launchMapIntent();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void showMapFragment() {
        MapFragment fragment = new MapFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    protected void showLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    protected void showRegisterFragment() {
        RegisterFragment fragment = new RegisterFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    protected void launchMapIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    protected void launchLogIntent() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    protected void launchRegisterIntent() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    protected void launchFavoriteIntent() {
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
        finish();
    }

    protected void hideItem() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        if (session.isLoggedIn()) {
            nav_Menu.findItem(R.id.favorite_fragment).setVisible(true);
            nav_Menu.findItem(R.id.logout).setVisible(true);
            nav_Menu.findItem(R.id.login_fragment).setVisible(false);
            nav_Menu.findItem(R.id.register_fragment).setVisible(false);
        } else {
            nav_Menu.findItem(R.id.login_fragment).setVisible(true);
            nav_Menu.findItem(R.id.register_fragment).setVisible(true);
            nav_Menu.findItem(R.id.favorite_fragment).setVisible(false);
            nav_Menu.findItem(R.id.logout).setVisible(false);
        }
    }

    protected void setaNomeUsuarioHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        tvNomeUsuario = (TextView) headerView.findViewById(R.id.tvNomeUsuario);
        tvEmailUsuario = (TextView) headerView.findViewById(R.id.tvEmailUsuario);

        if (session.isLoggedIn()) {
            HashMap<String, String> usuario = session.getUserDetails();
            tvNomeUsuario.setText("Bem vindo, " + usuario.get(SessionManager.KEY_NOME));
            tvEmailUsuario.setText(usuario.get(SessionManager.KEY_EMAIL));
        } else {
            tvNomeUsuario.setText(R.string.bem_vindo);
            tvEmailUsuario.setText("");
        }
    }

    protected void logout() {
        session.logoutUser();
    }
}

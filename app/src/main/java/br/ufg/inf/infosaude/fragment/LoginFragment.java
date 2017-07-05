package br.ufg.inf.infosaude.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import br.ufg.inf.infosaude.R;
import br.ufg.inf.infosaude.activity.MainActivity;
import br.ufg.inf.infosaude.activity.RegisterActivity;
import br.ufg.inf.infosaude.model.Usuario;
import br.ufg.inf.infosaude.services.ServicesUtils;
import br.ufg.inf.infosaude.services.UserService;
import br.ufg.inf.infosaude.utils.InputValidation;
import br.ufg.inf.infosaude.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment implements View.OnClickListener, Callback<Usuario> {

    private Button bLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView tvRegistrar;

    private InputValidation inputValidation;

    private Usuario usuario;
    View view;
    private UserService mService;
    SessionManager session;

    public LoginFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.fragment_login, container, false);

        initViews();
        initListeners();
        initObjects();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogin:
                try {
                    realizaLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tvRegistrar:
                redirecioneCadastro();
                break;

        }
    }

    private void initViews() {
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);

        bLogin = (Button) view.findViewById(R.id.bLogin);
        tvRegistrar = (TextView) view.findViewById(R.id.tvRegistrar);
    }

    private void initListeners() {
        bLogin.setOnClickListener(this);
        tvRegistrar.setOnClickListener(this);
    }

    private void initObjects() {
        usuario = new Usuario();
        inputValidation = new InputValidation(getContext());
    }

    private void realizaLogin() throws IOException {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (!inputValidation.isInputEditTextFilled(editTextEmail, getString(R.string.email_required))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(editTextEmail, getString(R.string.email_invalido))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(editTextPassword, getString(R.string.password_required))) {
            return;
        }

        mService = ServicesUtils.getUserService();

        Call<Usuario> call = mService.getUser(email, password);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
        if (response.isSuccessful()) {
            usuario = response.body();

            if (usuario != null) {
                Toast.makeText(super.getActivity(), R.string.login_sucesso, Toast.LENGTH_LONG).show();
                salvaDadosUsuarioSessao(usuario.getNome(), usuario.getEmail());
                Toast.makeText(super.getActivity(), R.string.login_sucesso, Toast.LENGTH_LONG).show();
                redirecioneHome();
            } else {
                Toast.makeText(super.getActivity(), R.string.usuario_senha_invalida, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(super.getActivity(), R.string.conexao_erro, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<Usuario> call, Throwable t) {
        Toast.makeText(super.getActivity(), R.string.conexao_erro, Toast.LENGTH_LONG).show();
    }

    private void salvaDadosUsuarioSessao(String nome, String email) {
        session.createLoginSession(nome, email);
    }

    protected void redirecioneHome() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    protected void redirecioneCadastro() {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }
}

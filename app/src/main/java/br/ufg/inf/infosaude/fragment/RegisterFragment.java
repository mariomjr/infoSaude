package br.ufg.inf.infosaude.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.ufg.inf.infosaude.R;
import br.ufg.inf.infosaude.model.Usuario;
import br.ufg.inf.infosaude.services.ServicesUtils;
import br.ufg.inf.infosaude.services.UserService;
import br.ufg.inf.infosaude.utils.InputValidation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class RegisterFragment extends Fragment implements View.OnClickListener, Callback<Usuario> {

    private Button bRegistrar;
    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    private InputValidation inputValidation;

    View view;
    private UserService mService;
    private Usuario usuario;

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_register, container, false);

        initViews();
        initListeners();
        initObjects();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRegistrar:
                cadastraUsuario();
                break;
        }
    }

    private void initViews() {
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) view.findViewById(R.id.editTextConfirmPassword);
        editTextNome = (EditText) view.findViewById(R.id.editTextNome);

        bRegistrar = (Button) view.findViewById(R.id.bRegistrar);
    }

    private void initListeners() {
        bRegistrar.setOnClickListener(this);
    }

    private void initObjects() {
        usuario = new Usuario();
        inputValidation = new InputValidation(getContext());
    }

    private void cadastraUsuario() {
        if (!inputValidation.isInputEditTextFilled(editTextNome, getString(R.string.nome_required))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(editTextEmail, getString(R.string.nome_required))) {
            return;
        }

        if (!inputValidation.isInputEditTextEmail(editTextEmail, getString(R.string.email_invalido))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(editTextPassword, getString(R.string.password_required))) {
            return;
        }

        if (!inputValidation.isInputEditTextMatches(editTextPassword, editTextConfirmPassword, getString(R.string.password_match))) {
            return;
        }

        populaUsuario();

        mService = ServicesUtils.getUserService();

        Call<Usuario> call = mService.createUser(usuario);
        call.enqueue(this);

    }

    private void populaUsuario() {
        usuario.setNome(editTextNome.getText().toString());
        usuario.setEmail(editTextEmail.getText().toString());
        usuario.setPassword(editTextPassword.getText().toString());
    }

    @Override
    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
        if (response.isSuccessful()) {
            usuario = response.body();

            if (usuario != null) {
                Toast.makeText(super.getActivity(), R.string.cadastro_sucesso, Toast.LENGTH_LONG).show();
                redirecioneLogin();
                Log.i(TAG, "Usuario cadastrado com sucesso!");
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

    public void redirecioneLogin() {
        Fragment fragment = new LoginFragment();
        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
}

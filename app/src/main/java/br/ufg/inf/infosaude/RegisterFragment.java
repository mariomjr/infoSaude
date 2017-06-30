package br.ufg.inf.infosaude;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import br.ufg.inf.infosaude.helpers.InputValidation;


public class RegisterFragment extends Fragment implements View.OnClickListener {

    private Button bRegistrar;
    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    private InputValidation inputValidation;

    View view;

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
                //TODO fazer requisicao para salvar
                postDataToSQLite();
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
//        databaseHelper = new DatabaseHelper(this.getContext());
        inputValidation = new InputValidation(getContext());
    }

    private void postDataToSQLite() {
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

//        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {
//
//            user.setName(textInputEditTextName.getText().toString().trim());
//            user.setEmail(textInputEditTextEmail.getText().toString().trim());
//            user.setPassword(textInputEditTextPassword.getText().toString().trim());
//
//            databaseHelper.addUser(user);
//
//            // Snack Bar to show success message that record saved successfully
//            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
//            emptyInputEditText();
//
//
//        } else {
//            // Snack Bar to show error message that record already exists
//            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
//        }
    }

    private void emptyInputEditText() {
        editTextNome.setText(null);
        editTextEmail.setText(null);
        editTextPassword.setText(null);
        editTextConfirmPassword.setText(null);
    }

}

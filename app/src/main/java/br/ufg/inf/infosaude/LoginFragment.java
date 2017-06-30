package br.ufg.inf.infosaude;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import br.ufg.inf.infosaude.helpers.InputValidation;
import br.ufg.inf.infosaude.model.User;
import br.ufg.inf.infosaude.sql.DatabaseHelper;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button bLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    private User user;
    View view;

    public LoginFragment() {
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
                verifyFromSQLite();
                break;
        }
    }

    private void initViews() {
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);

        bLogin = (Button) view.findViewById(R.id.bLogin);
    }

    private void initListeners() {
        bLogin.setOnClickListener(this);
    }

    private void initObjects() {
//        databaseHelper = new DatabaseHelper(this.getContext());
        user = new User();
        inputValidation = new InputValidation(getContext());
    }

    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(editTextEmail, getString(R.string.email_required))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(editTextEmail, getString(R.string.email_invalido))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(editTextPassword, getString(R.string.password_required))) {
            return;
        }
    }

    private void emptyInputEditText() {
        editTextEmail.setText(null);
        editTextPassword.setText(null);
    }
}

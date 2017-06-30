package br.ufg.inf.infosaude.helpers;

/**
 * Created by astr1x on 29/06/17.
 */

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class InputValidation {

    private Context context;

    public InputValidation(Context context) {
        this.context = context;
    }

    public boolean isInputEditTextFilled(EditText editText, String message) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            hideKeyboardFrom(editText);
            return false;
        }

        return true;
    }


    public boolean isInputEditTextEmail(EditText editText, String message) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            hideKeyboardFrom(editText);
            return false;
        }

        return true;
    }

    public boolean isInputEditTextMatches(EditText editText1, EditText editText2, String message) {
        String value1 = editText1.getText().toString().trim();
        String value2 = editText2.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            hideKeyboardFrom(editText2);
            return false;
        }

        return true;
    }

    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}


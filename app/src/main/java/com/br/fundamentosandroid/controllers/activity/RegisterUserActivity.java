package com.br.fundamentosandroid.controllers.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.br.fundamentosandroid.R;
import com.br.fundamentosandroid.exceptions.UserAlreadyRegistered;
import com.br.fundamentosandroid.models.entities.User;
import com.br.fundamentosandroid.models.service.UserBusinessService;
import com.br.fundamentosandroid.util.AppUtil;
import com.br.fundamentosandroid.util.SharedPreferenceUtil;

import java.util.Arrays;
import java.util.List;

public class RegisterUserActivity extends AppCompatActivity {

    private User mUser;
    private EditText mLogin, mPassword, mPasswordConfirm;
    private Button mButtonTerm, mButtonConfirmSignUp;
    private CheckBox mCheckBox;
    private final int WIDTH = 700;
    private final int HEIGHT = 650;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        bindElements();
        initUser();

    }

    private void initUser() {
        mUser = (mUser == null) ? new User() : mUser;
    }

    private void bindElements() {
        bindEditTextUser();
        bindEditTextPassword();
        bindEditTextConfirmPassword();
        bindButtonTerm();
        bindCheckTerm();
        bindButtonSignUp();
    }


    private void bindEditTextUser() {
        mLogin = AppUtil.get(findViewById(R.id.editTextLoginUser));
    }

    private void bindEditTextPassword() {
        mPassword = AppUtil.get(findViewById(R.id.editTextPasswordUser));
    }

    private void bindEditTextConfirmPassword() {
        mPasswordConfirm = AppUtil.get(findViewById(R.id.editTextConfirmPasswordUser));
    }

    private void bindButtonTerm() {
        mButtonTerm = AppUtil.get(findViewById(R.id.buttonTerm));
        mButtonTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogTermsAndConditions();
            }
        });
    }

    private void bindCheckTerm() {
        mCheckBox = AppUtil.get(findViewById(R.id.checkboxTerm));
    }

    private void bindButtonSignUp() {
        mButtonConfirmSignUp = AppUtil.get(findViewById(R.id.buttonConfirmSignIn));
        bindEventSignUp();
    }

    private void bindEventSignUp() {
        mButtonConfirmSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<? extends View> fields = Arrays.asList(mLogin, mPassword, mPasswordConfirm, mCheckBox);
                boolean isValid = AppUtil.validForm(fields, RegisterUserActivity.this);
                isValid = isValid && verifyPassword(mPassword.getText().toString(), mPasswordConfirm.getText().toString());
                if (isValid) {
                    bindUser();

                    try {
                        UserBusinessService.save(mUser);
                        Toast.makeText(RegisterUserActivity.this,R.string.msg_user_register_success,Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (UserAlreadyRegistered e) {
                        Toast.makeText(RegisterUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void bindUser() {
        mUser.setName(mLogin.getText().toString().toUpperCase());
        mUser.setPassword(mPassword.getText().toString().toUpperCase());
    }

    private boolean verifyPassword(String pass, String passConfirm) {
        boolean valid = false;
        if (pass.equals(passConfirm)) {
            valid = true;
        } else {
            Toast.makeText(RegisterUserActivity.this, getResources().getString(R.string.msg_password_no_confer), Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    private void createDialogTermsAndConditions() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_terms, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
        builder.setCustomTitle(view);
        builder.setMessage(getResources().getString(R.string.msg_terms));
        builder.setPositiveButton(R.string.msg_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mCheckBox.setChecked(true);
            }
        });
        AlertDialog alertDialog = builder.show();
        alertDialog.getWindow().setLayout(WIDTH, HEIGHT);
    }
}

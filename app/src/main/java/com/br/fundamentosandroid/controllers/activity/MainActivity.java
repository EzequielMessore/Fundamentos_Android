package com.br.fundamentosandroid.controllers.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.br.fundamentosandroid.R;
import com.br.fundamentosandroid.models.entities.User;
import com.br.fundamentosandroid.models.service.UserBusinessService;
import com.br.fundamentosandroid.util.AppUtil;
import com.br.fundamentosandroid.util.SharedPreferenceUtil;

public class MainActivity extends AppCompatActivity {

    private TextView mTxtLogin;
    private TextView mTxtPass;
    private Button mBtnSignUp;
    private Button mBtnSignIn;
    private CheckBox mCheckKeepSigned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_material);

        verifyConnected();
        bindElements();

        // Change typeface for the password field
        mTxtPass.setTypeface(Typeface.DEFAULT);
        mTxtPass.setTransformationMethod(new PasswordTransformationMethod());

        bindButtonSingUp();
    }


    private void verifyConnected() {
        if (SharedPreferenceUtil.isUserConnect()) {
            startActivity(new Intent(MainActivity.this, ServiceOrderListActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        verifyConnected();
    }


    private void bindButtonSingUp() {
        mBtnSignUp = AppUtil.get(findViewById(R.id.buttonSignUp));
        mBtnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setName(mTxtLogin.getText().toString().toUpperCase());
                user.setPassword(mTxtPass.getText().toString().toUpperCase());
                boolean validate = UserBusinessService.isUser(user);
                if (!validate) {
                    Toast.makeText(MainActivity.this, R.string.msg_login_invalid, Toast.LENGTH_SHORT).show();
                } else {
                    configureSharedPreference(user);
                    Intent intent = new Intent(MainActivity.this, ServiceOrderListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        mBtnSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterUserActivity.class));
            }
        });
    }

    private void configureSharedPreference(User user) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(SharedPreferenceUtil.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        User userPreference = UserBusinessService.getUserByName(user.getName());
        editor.putInt(SharedPreferenceUtil.PREFERENCE_ATTRIBUTE_USER_ID, userPreference.getId());
        editor.putString(SharedPreferenceUtil.PREFERENCE_ATTRIBUTE_USER_NAME, userPreference.getName());
        if (mCheckKeepSigned.isChecked()) {
            editor.putBoolean(SharedPreferenceUtil.PREFERENCE_CONNECT, mCheckKeepSigned.isChecked());
        }
        editor.apply();
    }

    @NonNull
    private MainActivity getActivity() {
        return MainActivity.this;
    }

    private void bindElements() {
        mTxtLogin = AppUtil.get(findViewById(R.id.editTextLogin));
        mTxtPass = AppUtil.get(findViewById(R.id.editTextPass));
        mCheckKeepSigned = AppUtil.get(findViewById(R.id.checkboxKeepSigned));
        mBtnSignIn = AppUtil.get(findViewById(R.id.buttonSignIn));
    }
}

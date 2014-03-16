package com.example.smart_fridge_android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {

    final String LOGIN_TAG = "login";
    final String REGISTER_TAG = "register";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        DatabaseHandler db = new DatabaseHandler(this);
        db.getDatabaseName();
    }

    public void onButtonClick(View v){

        switch (v.getId()){

            case R.id.btnLogin:

                EditText loginEmail = (EditText) findViewById(R.id.loginEmail);
                EditText loginPassword = (EditText) findViewById(R.id.loginPassword);

                List<NameValuePair> loginParams = new ArrayList<NameValuePair>();
                loginParams.add(new BasicNameValuePair("tag", LOGIN_TAG));
                loginParams.add(new BasicNameValuePair("email", loginEmail.getText().toString()));
                loginParams.add(new BasicNameValuePair("password", loginPassword.getText().toString()));

                Login login = new Login(loginParams);
                login.execute();
                break;

            case R.id.btnLinkToRegisterScreen:
                setContentView(R.layout.register);
                break;

            case R.id.btnRegister:

                EditText registerName = (EditText) findViewById(R.id.registerName);
                EditText registerEmail = (EditText) findViewById(R.id.registerEmail);
                EditText registerPassword = (EditText) findViewById(R.id.registerPassword);

                List<NameValuePair> registerParams = new ArrayList<NameValuePair>();
                registerParams.add(new BasicNameValuePair("tag", REGISTER_TAG));
                registerParams.add(new BasicNameValuePair("name", registerName.getText().toString()));
                registerParams.add(new BasicNameValuePair("email", registerEmail.getText().toString()));
                registerParams.add(new BasicNameValuePair("password", registerPassword.getText().toString()));

                Register register = new Register(registerParams);
                register.execute();
                break;
        }
    }
}

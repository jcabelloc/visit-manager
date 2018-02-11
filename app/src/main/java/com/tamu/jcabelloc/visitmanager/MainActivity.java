package com.tamu.jcabelloc.visitmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    EditText password2EditText;
    EditText usernameEditText;
    EditText passwordEditText;



    public void login(View view){
        Switch userRoleSwitch = (Switch)findViewById(R.id.userRoleSwitch);
        final String inputUserRole = userRoleSwitch.isChecked()?"supervisor":"agent";
        String action = loginButton.getTag().toString();
        if (action.equals("login")){
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", usernameEditText.getText().toString());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null && objects.size() == 1){
                        final String userRole = objects.get(0).getString("role");
                        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                            if (e == null) {
                                if (!userRole.equals(inputUserRole)){
                                    Toast.makeText(MainActivity.this, "Invalid Role", Toast.LENGTH_LONG).show();
                                } else if (inputUserRole.equals("supervisor")) {
                                    Toast.makeText(MainActivity.this, "Successful Login", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                                    startActivity(intent);
                                } else if (inputUserRole.equals("agent")) {
                                    Toast.makeText(MainActivity.this, "Successful Login, You are an agent!", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            }
                        });
                    }
                }
            });


        }else if (action.equals("signup")) {
            ParseUser user = new ParseUser();
            user.setUsername(usernameEditText.getText().toString());
            user.setPassword(passwordEditText.getText().toString());
            user.put("role", inputUserRole);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        if (inputUserRole.equals("supervisor")) {
                            Toast.makeText(MainActivity.this, "Successful Login", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                            startActivity(intent);
                        } else if (inputUserRole.equals("agent")) {
                            Toast.makeText(MainActivity.this, "Successful Sign Up and Login, You are an agent!", Toast.LENGTH_LONG).show();
                        }


                    }else {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.login:
                loginButton.setText("Log In");
                loginButton.setTag("login");
                password2EditText.setVisibility(View.INVISIBLE);
                Log.i("Selected", "Login Selected");
                return true;
            case R.id.signup:
                loginButton.setText("Sign Up");
                loginButton.setTag("signup");
                password2EditText.setVisibility(View.VISIBLE);
                Log.i("Selected", "Sign Up Selected");
                return  true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button)findViewById(R.id.loginButton);
        password2EditText = (EditText)findViewById(R.id.password2EditText);
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);



    }
}

package com.example.firsttry.android.eventapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.firsttry.android.eventapp.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import java.util.Arrays;
import java.util.List;



public class SignInActivity extends Activity implements View.OnClickListener {

    private Button mFbSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        try{
            getActionBar().hide();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        mFbSignInButton = (Button) findViewById(R.id.fb_sign_in_btn);
        mFbSignInButton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * goToMain() - it's used for coming back to MainActivity after successfull signing in/up
     */
    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.fb_sign_in_btn:
                fbSignIn();
                break;
        }
    }


    private void fbSignIn() {

        List<String> permissions = Arrays.asList("public_profile", "email");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Toast.makeText(getApplicationContext(), "Logging in failed.\nPlease, check your internet connection.", Toast.LENGTH_SHORT).show();
                } else if (user.isNew()) {
                    Toast.makeText(getApplicationContext(), "Welcome to Jimy!", Toast.LENGTH_SHORT).show();
                    goToMain();
                } else {
                    goToMain();
                }
            }
        });

    }
}

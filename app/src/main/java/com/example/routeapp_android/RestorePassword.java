package com.example.routeapp_android;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.routeapp_android.control.CallbackReceiver;
import com.example.routeapp_android.control.Client;
import com.example.routeapp_android.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Response;

/**
 * Class for the Login activity
 * @author Jon Calvo Gaminde
 */
public class RestorePassword extends AppCompatActivity implements View.OnClickListener, CallbackReceiver {
    private Button buttonRestore;
    private EditText tfLogin;
    private EditText tfEmail;
    private MediaPlayer mp;
    LottieAnimationView animation;


    /**
     * Initializes the Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);
        animation = findViewById(R.id.animation);
        animation.setVisibility(View.GONE);
        buttonRestore = (Button) findViewById(R.id.btnRestore);
        buttonRestore.setOnClickListener(this);
        tfLogin = (EditText) findViewById(R.id.tfLogin);
        tfEmail = (EditText) findViewById(R.id.tfEmail);
        mp = MediaPlayer.create(this, R.raw.button);
    }

    /**
     * This will try to restore the password. Controlls wether the data entered is
     * valid or not.
     * @param login The users login
     * @param email The users password
     */
    public void handleRestoreButtonAction(String login, String email){
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(login);
        boolean specialChars = m.find();
        if(login.length()>30 || specialChars){
            Toast.makeText(this,"You must enter a valid username.",Toast.LENGTH_LONG).show();
        }else if(login.length()<1 || email.length()<1){
            Toast.makeText(this,"You must enter a username and a password.",Toast.LENGTH_LONG).show();
        }else{
            try{
                mp.start();
                User user = new User();
                user.setLogin(login);
                user.setEmail(email);

                Client client = new Client();
                client.restorePassword(this, user);
                blockControls(true);

            }catch(Exception e){
                Toast.makeText(this, "Unexpected error happened.",Toast.LENGTH_LONG).show();
                blockControls(false);}
        }
    }




    /**
     * Checks if a button has been clicked, and calls their method
     * @param view The current view
     */
    public void onClick(View view){

        if(view.getId()==buttonRestore.getId())  {
            handleRestoreButtonAction(tfLogin.getText().toString(), tfEmail.getText().toString());
        }
    }

    private void blockControls(boolean block) {
        if(block){
            animation.setVisibility(View.VISIBLE);
        }else{
            animation.setVisibility(View.GONE);
        }
        buttonRestore.setEnabled(!block);
        tfLogin.setEnabled(!block);
        tfEmail.setEnabled(!block);
    }

    @Override
    public void onSuccess(Response response) {
        if (response.body() == null) {
            Toast.makeText(this, "Password restored.",Toast.LENGTH_LONG).show();
            blockControls(false);
        }
    }

    @Override
    public void onError(Throwable t) {
        Toast.makeText(this, "Unexpected error happened.",Toast.LENGTH_LONG).show();
        blockControls(false);

    }
}

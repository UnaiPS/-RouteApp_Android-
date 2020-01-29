package com.example.routeapp_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.routeapp_android.control.CallbackReceiver;
import com.example.routeapp_android.control.Client;
import com.example.routeapp_android.encryption.KeyReader;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.routeapp_android.model.Privilege;
import com.example.routeapp_android.model.Session;
import com.example.routeapp_android.model.User;

import retrofit2.Response;

/**
 * Class for the Login activity
 * @author Jon Calvo Gaminde
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, CallbackReceiver {
    private Button buttonLogin;
    private Button buttonSignup;
    private Button buttonRestore;
    private EditText tfLogin;
    private EditText pfPassword;
    private MediaPlayer mp;
    LottieAnimationView animation;


    /**
     * Initializes the Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            animation = findViewById(R.id.animation);
            animation.setVisibility(View.GONE);
            buttonLogin = (Button) findViewById(R.id.btnLogin);
            buttonLogin.setOnClickListener(this);
            buttonSignup = (Button) findViewById(R.id.btnSignUp);
            buttonSignup.setOnClickListener(this);
            buttonRestore = (Button) findViewById(R.id.btnRestore);
            buttonRestore.setOnClickListener(this);
            tfLogin = (EditText) findViewById(R.id.tfLogin);
            pfPassword = (EditText) findViewById(R.id.pfPassword);
            mp = MediaPlayer.create(this, R.raw.button);
            KeyReader.setKeyResource(getResources().openRawResource(R.raw.publickey));
            Client.setServerURL(getResources().getString(R.string.serverIp), getResources().getString(R.string.serverPort));
        } catch (Exception ex) {
            Logger.getAnonymousLogger().severe("Fatal error in startup.");
            finish();
            System.exit(0);
        }


    }

    /**
     * This will try to log in the user. Controlls wether the data entered is
     * valid or not.
     * @param login The users login
     * @param passwd The users password
     */
    public void handleLoginButtonAction(String login, String passwd){
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(login);
        boolean specialChars = m.find();
        //The limiter should do its job, but this code double checks
        if(login.length()>30 || specialChars){
            Toast.makeText(this,"You must enter a valid username.",Toast.LENGTH_LONG).show();
        }else if(login.length()<1 || passwd.length()<1){
            Toast.makeText(this,"You must enter a username and a password.",Toast.LENGTH_LONG).show();
        }else{
            try{
                mp.start();
                User user = new User();
                user.setLogin(login);
                user.setPassword(passwd);

                Client client = new Client();
                client.login(this, user);
                blockControls(true);

            }catch(Exception e){
                Toast.makeText(this, "Unexpected error happened." + e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                blockControls(false);
            /*}catch(BadLoginException e){
                Toast.makeText(this,"The user you have entered is not correct.",Toast.LENGTH_LONG).show();
            }catch(NoThreadAvailableException e){
                Toast.makeText(this,"Busy server. Please wait.",Toast.LENGTH_LONG).show();
            }catch(BadPasswordException e){
                Toast.makeText(this,"The password you have entered is not correct.",Toast.LENGTH_LONG).show();
            */}
        }
    }

    /**
     * This will try to open the sign up window.
     */
    public void handleSignUpButtonAction(){
        mp.start();
        Intent intent = new Intent(this, SignUp.class);
        setResult(RESULT_OK, intent);
        startActivity(intent);
    }

    /**
     * This will try to open the restore password window.
     */
    public void handleRestoreButtonAction(){
        mp.start();
        /*Intent intent = new Intent(this, SignUp.class);
        setResult(RESULT_OK, intent);
        startActivity(intent);*/
    }

    /**
     * Checks if a button has been clicked, and calls their method
     * @param view The current view
     */
    public void onClick(View view){

        if(view.getId()==buttonLogin.getId() ) {
            handleLoginButtonAction(tfLogin.getText().toString(), pfPassword.getText().toString());
        } else if(view.getId()==buttonSignup.getId()){
                handleSignUpButtonAction();
        } else if(view.getId()==buttonRestore.getId()){
            handleRestoreButtonAction();
        }
    }

    private void blockControls(boolean block) {
        if(block){
            animation.setVisibility(View.VISIBLE);
        }else{
            animation.setVisibility(View.GONE);
        }
        buttonLogin.setEnabled(!block);
        buttonRestore.setEnabled(!block);
        buttonSignup.setEnabled(!block);
        tfLogin.setEnabled(!block);
        pfPassword.setEnabled(!block);
    }

    @Override
    public void onSuccess(Response response) {

        if (response.body().getClass().equals(Session.class)) {
            User user;
            user = ((Session)response.body()).getLogged();
            blockControls(false);
            if (user.getPrivilege().equals(Privilege.USER)) {
                tfLogin.setText("");
                pfPassword.setText("");
                Intent intent = new Intent(this, ActivityRoute.class);
                intent.putExtra("USER", user);
                setResult(RESULT_OK, intent);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Admin users can only log in the desktop version of the app.",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        Toast.makeText(this, t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        blockControls(false);

    }
}

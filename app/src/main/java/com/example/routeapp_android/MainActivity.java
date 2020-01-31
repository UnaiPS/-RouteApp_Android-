package com.example.routeapp_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    private CheckBox cbRemember;
    private MediaPlayer mp;
    private Button buttonCredits;
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
            cbRemember = (CheckBox) findViewById(R.id.cbRemember);
            buttonCredits = (Button) findViewById(R.id.btnCredits);
            buttonCredits.setOnClickListener(this);
            mp = MediaPlayer.create(this, R.raw.button);

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "root", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            KeyReader.setKeyResource(getResources().openRawResource(R.raw.publickey));
            Client.setServerURL(getResources().getString(R.string.serverIp), getResources().getString(R.string.serverPort));

            Cursor row = db.rawQuery("select login, password from loginstorage where id = 1", null);

            if (row.moveToFirst()) {
                tfLogin.setText(row.getString(0));
                pfPassword.setText(row.getString(1));
                db.close();
                cbRemember.setChecked(true);
            } else {
                db.close();
            }

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
                Toast.makeText(this, "Unexpected error happened.",Toast.LENGTH_LONG).show();
                blockControls(false);}
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
        Intent intent = new Intent(this, RestorePassword.class);
        setResult(RESULT_OK, intent);
        startActivity(intent);
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
        } else if(view.getId()==buttonCredits.getId()){
            Toast.makeText(this,"Pressed the credits button",Toast.LENGTH_SHORT).show();
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.routeapp.unityanimation");
            if (launchIntent != null) {
                startActivity(launchIntent);//null pointer check in case package name was not found
            }else{
                Toast.makeText(this,"No credits found on your phone, please install the credits",Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * When a user is going to login, all the controls are going to be disabled
     * @param block A Boolean object
     */
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

    /**
     * This class is going to get the response if all goes fine and process it
     * @param response A Response object
     */
    @Override
    public void onSuccess(Response response) {

        if (response.body().getClass().equals(Session.class)) {
            User user;
            user = ((Session)response.body()).getLogged();
            blockControls(false);
            if (user.getPrivilege().equals(Privilege.USER)) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "root", null, 1);
                SQLiteDatabase db = admin.getWritableDatabase();
                if (cbRemember.isChecked()) {
                    ContentValues loginrow = new ContentValues();
                    loginrow.put("id", 1);
                    loginrow.put("login", tfLogin.getText().toString());
                    loginrow.put("password", pfPassword.getText().toString());
                    db.insert("loginstorage", null, loginrow);
                    db.close();
                } else {
                    db.delete("loginstorage", "id=1", null);
                    tfLogin.setText("");
                    pfPassword.setText("");
                }
                Intent intent = new Intent(this, ActivityRoute.class);
                intent.putExtra("USER", user);
                setResult(RESULT_OK, intent);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Admin users can only log in the desktop version of the app.",Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * This method is going to be executed when the operation with the server fails
     * @param t
     */
    @Override
    public void onError(Throwable t) {
        Toast.makeText(this, "Unexpected error happened.",Toast.LENGTH_LONG).show();
        blockControls(false);

    }
}

package nl.kimplusdelta.vca.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.LogInCallback;

import nl.kimplusdelta.vca.R;


/**
 * Created by KPD-Thomas on 16-4-2018.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText usernameView;
    private String passwordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Parse.initialize(this);

        usernameView = (EditText) findViewById(R.id.username);
        passwordView = "getwork";

        //passwordView = (EditText) findViewById(R.id.password);

        final Button login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validating the log in data
                boolean validationError = false;


                StringBuilder validationErrorMessage = new StringBuilder("Vul alstublieft");
                if (isEmpty(usernameView)) {
                    validationError = true;
                    validationErrorMessage.append("uw token in");
                }
                if (passwordView == "") {
                    if (validationError) {
                        validationErrorMessage.append("");
                    }
                    validationError = true;
                    validationErrorMessage.append("");
                }
                validationErrorMessage.append(".");

                if (validationError) {
                    Toast.makeText(LoginActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                //Setting up a progress dialog
                final ProgressDialog dlg = new ProgressDialog(LoginActivity.this);
                dlg.setTitle("Een ogenblik geduld..");
                dlg.setMessage("Log in.....");
                dlg.show();

                ParseUser.logInInBackground(usernameView.getText().toString(), passwordView.toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            dlg.dismiss();
                            alertDisplayer("Token correct","Welkom bij de VCA app");

                        } else {
                            dlg.dismiss();
                            ParseUser.logOut();
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }


                    }


                });

            }
        });

    }

    private boolean isEmpty(EditText text) {
        if (text.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                    }
                });

        int value= 51;
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.putExtra("key",value);
        startActivity(i);

        SharedPreferences sp = getSharedPreferences("key1", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("key2", 51);
        editor.commit();


        AlertDialog ok = builder.create();
        ok.show();
    }
}

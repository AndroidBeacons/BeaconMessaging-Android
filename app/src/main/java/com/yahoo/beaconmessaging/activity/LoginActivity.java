package com.yahoo.beaconmessaging.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.util.LoginManager;

import java.util.prefs.Preferences;

/**
 * Activity which displays a login screen to the user, offering registration as well.
 */
public class LoginActivity extends Activity {
  // UI references.
  private EditText usernameEditText;
  private EditText passwordEditText;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_login);

    // Set up the login form.
    usernameEditText = (EditText) findViewById(R.id.username);
    passwordEditText = (EditText) findViewById(R.id.password);
    passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == R.id.edittext_action_login ||
            actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
          login();
          return true;
        }
        return false;
      }
    });

    // Set up the submit button click handler
    Button actionButton = (Button) findViewById(R.id.action_button);
    actionButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        login();
      }
    });
  }

  private void login() {
    final String username = usernameEditText.getText().toString().trim();
    final String password = passwordEditText.getText().toString().trim();

    // Validate the log in data
    boolean validationError = false;
    StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));
    if (username.length() == 0) {
      validationError = true;
      validationErrorMessage.append(getString(R.string.error_blank_username));
    }
    if (password.length() == 0) {
      if (validationError) {
        validationErrorMessage.append(getString(R.string.error_join));
      }
      validationError = true;
      validationErrorMessage.append(getString(R.string.error_blank_password));
    }
    validationErrorMessage.append(getString(R.string.error_end));

    // If there is a validation error, display the error
    if (validationError) {
      Toast.makeText(LoginActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
          .show();
      return;
    }

    // Set up a progress dialog
    final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
    dialog.setMessage(getString(R.string.progress_login));
    dialog.show();
    // Call the Parse login method
      loginWithUserAndPassword(username, password, dialog, this);

  }

    public static void loginWithUserAndPassword(final String username, final String password, final ProgressDialog dialog, final Activity activity) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                LoginManager.loginComplete(username, password);
                dialog.dismiss();
                if (e != null) {
                    // Show the error message
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent;
                    // Start an intent for the dispatch activity
                    if (username.equals("admin")) {
                        intent = new Intent(activity, ExhibitAddActivity.class);
                    } else {
                        intent = new Intent(activity, HomeActivity.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            }
        });
    }
}

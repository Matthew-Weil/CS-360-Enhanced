package MainActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.weil_cs360_project_one.R;





public class MainActivity extends AppCompatActivity {
    private  EditText loginUsernameEditText;
    private EditText loginPasswordEditText;
    private Button createAccountButton;
    private Button loginButton;
    private TextView errorCode;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        // Initialize the components
        loginUsernameEditText = findViewById(R.id.login_username_field);
        loginPasswordEditText = findViewById(R.id.login_password_field);
        createAccountButton = findViewById(R.id.login_create_account_button);
        loginButton = findViewById(R.id.login_login_button);
        errorCode = findViewById(R.id.login_error_response);


    }
    @Override
    protected void onResume() {
        super.onResume();

        //clear the text in the username and password and error code
        loginUsernameEditText.setText("");
        loginPasswordEditText.setText("");
        errorCode.setText("");
    }

    /**
     * Create a new user based on the username and password entered by the user
     * @return true if the user was correctly made, false if not
     */
    public boolean createNewUser() {
        String username = loginUsernameEditText.getText().toString();
        String password = loginPasswordEditText.getText().toString();

        //Check if the username already exists
        User existingUser = AppDatabase.getDatabase(getApplicationContext()).userDao().getUserByUsername(username);
        if(existingUser != null) {
            return(false);
        }
        //if the username is not taken, create the new user account
        else {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            AppDatabase.getDatabase(getApplicationContext()).userDao().insertUser(newUser);
            return(true);
        }
    }

    /**
     * Login to a pre-existing user account based on the username and password entered by the user
     * @return true if the login was successful, false if not
     */
    public boolean loginToUser() {
        String username = loginUsernameEditText.getText().toString();
        String password = loginPasswordEditText.getText().toString();
        user = AppDatabase.getDatabase(getApplicationContext()).userDao().login(username, password);
        if(user != null) {
            return(true);
        }
        else {
            return(false);
        }
    }

    /**
     * Simple verification that the username and password are acceptable lengths
     * @return Valid if the username and password were both acceptable lengths
     *         PASSWORD if the password was incorrect length
     *         USERNAME if the username was incorrect length
     */
    public String verifyLegalUsernameAndPassword() {
        int usernameLength = loginUsernameEditText.getText().toString().length();
        int passwordLength = loginPasswordEditText.getText().toString().length();

        //Only return VALID if both the username and password are acceptable
        if(usernameLength >= 3 && usernameLength <= 15) {
            if(passwordLength >= 5 && passwordLength <= 15) {
                return("VALID");
            }
            else {
                return("PASSWORD");
            }
        }
        else {
            return("USERNAME");
        }
    }

    /**
     * Create a new account based on the username and password entered by the user
     * @param v
     */
    public void createAccount(View v) {
        errorCode.setText("CREATE THE ACCOUNT HERE");
        //Perform local username and password check
        String usernamePasswordStatus = verifyLegalUsernameAndPassword();

        //If the username and password are VALID create the new user
        if(usernamePasswordStatus.equals("VALID")) {
            boolean response = createNewUser();
            if(response) {
                errorCode.setText("Account Created");
            }
            else {
                errorCode.setText("Username Already Taken");
            }
        }
        else if(usernamePasswordStatus.equals("PASSWORD")) {
            errorCode.setText("Password must be between 5 and 15 characters");
        }
        else {
            errorCode.setText("Username must be between 5 and 15 characters");
        }

    }

    /**
     * login to a pre-existing account
     * @param v
     */
    public void loginToAccount(View v) {
        String usernamePasswordStatus = verifyLegalUsernameAndPassword();

        //If the username and password are VALID, login to the account and progress further into the app
        if(usernamePasswordStatus.equals("VALID")) {
            boolean response = loginToUser();

            //If the user was successfully logged into, go to the inventory management page
            if(response) {
                errorCode.setText("Successfully logged into user");
                Intent intent = new Intent(MainActivity.this, InventoryManagement.class);
                intent.putExtra("USER_ID", user.userId);
                startActivity(intent);
            }
            //Alert the user that their username and password were incorrect
            else {
                errorCode.setText("Incorrect Username/Password");
            }

        }
        else if(usernamePasswordStatus.equals("PASSWORD")) {
            errorCode.setText("Password must be between 5 and 15 characters");
        }
        else {
            errorCode.setText("Username must be between 5 and 15 characters");
        }
    }
}
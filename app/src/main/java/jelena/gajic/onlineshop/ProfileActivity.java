package jelena.gajic.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button passwordButton;
    Button endSessionButton;
    TextView usernameTextView;
    TextView emailTextView;

    OnlineShopDB dbHelper;
    private final String DB_NAME = "OnlineShop.db";

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        dbHelper = new OnlineShopDB(this, DB_NAME, null, 1);

        passwordButton = findViewById(R.id.PasswordButton);
        endSessionButton = findViewById(R.id.EndSesionButton);
        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        //username = getIntent().getStringExtra("username");
        // Setup account information
        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("username", "username");
        usernameTextView.setText(username);
        emailTextView.setText(dbHelper.getEmailByUsername(username));
        passwordButton.setOnClickListener(this);
        endSessionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.PasswordButton:
                Intent intent1 = new Intent(ProfileActivity.this, PasswordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case R.id.EndSesionButton:
                finishAffinity();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
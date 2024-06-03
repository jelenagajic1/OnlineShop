package jelena.gajic.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
   //Inicijalizacija
    Button buttonLogin;
    Button buttonRegister;
    LoginFragment loginFragment;
    RegisterFragment registerFragment;
    //Button ispis;
    FrameLayout fl;
    OnlineShopDB dbHelper;
    IBinderService binderInterface = null;
    private final String DB_NAME = "OnlineShop.db";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper= new OnlineShopDB(this,DB_NAME,null,1);
        buttonLogin=findViewById(R.id.loginButton);
        //ispis = findViewById(R.id.privremenButton);
        buttonRegister = findViewById(R.id.registerButton);
        fl = findViewById(R.id.container_layout);
        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        //ispis.setOnClickListener(this);

        //Inicijalizovanje fragmenata
        loginFragment= LoginFragment.newInstance("LoginFragmnet","LoginFragment");
        registerFragment = RegisterFragment.newInstance("RegisterFragment","RegisterFragment");

        }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.loginButton):
                //logika

                buttonLogin.setVisibility(View.INVISIBLE);
                buttonRegister.setVisibility(View.INVISIBLE);
                fl.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_layout, loginFragment)
                        .commit();
                break;
            case (R.id.registerButton):
                //logika

                buttonLogin.setVisibility(View.INVISIBLE);
                buttonRegister.setVisibility(View.INVISIBLE);
                fl.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_layout, registerFragment)
                        .commit();
                break;

        }
    }

}
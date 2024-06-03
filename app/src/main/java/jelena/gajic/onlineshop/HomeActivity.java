package jelena.gajic.onlineshop;



import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.FileReader;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener,ServiceConnection {


    LinearLayout WelcomeLayout;
    FrameLayout frameLayout;
    TextView welcomeTextView;
    TextView usernameTextView;
    Button homeButton;
    Button menuButton;
    Button accountButton;
    Button bagButton;
    Button addItemButton;
    EditText itemName;
    EditText itemPrice;
    EditText itemCategory;
    EditText imageName;
    Button addCategoryButton;
    EditText addCategoryEditText;
    OnlineShopDB dbHelper;
    HTTPHelper httpHelper;
    IBinderService binderInterface = null;

    private final String DB_NAME = "OnlineShop.db";
    private String user;
    AccountFragment accountFragment;
    MenuFragment menuFragment;


    private void displayUsernameAndWelcome() {
        usernameTextView.setVisibility(View.VISIBLE);
        welcomeTextView.setVisibility(View.VISIBLE);
        // Set the username text
        usernameTextView.setText(user);
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Bundle bundle = getIntent().getExtras();
        dbHelper = new OnlineShopDB(this, DB_NAME, null, 1);
        httpHelper = new HTTPHelper();
        WelcomeLayout = findViewById(R.id.welcomeLayout);
        frameLayout=findViewById(R.id.fragmentLayout);
        welcomeTextView = findViewById(R.id.WelcomeTextView);
        usernameTextView = findViewById(R.id.UsernameTextView);
        //usernameTextView.setText(bundle.getString("username","username"));

        if (getIntent() != null && getIntent().getExtras() != null) {
            String usr = getIntent().getStringExtra("username");
            if (usr != null) {
                user = usr;
                displayUsernameAndWelcome();
            }
        }



        homeButton = findViewById(R.id.HomeButton);
        menuButton = findViewById(R.id.MenuButton);
        accountButton = findViewById(R.id.AccountButton);
        bagButton = findViewById(R.id.BagButton);

        itemName = findViewById(R.id.itemNameEditText);
        itemPrice = findViewById(R.id.itemPriceEditText);
        itemCategory = findViewById(R.id.itemCategoryEditText);
        imageName = findViewById(R.id.imageNameEditText);
        addItemButton = findViewById(R.id.addItemButton);
        addCategoryEditText = findViewById(R.id.addCategoryEditText);
        addCategoryButton = findViewById(R.id.addCategoryButton);

        accountFragment = AccountFragment.newInstance("AccountFragment", "AccountFragment");
        menuFragment= MenuFragment.newInstance("MenuFragment","MenuFragment");

        homeButton.setOnClickListener(this);
        menuButton.setOnClickListener(this);
        accountButton.setOnClickListener(this);
        bagButton.setOnClickListener(this);
        addItemButton.setOnClickListener(this);
        addCategoryButton.setOnClickListener(this);
       /* if(dbHelper.isAdmin(username)){
            addItemButton.setVisibility(View.VISIBLE);
            itemName.setVisibility(View.VISIBLE);
            itemPrice.setVisibility(View.VISIBLE);
            itemCategory.setVisibility(View.VISIBLE);
            imageName.setVisibility(View.VISIBLE);
            addCategoryEditText.setVisibility(View.VISIBLE);
            addCategoryButton.setVisibility(View.VISIBLE);
        }
        else{
            addItemButton.setVisibility(View.INVISIBLE);
            itemName.setVisibility(View.INVISIBLE);
            itemPrice.setVisibility(View.INVISIBLE);
            itemCategory.setVisibility(View.INVISIBLE);
            imageName.setVisibility(View.INVISIBLE);
            addCategoryEditText.setVisibility(View.INVISIBLE);
            addCategoryButton.setVisibility(View.INVISIBLE);
        }*/
        Intent intent = new Intent(HomeActivity.this, SaleService.class);
        bindService(intent, HomeActivity.this, Context.BIND_AUTO_CREATE);
       /* Intent intent1 = new Intent(HomeActivity.this, MyService.class);
        intent1.putExtra("username", user);
        bindService(intent1, HomeActivity.this, Context.BIND_AUTO_CREATE);*/

    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.AccountButton:
                WelcomeLayout.setVisibility(View.INVISIBLE);
                addItemButton.setVisibility(View.INVISIBLE);
                itemName.setVisibility(View.INVISIBLE);
                itemPrice.setVisibility(View.INVISIBLE);
                itemCategory.setVisibility(View.INVISIBLE);
                imageName.setVisibility(View.INVISIBLE);
                addCategoryEditText.setVisibility(View.INVISIBLE);
                addCategoryButton.setVisibility(View.INVISIBLE);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, accountFragment, "fragment")
                        .commit();
                break;
            case R.id.HomeButton:
               WelcomeLayout.setVisibility(View.VISIBLE);
               // if(dbHelper.isAdmin(username)){
                    addItemButton.setVisibility(View.VISIBLE);
                    itemName.setVisibility(View.VISIBLE);
                    itemPrice.setVisibility(View.VISIBLE);
                    itemCategory.setVisibility(View.VISIBLE);
                    imageName.setVisibility(View.VISIBLE);
                    addCategoryEditText.setVisibility(View.VISIBLE);
                    addCategoryButton.setVisibility(View.VISIBLE);
                //}
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("fragment");
                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                break;
            case R.id.MenuButton:
               WelcomeLayout.setVisibility(View.INVISIBLE);
                addItemButton.setVisibility(View.INVISIBLE);
                itemName.setVisibility(View.INVISIBLE);
                itemPrice.setVisibility(View.INVISIBLE);
                itemCategory.setVisibility(View.INVISIBLE);
                imageName.setVisibility(View.INVISIBLE);
                addCategoryEditText.setVisibility(View.INVISIBLE);
                addCategoryButton.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, menuFragment, "fragment")
                        .commit();
                break;
            case R.id.BagButton:
                //to do
                WelcomeLayout.setVisibility(View.INVISIBLE);
                addItemButton.setVisibility(View.INVISIBLE);
                itemName.setVisibility(View.INVISIBLE);
                itemPrice.setVisibility(View.INVISIBLE);
                itemCategory.setVisibility(View.INVISIBLE);
                imageName.setVisibility(View.INVISIBLE);
                addCategoryEditText.setVisibility(View.INVISIBLE);
                addCategoryButton.setVisibility(View.INVISIBLE);
                break;

            case R.id.addItemButton:
                String itmName = itemName.getText().toString().trim();
                String itmPrice = itemPrice.getText().toString().trim();
                String itmCategory = itemCategory.getText().toString().trim();
                String itmImageName = imageName.getText().toString().trim();
                if(!itmName.isEmpty() && !itmPrice.isEmpty() && !itmCategory.isEmpty() && !itmImageName.isEmpty()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                if(!httpHelper.addItemToCategory(itmName, itmPrice, itmCategory, itmImageName))
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(HomeActivity.this, "Couldn't add item.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    Thread.currentThread().stop();
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    Drawable drawable = getDrawableFromName(this, itmImageName);
                    if(drawable != null) dbHelper.insertItem(new Model(drawable, itmName, itmPrice, itmCategory));
                }
                itemName.setText("");
                itemPrice.setText("");
                itemCategory.setText("");
                imageName.setText("");
                break;
            case R.id.addCategoryButton:
                String category = addCategoryEditText.getText().toString().trim();
                if(!category.isEmpty()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                if(!httpHelper.addCategory(category))
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(HomeActivity.this, "Couldn't add category.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    Thread.currentThread().stop();
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    addCategoryEditText.setText("");
                }
                break;
            default:
                break;
        }
    }
        @SuppressLint("MissingSuperCall")
        @Override
        public void onBackPressed(){


        }
    public Drawable getDrawableFromName(Context context, String imageName) {
        // Get the resource ID of the drawable
        int resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        // If resource ID is valid, return the drawable
        if (resourceId != 0) {
            return context.getResources().getDrawable(resourceId, null);
        } else {
            // Return null if resource ID is not found
            return null;
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.d("utorak", "onServiceConnected");
        binderInterface = IBinderService.Stub.asInterface(iBinder);
        try {
            binderInterface.setUsername(getIntent().getStringExtra("username"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            Log.d("utorak", String.valueOf(binderInterface.getSale()));
            if(binderInterface.getSale())
            {
                menuButton.setBackgroundColor(Color.RED);
                //welcomeTextView.setVisibility(View.INVISIBLE);
                WelcomeLayout.setVisibility(View.INVISIBLE);
                addItemButton.setVisibility(View.INVISIBLE);
                itemName.setVisibility(View.INVISIBLE);
                itemPrice.setVisibility(View.INVISIBLE);
                itemCategory.setVisibility(View.INVISIBLE);
                imageName.setVisibility(View.INVISIBLE);
                addCategoryEditText.setVisibility(View.INVISIBLE);
                addCategoryButton.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, menuFragment, "fragment")
                        .addToBackStack(String.valueOf(R.id.welcomeLayout))
                        .commit();

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }





    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        binderInterface = null;
    }

    @Override
    protected void onDestroy() {
        unbindService(HomeActivity.this);
        super.onDestroy();
    }

    public IBinderService getBinder() {
        return binderInterface;
    }
}
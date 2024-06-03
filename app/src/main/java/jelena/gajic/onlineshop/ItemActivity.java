package jelena.gajic.onlineshop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    Button backButton;
    TextView categoryText;
    ListView list;
    String category;
    String username;
    ItemAdapter adapter;
    HTTPHelper httpHelper;
    OnlineShopDB dbHelper;
    private final String DB_NAME = "OnlineShop.db";
    private BroadcastReceiver finishActivityReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        JNIsuma jni=new JNIsuma();
         //Initialize and register the BroadcastReceiver
        finishActivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getIntent().putExtra("isSale", false);
                if ("com.example.FINISH_ACTIVITY".equals(intent.getAction())) {
                    finish();
                }
            }
        };
       IntentFilter filter = new IntentFilter("com.example.FINISH_ACTIVITY");
        registerReceiver(finishActivityReceiver, filter);

        backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(this);
        categoryText = findViewById(R.id.ItemCategoryTextView);
        category = getIntent().getStringExtra("category");
        username = getIntent().getStringExtra("username");
        categoryText.setText(category);

        dbHelper = new OnlineShopDB(this, DB_NAME, null, 1);
        httpHelper= new HTTPHelper();
        list = findViewById(R.id.list_item);
        adapter = new ItemAdapter(this);
        list.setAdapter(adapter);

        //loadItemsByCategory(category);
        new Thread(new Runnable() {
            public void run() {
                try {
                    JSONArray items = httpHelper.getItemsByCategory(category);
                    try {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if(items != null){
                                    ArrayList<Model> foundItems = new ArrayList<Model>();
                                    int totalSum = 0;
                                    for(int i = 0; i < items.length(); i++){
                                        try {
                                            JSONObject item = items.getJSONObject(i);
                                            String name = item.getString("name");
                                            String price = item.getString("price");
                                            String category = item.getString("category");
                                            String imageName = item.getString("imageName");
                                            Model foundItem = new Model(getDrawableFromName(ItemActivity.this, imageName), name, price, category);
                                            foundItems.add(foundItem);
                                            int itemPrice = Integer.parseInt(price);
                                            totalSum = jni.suma(totalSum, itemPrice);
                                            Log.d("cena", String.valueOf(totalSum));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    for(Model item: foundItems) {
                                        //int suma=jni.suma(,)
                                        adapter.addShoppingItem(item);
                                    }
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

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



    /*private void loadItemsByCategory(String ctg) {
        Model[] items = dbHelper.getItemsByCategory(this, ctg);
        if (items != null) {
            for (Model item : items) {
                adapter.addShoppingItem(item);
            }
        }
    }*/


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.BackButton:
                onBackPressed();
                break;
        }
    }
}




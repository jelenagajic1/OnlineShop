package jelena.gajic.onlineshop;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PurchaseHistoryActivity extends AppCompatActivity {

    ListView list;
    TextView emptyTextView;
    PurchaseHistoryAdapter adapter;
    String username;

    OnlineShopDB dbHelper;
    private final String DB_NAME = "OnlineShop.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        adapter = new PurchaseHistoryAdapter(this);

        list = findViewById(R.id.PurschaseHistoryList);
        emptyTextView = findViewById(R.id.PurschaseHistoryEmptyList);
        list.setEmptyView(findViewById(R.id.empty_list));
        list.setAdapter(adapter);

        username = getIntent().getExtras().getString("username", "username");
        dbHelper = new OnlineShopDB(this, DB_NAME, null, 1);
        PurchaseHistoryModel[] purchases = dbHelper.getAllPurchaseHistoryItems(username);
        adapter.removeAll();
        if (purchases != null && purchases.length > 0) {
            for (PurchaseHistoryModel item : purchases) {
                adapter.addPurchaseHistoryItem(item);
            }
        }


    }
}
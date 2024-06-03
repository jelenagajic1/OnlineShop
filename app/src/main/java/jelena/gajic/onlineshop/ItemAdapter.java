package jelena.gajic.onlineshop;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList <Model> nizPodataka;
    OnlineShopDB dbHelper;
    private final String DB_NAME = "OnlineShop.db";

    public ItemAdapter(Context ctx) {
        this.mContext = ctx;
        nizPodataka = new ArrayList<Model>();
        dbHelper = new OnlineShopDB(mContext, DB_NAME, null, 1);
    }

    @Override
    public int getCount() {
        return nizPodataka.size();
    }

    @Override
    public Object getItem(int i) {
        Model podatak = null;
        try{
            podatak= nizPodataka.get(i);
        }catch (Exception e){
            e.printStackTrace();
        }
        return podatak;//mozda ne treba vratiti null
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void addItem(Model model) {
        nizPodataka.add(model);
        notifyDataSetChanged();
    }
    public void addShoppingItem(Model item) {
        nizPodataka.add(item);
        notifyDataSetChanged();
    }
    public void removeShoppingItem(Model item) {
        nizPodataka.remove(item);
        notifyDataSetChanged();
    }

    public void clearList(){
        nizPodataka.clear();
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView slika;
        TextView naziv;
        TextView cena;
        Button dodaj;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
    ViewHolder viewHolder=null;
    if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.model, null);
        viewHolder = new ViewHolder();
        viewHolder.naziv = (TextView)convertView.findViewById(R.id.ModelItemNameTextView);
        viewHolder.cena = (TextView)convertView.findViewById(R.id.ModelPriceTextView);
        viewHolder.slika = (ImageView)convertView.findViewById(R.id.ModelImage);
        viewHolder.dodaj = (Button)convertView.findViewById(R.id.AddButton);
        convertView.setTag(viewHolder);
    } else {
        viewHolder = (ViewHolder) convertView.getTag();
    }
        /* Get data Object on position from list/database */
        Model data = (Model) getItem(i);
        final String oldPrice = nizPodataka.get(i).getCena();
        Log.d("stara cena",oldPrice);
        String newPrice = oldPrice;
        Log.d("utorak", "adapter: " + String.valueOf(((ItemActivity)mContext).getIntent().getExtras().getBoolean("isSale", false)));
        if(((ItemActivity)mContext).getIntent().getExtras().getBoolean("isSale", false)){
            String priceString = newPrice.substring(0, newPrice.length()-1);
            Log.d("cena",priceString);
            float price = Float.parseFloat(priceString.trim());
            price = price * 0.8f;

            newPrice = String.valueOf((int)price + "RSD");
        }
        final String finalPrice = newPrice;
        viewHolder.cena.setText(finalPrice);
        viewHolder.slika.setImageDrawable(data.getSlika());
        viewHolder.naziv.setText(data.getNaziv());
        String rsd = mContext.getString(R.string.rsd);
        //viewHolder.cena.setText(String.valueOf(data.getCena()) + rsd);
        ViewHolder finalViewHolder = viewHolder;
        viewHolder.dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((Activity) mContext).getIntent().getExtras().getString("username", "username");
                String itemName = nizPodataka.get(i).getNaziv();
                nizPodataka.get(i).setCena(finalPrice);
                dbHelper.insertItemToPurchaseHistory(data, username);
                nizPodataka.get(i).setCena(oldPrice);
                Toast.makeText(mContext, "Predmet " + itemName + " dodat u korpu.", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
}
}

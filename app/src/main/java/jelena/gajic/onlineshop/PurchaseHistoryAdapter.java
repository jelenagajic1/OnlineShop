package jelena.gajic.onlineshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PurchaseHistoryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PurchaseHistoryModel>nizPodataka;

    public PurchaseHistoryAdapter(Context context) {
        this.context = context;
        nizPodataka = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return nizPodataka.size();
    }

    @Override
    public Object getItem(int i) {
        PurchaseHistoryModel model = null;

        try
        {
            model = nizPodataka.get(i);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return model;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public void add(PurchaseHistoryModel model){
        nizPodataka.add(model);
        notifyDataSetChanged();
    }
    public void addPurchaseHistoryItem(PurchaseHistoryModel item) {
        nizPodataka.add(item);
        notifyDataSetChanged();
    }
    public void removePosition(int i){
        nizPodataka.remove(i);
        notifyDataSetChanged();

    }public void RemoveModel(PurchaseHistoryModel model){
        nizPodataka.remove(model);
        notifyDataSetChanged();
    }public void removeAll(){
        nizPodataka.clear();
        notifyDataSetChanged();
    }
    private class ViewHolder{
        TextView status;
        TextView cena;
        TextView datum;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
       ViewHolder viewHolder = null;
       if(convertView==null){
           LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = inflater.inflate(R.layout.purchase_history_model,null);
           viewHolder = new ViewHolder();
           viewHolder.status = convertView.findViewById(R.id.PurhcaseStatus);
           viewHolder.cena = convertView.findViewById(R.id.PurchasePrice);
           viewHolder.datum = convertView.findViewById(R.id.PurchaseDate);

           convertView.setTag(viewHolder);
       }else {

           viewHolder = (ViewHolder)convertView.getTag();
       }  PurchaseHistoryModel model = (PurchaseHistoryModel) getItem(i);
        //viewHolder.status.setText(model.getStatus());
        PurchaseHistoryModel data = (PurchaseHistoryModel) getItem(i);
        switch(data.getStatus())
        {
            case CANCELLED:
                viewHolder.status.setText("CANCELLED");
                break;
            case DELIVERED:
                viewHolder.status.setText("DELIVERED");
                break;
            case WAITING_FOR_DELIVERY:
                viewHolder.status.setText("WAITING_FOR_DELIVERY");
                break;
        }
        String CenaZaPrikaz = String.valueOf(model.getUkupnaCena()) + " " + context.getString(R.string.rsd);
        viewHolder.cena.setText(String.valueOf(data.getUkupnaCena()) + " RSD");
        viewHolder.datum.setText(model.getDatum());
        return convertView;
    }
}

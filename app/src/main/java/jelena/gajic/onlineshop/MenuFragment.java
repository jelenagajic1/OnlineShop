package jelena.gajic.onlineshop;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ListView lista;
    ArrayAdapter <String> adapter;
    TextView empty;
    HTTPHelper httpHelper;

    OnlineShopDB dbHelper;
    private final String DB_NAME = "OnlineShop.db";
    boolean isSale = false;
    IBinderService binderInterface = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        lista = view.findViewById(R.id.listaKategorija);
        empty= view.findViewById(R.id.empty_list);
        lista.setEmptyView(empty);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1);
        httpHelper = new HTTPHelper();
        lista.setAdapter(adapter);
        dbHelper = new OnlineShopDB(getContext(), DB_NAME, null, 1);
       /* String[] categories = dbHelper.findCategories();
        if (categories == null || categories.length == 0) {

            insertItemsIntoDatabase();
            categories = dbHelper.findCategories();
        }
        for (String category : categories) {
            adapter.add(category);
         }*/
 new Thread(new Runnable() {
            public void run() {
                try {
                    JSONArray items = httpHelper.getAllItems();
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                if(items != null){
                                    ArrayList<String> categories = new ArrayList<String>();
                                    for(int i = 0; i < items.length(); i++){
                                        try {
                                            JSONObject item = items.getJSONObject(i);
                                            String ctg = item.getString("category");
                                            if(!categories.contains(ctg))
                                            {
                                                categories.add(ctg);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    for(String category: categories) {
                                        adapter.add(category);
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
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                Bundle bundle = new Bundle();
                try {
                    bundle.putBoolean("isSale", binderInterface.getSale());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                bundle.putString("category", adapter.getItem(i));
                bundle.putString("username", getActivity().getIntent().getExtras().getString("username", "username"));
                intent.putExtras(bundle);
                startActivity(intent);
                ;
            }
        });
        //Binding the service
        Intent intent = new Intent(getActivity(), SaleService.class);
        requireActivity().bindService(intent, (ServiceConnection) getActivity(), Context.BIND_AUTO_CREATE);
        binderInterface = ((HomeActivity)getActivity()).getBinder();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    try {
                        isSale = binderInterface.getSale();
                        Thread.sleep(1000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();


        return view;
    }
    private void insertItemsIntoDatabase() {
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.granule), "Granule","10580","Hrana"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.granule_piletina_bundeva), "Granule sa piletinom i bundevom","14599","Hrana"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.komadic_u_sosu), "Komadici mesa u sosu","16700","Hrana"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.piletina_s_pirincem), "Piletina sa pirincem","12800","Hrana"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.piletina_i_tunjevina), "Piletina i tunjevina","11580","Hrana"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.teo_pasteta_za_pse_s_piletinom), "Pasteta sa piletinom","3890","Hrana"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.cezar_pasteta_curetina), "Pasteta sa curetinom","4580","Hrana"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.teo_kobasica_za_pse_s_piletinom), "Kobasica sa piletinom","2380","Hrana"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.kobasica), "Kobasica","1600","Hrana"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.govedina_u_konzervi), "Govedina u konzervi","4500","Hrana"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.tasty_bites), "Tasty bites","1200","Hrana"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.pedigree_biskvit_za_pse), "Biskviti za pse","780","Hrana"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.briketi), "Briketi","450","Hrana"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.dentastix), "Dentastix.","790","Hrana"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.mleko), "Mleko","1100","Hrana"));


        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.igracka_aligator), "Plisani aligator","1580","Igračke"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.bacac_loptice_s_teniskom_lopticom), "Bacac loptice","3400","Igračke"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.cigra), "Cigra","490","Igračke"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.izbacivac), "Izbacivac lopte","10580","Igračke"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.kaktusi), "Kaktusi","330","Igračke"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.kanap), "Kanap","980","Igračke"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.kost), "Gumena kost","600","Igračke"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.kotur), "Kotur","500","Igračke"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.obruc), "Sareni obruc","710","Igračke"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.plisana_igracka), "Plisana igracka","1100","Igračke"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.plisana_papuca), "Plisana papuca","1280","Igračke"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.sladoled_igracka), "Igracka sladoled","1080","Igračke"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.roza_loptica), "Roza loptica","550","Igračke"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.loptica), "Teniska loptica","300","Igračke"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.kanap_lopta), "Kanap lopta","10580","Igračke"));


        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.sampon_suvo_pranje), "Sampon za suvo pranje","1000","Higijena"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.sampon), "Sampon","1000","Higijena"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.regenerator), "Regenerator","1000","Higijena"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.roler), "Roler","1000","Higijena"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.parfem), "Parfem","1000","Higijena"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.pelene), "Pelene","1000","Higijena"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.pasta_za_zube), "Pasta za zube","1000","Higijena"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.cetkice_za_zube), "Cetkice za zube","1000","Higijena"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.cetka), "Cetka","1000","Higijena"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.dezodorans), "Dezodorans","1000","Higijena"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.kapi_za_oci), "Kapi za oci","1000","Higijena"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.kapi_za_usi), "Kapi za usi","1000","Higijena"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.krema), "Krema","1000","Higijena"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.vlazne_maramice), "Vlazne maramice","1000","Higijena"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.kljesta_za_nokte), "kljesta za nokte","1000","Higijena"));


        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.adresar), "Adresar","400","Povoci"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.am), "Am","3500","Povoci"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.am_za_stenca), "Am za stenca","2300","Povoci"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.karabin_za_povodac), "Karabin za povodac","890","Povoci"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.ogrlica_za_pse_roze), "Roza ogrlica","720","Povoci"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.ogrlica_za_pse_sa_maramom), "Ogrlica sa maramom","780","Povoci"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.ogrlica_sa_cvecem), "Ogrlica sa cvecem","880","Povoci"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.plocica), "Plocica","980","Povoci"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.plocica_srce), "Plocica u obliku srca","1000","Povoci"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.crni_povodac), "Crni povodac","2380","Povoci"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.kiwi_povodac), "Kiwi povodac","2380","Povoci"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.pokretni_povodac), "Pokretni povodac","2380","Povoci"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.svetleci_privezak), "Svetleci privezak","2380","Povoci"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.marama_ogrlica), "Marama ogrlica","2380","Povoci"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.metalni_povodac), "Metalni povodac","2380","Povoci"));


        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.snale), "Snale","600","Odeća"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.odelo), "Odelo","6580","Odeća"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.leptir_masna), "Leptir masna","1430","Odeća"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.kaput), "Kaput","5600","Odeća"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.kape_za_usi), "Kape za usi","670","Odeća"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.kapa), "Kapa","3580","Odeća"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.kabanica), "Kabanica","2340","Odeća"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.dzemper), "Dzemper","4560","Odeća"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.cipele), "Cipele","4560","Odeća"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.braon_carape), "Carape braon","4560","Odeća"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.bozicna_kapa), "Bozicna kapa","4560","Odeća"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.cebe), "Cebe","4560","Odeća"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.krevet), "Krevet","4560","Odeća"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.prostirka), "Prostirka","4560","Odeća"));
        dbHelper.insertItem(new Model(getActivity().getDrawable(R.drawable.jakna), "Jakna","4560","Odeća"));





    }


    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().findViewById(R.id.MenuButton).setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().findViewById(R.id.MenuButton).setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red));
    }


    @Override
    public void onPause()
    {
        super.onPause();
        getActivity().findViewById(R.id.MenuButton).setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.brown_200));
    }

    @Override
    public void onStop()
    {
        super.onStop();
        getActivity().findViewById(R.id.MenuButton).setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.brown_200));
    }


}
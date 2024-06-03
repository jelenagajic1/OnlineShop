package jelena.gajic.onlineshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    EditText usernameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    Button registerButton;
    Bundle bundle;
    OnlineShopDB dbHelper;
    HTTPHelper httpHelper;
    CheckBox isAdminCheckBox;

    private final String DB_NAME = "OnlineShop.db";
    public static String BASE_URL = "http://192.168.8.103:3000/";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        dbHelper = new OnlineShopDB(getContext(), DB_NAME, null, 1);
        httpHelper = new HTTPHelper();
        usernameEditText = v.findViewById(R.id.UsernameEditText);
        emailEditText = v.findViewById(R.id.EmailEditText);
        passwordEditText = v.findViewById(R.id.PasswordEditText);
        registerButton = v.findViewById(R.id.RegisterButton);
        isAdminCheckBox = v.findViewById(R.id.isAdminCheckBox);
        registerButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.RegisterButton:
                String username = usernameEditText.getText().toString().trim();
                String mail = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                boolean isAdmin = isAdminCheckBox.isChecked();
                if(!username.isEmpty() && !mail.isEmpty() && !password.isEmpty()){
                    final String[] id = new String[1];
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                JSONObject jsonObject = httpHelper.createUser(username, mail, password, isAdmin);
                                if(jsonObject == null)
                                {
                                    try {
                                        getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getActivity(), "Failed to register", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Thread.currentThread().stop();
                                }
                                try {
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getActivity(), "Registered successfully", Toast.LENGTH_LONG).show();
                                            try {
                                                id[0] = jsonObject.getString("_id");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                dbHelper.insertUser(new Korisnik(username, mail, password), id[0], isAdmin);
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                Bundle loginInfo = new Bundle();
                                loginInfo.putString("username", username);
                                intent.putExtras(loginInfo);
                                startActivity(intent);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else{
                    Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}

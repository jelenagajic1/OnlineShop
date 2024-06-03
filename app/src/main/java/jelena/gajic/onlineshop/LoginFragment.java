package jelena.gajic.onlineshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    Button loginButton;
    EditText loginUsernameEditText;
    EditText loginPaswordEditText;
    HTTPHelper httpHelper;

    OnlineShopDB dbHelper;
    private final String DB_NAME = "OnlineShop.db";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = v.findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(this);
        loginUsernameEditText = v.findViewById(R.id.usernameEditText);
        loginPaswordEditText = v.findViewById(R.id.passwordEditText);
        httpHelper = new HTTPHelper();
        dbHelper = new OnlineShopDB(getContext(), DB_NAME, null, 1);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonLogin:
                String username = loginUsernameEditText.getText().toString();
                String pass = loginPaswordEditText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            if(!httpHelper.loginUser(username, pass))
                            {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Wrong username or password", Toast.LENGTH_LONG).show();
                                    }
                                });
                                Thread.currentThread().stop();
                            }
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            Bundle loginInfo = new Bundle();
                            loginInfo.putString("username", username);
                            intent.putExtras(loginInfo);
                            startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            default:
                break;
        }
    }
}
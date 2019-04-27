package com.example.kbrent.myapplication.ActivityClasses;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.kbrent.myapplication.ContainerClasses.User;
import com.example.kbrent.myapplication.HTTP.HttpClient;
import com.example.kbrent.myapplication.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import static com.example.kbrent.myapplication.R.*;

public class MainActivity extends AppCompatActivity {

    Button loginButton, registerButton;
    EditText editHost, editPort, editUser, editPass, editFirst, editLast, editEmail;
    RadioButton radioGenderMale, radioGenderFemale;
    RadioGroup radioSexGroup;
    Toolbar mTopToolbar;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        Iconify.with(new FontAwesomeModule());

        if(savedInstanceState == null && User.userName.isEmpty()) {

            loginButton = findViewById(id.buttonLogin);
            registerButton = findViewById(id.buttonRegister);

            editHost = findViewById(R.id.editHost);
            editPort = findViewById(id.editPort);
            editUser = findViewById(id.editUser);
            editPass = findViewById(id.editPass);
            editFirst = findViewById(id.editFirst);
            editLast = findViewById(id.editLast);
            editEmail = findViewById(id.editEmail);
            radioSexGroup = findViewById(id.radioSex);
            radioGenderMale = findViewById(id.radioMale);
            radioGenderFemale = findViewById(id.radioFemale);

            loginButton.setEnabled(false);
            registerButton.setEnabled(false);

            radioGenderMale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String hostInput = editHost.getText().toString().trim();
                    String portInput = editPort.getText().toString().trim();
                    String userInput = editUser.getText().toString().trim();
                    String passInput = editPass.getText().toString().trim();
                    String firstInput = editFirst.getText().toString().trim();
                    String lastInput = editLast.getText().toString().trim();
                    String emailInput = editEmail.getText().toString().trim();
                    if (!hostInput.isEmpty() && !portInput.isEmpty() && !userInput.isEmpty()
                            && !passInput.isEmpty() && !firstInput.isEmpty() && !lastInput.isEmpty()
                            && !emailInput.isEmpty()) {
                        registerButton.setEnabled(true);
                    }
                }
            });

            radioGenderFemale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String hostInput = editHost.getText().toString().trim();
                    String portInput = editPort.getText().toString().trim();
                    String userInput = editUser.getText().toString().trim();
                    String passInput = editPass.getText().toString().trim();
                    String firstInput = editFirst.getText().toString().trim();
                    String lastInput = editLast.getText().toString().trim();
                    String emailInput = editEmail.getText().toString().trim();
                    if (!hostInput.isEmpty() && !portInput.isEmpty() && !userInput.isEmpty()
                            && !passInput.isEmpty() && !firstInput.isEmpty() && !lastInput.isEmpty()
                            && !emailInput.isEmpty()) {
                        registerButton.setEnabled(true);
                    }
                }
            });


            loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    loginHttpPost checkLogin = new loginHttpPost();
                    checkLogin.execute();

                }
            });

            registerButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    registerHttpPost checkRegister = new registerHttpPost();
                    checkRegister.execute();

                }
            });

            editHost.addTextChangedListener(loginTextWatcher);
            editPort.addTextChangedListener(loginTextWatcher);
            editUser.addTextChangedListener(loginTextWatcher);
            editPass.addTextChangedListener(loginTextWatcher);

            editHost.addTextChangedListener(registerTextWatcher);
            editPort.addTextChangedListener(registerTextWatcher);
            editUser.addTextChangedListener(registerTextWatcher);
            editPass.addTextChangedListener(registerTextWatcher);
            editFirst.addTextChangedListener(registerTextWatcher);
            editLast.addTextChangedListener(registerTextWatcher);
            editEmail.addTextChangedListener(registerTextWatcher);
        } else {

            Intent intentApp = new Intent(this, MapActivity.class);
            startActivity(intentApp);
        }

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String hostInput = editHost.getText().toString().trim();
            String portInput = editPort.getText().toString().trim();
            String userInput = editUser.getText().toString().trim();
            String passInput = editPass.getText().toString().trim();

            loginButton.setEnabled(!hostInput.isEmpty() && !portInput.isEmpty() && !userInput.isEmpty() && !passInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher registerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String hostInput = editHost.getText().toString().trim();
            String portInput = editPort.getText().toString().trim();
            String userInput = editUser.getText().toString().trim();
            String passInput = editPass.getText().toString().trim();
            String firstInput = editFirst.getText().toString().trim();
            String lastInput = editLast.getText().toString().trim();
            String emailInput = editEmail.getText().toString().trim();

            if (radioSexGroup.getCheckedRadioButtonId() != -1) {
                registerButton.setEnabled(!hostInput.isEmpty() && !portInput.isEmpty() && !userInput.isEmpty()
                        && !passInput.isEmpty() && !firstInput.isEmpty() && !lastInput.isEmpty()
                        && !emailInput.isEmpty());
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @SuppressLint("StaticFieldLeak")
    public class loginHttpPost extends AsyncTask<URL, String, String> {


        @Override
        public String doInBackground(URL... urls) {

            HttpClient httpClient = new HttpClient();

            String serverHost = editHost.getText().toString();
            String serverPort = editPort.getText().toString();
            String userName = editUser.getText().toString();
            String userPass = editPass.getText().toString();
            User.userPass = userPass;
            String response = "";

            User.serverHost = serverHost;
            User.serverPort = serverPort;

            JSONObject body = new JSONObject();

            try {
                body.put("userName", userName);
                body.put("password", userPass);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                String serverPath = "user/login";
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/" + serverPath);
                response = HttpClient.postUrl(url, body);

                if(!response.contains("message")) {

                    User user = new User();
                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(response).getAsJsonObject();
                    String personId = rootObj.get("personID").getAsString();
                    String tokenId = rootObj.get("authToken").getAsString();
                    User.PersonID = personId;
                    User.authToken = tokenId;
                    User.userName = userName;

                    try {

                        serverPath = "person/" + personId;
                        url = new URL("http://" + serverHost + ":" + serverPort + "/" + serverPath);
                        String responsePersonID = httpClient.getUrl(url, tokenId);
                        //Log.e("HTTP", responsePersonID);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    //Add events
                    try {
                        serverPath = "event";
                        url = new URL("http://" + editHost.getText().toString() +
                                ":" + editPort.getText().toString() + "/" + serverPath);
                        String responseEvent = HttpClient.getUrl(url, User.authToken);
                        user.addEvents(responseEvent);
                        //Log.e("HTTP", responseEvent);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    //Add people
                    try {
                        serverPath = "person";
                        url = new URL("http://" + editHost.getText().toString() +
                                ":" + editPort.getText().toString() + "/" + serverPath);
                        String responsePerson = HttpClient.getUrl(url, User.authToken);
                        user.addPeople(responsePerson);
                        //Log.e("HTTP", responsePerson);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            StringBuilder sb = new StringBuilder();
            for(String tempString : User.fatherSide) {
                sb.append(tempString);
            }

            //Log.e("HTTP", sb.toString());

            return response;
        }

        public void onPostExecute(String response) {

            if (response.contains("message") || response.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
            } else {
                //JsonParser parser = new JsonParser();
                //JsonObject rootObj = parser.parse(response).getAsJsonObject();
                //String firstName = rootObj1.get("firstName").getAsString();
                //String lastName = rootObj1.get("lastName").getAsString();
                //Toast.makeText(getApplicationContext(), "Login success! For: " + firstName + " " + lastName, Toast.LENGTH_SHORT).show();

                //Log.e("HTTP", response);

                Intent intentApp = new Intent(MainActivity.this, MapActivity.class);

                MainActivity.this.startActivity(intentApp);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class registerHttpPost extends AsyncTask<URL, String, String> {

        @Override
        public String doInBackground(URL... urls) {

            String serverHost = editHost.getText().toString();
            String serverPort = editPort.getText().toString();
            String serverPath = "user/register";
            String userName = editUser.getText().toString();
            String userPass = editPass.getText().toString();
            User.userPass = userPass;
            String userFirst = editFirst.getText().toString();
            String userLast = editLast.getText().toString();
            String userEmail = editEmail.getText().toString();
            String userGenderMale = radioGenderMale.getText().toString();
            String response = "";

            User.serverHost = serverHost;
            User.serverPort = serverPort;

            JSONObject body = new JSONObject();
            User user = new User();

            try {
                body.put("userName", userName);
                body.put("password", userPass);
                body.put("email", userEmail);
                body.put("firstName", userFirst);
                body.put("lastName", userLast);
                if (userGenderMale.isEmpty()) {
                    body.put("gender", "f");
                } else {
                    body.put("gender", "m");
                }
            } catch (JSONException e) {
                System.out.println(e.toString());
            }

            try {
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/" + serverPath);
                response = HttpClient.postUrl(url, body);
            } catch (MalformedURLException e) {
                System.out.println(e.toString());
            }

            if(!response.contains("message")) {

                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(response).getAsJsonObject();
                String personId = rootObj.get("personID").getAsString();
                String tokenId = rootObj.get("token").getAsString();
                User.PersonID = personId;
                User.authToken = tokenId;
                User.userName = userName;
                User user1 = new User();

                //Add events
                try {
                    serverPath = "event";
                    URL url = new URL("http://" + editHost.getText().toString() +
                            ":" + editPort.getText().toString() + "/" + serverPath);
                    String responseEvent = HttpClient.getUrl(url, tokenId);
                    user1.addEvents(responseEvent);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                //Add people
                try {
                    serverPath = "person";
                    URL url = new URL("http://" + editHost.getText().toString() +
                            ":" + editPort.getText().toString() + "/" + serverPath);
                    String responsePerson = HttpClient.getUrl(url, tokenId);
                    user1.addPeople(responsePerson);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            return response;


        }

        public void onPostExecute(String response) {
            if (response.contains("User already") || response.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(getApplicationContext(), "Register succeeded for " + editFirst.getText().toString() + " " + editLast.getText().toString(), Toast.LENGTH_SHORT).show();

                Intent intentApp = new Intent(MainActivity.this, MapActivity.class);

                MainActivity.this.startActivity(intentApp);
            }
        }
    }
}

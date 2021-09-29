package com.example.familymapclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import request.LoginRequest;
import request.MultiplePeopleRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.MultipleEventsResponse;
import response.MultiplePeopleResponse;
import response.RegisterResponse;
import response.SinglePersonResponse;

public class LoginFragment extends Fragment {
    private static final String LOG_TAG = "LoginFragment";
    private static final String LOGIN_RESPONSE_KEY = "LoginResponseKey";
    private static final String REGISTER_RESPONSE_KEY = "RegisterResponseKey";
    private static final String TOAST_MESSAGE_KEY = "ToastMessageKey";
    private static final String SUCCESS_KEY = "SuccessKey";

    private GoogleMapsFragment GoogleMapsFragment;

    private EditText serverHostEditText;
    private EditText serverPortEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;

    private RadioButton maleButton;
    private RadioButton femaleButton;

    private Button loginButton;
    private Button registerButton;

    private static String serverHostString;
    private static String serverPortString;
    private static String usernameString;
    private static String passwordString;
    private static String firstNameString;
    private static String lastNameString;
    private static String emailString;
    private static String genderString = "";

    private boolean enableLogin() {
        return !serverHostString.isEmpty() &&
                !serverPortString.isEmpty() &&
                !usernameString.isEmpty() &&
                !passwordString.isEmpty();
    }

    private boolean enableRegistration() {
        return !serverHostString.isEmpty() &&
                !serverPortString.isEmpty() &&
                !usernameString.isEmpty() &&
                !passwordString.isEmpty() &&
                !firstNameString.isEmpty() &&
                !lastNameString.isEmpty() &&
                !emailString.isEmpty() &&
                !genderString.isEmpty();
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            serverHostString = serverHostEditText.getText().toString();
            serverPortString = serverPortEditText.getText().toString();
            usernameString = usernameEditText.getText().toString();
            passwordString = passwordEditText.getText().toString();
            firstNameString = firstNameEditText.getText().toString();
            lastNameString = lastNameEditText.getText().toString();
            emailString = emailEditText.getText().toString();

            loginButton.setEnabled(enableLogin());
            registerButton.setEnabled(enableRegistration());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private GoogleMapsFragment createMapFragment() {
        GoogleMapsFragment fragment = new GoogleMapsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void launchMapFragment() {
        FragmentManager fm = getFragmentManager();
        GoogleMapsFragment = (GoogleMapsFragment) fm.findFragmentById(R.id.mapFrameLayout);
        if (GoogleMapsFragment == null) {
            GoogleMapsFragment = createMapFragment();
            fm.beginTransaction().replace(R.id.loginFrameLayout, GoogleMapsFragment).commit();
        }
    }

    private static class LoginTask implements Runnable {
        private final Handler messageHandler;
        private final LoginRequest request;

        private void sendMessage(LoginResponse response, String toast, boolean success) {
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putString(LOGIN_RESPONSE_KEY, response.toString());
            messageBundle.putString(TOAST_MESSAGE_KEY, toast);
            messageBundle.putBoolean(SUCCESS_KEY, success);
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }

        public LoginTask(Handler messageHandler, LoginRequest request) {
            this.messageHandler = messageHandler;
            this.request = request;
        }

        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy(serverHostString, serverPortString);
            LoginResponse loginResponse;
            String toast;
            boolean success;
            try {
                loginResponse = proxy.login(request);
                if (loginResponse.isSuccess()) {
                    // get information from response
                    String username = loginResponse.getUsername();
                    String personID = loginResponse.getPersonID();
                    String tokenStr = loginResponse.getTokenStr();

                    // get the user's full name using proxy
                    SinglePersonResponse singlePersonResponse = proxy.getPerson(personID, tokenStr);
                    String firstName = singlePersonResponse.getFirstName();
                    String lastName = singlePersonResponse.getLastName();

                    DataCache cache = DataCache.getInstance();
                    cache.setLoggedIn(true);
                    // populate DataCache.java with username, auth token, first and last names, people, and events
                    cache.setUsername(username);
                    cache.setTokenStr(tokenStr);
                    cache.setFirstName(firstName);
                    cache.setLastName(lastName);
                    cache.setPersonID(personID);

                    GetPersonEventTask task = new GetPersonEventTask(tokenStr);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(task);

                    toast = cache.getToastMessage();
                    success = true;
                }
                else {
                    // create message for the toast
                    toast = loginResponse.getMessage();
                    success = false;
                }

            } catch (IOException e) {
                loginResponse = new LoginResponse("ERROR: Login Failed");

                toast = "ERROR: Login Failed";
                success = false;

                e.printStackTrace();
            }
            sendMessage(loginResponse, toast, success);
        }
    }

    private static class RegisterTask implements Runnable {
        private final Handler messageHandler;
        private final RegisterRequest request;

        private void sendMessage(RegisterResponse response, String toast, boolean success) {
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putString(REGISTER_RESPONSE_KEY, response.toString());
            messageBundle.putString(TOAST_MESSAGE_KEY, toast);
            messageBundle.putBoolean(SUCCESS_KEY, success);
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }

        public RegisterTask(Handler messageHandler, RegisterRequest request) {
            this.messageHandler = messageHandler;
            this.request = request;
        }

        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy(serverHostString, serverPortString);
            RegisterResponse registerResponse;
            String toast;
            boolean success;
            try {
                registerResponse = proxy.register(request);
                if (registerResponse.isSuccess()) {
                    String username = registerResponse.getUsername();
                    String personID = registerResponse.getPersonID();
                    String tokenStr = registerResponse.getAuthorizationToken();

                    SinglePersonResponse singlePersonResponse = proxy.getPerson(personID, tokenStr);
                    String firstName = singlePersonResponse.getFirstName();
                    String lastName = singlePersonResponse.getLastName();

                    DataCache cache = DataCache.getInstance();
                    cache.setLoggedIn(true);
                    cache.setUsername(username);
                    cache.setTokenStr(tokenStr);
                    cache.setFirstName(firstName);
                    cache.setLastName(lastName);
                    cache.setPersonID(personID);

                    GetPersonEventTask task = new GetPersonEventTask(tokenStr);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(task);

                    toast = cache.getToastMessage();
                    success = true;
                }
                else {
                    toast = registerResponse.getMessage();
                    success = false;
                }
            }
            catch (IOException e) {
                registerResponse = new RegisterResponse("ERROR: Registration Failed");
                toast = "ERROR: Registration Failed";
                success = false;
                e.printStackTrace();
            }
            sendMessage(registerResponse, toast, success);
        }
    }

    private static class GetPersonEventTask implements Runnable {
        private final String tokenStr;

        public GetPersonEventTask(String tokenStr) {
            this.tokenStr = tokenStr;
        }

        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy(serverHostString, serverPortString);
            try {
                MultiplePeopleResponse peopleResponse = proxy.getPeople(tokenStr);
                MultipleEventsResponse eventsResponse = proxy.getEvents(tokenStr);
                if (peopleResponse.isSuccess() && eventsResponse.isSuccess()) {
                    DataCache cache = DataCache.getInstance();
                    cache.addPeople(peopleResponse.getData());
                    cache.addEvents(eventsResponse.getData());
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "in onCreate()");
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(LOG_TAG, "in onCreateView()");

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        serverHostEditText = view.findViewById(R.id.serverHostEditText);
        serverPortEditText = view.findViewById(R.id.serverPortEditText);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        firstNameEditText = view.findViewById(R.id.firstNameEditText);
        lastNameEditText = view.findViewById(R.id.lastNameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);

        maleButton = view.findViewById(R.id.maleRadioButton);
        femaleButton = view.findViewById(R.id.femaleRadioButton);
        loginButton = view.findViewById(R.id.loginButton);
        registerButton = view.findViewById(R.id.registerButton);

        loginButton.setEnabled(false);
        registerButton.setEnabled(false);
        serverHostEditText.addTextChangedListener(textWatcher);
        serverPortEditText.addTextChangedListener(textWatcher);
        usernameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        firstNameEditText.addTextChangedListener(textWatcher);
        lastNameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);

        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderString = "m";
                registerButton.setEnabled(enableRegistration());
            }
        });
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderString = "f";
                registerButton.setEnabled(enableRegistration());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Set up a handler that will process messages from the task and make updates on the UI thread
                Handler UIThreadMessageHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        String text = bundle.getString(TOAST_MESSAGE_KEY, "");
                        boolean success = bundle.getBoolean(SUCCESS_KEY);
                        if (success) {
                            launchMapFragment();
                        }
                        else {
                            Toast.makeText(getActivity(), String.format(text), Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                // Create and execute the login task on a separate thread
                LoginRequest request = new LoginRequest(usernameString, passwordString);
                LoginTask task = new LoginTask(UIThreadMessageHandler, request);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler UIThreadMessageHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        String text = bundle.getString(TOAST_MESSAGE_KEY, "");
                        boolean success = bundle.getBoolean(SUCCESS_KEY);
                        if (success) {
                            launchMapFragment();
                        }
                        else {
                            Toast.makeText(getActivity(), String.format(text), Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                RegisterRequest request = new RegisterRequest(usernameString, passwordString, emailString, firstNameString, lastNameString, genderString);
                RegisterTask task = new RegisterTask(UIThreadMessageHandler, request);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);
            }
        });

        return view;
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.i(LOG_TAG, "in onAttachFragment(Fragment)");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(LOG_TAG, "in onActivityCreated(Bundle)");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "in onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "in onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "in onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "in onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(LOG_TAG, "in onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "in onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(LOG_TAG, "in onDetach()");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOG_TAG, "in onSaveInstanceState(Bundle)");
    }
}

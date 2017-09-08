package com.octalsoftaware.archi.views.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.data.DataManager;
import com.octalsoftaware.archi.data.events.ErrorEvent;
import com.octalsoftaware.archi.data.events.LoginSuccessEvent;
import com.octalsoftaware.archi.databinding.LoginActivityBinding;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.Util;
import com.octalsoftaware.archi.utils.Validations;
import com.octalsoftaware.archi.utils.constants.S;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import static com.octalsoftaware.archi.utils.constants.I.CHECK_RUNTIME_PERMISSION;
import static com.octalsoftaware.archi.utils.constants.S.user_device_token;

/**
 * Created by anandj on 4/14/2017.
 */

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private LoginActivityBinding mBinding;
    @Nullable
    private Context context = null;
    @Nullable
    private MyProgressDialog progressDialog = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization context
        context = this;

        // Marshmallow runtime permission
        checkRequestPermission();


        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.login_activity);

        // set font
        mBinding.txtLogintagline.setTypeface(Util.setTypefaceRegular(context));
        mBinding.txtAriscodingsystem.setTypeface(Util.setTypefaceRegular(context));
        mBinding.btnLogin.setTypeface(Util.setTypefaceBold(context));
        mBinding.edtEmail.setTypeface(Util.setTypefaceRegular(context));
        mBinding.edtPassword.setTypeface(Util.setTypefaceRegular(context));

        initEvent();
    }

    // method for runtime permission
    private void checkRequestPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE

            }, CHECK_RUNTIME_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        int i = 0;
        if (requestCode == CHECK_RUNTIME_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //GoNext();
            } else {
                    finish();
            }
        }
    }

    public void initEvent() {
        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                try {
                    submitLogin();
                }
                catch (Exception e){
                    Log.e(TAG,e.toString());
                }

            }
        });
    }

    public void submitLogin() throws JSONException {

        String email = mBinding.edtEmail.getText().toString().trim();
        String password = mBinding.edtPassword.getText().toString().trim();
        String device_token = MySharedPreferences.getPreferences(context,user_device_token);

        if (!Validations.loginValidation(mBinding.edtEmail, mBinding.edtPassword, context)) {
            return;
        }

        //precaution for double click
        mBinding.btnLogin.setEnabled(false);

        // show progress dialog
        progressDialog = new MyProgressDialog(context);
        progressDialog.show();

        // check device token is empty if empty than call again firebase
        if(device_token.equals("")){
            device_token = FirebaseInstanceId.getInstance().getToken();
            if(device_token==null)
                device_token = "";
        }

        // prepare json object
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(S.email,email);
        jsonObject.put(S.pass,password);
        jsonObject.put(S.user_device_type,S.device_type);
        jsonObject.put(S.device_token,device_token);

        // call api for login
        DataManager.login(ConvertJsonToMap.jsonToMap(jsonObject));
    }

    @Override
    public void onStart() {
        super.onStart();
        event.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        event.unregister(this);
    }

    @Subscribe
    public void onSuccess(@NonNull LoginSuccessEvent event) {
        Util.dismissPrograssDialog(progressDialog);
        mBinding.btnLogin.setEnabled(true);
        String result = event.getResponce();
        if(result!=null){
              try {
                  // Parse data
                  JSONObject jsonObject = new JSONObject(result).getJSONObject(S.response);
                  if(jsonObject.getBoolean(S.status)){
                      JSONObject  jsonObject1 =jsonObject.getJSONArray(S.data).getJSONObject(0);
                      // store data in SharedPreference
                      MySharedPreferences.setPreferences(context,jsonObject1.getString(S.api_user_id),S.user_id);
                      MySharedPreferences.setPreferences(context,jsonObject1.getString(S.api_name),S.user_name);
                      MySharedPreferences.setPreferences(context,jsonObject1.getString(S.api_email),S.user_email_id);
                      MySharedPreferences.setPreferences(context,jsonObject1.getString(S.api_device_token),S.user_device_token);
                      MySharedPreferences.setPreferences(context,jsonObject1.getString(S.api_status),S.user_active_status);

                      startHomeActivity();
                  }
                  else {
                      Util.showToast(context,jsonObject.getString(S.message));
                  }
              }
              catch (Exception e){
                  Log.e(TAG,e.toString());
              }
        }
    }

    @Subscribe
    public void onFailed(@NonNull ErrorEvent event) {
        Util.dismissPrograssDialog(progressDialog);
        mBinding.btnLogin.setEnabled(true);
        Log.e(TAG,event.getMessage());
       // Util.showToast(context, event.getMessage());

    }

    // start Landing Page
    private void startHomeActivity(){
        Intent intent = new Intent(context,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}

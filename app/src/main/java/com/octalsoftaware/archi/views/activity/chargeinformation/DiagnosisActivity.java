package com.octalsoftaware.archi.views.activity.chargeinformation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.data.DataManager;
import com.octalsoftaware.archi.data.events.ErrorEvent;
import com.octalsoftaware.archi.data.events.SucessEvent;
import com.octalsoftaware.archi.databinding.DiagnosisActivityBinding;
import com.octalsoftaware.archi.models.LocationModal;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.activity.BaseActivity;
import com.octalsoftaware.archi.views.adapter.DiagnosisAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.octalsoftaware.archi.utils.constants.S.user_id;

/**
 * Created by anandj on 5/4/2017.
 */

public class DiagnosisActivity extends BaseActivity implements View.OnClickListener {

    @Nullable
    DiagnosisActivityBinding locationSearchBinding = null;
    @Nullable
    private Context context = null;
    @Nullable
    private ArrayList<LocationModal> modalArrayList = null;
    @Nullable
    private DiagnosisAdapter locationAdapter = null;
    @Nullable
    private MyProgressDialog progressDialog = null;
    private String patient_id = "";
    private String api_type = "";
    @Nullable
    private HashMap<String,String> diagnosis_checkid = null;
    @Nullable
    private ArrayList<String> diagnosis_checkname = null;
    private static final String TAG = DiagnosisActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization context
        context = this;

        locationSearchBinding = DataBindingUtil.setContentView(this, R.layout.diagnosis_activity);

        diagnosis_checkid = new HashMap<>();
        diagnosis_checkname = new ArrayList<>();

        patient_id = getIntent().getStringExtra(S.patient_details);
        api_type = getIntent().getStringExtra(S.type);
        diagnosis_checkid = (HashMap<String, String>) getIntent().getSerializableExtra(S.check_id);
    //    diagnosis_checkid = getIntent().getStringArrayListExtra(S.check_id);

        modalArrayList = new ArrayList<>();


        locationSearchBinding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(@NonNull CharSequence charSequence, int i, int i1, int i2) {

                try {
                    modalArrayList.clear();
                    locationAdapter.notifyDataSetChanged();
                    getSuggestion(charSequence.toString());

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        locationSearchBinding.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        try {
                            if (!locationSearchBinding.edtSearch.getText().toString().equals(""))
                                getSuggestion(locationSearchBinding.edtSearch.getText().toString());

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }

                        return true;

                }
                return false;
            }
        });

        locationSearchBinding.edtSearch.setHint("Search " + api_type + " here...");
        setOnClickListener();

        intiAdapter();
    }

    private void getSuggestion(String s) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(S.api_record_id, patient_id);
        jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
        jsonObject.put(S.api_listing_type, api_type);
        jsonObject.put(S.api_ref_code, s);

        locationSearchBinding.pbIcd1.setVisibility(View.VISIBLE);
        DataManager.allList(ConvertJsonToMap.jsonToMap(jsonObject));
    }


    private void setOnClickListener() {
        locationSearchBinding.toggleIcon.setOnClickListener(this);
        locationSearchBinding.plusIcon.setOnClickListener(this);
    }


    private void intiAdapter() {
        locationAdapter = new DiagnosisAdapter(modalArrayList, this, api_type,diagnosis_checkid);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        locationSearchBinding.recylerview.setLayoutManager(mLayoutManager);
        locationSearchBinding.recylerview.setItemAnimator(new DefaultItemAnimator());
        locationSearchBinding.recylerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        locationSearchBinding.recylerview.setAdapter(locationAdapter);
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.toggle_icon:
                finish();
                break;
            case R.id.plus_icon:
                diagnosis_checkid.clear();
                if(locationAdapter!=null){
                    diagnosis_checkid.putAll(locationAdapter.getCheck_id());
                    filterByName(diagnosis_checkname,diagnosis_checkid);
                }

                break;
        }
    }

    public void filterByName(ArrayList<String> name, HashMap<String,String> id) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(S.start_activity_result,"");
        returnIntent.putExtra(S.check_id,id);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void initEvent() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        event.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        event.register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Subscribe
    public void onSuccess(@NonNull SucessEvent event) {
        //      Util.dismissPrograssDialog(progressDialog);
        locationSearchBinding.pbIcd1.setVisibility(View.GONE);
        String result = event.getResponce();
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result).getJSONObject(S.response);
                if (jsonObject.getBoolean(S.status)) {
                    if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_all_list)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(S.data);
                        modalArrayList.clear();
                        locationAdapter.notifyDataSetChanged();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            LocationModal locationModal = new LocationModal();
                            locationModal.setId(jsonObject1.getString(S.api_id));
                            locationModal.setOffice(jsonObject1.getString(S.api_office));
                            locationModal.setLname(jsonObject1.getString(S.api_lname));
                            locationModal.setFname(jsonObject1.getString(S.api_fname));
                            locationModal.setRef_code(jsonObject1.getString(S.api_ref_code));
                            locationModal.setTitle(jsonObject1.getString(S.api_title));
                            locationModal.setDiagnosis_code_type(jsonObject1.getString(S.api_diagnosis_code_type));

                            modalArrayList.add(locationModal);
                        }

                        locationAdapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    @Subscribe
    public void onFailed(@NonNull ErrorEvent event) {
        //    Util.dismissPrograssDialog(progressDialog);
        locationSearchBinding.pbIcd1.setVisibility(View.GONE);
        Log.e(TAG, event.getMessage());
    }
}

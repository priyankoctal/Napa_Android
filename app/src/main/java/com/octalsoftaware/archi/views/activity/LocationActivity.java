package com.octalsoftaware.archi.views.activity;

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
import com.octalsoftaware.archi.databinding.LocationSearchBinding;
import com.octalsoftaware.archi.models.LocationModal;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.adapter.LocationAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import static com.octalsoftaware.archi.utils.constants.S.user_id;

/**
 * Created by anandj on 4/28/2017.
 */

public class LocationActivity extends BaseActivity implements View.OnClickListener {

    @Nullable
    LocationSearchBinding locationSearchBinding = null;
    @Nullable
    private Context context = null;
    @Nullable
    private ArrayList<LocationModal> modalArrayList = null;
    @Nullable
    private LocationAdapter locationAdapter = null;
    @Nullable
    private MyProgressDialog progressDialog = null;
    private String patient_id = "";
    private String api_type = "";
    private static final String TAG = LocationActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization context
        context = this;

        locationSearchBinding = DataBindingUtil.setContentView(this, R.layout.location_search);

        patient_id = getIntent().getStringExtra(S.patient_details);
        api_type = getIntent().getStringExtra(S.type);

        modalArrayList = new ArrayList<>();


        locationSearchBinding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(@NonNull CharSequence charSequence, int i, int i1, int i2) {
                try {
                    //locationAdapter.filter(charSequence.toString());
                    try {

                        if (!api_type.equals(S.ModeOA))
                            getSuggestion(charSequence.toString());

                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }

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
                                if (!api_type.equals(S.ModeOA) && !api_type.equals(S.Position))
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
        // setsearchBarHint(api_type);
        setOnClickListener();

        callSuggestion();

        // progressDialog =  Util.showPrograsssDialog(context);
        // DataManager.getHospitalLocation();
    }

    private void callSuggestion() {

        if (api_type.equals(S.ModeOA)) {

            LocationModal locationModal = new LocationModal();
            locationModal.setId("");
            locationModal.setOffice("");
            locationModal.setLname("");
            locationModal.setFname("");
            locationModal.setRef_code("");
            locationModal.setTitle(S.general);
            locationModal.setDiagnosis_code_type("");

            modalArrayList.add(locationModal);

            LocationModal locationModal1 = new LocationModal();
            locationModal1.setId("");
            locationModal1.setOffice("");
            locationModal1.setLname("");
            locationModal1.setFname("");
            locationModal1.setRef_code("");
            locationModal1.setTitle(S.regional);
            locationModal1.setDiagnosis_code_type("");

            modalArrayList.add(locationModal1);

            LocationModal locationModal2 = new LocationModal();
            locationModal2.setId("");
            locationModal2.setOffice("");
            locationModal2.setLname("");
            locationModal2.setFname("");
            locationModal2.setRef_code("");
            locationModal2.setTitle(S.ivas_mac);
            locationModal2.setDiagnosis_code_type("");

            modalArrayList.add(locationModal2);

            intiAdapter();
           /* try {
                getSuggestionModeOfanesthesia();
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        } else if (api_type.equals(S.Position)) {
            //for (int i = 0; i < jsonArray.length(); i++) {

            LocationModal locationModal = new LocationModal();
            locationModal.setId("");
            locationModal.setOffice("");
            locationModal.setLname("");
            locationModal.setFname("");
            locationModal.setRef_code("");
            locationModal.setTitle(S.supine);
            locationModal.setDiagnosis_code_type("");

            modalArrayList.add(locationModal);

            LocationModal locationModal1 = new LocationModal();
            locationModal1.setId("");
            locationModal1.setOffice("");
            locationModal1.setLname("");
            locationModal1.setFname("");
            locationModal1.setRef_code("");
            locationModal1.setTitle(S.prone);
            locationModal1.setDiagnosis_code_type("");

            modalArrayList.add(locationModal1);

            LocationModal locationModal2 = new LocationModal();
            locationModal2.setId("");
            locationModal2.setOffice("");
            locationModal2.setLname("");
            locationModal2.setFname("");
            locationModal2.setRef_code("");
            locationModal2.setTitle(S.lithotomy);
            locationModal2.setDiagnosis_code_type("");

            modalArrayList.add(locationModal2);

            LocationModal locationModa3 = new LocationModal();
            locationModa3.setId("");
            locationModa3.setOffice("");
            locationModa3.setLname("");
            locationModa3.setFname("");
            locationModa3.setRef_code("");
            locationModa3.setTitle(S.lateral);
            locationModa3.setDiagnosis_code_type("");

            modalArrayList.add(locationModa3);

            LocationModal locationModa4 = new LocationModal();
            locationModa4.setId("");
            locationModa4.setOffice("");
            locationModa4.setLname("");
            locationModa4.setFname("");
            locationModa4.setRef_code("");
            locationModa4.setTitle(S.jack_knife);
            locationModa4.setDiagnosis_code_type("");

            modalArrayList.add(locationModa4);

            LocationModal locationModa5 = new LocationModal();
            locationModa5.setId("");
            locationModa5.setOffice("");
            locationModa5.setLname("");
            locationModa5.setFname("");
            locationModa5.setRef_code("");
            locationModa5.setTitle(S.sitting);
            locationModa5.setDiagnosis_code_type("");

            modalArrayList.add(locationModa5);

            intiAdapter();
            // }
        }
    }

    private void getSuggestion(String s) throws JSONException {
        if (api_type.equalsIgnoreCase(S.provider))
            api_type = S.anesthesiologist;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(S.api_record_id, patient_id);
        jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
        jsonObject.put(S.api_listing_type, api_type);
        jsonObject.put(S.api_ref_code, s);

        locationSearchBinding.pbIcd1.setVisibility(View.VISIBLE);

        DataManager.allList(ConvertJsonToMap.jsonToMap(jsonObject));
    }

    private void getSuggestionModeOfanesthesia() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(S.api_record_id, patient_id);

        DataManager.modeOfAnesthesia(ConvertJsonToMap.jsonToMap(jsonObject));
    }

    private void setOnClickListener() {
        locationSearchBinding.toggleIcon.setOnClickListener(this);
    }


    private void intiAdapter() {
        locationAdapter = new LocationAdapter(modalArrayList, this, api_type);
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
        }
    }

    public void filterByName(String name, String id) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(S.start_activity_result, name);
        returnIntent.putExtra(S.patient_id, id);
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

                        intiAdapter();
                    } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_mode_anesthesia)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(S.data);
                        modalArrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Iterator<String> iterator = jsonObject1.keys();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                LocationModal locationModal = new LocationModal();
                                locationModal.setId("");
                                locationModal.setOffice("");
                                locationModal.setLname("");
                                locationModal.setFname("");
                                locationModal.setRef_code("");
                                locationModal.setTitle(jsonObject1.getString(key));
                                locationModal.setDiagnosis_code_type("");

                                modalArrayList.add(locationModal);

                            }
                        }
                        intiAdapter();
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
        Log.e(TAG, event.getMessage());
    }
}

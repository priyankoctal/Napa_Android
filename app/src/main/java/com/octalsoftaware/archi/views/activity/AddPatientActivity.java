package com.octalsoftaware.archi.views.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.data.DataManager;
import com.octalsoftaware.archi.data.events.ErrorEvent;
import com.octalsoftaware.archi.data.events.SucessEvent;
import com.octalsoftaware.archi.databinding.AddPatientBinding;
import com.octalsoftaware.archi.models.HospitalModal;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.Util;
import com.octalsoftaware.archi.utils.Validations;
import com.octalsoftaware.archi.utils.constants.S;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.octalsoftaware.archi.utils.Util.showDatePicker;
import static com.octalsoftaware.archi.utils.Util.showTimePicker;

/**
 * Created by anandj on 5/8/2017.
 */

public class AddPatientActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    @Nullable
    Context context = null;
    @Nullable
    private MyProgressDialog progressDialog = null;
    private String TAG = AddPatientActivity.class.getSimpleName();
    @Nullable
    private AddPatientBinding mBinding = null;
    @Nullable
    private ArrayList<HospitalModal> hospitalModalArrayList = null;
    @NonNull
    private String select_gender = "";
    @Nullable
    private HospitalModal select_location_detatils = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization context
        context = this;


        mBinding = DataBindingUtil.setContentView(this, R.layout.add_patient);

        // set Title
        mBinding.mainToolbar.toolbarheading.setText(getString(R.string.add_patient));

        mBinding.mainToolbar.imgEdit.setVisibility(View.GONE);


        // set on clicklistener
        setOnClickListener();

        callLocationApi();

    }

    private void callLocationApi() {
        progressDialog = Util.showPrograsssDialog(context);
        DataManager.getHospitalLocation();
    }

    private void setOnClickListener() {
        mBinding.mainToolbar.toggleIcon.setOnClickListener(this);
        mBinding.edtDob.setOnClickListener(this);
        mBinding.btnSearch.setOnClickListener(this);
        mBinding.spinnerGender.setOnItemSelectedListener(this);
        mBinding.spinnerLocation.setOnItemSelectedListener(this);
        mBinding.edtStartDate.setOnClickListener(this);
        mBinding.edtStartTime.setOnClickListener(this);
        mBinding.edtEndDate.setOnClickListener(this);
        mBinding.edtEndTime.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.toggle_icon:
                finish();
                break;
            case R.id.edt_dob:
                hideKeyboard();
                showDatePicker(mBinding.edtDob, context, "", "yes");
                break;
            case R.id.btn_search:
                addPatient();
                break;
            case R.id.edt_start_date:
                mBinding.edtEndDate.setText("");
                hideKeyboard();
                showDatePicker(mBinding.edtStartDate, context, "", "");
                break;
            case R.id.edt_start_time:
                hideKeyboard();
                mBinding.edtEndTime.setText("");
                showTimePicker(mBinding.edtStartTime, context, "", "", "", "");
                break;
            case R.id.edt_end_date:
                if (mBinding.edtStartDate.getText().toString().isEmpty())
                    Util.showToast(context, getString(R.string.first_enter_start_time));
                else
                    showDatePicker(mBinding.edtEndDate, context, mBinding.edtStartDate.getText().toString(), "");
                break;
            case R.id.edt_end_time:
                if (!mBinding.edtStartTime.getText().toString().isEmpty()) {
                    if (mBinding.edtStartDate.getText().toString().equals(mBinding.edtEndDate.getText().toString())) {
                        String[] arr = Util.getHourOfTime(mBinding.edtStartTime.getText().toString());
                        showTimePicker(mBinding.edtEndTime, context, arr[0], arr[1], "", "");
                    } else
                        showTimePicker(mBinding.edtEndTime, context, "", "", "", "");
                } else
                    Util.showToast(context, getString(R.string.first_enter_start_time));
                break;


        }
    }

    private void iniSpinner() {
        ArrayAdapter<HospitalModal> spinner_adpater = new ArrayAdapter<HospitalModal>(getApplicationContext(), R.layout.spinner_item, hospitalModalArrayList);
        mBinding.spinnerLocation.setAdapter(spinner_adpater);

        String[] gender = new String[]{"Select Gender", getString(R.string.male), getString(R.string.female)};
        ArrayAdapter<String> gender_adapter = new ArrayAdapter<String>(context, R.layout.spinner_item, gender);
        mBinding.spinnerGender.setAdapter(gender_adapter);
    }

    private void addPatient() {
        hideKeyboard();
        try {
            //   {"user_id":"251","location_id":"1","state_id":"4","first_name":"raj","last_name":"sharma","dob":"1991-01-21","gender":"M","start_date":"2017-04-15"}
            String first_name = mBinding.edtFirstName.getText().toString().trim();
            String last_name = mBinding.edtLastName.getText().toString().trim();
            String dob = mBinding.edtDob.getText().toString();
            String start_date = mBinding.edtStartDate.getText().toString();
            String end_date = mBinding.edtEndDate.getText().toString();
            String start_time = mBinding.edtStartTime.getText().toString();
            String end_time = mBinding.edtEndTime.getText().toString();
            if (!Validations.addPatientValidation(mBinding.edtFirstName, mBinding.edtLastName, mBinding.edtDob, mBinding.edtStartDate, mBinding.edtEndDate, mBinding.edtStartTime, mBinding.edtEndTime, select_gender, select_location_detatils, context)) {
                return;
            }
            //precaution for double click
            mBinding.btnSearch.setEnabled(false);

            progressDialog = Util.showPrograsssDialog(context);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, S.user_id));
            jsonObject.put(S.api_location_id, select_location_detatils.getId());
            jsonObject.put(S.api_state_id, select_location_detatils.getState_id());
            jsonObject.put(S.api_fname, first_name);
            jsonObject.put(S.api_lname, last_name);
            jsonObject.put(S.api_dob, dob);
            jsonObject.put(S.api_gender, select_gender);
            jsonObject.put(S.api_start_date, start_date);
            jsonObject.put(S.api_end_date, end_date);
            jsonObject.put(S.api_start_time, start_time);
            jsonObject.put(S.api_end_time, end_time);


            DataManager.addPatient(ConvertJsonToMap.jsonToMap(jsonObject));
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
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

    @Subscribe
    public void onSuccess(@NonNull SucessEvent event) {
        Util.dismissPrograssDialog(progressDialog);
        mBinding.btnSearch.setEnabled(true);
        String result = event.getResponce();
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result).getJSONObject(S.response);
                if (jsonObject.getBoolean(S.status)) {
                    if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_add_patient)) {
                        Util.showToast(context, jsonObject.getString(S.message));
                        finish();
                    } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_locations)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(S.data);
                        hospitalModalArrayList = new ArrayList<>();
                        // add first location
                        addFirstHospital();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            HospitalModal hospitalModal = new HospitalModal();
                            hospitalModal.setId(jsonObject1.getString(S.api_id));
                            hospitalModal.setName(jsonObject1.getString(S.api_name));
                            hospitalModal.setCode_id(jsonObject1.getString(S.api_code_id));
                            hospitalModal.setStatus(jsonObject1.getString(S.api_status));
                            hospitalModal.setState_id(jsonObject1.getString(S.api_state_id));

                            hospitalModalArrayList.add(hospitalModal);
                        }
                        iniSpinner();
                    }
                } else
                    Util.showToast(context, jsonObject.getString(S.message));

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    @Subscribe
    public void onFailed(@NonNull ErrorEvent event) {
        Util.dismissPrograssDialog(progressDialog);
        Log.e(TAG, event.getMessage());
    }

    private void addFirstHospital() {
        HospitalModal hospitalModal = new HospitalModal();
        hospitalModal.setId("");
        hospitalModal.setName("Select Location");
        hospitalModal.setCode_id("");
        hospitalModal.setStatus("");
        hospitalModal.setState_id("");

        hospitalModalArrayList.add(hospitalModal);
    }

    @Override
    public void onItemSelected(@NonNull AdapterView<?> parent, View view, int position, long id) {
        hideKeyboard();
        switch (parent.getId()) {
            case R.id.spinner_location:
                //   Toast.makeText(context, "location", Toast.LENGTH_SHORT).show();
                if (position != 0) {
                    select_location_detatils = hospitalModalArrayList.get(position);
                    // Toast.makeText(context, hospitalModalArrayList.get(position).getName()+" location", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.spinner_gender:
                if (position == 1)
                    select_gender = getString(R.string.male);
                else if (position == 2)
                    select_gender = getString(R.string.female);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

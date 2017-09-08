package com.octalsoftaware.archi.views.activity.chargeinformation;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.data.DataManager;
import com.octalsoftaware.archi.data.events.ErrorEvent;
import com.octalsoftaware.archi.data.events.SucessEvent;
import com.octalsoftaware.archi.databinding.InvasiveLinesBinding;
import com.octalsoftaware.archi.models.AdvancedQIModal;
import com.octalsoftaware.archi.realm.Database;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.Util;
import com.octalsoftaware.archi.utils.constants.I;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.activity.BaseActivity;
import com.octalsoftaware.archi.views.activity.LocationActivity;
import com.octalsoftaware.archi.utils.CustomArrayList;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.octalsoftaware.archi.utils.Util.setTypefaceRegular;
import static com.octalsoftaware.archi.utils.Util.showNoteDialog;
import static com.octalsoftaware.archi.utils.Util.showTimePicker;
import static com.octalsoftaware.archi.utils.constants.S.user_id;

/**
 * Created by anandj on 4/18/2017.
 */

public class InvasiveLinesActivity extends BaseActivity implements View.OnClickListener {

    @Nullable
    private InvasiveLinesBinding mBinding = null;
    @Nullable
    private Context context = null;
    @Nullable
    private MyProgressDialog progressDialog = null;
    private String note_str = "";
    private String patient_id = "";
    private String patient_name = "";
    private String provider_name = "";
    public static String TAG = InvasiveLinesActivity.class.getSimpleName();
    @Nullable
    private JSONObject jsonObject = null;
    @NonNull
    private CustomArrayList<AdvancedQIModal> mInvaiAdvancedQIModals = new CustomArrayList<>();
    @Nullable
    private Database database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization context
        context = this;

        patient_id = getIntent().getStringExtra(S.patient_details);
        provider_name = getIntent().getStringExtra(S.anesthesiologist);

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.invasive_lines);

        // set Title
        mBinding.mainToolbar.toolbarheading.setText(getString(R.string.invasive_lines));

        patient_name = getIntent().getStringExtra(S.patient_name);

        database = new Database(context);
        // set patient name
        mBinding.txtPatientname.setText(patient_name);

        mBinding.txtDoctorname.setText(provider_name);

        mBinding.mainToolbar.imgEdit.setVisibility(View.GONE);

        // set font
        setTypeface();

        // onclick
        setOnClickListner();

        try {
            if (Util.isNetworkConnected(context)) {
                // get prefill Data
                getPreFillData();
            } else {
                // get prefill data from local database
                autofillInvasiveLine(database.getInvasiveLine(patient_id, S.api_charge_details));
                // showPrefillData(database.getChargeDetails(patient_id, S.api_save_qi, getString(R.string.charge_information)));
            }


        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }


    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.toggle_icon:
                onBackPressed();
                break;
            case R.id.img_edit:
                // show dialog note
                showNoteDialog(context, note_str, patient_id, S.invasivelinelist_status);
                break;
            case R.id.txt_statrtime:
                showTimePicker(mBinding.txtStatrtime, context, "", "", "", "");
                break;
            case R.id.txt_endtime:
                if (!mBinding.txtStatrtime.getText().toString().equals("")) {
                    String[] arr = Util.getHourOfTime(mBinding.txtStatrtime.getText().toString());
                    showTimePicker(mBinding.txtEndtime, context, arr[0], arr[1], "", "");
                } else
                    Util.showToast(context, getString(R.string.first_enter_start_time));

                break;
            case R.id.checkbox_separate:
                if (mBinding.checkboxSeparate.isChecked())
                    mBinding.checkboxSeparateSwan.setChecked(false);
                break;
            case R.id.checkbox_separate_swan:
                if (mBinding.checkboxSeparateSwan.isChecked())
                    mBinding.checkboxSeparate.setChecked(false);
                break;
            case R.id.rr_provider:
                Intent intent = new Intent(context, LocationActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.type, S.provider);
                startActivityForResult(intent, I.ANESTHESIOLOGIST);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getStringExtra(S.start_activity_result) != null) {
            if (requestCode == I.ANESTHESIOLOGIST)
                mBinding.txtDoctorname.setText(data.getStringExtra(S.start_activity_result));
        }
    }

    private void setTypeface() {
        mBinding.checkboxSystem.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxPrior.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxAfter.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxArterial.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxCvpMoreFive.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxCvpLessFive.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxUltrasoundguide.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxSeparate.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxSeparateSwan.setTypeface(setTypefaceRegular(context));
    }

    private void setOnClickListner() {
        mBinding.mainToolbar.toggleIcon.setOnClickListener(this);
        mBinding.mainToolbar.imgEdit.setOnClickListener(this);
        mBinding.txtStatrtime.setOnClickListener(this);
        mBinding.txtEndtime.setOnClickListener(this);
        mBinding.checkboxSeparate.setOnClickListener(this);
        mBinding.checkboxSeparateSwan.setOnClickListener(this);
        mBinding.rrProvider.setOnClickListener(this);
    }

    @Override
    public void initEvent() {

    }

    public void sendNote(@NonNull JSONObject jsonObject) throws JSONException {

        progressDialog = Util.showPrograsssDialog(context);
        DataManager.addNotes(ConvertJsonToMap.jsonToMap(jsonObject));
    }


    @Override
    protected void onResume() {
        super.onResume();
        event.register(this);

    }

    private void getPreFillData() throws JSONException {
        progressDialog = Util.showPrograsssDialog(context);
        DataManager.getInvasiveLine(ConvertJsonToMap.jsonToMap(Util.getDefaultJson(context, patient_id)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        event.unregister(this);
    }

    private void autofillInvasiveLine(@NonNull CustomArrayList<AdvancedQIModal> arrayList) {
        if (arrayList.contains(S.api_invasive_line_provider.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_invasive_line_provider.toLowerCase());
            mBinding.txtDoctorname.setText(arrayList.get(pos).getId());
        }
        if (arrayList.contains(S.api_invasive_start_time.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_invasive_start_time.toLowerCase());
            mBinding.txtStatrtime.setText(arrayList.get(pos).getId());
        }
        if (arrayList.contains(S.api_invasive_end_time.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_invasive_end_time.toLowerCase());
            mBinding.txtEndtime.setText(arrayList.get(pos).getId());
        }
        if (arrayList.contains(S.api_invasive_prep_area.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_invasive_prep_area.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxSystem.setChecked(true);
        }
        if (arrayList.contains(S.api_invasive_in_or_prior_to_induction.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_invasive_in_or_prior_to_induction.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxPrior.setChecked(true);
        }
        if (arrayList.contains(S.api_invasive_in_or_after_to_induction.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_invasive_in_or_after_to_induction.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxAfter.setChecked(true);
        }
        if (arrayList.contains(S.api_invasive_arterial_line.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_invasive_arterial_line.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxArterial.setChecked(true);
        }
        if (arrayList.contains(S.api_invasive_cvp_line_more_5.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_invasive_cvp_line_more_5.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxCvpMoreFive.setChecked(true);
        }
        if (arrayList.contains(S.api_invasive_cvp_line_less_5.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_invasive_cvp_line_less_5.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxCvpLessFive.setChecked(true);
        }
        if (arrayList.contains(S.api_invasive_swan_ganz_catheter.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_invasive_swan_ganz_catheter.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxSwanGanz.setChecked(true);
        }
        if (arrayList.contains(S.api_invasive_ultrasound_guided.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_invasive_ultrasound_guided.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxUltrasoundguide.setChecked(true);
        }
        if (arrayList.contains(S.api_invasive_double_stick.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_invasive_double_stick.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.two_separate_cvp_stick)))
                mBinding.checkboxSeparate.setChecked(true);
            else
                mBinding.checkboxSeparateSwan.setChecked(true);
        }
        //  if(arrayList)
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
        Util.dismissPrograssDialog(progressDialog);
        String result = event.getResponce();
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result).getJSONObject(S.response);
                if (jsonObject.getBoolean(S.status)) {
                    if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_get_notes)) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject(S.data);
                        note_str = jsonObject1.getString(S.api_notes);
                    } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_add_notes)) {
                        Util.showToast(context, jsonObject.getString(S.message));
                        JSONObject jsonObject1 = jsonObject.getJSONObject(S.data);
                        note_str = jsonObject1.getString(S.api_notes);
                    } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_save_invasive_charge)) {
                        Util.showToast(context, jsonObject.getString(S.message));
                        finish();
                    } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_charge_details)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(S.data);
                        mInvaiAdvancedQIModals.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            AdvancedQIModal invasiveModal = new AdvancedQIModal();
                            invasiveModal.setId(jsonObject1.getString(S.api_value));
                            invasiveModal.setName(jsonObject1.getString(S.api_charge_key));

                            mInvaiAdvancedQIModals.add(invasiveModal);
                        }
                        autofillInvasiveLine(mInvaiAdvancedQIModals);
                        if (database.getSavedSatus(patient_id, S.api_charge_details))
                            database.updateQiDetails(patient_id, jsonObject, S.api_charge_details);
                        else
                            database.savedQiDetails(patient_id, jsonObject, S.api_charge_details);
                    }


                }
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

    @Override
    public void onBackPressed() {
        if (Util.isNetworkConnected(context)) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put(S.api_record_id, patient_id);
                jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));

                JSONObject jsonObject1 = new JSONObject();
                if (!mBinding.txtDoctorname.getText().toString().equals(""))
                    jsonObject1.put(S.api_invasive_line_provider, mBinding.txtDoctorname.getText().toString());
                else
                    jsonObject1.put(S.api_invasive_line_provider, "");

                if (!mBinding.txtStatrtime.getText().toString().equals(""))
                    jsonObject1.put(S.api_invasive_start_time, mBinding.txtStatrtime.getText().toString());
                else
                    jsonObject1.put(S.api_invasive_start_time, "");
                if (!mBinding.txtEndtime.getText().toString().equals(""))
                    jsonObject1.put(S.api_invasive_end_time, mBinding.txtEndtime.getText().toString());
                else
                    jsonObject1.put(S.api_invasive_end_time, "");
                if (mBinding.checkboxSystem.isChecked())
                    jsonObject1.put(S.api_invasive_prep_area, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_invasive_prep_area, getString(R.string.no));
                if (mBinding.checkboxPrior.isChecked())
                    jsonObject1.put(S.api_invasive_in_or_prior_to_induction, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_invasive_in_or_prior_to_induction, getString(R.string.no));
                if (mBinding.checkboxAfter.isChecked())
                    jsonObject1.put(S.api_invasive_in_or_after_to_induction, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_invasive_in_or_after_to_induction, getString(R.string.no));
                if (mBinding.checkboxArterial.isChecked())
                    jsonObject1.put(S.api_invasive_arterial_line, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_invasive_arterial_line, getString(R.string.no));
                if (mBinding.checkboxCvpMoreFive.isChecked())
                    jsonObject1.put(S.api_invasive_cvp_line_more_5, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_invasive_cvp_line_more_5, getString(R.string.no));
                if (mBinding.checkboxCvpLessFive.isChecked())
                    jsonObject1.put(S.api_invasive_cvp_line_less_5, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_invasive_cvp_line_less_5, getString(R.string.no));
                if (mBinding.checkboxUltrasoundguide.isChecked())
                    jsonObject1.put(S.api_invasive_ultrasound_guided, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_invasive_ultrasound_guided, getString(R.string.no));
                if (mBinding.checkboxSeparate.isChecked())
                    jsonObject1.put(S.api_invasive_double_stick, mBinding.checkboxSeparate.getText().toString());
                else if (mBinding.checkboxSeparateSwan.isChecked())
                    jsonObject1.put(S.api_invasive_double_stick, mBinding.checkboxSeparateSwan.getText().toString());
                else
                    jsonObject1.put(S.api_invasive_double_stick, "");
                if (mBinding.checkboxSwanGanz.isChecked())
                    jsonObject1.put(S.api_invasive_swan_ganz_catheter, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_invasive_swan_ganz_catheter, getString(R.string.no));

                if (jsonObject1.length() != 0) {
                    jsonObject.put(S.api_charge_measures, jsonObject1);
                    Util.confirmDialog(context, getString(R.string.do_you_want_to_save), getString(R.string.yes), getString(R.string.no), "", 0);
                } else
                    finish();


            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else
            finish();

    }

    public void submitInvasiveLine() throws JSONException {
        if (Util.isNetworkConnected(context)) {
            if (jsonObject != null) {
                progressDialog = Util.showPrograsssDialog(context);
                DataManager.saveInvasiveCharge(ConvertJsonToMap.jsonToMap(jsonObject));
            }
        } else {
            Util.showToast(context, getString(R.string.no_internet_connection));
            finish();
        }
    }
}

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
import com.octalsoftaware.archi.databinding.CardiacAndTeeBinding;
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
 * Created by anandj on 4/25/2017.
 */

public class CardiacAndTEE extends BaseActivity implements View.OnClickListener {
    @Nullable
    CardiacAndTeeBinding mBinding = null;
    @Nullable
    private Context context = null;
    @Nullable
    private MyProgressDialog progressDialog = null;
    private String note_str = "";
    private String patient_id = "";
    private String patient_name = "";
    private String provider_name = "";
    @Nullable
    private JSONObject jsonObject = null;
    public static String TAG = CardiacAndTEE.class.getSimpleName();
    @Nullable
    private Database database;
    @NonNull
    private CustomArrayList<AdvancedQIModal> mInvaiAdvancedQIModals = new CustomArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization context
        context = this;

        patient_id = getIntent().getStringExtra(S.patient_details);
        provider_name = getIntent().getStringExtra(S.anesthesiologist);

        database = new Database(context);

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.cardiac_and_tee);

        // set Title
        mBinding.mainToolbar.toolbarheading.setText(getString(R.string.cardiac_tee));

        // set provider name
        mBinding.txtDoctorname.setText(provider_name);


        patient_name = getIntent().getStringExtra(S.patient_name);

        mBinding.mainToolbar.imgEdit.setVisibility(View.GONE);

        // set custom font
        settCustomFont();

        // on click
        setOnClickistner();

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

    private void getPreFillData() throws JSONException {
        progressDialog = Util.showPrograsssDialog(context);
        DataManager.getInvasiveLine(ConvertJsonToMap.jsonToMap(Util.getDefaultJson(context, patient_id)));
    }

    private void settCustomFont() {
        mBinding.checkboxSystem.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxPrior.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxAfter.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxOffPump.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxHypothermic.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxRedoCabg.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxPumpProcudure.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxTeeInsertion.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxTeeInterpretation.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxCongenital.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxDopperColorFlow.setTypeface(setTypefaceRegular(context));
    }

    private void setOnClickistner() {
        mBinding.llSystem.setOnClickListener(this);
        mBinding.llPrior.setOnClickListener(this);
        mBinding.llAfter.setOnClickListener(this);
        mBinding.llOffPump.setOnClickListener(this);
        mBinding.llHypothermic.setOnClickListener(this);
        mBinding.llRedoCabg.setOnClickListener(this);
        mBinding.llPumpProcudure.setOnClickListener(this);
        mBinding.llTeeInsertion.setOnClickListener(this);
        mBinding.llTeeInterpretation.setOnClickListener(this);
        mBinding.llCongenital.setOnClickListener(this);
        mBinding.llDopperColorFlow.setOnClickListener(this);
        mBinding.txtStatrtime.setOnClickListener(this);
        mBinding.txtEndtime.setOnClickListener(this);
        mBinding.mainToolbar.toggleIcon.setOnClickListener(this);
        mBinding.mainToolbar.imgEdit.setOnClickListener(this);
        mBinding.rrProvider.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.ll_dopper_color_flow:
                break;
            case R.id.ll_congenital:
                break;
            case R.id.ll_tee_interpretation:
                break;
            case R.id.ll_tee_insertion:
                break;
            case R.id.ll_pump_procudure:
                break;
            case R.id.ll_redo_cabg:
                break;
            case R.id.ll_hypothermic:
                break;
            case R.id.ll_off_pump:
                break;
            case R.id.ll_after:
                break;
            case R.id.ll_prior:
                break;
            case R.id.ll_system:
                break;
            case R.id.txt_statrtime:
                // show time picker
                showTimePicker(mBinding.txtStatrtime, context, "", "", "", "");
                break;
            case R.id.txt_endtime:
                if (!mBinding.txtStatrtime.getText().toString().trim().equals("")) {
                    String[] arr = Util.getHourOfTime(mBinding.txtStatrtime.getText().toString());
                    // show time picker
                    showTimePicker(mBinding.txtEndtime, context, arr[0], arr[1], "", "");
                } else
                    Util.showToast(context, getString(R.string.first_enter_start_time));
                break;
            case R.id.toggle_icon:
                onBackPressed();
                break;
            case R.id.img_edit:
                // show dialog note
                showNoteDialog(context, note_str, patient_id, S.cardic_status);
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

    @Override
    public void initEvent() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        event.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        event.unregister(this);
    }

    public void sendNote(@NonNull JSONObject jsonObject) throws JSONException {

        progressDialog = Util.showPrograsssDialog(context);
        DataManager.addNotes(ConvertJsonToMap.jsonToMap(jsonObject));
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            DataManager.getNotes(ConvertJsonToMap.jsonToMap(Util.getNoteParameter(MySharedPreferences.getPreferences(context, user_id), patient_id, S.cardic_status)));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
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

    private void autofillInvasiveLine(@NonNull CustomArrayList<AdvancedQIModal> arrayList) {
        if (arrayList.contains(S.api_cardiac_and_tee.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cardiac_and_tee.toLowerCase());
            mBinding.txtDoctorname.setText(arrayList.get(pos).getId());
        }
        if (arrayList.contains(S.api_cardiac_tee_start_time.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cardiac_tee_start_time.toLowerCase());
            mBinding.txtStatrtime.setText(arrayList.get(pos).getId());
        }
        if (arrayList.contains(S.api_cardiac_tee_end_time.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cardiac_tee_end_time.toLowerCase());
            mBinding.txtEndtime.setText(arrayList.get(pos).getId());
        }
        if (arrayList.contains(S.api_cardiac_tee_prep_area.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cardiac_tee_prep_area.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxSystem.setChecked(true);
        }
        if (arrayList.contains(S.api_cardiac_tee_in_or_prior_to_induction.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cardiac_tee_in_or_prior_to_induction.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxPrior.setChecked(true);
        }
        if (arrayList.contains(S.api_cardiac_tee_in_or_after_to_induction.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cardiac_tee_in_or_after_to_induction.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxAfter.setChecked(true);
        }
        if (arrayList.contains(S.api_cardiac_tee_off_pump.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cardiac_tee_off_pump.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxOffPump.setChecked(true);
        }
        if (arrayList.contains(S.api_cardiac_tee_hypothermic_circulatory.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cardiac_tee_hypothermic_circulatory.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxHypothermic.setChecked(true);
        }
        if (arrayList.contains(S.api_cardiac_tee_re_do_cabg.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cardiac_tee_re_do_cabg.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxRedoCabg.setChecked(true);
        }
        if (arrayList.contains(S.api_cardiac_tee_pump_procedure.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cardiac_tee_pump_procedure.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxPumpProcudure.setChecked(true);
        }
        if (arrayList.contains(S.api_cardiac_tee_tee_probe_insertion.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cardiac_tee_tee_probe_insertion.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxTeeInsertion.setChecked(true);
        }
        if (arrayList.contains(S.api_cardiac_tee_tee_interpretation.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cardiac_tee_tee_interpretation.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxTeeInterpretation.setChecked(true);
        }
        if (arrayList.contains(S.api_cardiac_tee_tee_congenital.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cardiac_tee_tee_congenital.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxCongenital.setChecked(true);
        }
        if (arrayList.contains(S.api_cardiac_tee_dopper_echo_color_flow.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cardiac_tee_dopper_echo_color_flow.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxDopperColorFlow.setChecked(true);
        }
        if (arrayList.contains(S.api_cartiac_tee_dopper_echo_pulse_wave.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_cartiac_tee_dopper_echo_pulse_wave.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxechoPulseFlow.setChecked(true);
        }

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
                    jsonObject1.put(S.api_cardiac_and_tee, mBinding.txtDoctorname.getText().toString());
                else
                    jsonObject1.put(S.api_cardiac_and_tee, "");

                if (!mBinding.txtStatrtime.getText().toString().equals(""))
                    jsonObject1.put(S.api_cardiac_tee_start_time, mBinding.txtStatrtime.getText().toString());
                else
                    jsonObject1.put(S.api_cardiac_tee_start_time, "");

                if (!mBinding.txtEndtime.getText().toString().equals(""))
                    jsonObject1.put(S.api_cardiac_tee_end_time, mBinding.txtEndtime.getText().toString());
                else
                    jsonObject1.put(S.api_cardiac_tee_end_time, "");

                if (mBinding.checkboxSystem.isChecked())
                    jsonObject1.put(S.api_cardiac_tee_prep_area, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_cardiac_tee_prep_area, getString(R.string.no));

                if (mBinding.checkboxPrior.isChecked())
                    jsonObject1.put(S.api_cardiac_tee_in_or_prior_to_induction, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_cardiac_tee_in_or_prior_to_induction, getString(R.string.no));

                if (mBinding.checkboxAfter.isChecked())
                    jsonObject1.put(S.api_cardiac_tee_in_or_after_to_induction, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_cardiac_tee_in_or_after_to_induction, getString(R.string.no));

                if (mBinding.checkboxOffPump.isChecked())
                    jsonObject1.put(S.api_cardiac_tee_off_pump, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_cardiac_tee_off_pump, getString(R.string.no));

                if (mBinding.checkboxHypothermic.isChecked())
                    jsonObject1.put(S.api_cardiac_tee_hypothermic_circulatory, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_cardiac_tee_hypothermic_circulatory, getString(R.string.no));

                if (mBinding.checkboxRedoCabg.isChecked())
                    jsonObject1.put(S.api_cardiac_tee_re_do_cabg, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_cardiac_tee_re_do_cabg, getString(R.string.no));

                if (mBinding.checkboxPumpProcudure.isChecked())
                    jsonObject1.put(S.api_cardiac_tee_pump_procedure, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_cardiac_tee_pump_procedure, getString(R.string.no));

                if (mBinding.checkboxTeeInsertion.isChecked())
                    jsonObject1.put(S.api_cardiac_tee_tee_probe_insertion, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_cardiac_tee_tee_probe_insertion, getString(R.string.no));

                if (mBinding.checkboxTeeInterpretation.isChecked())
                    jsonObject1.put(S.api_cardiac_tee_tee_interpretation, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_cardiac_tee_tee_interpretation, getString(R.string.no));

                if (mBinding.checkboxCongenital.isChecked())
                    jsonObject1.put(S.api_cardiac_tee_tee_congenital, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_cardiac_tee_tee_congenital, getString(R.string.no));

                if (mBinding.checkboxDopperColorFlow.isChecked())
                    jsonObject1.put(S.api_cardiac_tee_dopper_echo_color_flow, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_cardiac_tee_dopper_echo_color_flow, getString(R.string.no));

                if (mBinding.checkboxechoPulseFlow.isChecked())
                    jsonObject1.put(S.api_cartiac_tee_dopper_echo_pulse_wave, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_cartiac_tee_dopper_echo_pulse_wave, getString(R.string.no));

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

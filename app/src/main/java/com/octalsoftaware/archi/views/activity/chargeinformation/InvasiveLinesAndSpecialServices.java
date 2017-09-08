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
import com.octalsoftaware.archi.databinding.InvasiveLinesSpecialServicesBinding;
import com.octalsoftaware.archi.models.AdvancedQIModal;
import com.octalsoftaware.archi.realm.Database;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.Util;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.activity.BaseActivity;
import com.octalsoftaware.archi.utils.CustomArrayList;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.octalsoftaware.archi.utils.constants.S.user_id;

/**
 * Created by anandj on 4/18/2017.
 */

public class InvasiveLinesAndSpecialServices extends BaseActivity implements View.OnClickListener {
    @Nullable
    private Context context = null;
    @Nullable
    private InvasiveLinesSpecialServicesBinding mBinding = null;
    @Nullable
    private MyProgressDialog progressDialog = null;
    private String note_str = "";
    private String patient_id = "";
    private String patient_name = "";
    private String provider_name = "";
    public static String TAG = InvasiveLinesAndSpecialServices.class.getSimpleName();
    @NonNull
    private CustomArrayList<AdvancedQIModal> mInvaiAdvancedQIModals = new CustomArrayList<>();
    @Nullable
    private Database database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization context
        context = this;

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.invasive_lines_special_services);

        // set Title
        mBinding.mainToolbar.toolbarheading.setText(getString(R.string.invasivelineandspecialservice));
        mBinding.mainToolbar.toggleIconHome.setVisibility(View.GONE);

        patient_name = getIntent().getStringExtra(S.patient_name);

        provider_name = getIntent().getStringExtra(S.anesthesiologist);
        database = new Database(context);

        // set patient name
        mBinding.txtPatientname.setText(patient_name);


        // set on clickListener
        setOnClickListner();

        // set custom font
        setTyface();

        patient_id = getIntent().getStringExtra(S.patient_details);

        // get Previous notes
        getNote();

        try {
            if (Util.isNetworkConnected(context)) {

                // get prefill Data
                getPreFillData();
            } else {
                // get prefill data from local database
                autofillInvasiveLine(database.getQIDetails(patient_id, S.api_charge_details));
            }


        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }

    }

    private void getPreFillData() throws JSONException {
        progressDialog = Util.showPrograsssDialog(context);
        DataManager.getInvasiveLine(ConvertJsonToMap.jsonToMap(Util.getDefaultJson(context, patient_id)));
    }

    private void getNote() {
        try {
            DataManager.getNotes(ConvertJsonToMap.jsonToMap(Util.getNoteParameter(MySharedPreferences.getPreferences(context, user_id), patient_id, S.invasive_status)));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        event.register(this);


    }

    @Override
    protected void onPause() {
        super.onPause();
        event.unregister(this);
    }

    public void saveInvasiveCharge() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
        jsonObject.put(S.api_record_id, patient_id);

        JSONObject jsonObject1 = new JSONObject();
        if (mBinding.checkboxSystem.isChecked())
            jsonObject1.put(getString(R.string.one_lung_ventillation), getString(R.string.yes));
        else
            jsonObject1.put(getString(R.string.one_lung_ventillation), getString(R.string.no));
        if (mBinding.checkboxMedical.isChecked())
            jsonObject1.put(getString(R.string.controlled_hypotension), getString(R.string.yes));
        else
            jsonObject1.put(getString(R.string.controlled_hypotension), getString(R.string.no));
        if (mBinding.checkboxPatient.isChecked())
            jsonObject1.put(getString(R.string.special_catheter), getString(R.string.yes));
        else
            jsonObject1.put(getString(R.string.special_catheter), getString(R.string.no));
        if (Util.isNetworkConnected(context)) {
            progressDialog = Util.showPrograsssDialog(context);
            jsonObject.put(S.api_charge_measures, jsonObject1);
            DataManager.saveInvasiveCharge(ConvertJsonToMap.toMap(jsonObject));
        } else {
            Util.showToast(context, getString(R.string.no_internet_connection));
            if (!mBinding.rrInvasivelines.isEnabled()) {
                mBinding.rrInvasivelines.setEnabled(true);
                Intent intent = new Intent(context, InvasiveLinesActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.patient_name, patient_name);
                intent.putExtra(S.anesthesiologist, provider_name);
                startActivity(intent);
            } else if (!mBinding.rrCardiacTee.isEnabled()) {
                mBinding.rrCardiacTee.setEnabled(true);
                Intent intent = new Intent(context, CardiacAndTEE.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.patient_name, patient_name);
                intent.putExtra(S.anesthesiologist, provider_name);
                startActivity(intent);
            } else if (!mBinding.rrPostOpPainBlocks.isEnabled()) {
                mBinding.rrPostOpPainBlocks.setEnabled(true);
                Intent intent = new Intent(context, PostOpPlainBlocksActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.patient_name, patient_name);
                intent.putExtra(S.anesthesiologist, provider_name);
                startActivity(intent);
            } else
                finish();

        }
    }

    @Override
    public void onClick(@NonNull View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.toggle_icon:
                onBackPressed();
                break;
            case R.id.img_edit:
                // show dialog note
                Util.showNoteDialog(context, note_str, patient_id, S.invasive_status);
                break;
            case R.id.rr_invasivelines:
                try {
                    mBinding.rrInvasivelines.setEnabled(false);
                    saveInvasiveCharge();

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                /*intent = new Intent(context,InvasiveLinesActivity.class);
                intent.putExtra(S.patient_details,patient_id);
                intent.putExtra(S.patient_name, patient_name);
                intent.putExtra(S.anesthesiologist,provider_name);
                startActivity(intent);*/
                break;
            case R.id.rr_cardiac_tee:
                try {
                    mBinding.rrCardiacTee.setEnabled(false);
                    saveInvasiveCharge();

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
              /*  intent = new Intent(context, CardiacAndTEE.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.patient_name, patient_name);
                intent.putExtra(S.anesthesiologist, provider_name);
                startActivity(intent);*/
                break;
            case R.id.rr_post_op_pain_blocks:
                try {
                    mBinding.rrPostOpPainBlocks.setEnabled(false);
                    saveInvasiveCharge();

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
              /*  intent = new Intent(context, PostOpPlainBlocksActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.patient_name, patient_name);
                intent.putExtra(S.anesthesiologist, provider_name);
                startActivity(intent);*/
                break;
        }
    }

    private void setOnClickListner() {
        mBinding.mainToolbar.imgEdit.setOnClickListener(this);
        mBinding.mainToolbar.toggleIcon.setOnClickListener(this);
        mBinding.rrInvasivelines.setOnClickListener(this);
        mBinding.rrCardiacTee.setOnClickListener(this);
        mBinding.rrPostOpPainBlocks.setOnClickListener(this);
    }

    private void setTyface() {
        mBinding.checkboxSystem.setTypeface(Util.setTypefaceRegular(context));
        mBinding.checkboxMedical.setTypeface(Util.setTypefaceRegular(context));
        mBinding.checkboxPatient.setTypeface(Util.setTypefaceRegular(context));
    }

    @Override
    public void initEvent() {

    }

    public void sendNote(@NonNull JSONObject jsonObject) throws JSONException {

        progressDialog = Util.showPrograsssDialog(context);
        DataManager.addNotes(ConvertJsonToMap.jsonToMap(jsonObject));
    }

    private void autofillInvasiveLine(@NonNull CustomArrayList<AdvancedQIModal> arrayList) {
        if (arrayList.contains(mBinding.checkboxSystem.getText().toString().toLowerCase())) {
            int pos = arrayList.indexOf(mBinding.checkboxSystem.getText().toString().toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxSystem.setChecked(true);
        }
        if (arrayList.contains(mBinding.checkboxMedical.getText().toString().toLowerCase())) {
            int pos = arrayList.indexOf(mBinding.checkboxMedical.getText().toString().toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxMedical.setChecked(true);
        }
        if (arrayList.contains(mBinding.checkboxPatient.getText().toString().toLowerCase())) {
            int pos = arrayList.indexOf(mBinding.checkboxPatient.getText().toString().toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxPatient.setChecked(true);
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
                        if (!mBinding.rrInvasivelines.isEnabled()) {
                            mBinding.rrInvasivelines.setEnabled(true);
                            Intent intent = new Intent(context, InvasiveLinesActivity.class);
                            intent.putExtra(S.patient_details, patient_id);
                            intent.putExtra(S.patient_name, patient_name);
                            intent.putExtra(S.anesthesiologist, provider_name);
                            startActivity(intent);
                        } else if (!mBinding.rrCardiacTee.isEnabled()) {
                            mBinding.rrCardiacTee.setEnabled(true);
                            Intent intent = new Intent(context, CardiacAndTEE.class);
                            intent.putExtra(S.patient_details, patient_id);
                            intent.putExtra(S.patient_name, patient_name);
                            intent.putExtra(S.anesthesiologist, provider_name);
                            startActivity(intent);
                        } else if (!mBinding.rrPostOpPainBlocks.isEnabled()) {
                            mBinding.rrPostOpPainBlocks.setEnabled(true);
                            Intent intent = new Intent(context, PostOpPlainBlocksActivity.class);
                            intent.putExtra(S.patient_details, patient_id);
                            intent.putExtra(S.patient_name, patient_name);
                            intent.putExtra(S.anesthesiologist, provider_name);
                            startActivity(intent);
                        } else
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
        if (Util.isNetworkConnected(context))
            Util.confirmDialog(context, getString(R.string.do_you_want_to_save), getString(R.string.yes), getString(R.string.no), "", 0);
        else
            finish();
    }
}

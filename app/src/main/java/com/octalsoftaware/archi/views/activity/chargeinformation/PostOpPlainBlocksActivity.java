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
import com.octalsoftaware.archi.databinding.PostOpPainBlocksBinding;
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

public class PostOpPlainBlocksActivity extends BaseActivity implements View.OnClickListener {
    @Nullable
    PostOpPainBlocksBinding mBinding = null;
    @Nullable
    Context context = null;
    @Nullable
    private MyProgressDialog progressDialog = null;
    private String note_str = "";
    private String patient_id = "";
    private String provider_name = "";
    @Nullable
    private JSONObject jsonObject = null;
    public static String TAG = PostOpPlainBlocksActivity.class.getSimpleName();
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

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.post_op_pain_blocks);

        database = new Database(context);

        // set Title
        mBinding.mainToolbar.toolbarheading.setText(getString(R.string.post_op_pain));

        // set provider name
        mBinding.txtDoctorname.setText(provider_name);

        mBinding.mainToolbar.imgEdit.setVisibility(View.GONE);

        // set custom font
        settCustomFont();

        // set on OnClickListner
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

    private void getPreFillData() throws JSONException {
        progressDialog = Util.showPrograsssDialog(context);
        DataManager.getInvasiveLine(ConvertJsonToMap.jsonToMap(Util.getDefaultJson(context, patient_id)));
    }

    private void setOnClickListner() {
        mBinding.txtStatrtime.setOnClickListener(this);
        mBinding.txtEndtime.setOnClickListener(this);
        mBinding.rrChestAbdomen.setOnClickListener(this);
        mBinding.rrNeuraxial.setOnClickListener(this);
        mBinding.rrUpperExtremity.setOnClickListener(this);
        mBinding.rrLowerExtremity.setOnClickListener(this);
        mBinding.mainToolbar.imgEdit.setOnClickListener(this);
        mBinding.mainToolbar.toggleIcon.setOnClickListener(this);
        mBinding.rrProvider.setOnClickListener(this);
    }

    private void settCustomFont() {
        mBinding.checkboxSystem.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxPrior.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxAfter.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxUnilateral.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxUntrasound.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxBilateral.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxContinuous.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxSingleShot.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxImageSaved.setTypeface(setTypefaceRegular(context));
    }

    @Override
    public void onClick(@NonNull View view) {
        Intent intent = new Intent(context, LowerExtremityActivity.class);
        intent.putExtra(S.patient_details, patient_id);
        switch (view.getId()) {
            case R.id.txt_endtime:
                if (!mBinding.txtStatrtime.getText().toString().equals("")) {
                    String[] arr = Util.getHourOfTime(mBinding.txtStatrtime.getText().toString());
                    // show time picker
                    showTimePicker(mBinding.txtEndtime, context, arr[0], arr[1], "", "");
                } else
                    Util.showToast(context, getString(R.string.first_enter_start_time));


                break;
            case R.id.txt_statrtime:
                mBinding.txtEndtime.setText("");
                // show time picker
                showTimePicker(mBinding.txtStatrtime, context, "", "", "", "");
                break;
            case R.id.rr_lower_extremity:
               /* intent.putExtra(S.lower_array_list, Util.prepareLowerExtremityData());
                intent.putExtra(S.status, I.LOWER_EXTREMITY);*/
                intent = new Intent(context, LowerExtremityActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                startActivity(intent);
                break;
            case R.id.rr_upper_extremity:
                intent = new Intent(context, UpperExtremityActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                startActivity(intent);
                break;
            case R.id.rr_neuraxial:
                intent = new Intent(context, NEURAXIAL.class);
                intent.putExtra(S.patient_details, patient_id);
                startActivity(intent);
                break;
            case R.id.rr_chest_abdomen:
                intent = new Intent(context, ChestAndAbdomenActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                startActivity(intent);
                break;
            case R.id.toggle_icon:
                onBackPressed();
                break;
            case R.id.img_edit:
                // show dialog note
                showNoteDialog(context, note_str, patient_id, S.post_op_status);
                break;
            case R.id.rr_provider:
                intent = new Intent(context, LocationActivity.class);
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

    }


    @Override
    protected void onPause() {
        super.onPause();
        event.unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void sendNote(@NonNull JSONObject jsonObject) throws JSONException {

        progressDialog = Util.showPrograsssDialog(context);
        DataManager.addNotes(ConvertJsonToMap.jsonToMap(jsonObject));
    }

    @Override
    protected void onResume() {
        super.onResume();
        event.register(this);
        try {
            DataManager.getNotes(ConvertJsonToMap.jsonToMap(Util.getNoteParameter(MySharedPreferences.getPreferences(context, user_id), patient_id, S.post_op_status)));
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

    @Override
    public void onBackPressed() {
        if (Util.isNetworkConnected(context)) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put(S.api_record_id, patient_id);
                jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));

                JSONObject jsonObject1 = new JSONObject();
                if (!mBinding.txtDoctorname.getText().toString().equals(""))
                    jsonObject1.put(S.api_post_op_pain_block, mBinding.txtDoctorname.getText().toString());
                else
                    jsonObject1.put(S.api_post_op_pain_block, "");

                if (!mBinding.txtStatrtime.getText().toString().equals(""))
                    jsonObject1.put(S.api_postop_start_time, mBinding.txtStatrtime.getText().toString());
                else
                    jsonObject1.put(S.api_postop_start_time, "");

                if (!mBinding.txtEndtime.getText().toString().equals(""))
                    jsonObject1.put(S.api_postop_end_time, mBinding.txtEndtime.getText().toString());
                else
                    jsonObject1.put(S.api_postop_end_time, "");

                if (mBinding.checkboxSystem.isChecked())
                    jsonObject1.put(S.api_postop_prep_area, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_postop_prep_area, getString(R.string.no));

                if (mBinding.checkboxPrior.isChecked())
                    jsonObject1.put(S.api_postop_in_or_prior_to_induction, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_postop_in_or_prior_to_induction, getString(R.string.no));

                if (mBinding.checkboxAfter.isChecked())
                    jsonObject1.put(S.api_postop_in_or_after_to_induction, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_postop_in_or_after_to_induction, getString(R.string.no));

                if (mBinding.checkboxUnilateral.isChecked())
                    jsonObject1.put(S.api_postop_unilateral, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_postop_unilateral, getString(R.string.no));

                if (mBinding.checkboxBilateral.isChecked())
                    jsonObject1.put(S.api_postop_bilateral, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_postop_bilateral, getString(R.string.no));

                if (mBinding.checkboxUntrasound.isChecked())
                    jsonObject1.put(S.api_postop_ultrasound_guided, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_postop_ultrasound_guided, getString(R.string.no));
                if (mBinding.checkboxContinuous.isChecked())
                    jsonObject1.put(S.api_postop_continuous_catheter, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_postop_continuous_catheter, getString(R.string.no));

                if (mBinding.checkboxSingleShot.isChecked())
                    jsonObject1.put(S.api_postop_single_shot, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_postop_single_shot, getString(R.string.no));

                if (mBinding.checkboxImageSaved.isChecked())
                    jsonObject1.put(S.api_postop_image_saved, getString(R.string.yes));
                else
                    jsonObject1.put(S.api_postop_image_saved, getString(R.string.no));

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

    private void autofillInvasiveLine(@NonNull CustomArrayList<AdvancedQIModal> arrayList) {
        if (arrayList.contains(S.api_post_op_pain_block.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_post_op_pain_block.toLowerCase());
            mBinding.txtDoctorname.setText(arrayList.get(pos).getId());
        }
        if (arrayList.contains(S.api_postop_start_time.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_postop_start_time.toLowerCase());
            mBinding.txtStatrtime.setText(arrayList.get(pos).getId());
        }
        if (arrayList.contains(S.api_postop_end_time.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_postop_end_time.toLowerCase());
            mBinding.txtEndtime.setText(arrayList.get(pos).getId());
        }
        if (arrayList.contains(S.api_postop_prep_area.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_postop_prep_area.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxSystem.setChecked(true);
        }
        if (arrayList.contains(S.api_postop_in_or_prior_to_induction.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_postop_in_or_prior_to_induction.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxPrior.setChecked(true);
        }
        if (arrayList.contains(S.api_postop_in_or_after_to_induction.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_postop_in_or_after_to_induction.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxAfter.setChecked(true);
        }
        if (arrayList.contains(S.api_postop_unilateral.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_postop_unilateral.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxUnilateral.setChecked(true);
        }
        if (arrayList.contains(S.api_postop_bilateral.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_postop_bilateral.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxBilateral.setChecked(true);
        }
        if (arrayList.contains(S.api_postop_ultrasound_guided.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_postop_ultrasound_guided.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxUntrasound.setChecked(true);
        }
        if (arrayList.contains(S.api_postop_continuous_catheter.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_postop_continuous_catheter.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxContinuous.setChecked(true);
        }
        if (arrayList.contains(S.api_postop_single_shot.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_postop_single_shot.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxSingleShot.setChecked(true);
        }
        if (arrayList.contains(S.api_postop_image_saved.toLowerCase())) {
            int pos = arrayList.indexOf(S.api_postop_image_saved.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxImageSaved.setChecked(true);
        }
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

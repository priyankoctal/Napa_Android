package com.octalsoftaware.archi.views.activity.chargeinformation;

import android.content.Context;
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
import com.octalsoftaware.archi.databinding.ChestAndAbdomenBinding;
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
 * Created by anandj on 5/26/2017.
 */

public class ChestAndAbdomenActivity extends BaseActivity implements View.OnClickListener {

    private String patient_id = "";
    @Nullable
    ChestAndAbdomenBinding mBinding = null;
    @Nullable
    Context context = null;
    @Nullable
    private JSONObject jsonmain = null;
    @Nullable
    private MyProgressDialog progressDialog = null;
    @Nullable
    public Database database;
    @NonNull
    private CustomArrayList<AdvancedQIModal> mInvaiAdvancedQIModals = new CustomArrayList<>();
    public static String TAG = LowerExtremityActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        patient_id = getIntent().getStringExtra(S.patient_details);
        // initialization context
        context = this;

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.chest_and_abdomen);

        database = new Database(context);

        // set Title
        mBinding.mainToolbar.toolbarheading.setText(getString(R.string.chest_and_abdomen));

        mBinding.mainToolbar.imgEdit.setVisibility(View.GONE);

        // set on Click Listener
        setOnCLickLisetner();
    }

    private void setOnCLickLisetner() {
        mBinding.mainToolbar.toggleIcon.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        event.register(this);
        try {
            if (Util.isNetworkConnected(context)) {
                progressDialog = Util.showPrograsssDialog(context);
                // get prefill Data from server
                getPrefillData();
            } else {
                // get prefill data from local database
                autofillInvasiveLine(database.getInvasiveLine(patient_id, S.api_charge_details));
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void getPrefillData() throws JSONException {
        DataManager.getInvasiveLine(ConvertJsonToMap.jsonToMap(Util.getDefaultJson(context, patient_id)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        event.unregister(this);
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.toggle_icon:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (Util.isNetworkConnected(context)) {
            try {
                jsonmain = new JSONObject();
                jsonmain.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
                jsonmain.put(S.api_record_id, patient_id);
                JSONObject jsonObject = new JSONObject();
                if (mBinding.checkboxFemoral.isChecked())
                    jsonObject.put(S.paravertebral_thoracic_single_level, getString(R.string.yes));
                else
                    jsonObject.put(S.paravertebral_thoracic_single_level, getString(R.string.no));
                if (mBinding.checkboxAdductor.isChecked())
                    jsonObject.put(S.paravertebral_thoracic_additional_level, getString(R.string.yes));
                else
                    jsonObject.put(S.paravertebral_thoracic_additional_level, getString(R.string.no));
                if (mBinding.checkboxPlexus.isChecked())
                    jsonObject.put(S.interscalene_block, getString(R.string.yes));
                else
                    jsonObject.put(S.interscalene_block, getString(R.string.no));

                if (mBinding.checkboxSciatic.isChecked())
                    jsonObject.put(S.interscalene_block_multiple, getString(R.string.yes));
                else
                    jsonObject.put(S.interscalene_block_multiple, getString(R.string.no));

                if (mBinding.checkboxCandel.isChecked())
                    jsonObject.put(S.tap_or_rectus_sheath_block, getString(R.string.yes));
                else
                    jsonObject.put(S.tap_or_rectus_sheath_block, getString(R.string.no));

                jsonmain.put(S.api_charge_measures, jsonObject);
                Util.confirmDialog(context, getString(R.string.do_you_want_to_save), getString(R.string.yes), getString(R.string.no), "", 0);


            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else
            finish();

    }

    private void autofillInvasiveLine(@NonNull CustomArrayList<AdvancedQIModal> arrayList) {
        if (arrayList.contains(S.paravertebral_thoracic_single_level.toLowerCase())) {
            int pos = arrayList.indexOf(S.paravertebral_thoracic_single_level.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxFemoral.setChecked(true);
        }
        if (arrayList.contains(S.paravertebral_thoracic_additional_level.toLowerCase())) {
            int pos = arrayList.indexOf(S.paravertebral_thoracic_additional_level.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxAdductor.setChecked(true);
        }
        if (arrayList.contains(S.interscalene_block.toLowerCase())) {
            int pos = arrayList.indexOf(S.interscalene_block.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxPlexus.setChecked(true);
        }
        if (arrayList.contains(S.interscalene_block_multiple.toLowerCase())) {
            int pos = arrayList.indexOf(S.interscalene_block_multiple.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxSciatic.setChecked(true);
        }
        if (arrayList.contains(S.tap_or_rectus_sheath_block.toLowerCase())) {
            int pos = arrayList.indexOf(S.tap_or_rectus_sheath_block.toLowerCase());
            if (arrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.yes)))
                mBinding.checkboxCandel.setChecked(true);
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
                    if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_save_invasive_charge)) {
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

    public void submitInvasiveLine() throws JSONException {
        if (Util.isNetworkConnected(context)) {
            if (jsonmain != null) {
                progressDialog = Util.showPrograsssDialog(context);
                DataManager.saveInvasiveCharge(ConvertJsonToMap.jsonToMap(jsonmain));
            }
        } else {
            Util.showToast(context, getString(R.string.no_internet_connection));
            finish();
        }

    }

}

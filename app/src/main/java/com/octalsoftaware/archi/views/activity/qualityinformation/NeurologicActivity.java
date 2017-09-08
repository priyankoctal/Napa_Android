package com.octalsoftaware.archi.views.activity.qualityinformation;

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
import com.octalsoftaware.archi.databinding.NeurologicBinding;
import com.octalsoftaware.archi.models.AdvancedQIModal;
import com.octalsoftaware.archi.realm.Database;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.CustomArrayList;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.Util;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.activity.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.octalsoftaware.archi.utils.constants.S.user_id;

/**
 * Created by anandj on 4/26/2017.
 */

public class NeurologicActivity extends BaseActivity implements View.OnClickListener {

    @Nullable
    private NeurologicBinding mBinding = null;
    @Nullable
    private Context context = null;
    @Nullable
    private MyProgressDialog progressDialog = null;
    public static String TAG = NeurologicActivity.class.getSimpleName();
    private String patient_id = "";
    @NonNull
    private CustomArrayList<AdvancedQIModal> save_qi = new CustomArrayList<>();
    @Nullable
    private JSONObject jsonmain = null;
    @Nullable
    private Database database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization context
        context = this;

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.neurologic);

        patient_id = getIntent().getStringExtra(S.patient_details);

        database = new Database(context);

        // set Title
        mBinding.mainToolbar.toolbarheading.setText(getString(R.string.neurologic));

        mBinding.mainToolbar.imgEdit.setVisibility(View.GONE);
        // set on Click Listener
        setOnCLickLisetner();
    }

    public void submitQIAirway() throws JSONException {
        if (jsonmain != null) {
            if (Util.isNetworkConnected(context)) {
                progressDialog = Util.showPrograsssDialog(context);
                DataManager.saveQualityInformation(ConvertJsonToMap.jsonToMap(jsonmain));
            } else {
                Util.showToast(context, getString(R.string.no_internet_connection));
                finish();
            }
        }
    }

    private void setOnCLickLisetner() {
        mBinding.checkboxLeft1.setOnClickListener(this);
        mBinding.checkboxRight1.setOnClickListener(this);

        mBinding.checkboxLeft2.setOnClickListener(this);
        mBinding.checkboxRight2.setOnClickListener(this);

        mBinding.checkboxLeft3.setOnClickListener(this);
        mBinding.checkboxRight3.setOnClickListener(this);

        mBinding.checkboxLeft4.setOnClickListener(this);
        mBinding.checkboxRight4.setOnClickListener(this);

        mBinding.checkboxLeft5.setOnClickListener(this);
        mBinding.checkboxRight5.setOnClickListener(this);

        mBinding.checkboxLeft6.setOnClickListener(this);
        mBinding.checkboxRight6.setOnClickListener(this);

        mBinding.checkboxLeft7.setOnClickListener(this);
        mBinding.checkboxRight7.setOnClickListener(this);

        mBinding.checkboxLeft8.setOnClickListener(this);
        mBinding.checkboxRight8.setOnClickListener(this);

        mBinding.mainToolbar.toggleIcon.setOnClickListener(this);
        mBinding.mainToolbar.imgEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.checkbox_left_1:
                if (mBinding.checkboxLeft1.isChecked())
                    mBinding.checkboxRight1.setChecked(false);
                break;
            case R.id.checkbox_right_1:
                if (mBinding.checkboxRight1.isChecked())
                    mBinding.checkboxLeft1.setChecked(false);
                break;
            case R.id.checkbox_left_2:
                if (mBinding.checkboxLeft2.isChecked())
                    mBinding.checkboxRight2.setChecked(false);
                break;
            case R.id.checkbox_right_2:
                if (mBinding.checkboxRight2.isChecked())
                    mBinding.checkboxLeft2.setChecked(false);
                break;
            case R.id.checkbox_left_3:
                if (mBinding.checkboxLeft3.isChecked())
                    mBinding.checkboxRight3.setChecked(false);
                break;
            case R.id.checkbox_right_3:
                if (mBinding.checkboxRight3.isChecked())
                    mBinding.checkboxLeft3.setChecked(false);
                break;
            case R.id.checkbox_left_4:
                if (mBinding.checkboxLeft4.isChecked())
                    mBinding.checkboxRight4.setChecked(false);
                break;
            case R.id.checkbox_right_4:
                if (mBinding.checkboxRight4.isChecked())
                    mBinding.checkboxLeft4.setChecked(false);
                break;
            case R.id.checkbox_left_5:
                if (mBinding.checkboxLeft5.isChecked())
                    mBinding.checkboxRight5.setChecked(false);
                break;
            case R.id.checkbox_right_5:
                if (mBinding.checkboxRight5.isChecked())
                    mBinding.checkboxLeft5.setChecked(false);
                break;
            case R.id.checkbox_left_6:
                if (mBinding.checkboxLeft6.isChecked())
                    mBinding.checkboxRight6.setChecked(false);
                break;
            case R.id.checkbox_right_6:
                if (mBinding.checkboxRight6.isChecked())
                    mBinding.checkboxLeft6.setChecked(false);
                break;
            case R.id.checkbox_left_7:
                if (mBinding.checkboxLeft7.isChecked())
                    mBinding.checkboxRight7.setChecked(false);
                break;
            case R.id.checkbox_right_7:
                if (mBinding.checkboxRight7.isChecked())
                    mBinding.checkboxLeft7.setChecked(false);
                break;
            case R.id.checkbox_left_8:
                if (mBinding.checkboxLeft8.isChecked())
                    mBinding.checkboxRight8.setChecked(false);
                break;
            case R.id.checkbox_right_8:
                if (mBinding.checkboxRight8.isChecked())
                    mBinding.checkboxLeft8.setChecked(false);
                break;
            case R.id.toggle_icon:
                onBackPressed();
                break;
            case R.id.img_edit:
                Util.showNoteDialog(context, "", "", "");
                break;
        }
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void onBackPressed() {
        if (Util.isNetworkConnected(context)) {
            try {
                jsonmain = new JSONObject();
                jsonmain.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
                jsonmain.put(S.api_record_id, patient_id);

                JSONObject jsonObject = new JSONObject();
                if (mBinding.checkboxLeft1.isChecked())
                    jsonObject.put(getString(R.string.cva), getString(R.string.intra_op));
                else if (mBinding.checkboxRight1.isChecked())
                    jsonObject.put(getString(R.string.cva), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.cva), getString(R.string.no));

                if (mBinding.checkboxLeft2.isChecked())
                    jsonObject.put(getString(R.string.hypoxemic_brain_injury), getString(R.string.intra_op));
                else if (mBinding.checkboxRight2.isChecked())
                    jsonObject.put(getString(R.string.hypoxemic_brain_injury), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.hypoxemic_brain_injury), getString(R.string.no));

                if (mBinding.checkboxLeft3.isChecked())
                    jsonObject.put(getString(R.string.other_brain_injury), getString(R.string.intra_op));
                else if (mBinding.checkboxRight3.isChecked())
                    jsonObject.put(getString(R.string.other_brain_injury), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.other_brain_injury), getString(R.string.no));

                if (mBinding.checkboxLeft4.isChecked())
                    jsonObject.put(getString(R.string.peripheral_nerve_injury), getString(R.string.intra_op));
                else if (mBinding.checkboxRight4.isChecked())
                    jsonObject.put(getString(R.string.peripheral_nerve_injury), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.peripheral_nerve_injury), getString(R.string.no));

                if (mBinding.checkboxLeft5.isChecked())
                    jsonObject.put(getString(R.string.visual_loss), getString(R.string.intra_op));
                else if (mBinding.checkboxRight5.isChecked())
                    jsonObject.put(getString(R.string.visual_loss), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.visual_loss), getString(R.string.no));

                if (mBinding.checkboxLeft6.isChecked())
                    jsonObject.put(getString(R.string.seizure), getString(R.string.intra_op));
                else if (mBinding.checkboxRight6.isChecked())
                    jsonObject.put(getString(R.string.seizure), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.seizure), getString(R.string.no));

                if (mBinding.checkboxLeft7.isChecked())
                    jsonObject.put(getString(R.string.other_spinal_cord_injury), getString(R.string.intra_op));
                else if (mBinding.checkboxRight7.isChecked())
                    jsonObject.put(getString(R.string.other_spinal_cord_injury), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.other_spinal_cord_injury), getString(R.string.no));

                if (mBinding.checkboxRight8.isChecked())
                    jsonObject.put(getString(R.string.post_op_delirium), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.post_op_delirium), getString(R.string.no));
                if (jsonObject.length() != 0) {
                    jsonmain.put(S.api_qi_measures, jsonObject);
                    Util.confirmDialog(context, getString(R.string.do_you_want_to_save), getString(R.string.yes), getString(R.string.no), "", 0);
                } else
                    finish();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        } else
            finish();

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
        try {
            if (Util.isNetworkConnected(context)) {
                progressDialog = Util.showPrograsssDialog(context);
                // get prefill Data from server
                getPrefillData();
            } else {
                // get prefill data from local database
                fillQIDetails(database.getQIDetails(patient_id, S.api_save_qi));
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void getPrefillData() throws JSONException {
        DataManager.getSaveQi(ConvertJsonToMap.jsonToMap(Util.getDefaultJson(context, patient_id)));
    }

    private void fillQIDetails(@NonNull CustomArrayList<AdvancedQIModal> save_qi) {
        if (save_qi.contains(getString(R.string.cva).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.cva).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft1.setChecked(true);
            else if ((save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op))))
                mBinding.checkboxRight1.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.hypoxemic_brain_injury).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.hypoxemic_brain_injury).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft2.setChecked(true);
            else if ((save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op))))
                mBinding.checkboxRight2.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.other_brain_injury).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.other_brain_injury).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft3.setChecked(true);
            else if ((save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op))))
                mBinding.checkboxRight3.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.peripheral_nerve_injury).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.peripheral_nerve_injury).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft4.setChecked(true);
            else if ((save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op))))
                mBinding.checkboxRight4.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.visual_loss).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.visual_loss).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft5.setChecked(true);
            else if ((save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op))))
                mBinding.checkboxRight5.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.seizure).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.seizure).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft6.setChecked(true);
            else if ((save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op))))
                mBinding.checkboxRight6.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.other_spinal_cord_injury).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.other_spinal_cord_injury).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft7.setChecked(true);
            else if ((save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op))))
                mBinding.checkboxRight7.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.post_op_delirium).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.post_op_delirium).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft8.setChecked(true);
            else if ((save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op))))
                mBinding.checkboxRight8.setChecked(true);
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
                    if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_save_qi)) {
                        Util.showToast(context, jsonObject.getString(S.message));

                        finish();
                    }
                    if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_qi_details)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(S.data);
                        save_qi.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            AdvancedQIModal advancedQIModal = new AdvancedQIModal();
                            advancedQIModal.setName(jsonObject1.getString(S.api_measure_name));
                            advancedQIModal.setId(jsonObject1.getString(S.api_value));
                            save_qi.add(advancedQIModal);
                        }
                        fillQIDetails(save_qi);
                        if (database.getSavedSatus(patient_id, S.api_save_qi))
                            database.updateQiDetails(patient_id, jsonObject, S.api_save_qi);
                        else
                            database.savedQiDetails(patient_id, jsonObject, S.api_save_qi);

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
}

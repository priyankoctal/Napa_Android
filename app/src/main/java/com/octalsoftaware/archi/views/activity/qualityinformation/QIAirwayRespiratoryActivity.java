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
import com.octalsoftaware.archi.databinding.QiAirwayRespiratoryBinding;
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

public class QIAirwayRespiratoryActivity extends BaseActivity implements View.OnClickListener {


    private QiAirwayRespiratoryBinding mBinding = null;
    private Context context = null;
    private MyProgressDialog progressDialog = null;
    private CustomArrayList<AdvancedQIModal> save_qi = new CustomArrayList<>();
    public static String TAG = QIAirwayRespiratoryActivity.class.getSimpleName();
    private String patient_id = "";
    private JSONObject jsonmain = null;
    private Database database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization context
        context = this;

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.qi_airway_respiratory);

        patient_id = getIntent().getStringExtra(S.patient_details);

        database = new Database(context);


        // set Title
        mBinding.mainToolbar.toolbarheading.setText(getString(R.string.airway_respiratory));

        mBinding.mainToolbar.imgEdit.setVisibility(View.GONE);

        // set on Click Listener
        setOnCLickLisetner();
    }


    public void submitQIAirway() throws JSONException {

       /* JSONObject jsonmain = new JSONObject();
        jsonmain.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
        jsonmain.put(S.api_record_id, patient_id);

        JSONObject jsonObject = new JSONObject();
        if (mBinding.checkboxLeft1.isChecked())
            jsonObject.put(getString(R.string.planned_use_of_difficult), getString(R.string.intra_op));
        if (mBinding.checkboxLeft2.isChecked())
            jsonObject.put(getString(R.string.unplanned_use_of_difficult), getString(R.string.intra_op));
        if (mBinding.checkboxLeft3.isChecked())
            jsonObject.put(getString(R.string.unable_to_intubate), getString(R.string.intra_op));
        if (mBinding.checkboxLeft4.isChecked())
            jsonObject.put(getString(R.string.surgical_airway_required), getString(R.string.intra_op));
        if(mBinding.checkboxLeft5.isChecked())
            jsonObject.put(getString(R.string.aspiration), getString(R.string.intra_op));
        if(mBinding.checkboxRight5.isChecked())
            jsonObject.put(getString(R.string.aspiration), getString(R.string.post_op));
        if(mBinding.checkboxLeft6.isChecked())
            jsonObject.put(getString(R.string.laryngospasm_with_intervention), getString(R.string.intra_op));
        if(mBinding.checkboxRight6.isChecked())
            jsonObject.put(getString(R.string.laryngospasm_with_intervention), getString(R.string.post_op));
        if(mBinding.checkboxLeft7.isChecked())
            jsonObject.put(getString(R.string.bronchospasm_with_intervention), getString(R.string.intra_op));
        if(mBinding.checkboxRight7.isChecked())
            jsonObject.put(getString(R.string.bronchospasm_with_intervention), getString(R.string.post_op));
        if(mBinding.checkboxLeft8.isChecked())
            jsonObject.put(getString(R.string.non_dental_upper_airway_trauma), getString(R.string.intra_op));
        if(mBinding.checkboxRight8.isChecked())
            jsonObject.put(getString(R.string.non_dental_upper_airway_trauma), getString(R.string.post_op));
        if(mBinding.checkboxLeft9.isChecked())
            jsonObject.put(getString(R.string.pulmonary_edema), getString(R.string.intra_op));
        if(mBinding.checkboxRight9.isChecked())
            jsonObject.put(getString(R.string.pulmonary_edema), getString(R.string.post_op));
        if(mBinding.checkboxLeft10.isChecked())
            jsonObject.put(getString(R.string.air_embolus), getString(R.string.intra_op));
        if(mBinding.checkboxRight10.isChecked())
            jsonObject.put(getString(R.string.air_embolus), getString(R.string.post_op));
        if(mBinding.checkboxLeft11.isChecked())
            jsonObject.put(getString(R.string.unplanned_intubation), getString(R.string.intra_op));
        if(mBinding.checkboxRight11.isChecked())
            jsonObject.put(getString(R.string.unplanned_intubation), getString(R.string.post_op));
        if(mBinding.checkboxLeft12.isChecked())
            jsonObject.put(getString(R.string.post_op_mechanical_ventilation), getString(R.string.intra_op));
        if(mBinding.checkboxRight12.isChecked())
            jsonObject.put(getString(R.string.post_op_mechanical_ventilation), getString(R.string.post_op));
        if(mBinding.checkboxLeft13.isChecked())
            jsonObject.put(getString(R.string.narcan_given), getString(R.string.intra_op));
        if(mBinding.checkboxRight13.isChecked())
            jsonObject.put(getString(R.string.narcan_given), getString(R.string.post_op));

        jsonmain.put(S.api_qi_measures,jsonObject);*/

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
        mBinding.checkboxLeft5.setOnClickListener(this);
        mBinding.checkboxRight5.setOnClickListener(this);
        mBinding.checkboxLeft6.setOnClickListener(this);
        mBinding.checkboxRight6.setOnClickListener(this);
        mBinding.checkboxLeft7.setOnClickListener(this);
        mBinding.checkboxRight7.setOnClickListener(this);
        mBinding.checkboxLeft8.setOnClickListener(this);
        mBinding.checkboxRight8.setOnClickListener(this);
        mBinding.checkboxLeft9.setOnClickListener(this);
        mBinding.checkboxRight9.setOnClickListener(this);
        mBinding.checkboxLeft10.setOnClickListener(this);
        mBinding.checkboxRight10.setOnClickListener(this);
        mBinding.checkboxLeft11.setOnClickListener(this);
        mBinding.checkboxRight11.setOnClickListener(this);
        mBinding.checkboxLeft12.setOnClickListener(this);
        mBinding.checkboxRight12.setOnClickListener(this);
        mBinding.checkboxLeft13.setOnClickListener(this);
        mBinding.checkboxRight13.setOnClickListener(this);
        mBinding.checkboxLeft14.setOnClickListener(this);
        mBinding.checkboxRight14.setOnClickListener(this);
        mBinding.checkboxLeft15.setOnClickListener(this);
        mBinding.checkboxRight15.setOnClickListener(this);
        mBinding.mainToolbar.toggleIcon.setOnClickListener(this);
        mBinding.mainToolbar.imgEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
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
            case R.id.checkbox_left_9:
                if (mBinding.checkboxLeft9.isChecked())
                    mBinding.checkboxRight9.setChecked(false);
                break;
            case R.id.checkbox_right_9:
                if (mBinding.checkboxRight9.isChecked())
                    mBinding.checkboxLeft9.setChecked(false);
                break;
            case R.id.checkbox_left_10:
                if (mBinding.checkboxLeft10.isChecked())
                    mBinding.checkboxRight10.setChecked(false);
                break;
            case R.id.checkbox_right_10:
                if (mBinding.checkboxRight10.isChecked())
                    mBinding.checkboxLeft10.setChecked(false);
                break;
            case R.id.checkbox_left_11:
                if (mBinding.checkboxLeft11.isChecked())
                    mBinding.checkboxRight11.setChecked(false);
                break;
            case R.id.checkbox_right_11:
                if (mBinding.checkboxRight11.isChecked())
                    mBinding.checkboxLeft11.setChecked(false);
                break;
            case R.id.checkbox_left_12:
                if (mBinding.checkboxLeft12.isChecked())
                    mBinding.checkboxRight12.setChecked(false);
                break;
            case R.id.checkbox_right_12:
                if (mBinding.checkboxRight12.isChecked())
                    mBinding.checkboxLeft12.setChecked(false);
                break;
            case R.id.checkbox_left_13:
                if (mBinding.checkboxLeft13.isChecked())
                    mBinding.checkboxRight13.setChecked(false);
                break;
            case R.id.checkbox_right_13:
                if (mBinding.checkboxRight13.isChecked())
                    mBinding.checkboxLeft13.setChecked(false);
                break;
            case R.id.checkbox_left_14:
                if (mBinding.checkboxLeft14.isChecked())
                    mBinding.checkboxRight14.setChecked(false);
                break;
            case R.id.checkbox_right_14:
                if (mBinding.checkboxRight14.isChecked())
                    mBinding.checkboxLeft14.setChecked(false);
                break;
            case R.id.checkbox_left_15:
                if (mBinding.checkboxLeft15.isChecked())
                    mBinding.checkboxRight15.setChecked(false);
                break;
            case R.id.checkbox_right_15:
                if (mBinding.checkboxRight15.isChecked())
                    mBinding.checkboxLeft15.setChecked(false);
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
                    jsonObject.put(getString(R.string.planned_use_of_difficult), getString(R.string.intra_op));
                else
                    jsonObject.put(getString(R.string.planned_use_of_difficult), getString(R.string.no));

                if (mBinding.checkboxLeft2.isChecked())
                    jsonObject.put(getString(R.string.unplanned_use_of_difficult), getString(R.string.intra_op));
                else
                    jsonObject.put(getString(R.string.unplanned_use_of_difficult), getString(R.string.no));

                if (mBinding.checkboxLeft3.isChecked())
                    jsonObject.put(getString(R.string.unable_to_intubate), getString(R.string.intra_op));
                else
                    jsonObject.put(getString(R.string.unable_to_intubate), getString(R.string.no));

                if (mBinding.checkboxLeft4.isChecked())
                    jsonObject.put(getString(R.string.surgical_airway_required), getString(R.string.intra_op));
                else
                    jsonObject.put(getString(R.string.surgical_airway_required), getString(R.string.no));

                if (mBinding.checkboxLeft5.isChecked())
                    jsonObject.put(getString(R.string.aspiration), getString(R.string.intra_op));
                else if (mBinding.checkboxRight5.isChecked())
                    jsonObject.put(getString(R.string.aspiration), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.aspiration), getString(R.string.no));

                if (mBinding.checkboxLeft6.isChecked())
                    jsonObject.put(getString(R.string.laryngospasm_with_intervention), getString(R.string.intra_op));
                else if (mBinding.checkboxRight6.isChecked())
                    jsonObject.put(getString(R.string.laryngospasm_with_intervention), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.laryngospasm_with_intervention), getString(R.string.no));

                if (mBinding.checkboxLeft7.isChecked())
                    jsonObject.put(getString(R.string.bronchospasm_with_intervention), getString(R.string.intra_op));
                else if (mBinding.checkboxRight7.isChecked())
                    jsonObject.put(getString(R.string.bronchospasm_with_intervention), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.bronchospasm_with_intervention), getString(R.string.no));

                if (mBinding.checkboxLeft8.isChecked())
                    jsonObject.put(getString(R.string.non_dental_upper_airway_trauma), getString(R.string.intra_op));
                else if (mBinding.checkboxRight8.isChecked())
                    jsonObject.put(getString(R.string.non_dental_upper_airway_trauma), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.non_dental_upper_airway_trauma), getString(R.string.no));

                if (mBinding.checkboxLeft9.isChecked())
                    jsonObject.put(getString(R.string.pulmonary_edema), getString(R.string.intra_op));
                else if (mBinding.checkboxRight9.isChecked())
                    jsonObject.put(getString(R.string.pulmonary_edema), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.pulmonary_edema), getString(R.string.no));

                if (mBinding.checkboxLeft10.isChecked())
                    jsonObject.put(getString(R.string.air_embolus), getString(R.string.intra_op));
                else if (mBinding.checkboxRight10.isChecked())
                    jsonObject.put(getString(R.string.air_embolus), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.air_embolus), getString(R.string.no));

                if (mBinding.checkboxLeft11.isChecked())
                    jsonObject.put(getString(R.string.unplanned_intubation), getString(R.string.intra_op));
                else if (mBinding.checkboxRight11.isChecked())
                    jsonObject.put(getString(R.string.unplanned_intubation), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.unplanned_intubation), getString(R.string.no));

                if (mBinding.checkboxLeft12.isChecked())
                    jsonObject.put(getString(R.string.post_op_mechanical_ventilation), getString(R.string.intra_op));
                else if (mBinding.checkboxRight12.isChecked())
                    jsonObject.put(getString(R.string.post_op_mechanical_ventilation), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.post_op_mechanical_ventilation), getString(R.string.no));

                if (mBinding.checkboxLeft13.isChecked())
                    jsonObject.put(getString(R.string.narcan_given), getString(R.string.intra_op));
                else if (mBinding.checkboxRight13.isChecked())
                    jsonObject.put(getString(R.string.narcan_given), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.narcan_given), getString(R.string.no));

                if (mBinding.checkboxLeft14.isChecked())
                    jsonObject.put(getString(R.string.negative_pressure_pulmonary_edema), getString(R.string.intra_op));
                else if (mBinding.checkboxRight14.isChecked())
                    jsonObject.put(getString(R.string.negative_pressure_pulmonary_edema), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.negative_pressure_pulmonary_edema), getString(R.string.no));

                if (mBinding.checkboxLeft15.isChecked())
                    jsonObject.put(getString(R.string.pulmonary_embolus), getString(R.string.intra_op));
                else if (mBinding.checkboxRight15.isChecked())
                    jsonObject.put(getString(R.string.pulmonary_embolus), getString(R.string.post_op));
                else
                    jsonObject.put(getString(R.string.pulmonary_embolus), getString(R.string.no));

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
        if (save_qi.contains(getString(R.string.planned_use_of_difficult).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.planned_use_of_difficult).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft1.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight1.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.unplanned_use_of_difficult).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.unplanned_use_of_difficult).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft2.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight2.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.unable_to_intubate).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.unable_to_intubate).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft3.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight3.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.surgical_airway_required).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.surgical_airway_required).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft4.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight4.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.aspiration).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.aspiration).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft5.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight5.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.laryngospasm_with_intervention).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.laryngospasm_with_intervention).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft6.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight6.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.bronchospasm_with_intervention).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.bronchospasm_with_intervention).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft7.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight7.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.non_dental_upper_airway_trauma).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.non_dental_upper_airway_trauma).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft8.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight8.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.pulmonary_edema).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.pulmonary_edema).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft9.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight9.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.air_embolus).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.air_embolus).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft10.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight10.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.unplanned_intubation).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.unplanned_intubation).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft11.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight11.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.post_op_mechanical_ventilation).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.post_op_mechanical_ventilation).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft12.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight12.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.narcan_given).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.narcan_given).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft13.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight13.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.negative_pressure_pulmonary_edema).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.negative_pressure_pulmonary_edema).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft14.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight14.setChecked(true);
        }
        if (save_qi.contains(getString(R.string.pulmonary_embolus).toLowerCase())) {
            int pos = save_qi.indexOf(getString(R.string.pulmonary_embolus).toLowerCase());
            if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.intra_op)))
                mBinding.checkboxLeft15.setChecked(true);
            else if (save_qi.get(pos).getId().equalsIgnoreCase(getString(R.string.post_op)))
                mBinding.checkboxRight15.setChecked(true);
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

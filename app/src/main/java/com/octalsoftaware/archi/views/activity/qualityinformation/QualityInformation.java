package com.octalsoftaware.archi.views.activity.qualityinformation;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.data.DataManager;
import com.octalsoftaware.archi.data.events.ErrorEvent;
import com.octalsoftaware.archi.data.events.SucessEvent;
import com.octalsoftaware.archi.databinding.QualityInformationBinding;
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

import static com.octalsoftaware.archi.utils.Util.setTypefaceRegular;
import static com.octalsoftaware.archi.utils.Util.showNoteDialog;
import static com.octalsoftaware.archi.utils.constants.S.user_id;

/**
 * Created by anandj on 4/18/2017.
 */

public class QualityInformation extends BaseActivity implements View.OnClickListener {

    @Nullable
    private QualityInformationBinding mBinding = null;
    @Nullable
    private Context context = null;
    @Nullable
    private MyProgressDialog progressDialog = null;
    private String note_str = "";
    private String patient_id = "";
    @NonNull
    private String osa_score = "";
    @NonNull
    private String admission_pain_store = "";
    private String patient_name = "";
    @NonNull
    private CustomArrayList<AdvancedQIModal> mqiModalArrayList = new CustomArrayList<>();
    public static String TAG = QualityInformation.class.getSimpleName();
    @NonNull
    private String type = "back";
    @Nullable
    private JSONObject jsonmain = null;
    @Nullable
    private Database database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization context
        context = this;

        patient_id = getIntent().getStringExtra(S.patient_details);
        patient_name = getIntent().getStringExtra(S.patient_name);

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.quality_information);

        // set Title
        mBinding.mainToolbar.toolbarheading.setText(getString(R.string.qty_information));

        // set patient name
        mBinding.txtPatientname.setText(patient_name);

        // database
        database = new Database(context);

        // set font
        setTypeface();

        // onclick
        setOnClickListner();

    }

    private void setOnClickListner() {
        mBinding.mainToolbar.imgEdit.setOnClickListener(this);
        mBinding.mainToolbar.toggleIcon.setOnClickListener(this);
        mBinding.txtSubmit.setOnClickListener(this);
        mBinding.txtNA.setOnClickListener(this);
        mBinding.txtOne.setOnClickListener(this);
        mBinding.txtTwo.setOnClickListener(this);
        mBinding.txtThree.setOnClickListener(this);
        mBinding.txtFour.setOnClickListener(this);
        mBinding.txtFive.setOnClickListener(this);
        mBinding.txtSix.setOnClickListener(this);
        mBinding.txtSeven.setOnClickListener(this);
        mBinding.txtEight.setOnClickListener(this);
        mBinding.txtNine.setOnClickListener(this);
        mBinding.txtTen.setOnClickListener(this);

        mBinding.txtOneOsa.setOnClickListener(this);
        mBinding.txtTwoOsa.setOnClickListener(this);
        mBinding.txtThreeOsa.setOnClickListener(this);
        mBinding.txtFourOsa.setOnClickListener(this);
        mBinding.txtFiveOsa.setOnClickListener(this);
        mBinding.txtSixOsa.setOnClickListener(this);
        mBinding.txtSevenOsa.setOnClickListener(this);
        mBinding.txtEightOsa.setOnClickListener(this);
        mBinding.txtCross.setOnClickListener(this);

        mBinding.checkboxAbove.setOnClickListener(this);
        mBinding.checkboxBelow.setOnClickListener(this);
        mBinding.checkboxHandoff.setOnClickListener(this);
        mBinding.checkboxNotPerHandoff.setOnClickListener(this);

    }

    private void setTypeface() {
        mBinding.checkboxSystem.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxNoListedEvent.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxAbove.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxBelow.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxHandoff.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxNotPerHandoff.setTypeface(setTypefaceRegular(context));
        mBinding.checkboxLnown.setTypeface(setTypefaceRegular(context));
    }

    public void submitQIDetails(String type) throws JSONException {

      /*  JSONObject jsonObject = new JSONObject();
        jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
        jsonObject.put(S.api_record_id, patient_id);

        JSONObject jsonObject1 = new JSONObject();

        if (mBinding.checkboxSystem.isChecked())
            jsonObject1.put(getString(R.string.qtyinformationdetails), S.yes);
        if (mBinding.checkboxNoListedEvent.isChecked())
            jsonObject1.put(getString(R.string.no_listed_event), S.yes);

        if (mBinding.checkboxAbove.isChecked())
            jsonObject1.put(getString(R.string.addmission_temperature), getString(R.string.above_equal));
        if (mBinding.checkboxBelow.isChecked())
            jsonObject1.put(getString(R.string.addmission_temperature), getString(R.string.above_equal));
        if (mBinding.checkboxHandoff.isChecked())
            jsonObject1.put(getString(R.string.pacu_direct_handoff), getString(R.string.per_handoff));
        if (mBinding.checkboxNotPerHandoff.isChecked())
            jsonObject1.put(getString(R.string.pacu_direct_handoff), getString(R.string.not_per_handoff));
        if (!admission_pain_store.equals(""))
            jsonObject1.put(getString(R.string.admission_pain_store), admission_pain_store);
        if (!osa_score.equals(""))
            jsonObject1.put(getString(R.string.osa_score), osa_score);
        if (mBinding.checkboxLnown.isChecked())
            jsonObject1.put(getString(R.string.known_osa), S.yes);

        if (jsonObject1.length() != 0) {
            progressDialog = Util.showPrograsssDialog(context);
            jsonObject.put(S.api_qi_measures, jsonObject1);
            DataManager.saveQualityInformation(ConvertJsonToMap.jsonToMap(jsonObject));
        } else {
            if (type.equalsIgnoreCase("back"))
                finish();
            else
                startAnotherActivity();
        }*/

        if (jsonmain != null) {
            if (Util.isNetworkConnected(context)) {
                progressDialog = Util.showPrograsssDialog(context);
                DataManager.saveQualityInformation(ConvertJsonToMap.jsonToMap(jsonmain));
            } else {
                Util.showToast(context, getString(R.string.no_internet_connection));
                startAnotherActivity();
            }

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
                showNoteDialog(context, note_str, patient_id, S.qi_status);
                break;
            case R.id.txt_submit:
                try {

                    if (prepareQualityInformation()) {
                        type = "submit";
                        submitQIDetails(type);
                    } else {
                        startAnotherActivity();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.toString());
                }

                break;
            case R.id.txt_n_a:
                admission_pain_store = getString(R.string.n_a);
                changeTextViewFormat(mBinding.txtNA);
                break;
            case R.id.txt_one:
                admission_pain_store = getString(R.string.one);
                changeTextViewFormat(mBinding.txtOne);
                break;
            case R.id.txt_two:
                admission_pain_store = getString(R.string.two);
                changeTextViewFormat(mBinding.txtTwo);
                break;
            case R.id.txt_three:
                admission_pain_store = getString(R.string.three);
                changeTextViewFormat(mBinding.txtThree);
                break;
            case R.id.txt_four:
                admission_pain_store = getString(R.string.four);
                changeTextViewFormat(mBinding.txtFour);
                break;
            case R.id.txt_five:
                admission_pain_store = getString(R.string.five);
                changeTextViewFormat(mBinding.txtFive);
                break;
            case R.id.txt_six:
                admission_pain_store = getString(R.string.six);
                changeTextViewFormat(mBinding.txtSix);
                break;
            case R.id.txt_seven:
                admission_pain_store = getString(R.string.seven);
                changeTextViewFormat(mBinding.txtSeven);
                break;
            case R.id.txt_eight:
                admission_pain_store = getString(R.string.eight);
                changeTextViewFormat(mBinding.txtEight);
                break;
            case R.id.txt_nine:
                admission_pain_store = getString(R.string.nine);
                changeTextViewFormat(mBinding.txtNine);
                break;
            case R.id.txt_ten:
                admission_pain_store = getString(R.string.ten);
                changeTextViewFormat(mBinding.txtTen);
                break;
            case R.id.txt_cross:
                osa_score = "";
                changeTextViewOSA(mBinding.txtEightOsa, false);
                break;
            case R.id.txt_eight_osa:
                osa_score = "8";
                changeTextViewOSA(mBinding.txtEightOsa, true);
                break;
            case R.id.txt_seven_osa:
                osa_score = "7";
                changeTextViewOSA(mBinding.txtSevenOsa, true);
                break;
            case R.id.txt_six_osa:
                osa_score = "6";
                changeTextViewOSA(mBinding.txtSixOsa, true);
                break;
            case R.id.txt_five_osa:
                osa_score = "5";
                changeTextViewOSA(mBinding.txtFiveOsa, true);
                break;
            case R.id.txt_four_osa:
                osa_score = "4";
                changeTextViewOSA(mBinding.txtFourOsa, true);
                break;
            case R.id.txt_three_osa:
                osa_score = "3";
                changeTextViewOSA(mBinding.txtThreeOsa, true);
                break;
            case R.id.txt_two_osa:
                osa_score = "2";
                changeTextViewOSA(mBinding.txtTwoOsa, true);
                break;
            case R.id.txt_one_osa:
                osa_score = "1";
                changeTextViewOSA(mBinding.txtOneOsa, true);
                break;
            case R.id.checkbox_above:
                if (mBinding.checkboxAbove.isChecked())
                    mBinding.checkboxBelow.setChecked(false);
                break;
            case R.id.checkbox_below:
                if (mBinding.checkboxBelow.isChecked())
                    mBinding.checkboxAbove.setChecked(false);
                break;
            case R.id.checkbox_not_per_handoff:
                if (mBinding.checkboxNotPerHandoff.isChecked())
                    mBinding.checkboxHandoff.setChecked(false);
                break;
            case R.id.checkbox_handoff:
                if (mBinding.checkboxHandoff.isChecked())
                    mBinding.checkboxNotPerHandoff.setChecked(false);
                break;
        }
    }

    private void changeTextViewOSA(@NonNull TextView textView, boolean b) {

        mBinding.txtOneOsa.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtOneOsa.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtTwoOsa.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtTwoOsa.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtThreeOsa.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtThreeOsa.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtFourOsa.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtFourOsa.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtFiveOsa.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtFiveOsa.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtSixOsa.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtSixOsa.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtSevenOsa.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtSevenOsa.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtEightOsa.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtEightOsa.setBackgroundResource(R.drawable.textviewsquire);

        if (b) {
            textView.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
            textView.setBackgroundResource(R.drawable.textviewfillsqr);
        }
    }

    private void changeTextViewFormat(@NonNull TextView textView) {
        mBinding.txtNA.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtNA.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtOne.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtOne.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtTwo.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtTwo.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtThree.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtThree.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtFour.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtFour.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtFive.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtFive.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtSeven.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtSeven.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtSix.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtEight.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtEight.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtNine.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtNine.setBackgroundResource(R.drawable.textviewsquire);

        mBinding.txtTen.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        mBinding.txtTen.setBackgroundResource(R.drawable.textviewsquire);

        textView.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
        textView.setBackgroundResource(R.drawable.textviewfillsqr);


    }

    private void getNote() throws JSONException {
        DataManager.getNotes(ConvertJsonToMap.jsonToMap(Util.getNoteParameter(MySharedPreferences.getPreferences(context, user_id), patient_id, S.qi_status)));
    }

    private void getPrefillData() throws JSONException {
        progressDialog = Util.showPrograsssDialog(context);
        DataManager.getSaveQi(ConvertJsonToMap.jsonToMap(Util.getDefaultJson(context, patient_id)));
    }

    @Override
    public void initEvent() {
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

        try {
            if (Util.isNetworkConnected(context)) {
                // get Previous notes
                getNote();
                // get prefill Data
                getPrefillData();

            } else {
                // get prefill data from local database
                fillQIDetails(database.getQIDetails(patient_id, S.api_save_qi));
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        event.unregister(this);
    }

    public void sendNote(@NonNull JSONObject jsonObject) throws JSONException {

        progressDialog = Util.showPrograsssDialog(context);
        DataManager.addNotes(ConvertJsonToMap.jsonToMap(jsonObject));
    }

    private void fillQIDetails(@NonNull CustomArrayList<AdvancedQIModal> mqiModalArrayList) {
        // if(mqiModalArrayList.contains(mBinding.checkboxSystem.getText().toString().toLowerCase())){
        int pos = mqiModalArrayList.indexOf(mBinding.checkboxSystem.getText().toString().toLowerCase());
        if (mqiModalArrayList.get(pos).getId().equalsIgnoreCase(S.yes))
            mBinding.checkboxSystem.setChecked(true);
        //   }
        // if(mqiModalArrayList.contains(mBinding.checkboxNoListedEvent.getText().toString().toLowerCase())){
        pos = mqiModalArrayList.indexOf(mBinding.checkboxNoListedEvent.getText().toString().toLowerCase());
        if (mqiModalArrayList.get(pos).getId().equalsIgnoreCase(S.yes))
            mBinding.checkboxNoListedEvent.setChecked(true);
        //   }
        // mBinding.checkboxSystem.setChecked(mqiModalArrayList.contains(mBinding.checkboxSystem.getText().toString().toLowerCase()));
        // mBinding.checkboxNoListedEvent.setChecked(mqiModalArrayList.contains(mBinding.checkboxNoListedEvent.getText().toString().toLowerCase()));
        // if (mqiModalArrayList.contains(getString(R.string.addmission_temperature).toLowerCase())) {
        pos = mqiModalArrayList.indexOf(getString(R.string.addmission_temperature).toLowerCase());
        if (mqiModalArrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.above_equal)))
            mBinding.checkboxAbove.setChecked(true);
        else if (mqiModalArrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.below)))
            mBinding.checkboxBelow.setChecked(true);
        //  }
        //  if (mqiModalArrayList.contains(getString(R.string.pacu_direct_handoff).toLowerCase())) {
        pos = mqiModalArrayList.indexOf(getString(R.string.pacu_direct_handoff).toLowerCase());
        if (mqiModalArrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.per_handoff)))
            mBinding.checkboxHandoff.setChecked(true);
        else if (mqiModalArrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.not_per_handoff)))
            mBinding.checkboxNotPerHandoff.setChecked(true);
        //    }
        //     if (mqiModalArrayList.contains(getString(R.string.admission_pain_store).toLowerCase())) {
        pos = mqiModalArrayList.indexOf(getString(R.string.admission_pain_store).toLowerCase());
        if (!mqiModalArrayList.get(pos).getId().equals("")) {
            if (mqiModalArrayList.get(pos).getId().equalsIgnoreCase(getString(R.string.n_a))) {
                admission_pain_store = getString(R.string.n_a);
                changeTextViewFormat(mBinding.txtNA);
            } else {
                int id = Integer.parseInt(mqiModalArrayList.get(pos).getId());
                switch (id) {
                    case 1:
                        admission_pain_store = getString(R.string.one);
                        changeTextViewFormat(mBinding.txtOne);
                        break;
                    case 2:
                        admission_pain_store = getString(R.string.two);
                        changeTextViewFormat(mBinding.txtTwo);
                        break;
                    case 3:
                        admission_pain_store = getString(R.string.three);
                        changeTextViewFormat(mBinding.txtThree);
                        break;
                    case 4:
                        admission_pain_store = getString(R.string.four);
                        changeTextViewFormat(mBinding.txtFour);
                        break;
                    case 5:
                        admission_pain_store = getString(R.string.five);
                        changeTextViewFormat(mBinding.txtFive);
                        break;
                    case 6:
                        admission_pain_store = getString(R.string.six);
                        changeTextViewFormat(mBinding.txtSix);
                        break;
                    case 7:
                        admission_pain_store = getString(R.string.seven);
                        changeTextViewFormat(mBinding.txtSeven);
                        break;
                    case 8:
                        admission_pain_store = getString(R.string.eight);
                        changeTextViewFormat(mBinding.txtEight);
                        break;
                    case 9:
                        admission_pain_store = getString(R.string.nine);
                        changeTextViewFormat(mBinding.txtNine);
                        break;
                    case 10:
                        admission_pain_store = getString(R.string.ten);
                        changeTextViewFormat(mBinding.txtTen);
                        break;
                }
            }

        }
        //   if (mqiModalArrayList.contains(getString(R.string.osa_score).toLowerCase())) {
        pos = mqiModalArrayList.indexOf(getString(R.string.osa_score).toLowerCase());
        if (!mqiModalArrayList.get(pos).getId().equals("")) {
            int id = Integer.parseInt(mqiModalArrayList.get(pos).getId());
            switch (id) {
                case 1:
                    osa_score = "1";
                    changeTextViewOSA(mBinding.txtOneOsa, true);
                    break;
                case 2:
                    osa_score = "2";
                    changeTextViewOSA(mBinding.txtTwoOsa, true);
                    break;
                case 3:
                    osa_score = "3";
                    changeTextViewOSA(mBinding.txtThreeOsa, true);
                    break;
                case 4:
                    osa_score = "4";
                    changeTextViewOSA(mBinding.txtFourOsa, true);
                    break;
                case 5:
                    osa_score = "5";
                    changeTextViewOSA(mBinding.txtFiveOsa, true);
                    break;
                case 6:
                    osa_score = "6";
                    changeTextViewOSA(mBinding.txtSixOsa, true);
                    break;
                case 7:
                    osa_score = "7";
                    changeTextViewOSA(mBinding.txtSevenOsa, true);
                    break;
                case 8:
                    osa_score = "8";
                    changeTextViewOSA(mBinding.txtEightOsa, true);
                    break;
            }
        }

        //   }
        pos = mqiModalArrayList.indexOf(mBinding.checkboxLnown.getText().toString().toLowerCase());
        if (mqiModalArrayList.get(pos).getId().equalsIgnoreCase(S.yes))
            mBinding.checkboxLnown.setChecked(true);
        //   mBinding.checkboxLnown.setChecked(mqiModalArrayList.contains(mBinding.checkboxLnown.getText().toString().toLowerCase()));
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
                    } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_save_qi)) {
                        if (jsonObject.getString(S.message) != null && !jsonObject.getString(S.message).equalsIgnoreCase("null"))
                            Util.showToast(context, jsonObject.getString(S.message));
                        if (!type.equalsIgnoreCase("back"))
                            startAnotherActivity();
                        else
                            finish();
                    } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_qi_details)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(S.data);
                        mqiModalArrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            AdvancedQIModal advancedQIModal = new AdvancedQIModal();
                            advancedQIModal.setName(jsonObject1.getString(S.api_measure_name));
                            advancedQIModal.setId(jsonObject1.getString(S.api_value));
                            mqiModalArrayList.add(advancedQIModal);

                        }
                        fillQIDetails(mqiModalArrayList);
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

    private void startAnotherActivity() {
        Intent intent = new Intent(context, AdvancedQIActivity.class);
        intent.putExtra(S.patient_details, patient_id);
        intent.putExtra(S.patient_name, patient_name);
        intent.putExtra(S.save_qi_data, mqiModalArrayList);
        startActivity(intent);
    }

    @Subscribe
    public void onFailed(@NonNull ErrorEvent event) {
        Util.dismissPrograssDialog(progressDialog);
        Log.e(TAG, event.getMessage());
    }

    private boolean prepareQualityInformation() throws JSONException {
        jsonmain = new JSONObject();
        jsonmain.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
        jsonmain.put(S.api_record_id, patient_id);

        JSONObject jsonObject1 = new JSONObject();

        if (mBinding.checkboxSystem.isChecked())
            jsonObject1.put(getString(R.string.qtyinformationdetails), S.yes);
        else
            jsonObject1.put(getString(R.string.qtyinformationdetails), "");

        if (mBinding.checkboxNoListedEvent.isChecked())
            jsonObject1.put(getString(R.string.no_listed_event), S.yes);
        else
            jsonObject1.put(getString(R.string.no_listed_event), "");

        if (mBinding.checkboxAbove.isChecked())
            jsonObject1.put(getString(R.string.addmission_temperature), getString(R.string.above_equal));
        else if (mBinding.checkboxBelow.isChecked())
            jsonObject1.put(getString(R.string.addmission_temperature), getString(R.string.below));
        else
            jsonObject1.put(getString(R.string.addmission_temperature), "");

        /*if (mBinding.checkboxBelow.isChecked())
            jsonObject1.put(getString(R.string.addmission_temperature), getString(R.string.below));
        else
            jsonObject1.put(getString(R.string.addmission_temperature), "");*/
        if (mBinding.checkboxHandoff.isChecked())
            jsonObject1.put(getString(R.string.pacu_direct_handoff), getString(R.string.per_handoff));
        else if (mBinding.checkboxNotPerHandoff.isChecked())
            jsonObject1.put(getString(R.string.pacu_direct_handoff), getString(R.string.not_per_handoff));
        else
            jsonObject1.put(getString(R.string.pacu_direct_handoff), "");

     /*   if (mBinding.checkboxNotPerHandoff.isChecked())
            jsonObject1.put(getString(R.string.pacu_direct_handoff), getString(R.string.not_per_handoff));
        else
            jsonObject1.put(getString(R.string.pacu_direct_handoff), "");*/

        if (!admission_pain_store.equals(""))
            jsonObject1.put(getString(R.string.admission_pain_store), admission_pain_store);
        else
            jsonObject1.put(getString(R.string.admission_pain_store), "");

        if (!osa_score.equals(""))
            jsonObject1.put(getString(R.string.osa_score), osa_score);
        else
            jsonObject1.put(getString(R.string.osa_score), "");

        if (mBinding.checkboxLnown.isChecked())
            jsonObject1.put(getString(R.string.known_osa), S.yes);
        else
            jsonObject1.put(getString(R.string.known_osa), "");

        if (jsonObject1.length() != 0) {
            jsonmain.put(S.api_qi_measures, jsonObject1);
            return true;
        } else
            return false;
    }

    @Override
    public void onBackPressed() {
        try {
           /* jsonmain = new JSONObject();
            jsonmain.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
            jsonmain.put(S.api_record_id, patient_id);

            JSONObject jsonObject1 = new JSONObject();

            if (mBinding.checkboxSystem.isChecked())
                jsonObject1.put(getString(R.string.qtyinformationdetails), S.yes);
            if (mBinding.checkboxNoListedEvent.isChecked())
                jsonObject1.put(getString(R.string.no_listed_event), S.yes);

            if (mBinding.checkboxAbove.isChecked())
                jsonObject1.put(getString(R.string.addmission_temperature), getString(R.string.above_equal));
            if (mBinding.checkboxBelow.isChecked())
                jsonObject1.put(getString(R.string.addmission_temperature), getString(R.string.above_equal));
            if (mBinding.checkboxHandoff.isChecked())
                jsonObject1.put(getString(R.string.pacu_direct_handoff), getString(R.string.per_handoff));
            if (mBinding.checkboxNotPerHandoff.isChecked())
                jsonObject1.put(getString(R.string.pacu_direct_handoff), getString(R.string.not_per_handoff));
            if (!admission_pain_store.equals(""))
                jsonObject1.put(getString(R.string.admission_pain_store), admission_pain_store);
            if (!osa_score.equals(""))
                jsonObject1.put(getString(R.string.osa_score), osa_score);
            if (mBinding.checkboxLnown.isChecked())
                jsonObject1.put(getString(R.string.known_osa), S.yes);*/

            if (Util.isNetworkConnected(context)) {
                if (prepareQualityInformation()) {
                    type = "back";
                    Util.confirmDialog(context, getString(R.string.do_you_want_to_save), getString(R.string.yes), getString(R.string.no), type, 0);
                } else
                    finish();
            } else
                finish();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}

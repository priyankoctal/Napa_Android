package com.octalsoftaware.archi.views.activity.chargeinformation;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.data.DataManager;
import com.octalsoftaware.archi.data.events.ErrorEvent;
import com.octalsoftaware.archi.data.events.SucessEvent;
import com.octalsoftaware.archi.databinding.ChargeInformationBinding;
import com.octalsoftaware.archi.models.ChargeInformationModal;
import com.octalsoftaware.archi.models.ProceduresModal;
import com.octalsoftaware.archi.models.chargemodal.ChargeInformationPrefillmodalData;
import com.octalsoftaware.archi.models.chargemodal.DiagnoseList;
import com.octalsoftaware.archi.models.chargemodal.ProcedureList;
import com.octalsoftaware.archi.realm.Database;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.Util;
import com.octalsoftaware.archi.utils.constants.I;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.activity.BaseActivity;
import com.octalsoftaware.archi.views.activity.LocationActivity;
import com.octalsoftaware.archi.views.adapter.ChargeInformationAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.octalsoftaware.archi.R.id.checkbox_system;
import static com.octalsoftaware.archi.R.id.txt_five;
import static com.octalsoftaware.archi.R.string.crna;
import static com.octalsoftaware.archi.utils.Util.setTypefaceBold;
import static com.octalsoftaware.archi.utils.Util.setTypefaceRegular;
import static com.octalsoftaware.archi.utils.Util.showDatePicker;
import static com.octalsoftaware.archi.utils.Util.showNoteDialog;
import static com.octalsoftaware.archi.utils.Util.showTimePicker;
import static com.octalsoftaware.archi.utils.constants.S.user_id;

/**
 * Created by anandj on 4/18/2017.
 */

public class ChargeInformation extends BaseActivity implements View.OnClickListener {

    private String TAG = ChargeInformation.class.getSimpleName();
    @Nullable
    private ChargeInformationBinding mBinding = null;
    @Nullable
    private Context context = null;
    @Nullable
    private ArrayList<ChargeInformationModal> chargeInformationModalArrayList = null;
    @Nullable
    private ChargeInformationAdapter chargeInformationAdapter = null;
    private String note_str = "";
    private String patient_id = "";
    private String patient_name = "";
    private MyProgressDialog progressDialog;
    // private String api_name = "";
    @Nullable
    private HashMap<String, String> dialogsis_check_id = null;
    @Nullable
    private ArrayList<ProceduresModal> proceduresModalArrayList = null;
    @NonNull
    private String asa_physical_status = "";
    @NonNull
    private ArrayList<ChargeInformationPrefillmodalData> chargeInformationPrefillmodalDataArrayList = new ArrayList<>();
    @Nullable
    private Database database;
    @NonNull
    private String api_calling_place = "activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization context
        context = this;

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.charge_information);

        // set Title
        mBinding.mainToolbar.toolbarheading.setText(getString(R.string.charge_information));


        mBinding.nestedScrollView.requestFocus();

        dialogsis_check_id = new HashMap<>();
        proceduresModalArrayList = new ArrayList<>();

        patient_id = getIntent().getStringExtra(S.patient_details);
        patient_name = getIntent().getStringExtra(S.patient_name);

        database = new Database(context);
        // set patient name
        mBinding.txtPatientname.setText(patient_name);

        // set font
        setTypeface();

        // onclick
        setOnClickListner();

        iniiadpter();

        // get past date data
        try {
            if (Util.isNetworkConnected(context)) {
                // get prefill Data
                getPreFillData();
            } else {
                showPrefillData(database.getChargeDetails(patient_id, S.api_get_saved_charge));
            }


        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }


    }

    private void getNote() {
        try {
            DataManager.getNotes(ConvertJsonToMap.jsonToMap(Util.getNoteParameter(MySharedPreferences.getPreferences(context, user_id), patient_id, S.charge_status)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTypeface() {
        mBinding.txtPatientname.setTypeface(setTypefaceBold(context));
        mBinding.txtAsaphysicalstatus.setTypeface(setTypefaceBold(context));
        mBinding.txtAnesthesiatime.setTypeface(setTypefaceBold(context));
        mBinding.txtPreoptime.setTypeface(setTypefaceRegular(context));
        mBinding.txtSubmit.setTypeface(setTypefaceBold(context));
    }

    private void setOnClickListner() {
        mBinding.mainToolbar.toggleIcon.setOnClickListener(this);
        mBinding.mainToolbar.imgEdit.setOnClickListener(this);
        mBinding.txtOne.setOnClickListener(this);
        mBinding.txtTwo.setOnClickListener(this);
        mBinding.txtThree.setOnClickListener(this);
        mBinding.txtFour.setOnClickListener(this);
        mBinding.txtFive.setOnClickListener(this);
        mBinding.txtSix.setOnClickListener(this);
        mBinding.txtStatrtime.setOnClickListener(this);
        mBinding.txtEndtime.setOnClickListener(this);
        mBinding.txtOrtime.setOnClickListener(this);
        mBinding.pacuTime.setOnClickListener(this);
        mBinding.txtSubmit.setOnClickListener(this);
        mBinding.txtStatrtimebottom.setOnClickListener(this);
        mBinding.txtEndtimebottom.setOnClickListener(this);
        mBinding.checkboxSystem.setOnClickListener(this);
    }

    public void sendNote(@NonNull JSONObject jsonObject) throws JSONException {

        progressDialog = Util.showPrograsssDialog(context);
        DataManager.addNotes(ConvertJsonToMap.jsonToMap(jsonObject));
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.txt_endtimebottom:
                String start_time = mBinding.txtStatrtimebottom.getText().toString();
                if (TextUtils.isEmpty(start_time))
                    Util.showToast(context, getString(R.string.select_start_date));
                else
                    showDatePicker(mBinding.txtEndtimebottom, context, mBinding.txtStatrtimebottom.getText().toString(), "");
                break;
            case R.id.txt_statrtimebottom:
                mBinding.txtStatrtimebottom.setText("");
                showDatePicker(mBinding.txtStatrtimebottom, context, "", "");
                break;
            case R.id.toggle_icon:
               onBackPressed();
                break;
            case R.id.img_edit:
                // show dialog note
                showNoteDialog(context, note_str, patient_id, S.charge_status);
                break;
            case R.id.txt_submit:
                try {
                    saveChargeInformation("activity");
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
              /*  Intent intent = new Intent(context, InvasiveLinesAndSpecialServices.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.patient_name, patient_name);
                intent.putExtra(S.anesthesiologist, chargeInformationModalArrayList.get(1).getDoctor_name());
                startActivity(intent);*/
                break;

            case R.id.txt_one:
                mBinding.txtOne.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
                mBinding.txtOne.setBackgroundResource(R.drawable.textviewfillsqr);

                mBinding.txtTwo.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtTwo.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtThree.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtThree.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFour.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFour.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFive.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFive.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtSix.setBackgroundResource(R.drawable.textviewsquire);

                asa_physical_status = "1";
                mBinding.checkboxSystem.setChecked(false);
                break;
            case R.id.txt_two:
                mBinding.txtOne.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtOne.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtTwo.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
                mBinding.txtTwo.setBackgroundResource(R.drawable.textviewfillsqr);

                mBinding.txtThree.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtThree.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFour.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFour.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFive.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFive.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtSix.setBackgroundResource(R.drawable.textviewsquire);

                asa_physical_status = "2";
                mBinding.checkboxSystem.setChecked(false);
                break;
            case R.id.txt_three:
                mBinding.txtOne.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtOne.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtTwo.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtTwo.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtThree.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
                mBinding.txtThree.setBackgroundResource(R.drawable.textviewfillsqr);

                mBinding.txtFour.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFour.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFive.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFive.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtSix.setBackgroundResource(R.drawable.textviewsquire);

                asa_physical_status = "3";
                mBinding.checkboxSystem.setChecked(false);
                break;
            case R.id.txt_four:
                mBinding.txtOne.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtOne.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtTwo.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtTwo.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtThree.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtThree.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFour.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
                mBinding.txtFour.setBackgroundResource(R.drawable.textviewfillsqr);

                mBinding.txtFive.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFive.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtSix.setBackgroundResource(R.drawable.textviewsquire);

                asa_physical_status = "4";
                mBinding.checkboxSystem.setChecked(false);
                break;
            case txt_five:
                mBinding.txtOne.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtOne.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtTwo.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtTwo.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtThree.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtThree.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFour.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFour.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFive.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
                mBinding.txtFive.setBackgroundResource(R.drawable.textviewfillsqr);

                mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtSix.setBackgroundResource(R.drawable.textviewsquire);

                asa_physical_status = "5";
                mBinding.checkboxSystem.setChecked(false);
                break;
            case R.id.txt_six:
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

                mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
                mBinding.txtSix.setBackgroundResource(R.drawable.textviewfillsqr);

                asa_physical_status = "6";
                mBinding.checkboxSystem.setChecked(false);
                break;
            case checkbox_system:
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

                mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtSix.setBackgroundResource(R.drawable.textviewsquire);

                asa_physical_status = "E";
                break;
            case R.id.txt_statrtime:
                mBinding.txtEndtime.setText("");
                mBinding.txtOrtime.setText("");
                showTimePicker(mBinding.txtStatrtime, context, "", "", "", "");
                break;
            case R.id.txt_endtime:
                if (mBinding.txtStatrtime.getText().toString().trim().equals(""))
                    Util.showToast(context, getString(R.string.first_enter_start_time));
                else {
                    String[] arr = Util.getHourOfTime(mBinding.txtStatrtime.getText().toString());
                    mBinding.txtOrtime.setText("");
                    showTimePicker(mBinding.txtEndtime, context, arr[0], arr[1], "", "");
                }
                break;
            case R.id.txt_ortime:
                if (!mBinding.txtStatrtime.getText().toString().trim().equals("") && !mBinding.txtEndtime.getText().toString().trim().equals("")) {
                    String[] arr = Util.getHourOfTime(mBinding.txtStatrtime.getText().toString());
                    String[] arr1 = Util.getHourOfTime(mBinding.txtEndtime.getText().toString());
                    showTimePicker(mBinding.txtOrtime, context, arr[0], arr[1], arr1[0], arr1[1]);
                } else
                    Util.showToast(context, getString(R.string.first_enter_pre_op_time));
                break;
            case R.id.pacu_time:
                if (!mBinding.txtOrtime.getText().toString().trim().equals("")) {
                    String[] arr = Util.getHourOfTime(mBinding.txtOrtime.getText().toString());
                    String[] arr1 = Util.getHourOfTime(mBinding.txtEndtime.getText().toString());
                    showTimePicker(mBinding.pacuTime, context, arr[0], arr[1], arr1[0], arr1[1]);
                } else
                    Util.showToast(context, getString(R.string.first_enter_or_time));


                break;
        }
    }

    public void saveChargeInformation(@NonNull String activty) throws JSONException {


        JSONObject jsonObject = new JSONObject();
       /* jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
        jsonObject.put(S.api_record_id, patient_id);*/
        jsonObject.put(S.surgeon, chargeInformationModalArrayList.get(0).getId());
        jsonObject.put(S.anesthesiologist, chargeInformationModalArrayList.get(1).getId());
        jsonObject.put(S.api_CRNA, chargeInformationModalArrayList.get(2).getId());
        jsonObject.put(S.srna, chargeInformationModalArrayList.get(4).getId());
        jsonObject.put(S.api_Resident, chargeInformationModalArrayList.get(3).getId());
        jsonObject.put(S.api_position, chargeInformationModalArrayList.get(7).getDoctor_name());
        jsonObject.put(S.api_modeofAnesthesia, chargeInformationModalArrayList.get(8).getDoctor_name());
        jsonObject.put(S.api_asa, asa_physical_status);
        jsonObject.put(S.api_end_date, mBinding.txtEndtimebottom.getText().toString());
        jsonObject.put(S.api_start_time, mBinding.txtStatrtime.getText().toString());
        jsonObject.put(S.api_end_time, mBinding.txtEndtime.getText().toString());
        jsonObject.put(S.api_start_date, mBinding.txtStatrtimebottom.getText().toString());
        jsonObject.put(S.api_adjust_time, mBinding.txtOrtime.getText().toString());
        jsonObject.put(S.api_reason_adjust, mBinding.edtFounditem.getText().toString());
        jsonObject.put(S.api_pacu_time, mBinding.pacuTime.getText().toString());

        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray1 = new JSONArray();

        for (Object o : dialogsis_check_id.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put(S.api_diagnose_id, pair.getKey());
            jsonArray.put(jsonObject1);
        }

        for (int i = 0; i < proceduresModalArrayList.size(); i++) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put(S.api_procedure_codes, proceduresModalArrayList.get(i).getCode_statis());
            jsonObject1.put(S.api_modifier1, proceduresModalArrayList.get(i).getMode1());
            jsonObject1.put(S.api_modifier2, proceduresModalArrayList.get(i).getMode2());
            jsonObject1.put(S.api_modifier3, proceduresModalArrayList.get(i).getMode3());
            jsonObject1.put(S.api_icd1, proceduresModalArrayList.get(i).getIcd1());
            jsonObject1.put(S.api_icd2, proceduresModalArrayList.get(i).getIcd2());
            jsonObject1.put(S.api_icd3, proceduresModalArrayList.get(i).getIcd3());
            jsonObject1.put(S.api_icd4, proceduresModalArrayList.get(i).getIcd4());

            jsonArray1.put(jsonObject1);
        }
        jsonObject.put(S.api_diagnoses, jsonArray);
        jsonObject.put(S.api_procedures, jsonArray1);


        if (jsonObject.length() != 0) {
            progressDialog = Util.showPrograsssDialog(context);
            jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
            jsonObject.put(S.api_record_id, patient_id);
            if (Util.isNetworkConnected(context)) {
                DataManager.saveChargeInformation(ConvertJsonToMap.toMap(jsonObject));
               /* if (database.getSavedSatus(patient_id, S.api_save_qi, getString(R.string.charge_information)))
                    database.updateChargeAndQiDetails(patient_id, jsonObject, S.api_save_charge, getString(R.string.charge_information));
                else
                    database.saveChargeAndQiDetails(patient_id, jsonObject, S.api_save_charge, 1, getString(R.string.charge_information));*/
            } else {
                Util.showToast(context, getString(R.string.no_internet_connection));
                if(activty.equalsIgnoreCase("activity"))
                          startAnotherActivity();
                else
                    finish();
            }


        } else
            startAnotherActivity();


        //params : user_id, record_id, surgeon, anesthesiologist, crna, srna, resident, position, modeofAnesthesia, asa, end_date, start_time, end_time, start_date, adjust_time, reason_adjust, pacu_time, diagnoses[] = diagnose_id, diagnosis_desc, diagnosis_code, diagnosis_code_type, procedures[] = procedure_code, modifier1, modifier2, modifier3, icd1, icd2, icd3, icd4
    }

    @Override
    public void initEvent() {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void iniiadpter() {
        chargeInformationModalArrayList = new ArrayList<>();
        chargeInformationAdapter = new ChargeInformationAdapter(chargeInformationModalArrayList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mBinding.recylerview.setLayoutManager(mLayoutManager);
        mBinding.recylerview.setItemAnimator(new DefaultItemAnimator());
        mBinding.recylerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mBinding.recylerview.setAdapter(chargeInformationAdapter);

        mBinding.recylerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mBinding.recylerview.setNestedScrollingEnabled(false);


        fillData();
    }

    private void fillData() {
        ChargeInformationModal modal = new ChargeInformationModal();
        modal.setId("");
        modal.setName(getString(R.string.surgon));
        modal.setDoctor_name("");

        chargeInformationModalArrayList.add(modal);

        ChargeInformationModal modal1 = new ChargeInformationModal();
        modal1.setId("");
        modal1.setName(getString(R.string.anesthesiologist));
        modal1.setDoctor_name("");

        chargeInformationModalArrayList.add(modal1);

        ChargeInformationModal modal2 = new ChargeInformationModal();
        modal2.setId("");
        modal2.setName(getString(crna));
        modal2.setDoctor_name("");

        chargeInformationModalArrayList.add(modal2);

        ChargeInformationModal modal3 = new ChargeInformationModal();
        modal3.setId("");
        modal3.setName(getString(R.string.resident));
        modal3.setDoctor_name("");

        chargeInformationModalArrayList.add(modal3);

        ChargeInformationModal modal4 = new ChargeInformationModal();
        modal4.setId("");
        modal4.setName(getString(R.string.srna));
        modal4.setDoctor_name("");

        chargeInformationModalArrayList.add(modal4);

        ChargeInformationModal modal5 = new ChargeInformationModal();
        modal5.setId("1");
        modal5.setName(getString(R.string.procedure));
        modal5.setDoctor_name("");

        chargeInformationModalArrayList.add(modal5);

        ChargeInformationModal modal6 = new ChargeInformationModal();
        modal6.setId("");
        modal6.setName(getString(R.string.diagnosis));
        modal6.setDoctor_name("");

        chargeInformationModalArrayList.add(modal6);

        ChargeInformationModal modal7 = new ChargeInformationModal();
        modal7.setId("");
        modal7.setName(getString(R.string.position));
        modal7.setDoctor_name("");

        chargeInformationModalArrayList.add(modal7);

        ChargeInformationModal modal8 = new ChargeInformationModal();
        modal8.setId("");
        modal8.setName(getString(R.string.mode_of_anesthesia));
        modal8.setDoctor_name("");

        chargeInformationModalArrayList.add(modal8);


        chargeInformationAdapter.notifyDataSetChanged();
    }

    public void getChargeInformation(int pos) {
        Intent intent = null;
        switch (pos) {
            case 0:
                intent = new Intent(context, LocationActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.type, S.surgeon);
                startActivityForResult(intent, I.SURGEON);
                break;
            case 1:
                intent = new Intent(context, LocationActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.type, S.anesthesiologist);
                startActivityForResult(intent, I.ANESTHESIOLOGIST);
                break;
            case 2:
                intent = new Intent(context, LocationActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.type, S.CRNA);
                startActivityForResult(intent, I.CRNA);
                break;
            case 3:
                intent = new Intent(context, LocationActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.type, S.Resident);
                startActivityForResult(intent, I.RESIDENT);
                break;
            case 4:
                intent = new Intent(context, LocationActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.type, S.srna);
                startActivityForResult(intent, I.SRNA);
                break;
            case 5:
                intent = new Intent(context, ProceduresActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.type, S.procedures);
                startActivityForResult(intent, I.PROCEDURES);
                break;
            case 6:
                intent = new Intent(context, DiagnosisActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.type, S.Diagnose);
                intent.putExtra(S.check_id, dialogsis_check_id);
                startActivityForResult(intent, I.DIAGNOSE);
                break;
            case 7:
                intent = new Intent(context, LocationActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.type, S.Position);
                startActivityForResult(intent, I.POSITION);
                break;
            case 8:
                intent = new Intent(context, LocationActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(S.type, S.ModeOA);
                startActivityForResult(intent, I.MODEOFANESTHESIA);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getStringExtra(S.start_activity_result) != null) {
            if (requestCode == I.SURGEON) {
                chargeInformationModalArrayList.get(0).setDoctor_name(data.getStringExtra(S.start_activity_result));
                chargeInformationModalArrayList.get(0).setId(data.getStringExtra(S.patient_id));
            } else if (requestCode == I.ANESTHESIOLOGIST) {
                chargeInformationModalArrayList.get(1).setDoctor_name(data.getStringExtra(S.start_activity_result));
                chargeInformationModalArrayList.get(1).setId(data.getStringExtra(S.patient_id));
            } else if (requestCode == I.CRNA) {
                chargeInformationModalArrayList.get(2).setDoctor_name(data.getStringExtra(S.start_activity_result));
                chargeInformationModalArrayList.get(2).setId(data.getStringExtra(S.patient_id));
            } else if (requestCode == I.RESIDENT) {
                chargeInformationModalArrayList.get(3).setDoctor_name(data.getStringExtra(S.start_activity_result));
                chargeInformationModalArrayList.get(3).setId(data.getStringExtra(S.patient_id));
            } else if (requestCode == I.SRNA) {
                chargeInformationModalArrayList.get(4).setDoctor_name(data.getStringExtra(S.start_activity_result));
                chargeInformationModalArrayList.get(4).setId(data.getStringExtra(S.patient_id));
            } else if (requestCode == I.PROCEDURES) {
                proceduresModalArrayList = (ArrayList<ProceduresModal>) data.getSerializableExtra(S.check_id);
            } else if (requestCode == I.DIAGNOSE) {
                HashMap<String, String> hashMap = (HashMap<String, String>) data.getSerializableExtra(S.check_id);
                dialogsis_check_id.clear();
                dialogsis_check_id.putAll(hashMap);
                chargeInformationModalArrayList.get(6).setDoctor_name("");
                Iterator it = hashMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    if (chargeInformationModalArrayList.get(6).getDoctor_name().equals(""))
                        chargeInformationModalArrayList.get(6).setDoctor_name(pair.getValue() + " .");
                    else
                        chargeInformationModalArrayList.get(6).setDoctor_name(chargeInformationModalArrayList.get(6).getDoctor_name() + "\n" + pair.getValue() + " .");
                    it.remove(); // avoids a ConcurrentModificationException
                }

            } else if (requestCode == I.POSITION) {
                chargeInformationModalArrayList.get(7).setDoctor_name(data.getStringExtra(S.start_activity_result));
                chargeInformationModalArrayList.get(7).setId(data.getStringExtra(S.patient_id));
            } else if (requestCode == I.MODEOFANESTHESIA)
                chargeInformationModalArrayList.get(8).setDoctor_name(data.getStringExtra(S.start_activity_result));

            chargeInformationAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        event.register(this);

        try {
            // get Previous notes
            getNote();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        event.unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void getPreFillData() throws JSONException {
        progressDialog = Util.showPrograsssDialog(context);
        DataManager.getSaveCharge(ConvertJsonToMap.jsonToMap(Util.getDefaultJson(context, patient_id)));
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
                    } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_save_charge)) {
                        Util.showToast(context, jsonObject.getString(S.message));
                        if(api_calling_place.equalsIgnoreCase("activity"))
                              startAnotherActivity();
                        else
                            finish();
                    } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_get_saved_charge)) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject(S.data);
                        chargeInformationPrefillmodalDataArrayList.clear();
                        ChargeInformationPrefillmodalData data = new ChargeInformationPrefillmodalData();
                        data.setRecordId(jsonObject1.getString(S.api_record_id));
                        data.setSurgeon(jsonObject1.getString(S.surgeon));
                        data.setAnesthesiologist(jsonObject1.getString(S.anesthesiologist));
                        data.setCrna(jsonObject1.getString(S.api_CRNA));
                        data.setSrna(jsonObject1.getString(S.srna));
                        data.setResident(jsonObject1.getString(S.api_Resident));
                        data.setPosition(jsonObject1.getString(S.api_position));
                        data.setModeofAnesthesia(jsonObject1.getString(S.api_modeofAnesthesia));
                        data.setAsa(jsonObject1.getString(S.api_asa));
                        data.setStartDate(jsonObject1.getString(S.api_start_date));
                        data.setEndDate(jsonObject1.getString(S.api_end_date));
                        data.setStartTime(jsonObject1.getString(S.api_start_time));
                        data.setEndTime(jsonObject1.getString(S.api_end_time));
                        data.setAdjustTime(jsonObject1.getString(S.api_adjust_time));
                        data.setIsAdjustTime(jsonObject1.getString(S.api_is_adjust_time));
                        data.setReasonAdjust(jsonObject1.getString(S.api_reason_adjust));
                        data.setPacuTime(jsonObject1.getString(S.api_pacu_time));

                        JSONArray diagnosList = jsonObject1.getJSONArray(S.api_DiagnoseList);
                        ArrayList<DiagnoseList> diagnoseListArrayList = new ArrayList<>();
                        for (int i = 0; i < diagnosList.length(); i++) {
                            JSONObject jsonObject2 = diagnosList.getJSONObject(i);
                            DiagnoseList diagnoseList = new DiagnoseList();
                            diagnoseList.setId(jsonObject2.getString(S.api_id));
                            diagnoseList.setDiagnoseId(jsonObject2.getString(S.api_diagnose_id));
                            diagnoseList.setDiagnosisCode(jsonObject2.getString(S.api_diagnosis_desc));
                            diagnoseList.setDiagnosisCode(jsonObject2.getString(S.api_diagnosis_code));


                            dialogsis_check_id.put(jsonObject2.getString(S.api_diagnose_id), jsonObject2.getString(S.api_diagnose_id));
                            diagnoseListArrayList.add(diagnoseList);
                        }
                        data.setDiagnoseList(diagnoseListArrayList);
                        JSONArray procedureList = jsonObject1.getJSONArray(S.api_ProcedureList);
                        ArrayList<ProcedureList> procedureListArrayList = new ArrayList<>();
                        for (int i = 0; i < procedureList.length(); i++) {
                            JSONObject jsonObject2 = procedureList.getJSONObject(i);
                            ProcedureList procedureList1 = new ProcedureList();
                            procedureList1.setId(jsonObject2.getString(S.api_id));
                            procedureList1.setProcedureCode(jsonObject2.getString(S.api_procedure_codes));
                            procedureList1.setProcedureCodeType(jsonObject2.getString(S.api_procedure_code_type));
                            procedureList1.setIcd1(jsonObject2.getString(S.api_icd1));
                            procedureList1.setIcd2(jsonObject2.getString(S.api_icd2));
                            procedureList1.setIcd3(jsonObject2.getString(S.api_icd3));
                            procedureList1.setIcd4(jsonObject2.getString(S.api_icd4));
                            procedureList1.setModifier1(jsonObject2.getString(S.api_modifier1));
                            procedureList1.setModifier2(jsonObject2.getString(S.api_modifier2));
                            procedureList1.setModifier3(jsonObject2.getString(S.api_modifier3));

                            procedureListArrayList.add(procedureList1);
                        }
                        data.setProcedureList(procedureListArrayList);
                        chargeInformationPrefillmodalDataArrayList.add(data);
                        showPrefillData(chargeInformationPrefillmodalDataArrayList);
                        if (database.getSavedSatus(patient_id, S.api_get_saved_charge))
                            database.updateQiDetails(patient_id, jsonObject, S.api_get_saved_charge);
                        else
                            database.savedQiDetails(patient_id, jsonObject, S.api_get_saved_charge);
                    }
                } else {
                    if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_save_charge))
                        Util.showToast(context, jsonObject.getString(S.message));
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

    private void startAnotherActivity() {
        Intent intent = new Intent(context, InvasiveLinesAndSpecialServices.class);
        intent.putExtra(S.patient_details, patient_id);
        intent.putExtra(S.patient_name, patient_name);
        intent.putExtra(S.anesthesiologist, chargeInformationModalArrayList.get(1).getDoctor_name());
        startActivity(intent);
    }

    private void showPrefillData(@NonNull ArrayList<ChargeInformationPrefillmodalData> chargeInformationPrefillmodalDataArrayList) {
        chargeInformationModalArrayList.get(0).setDoctor_name(chargeInformationPrefillmodalDataArrayList.get(0).getSurgeon());
        // chargeInformationModalArrayList.get(0).setId(chargeInformationPrefillmodalDataArrayList.get(0).getSurgeon());
        chargeInformationModalArrayList.get(1).setDoctor_name(chargeInformationPrefillmodalDataArrayList.get(0).getAnesthesiologist());
        chargeInformationModalArrayList.get(2).setDoctor_name(chargeInformationPrefillmodalDataArrayList.get(0).getCrna());
        chargeInformationModalArrayList.get(3).setDoctor_name(chargeInformationPrefillmodalDataArrayList.get(0).getResident());
        chargeInformationModalArrayList.get(4).setDoctor_name(chargeInformationPrefillmodalDataArrayList.get(0).getResident());
        chargeInformationModalArrayList.get(7).setDoctor_name(chargeInformationPrefillmodalDataArrayList.get(0).getPosition());
        chargeInformationModalArrayList.get(8).setDoctor_name(chargeInformationPrefillmodalDataArrayList.get(0).getModeofAnesthesia());
        List<DiagnoseList> diagnoseList = chargeInformationPrefillmodalDataArrayList.get(0).getDiagnoseList();
        for (int i = 0; i < diagnoseList.size(); i++) {
            if (i == 0)
                chargeInformationModalArrayList.get(6).setDoctor_name(diagnoseList.get(i).getDiagnosisCode());//chargeInformationModalArrayList.get(6).setDoctor_name(pair.getValue() + " .");
            else
                chargeInformationModalArrayList.get(6).setDoctor_name(diagnoseList.get(i).getDiagnosisCode() + "\n" + diagnoseList.get(i).getDiagnosisCode() + " .");
            //chargeInformationModalArrayList.get(6).setDoctor_name(chargeInformationModalArrayList.get(6).getDoctor_name() + "\n" + pair.getValue() + " .");
        }
        chargeInformationAdapter.notifyDataSetChanged();

        mBinding.txtStatrtime.setText(chargeInformationPrefillmodalDataArrayList.get(0).getStartTime());
        mBinding.txtEndtime.setText(chargeInformationPrefillmodalDataArrayList.get(0).getEndTime());
        mBinding.txtStatrtimebottom.setText(chargeInformationPrefillmodalDataArrayList.get(0).getStartDate());
        mBinding.txtEndtimebottom.setText(chargeInformationPrefillmodalDataArrayList.get(0).getEndDate());
        mBinding.edtFounditem.setText(chargeInformationPrefillmodalDataArrayList.get(0).getReasonAdjust());
        mBinding.txtOrtime.setText(chargeInformationPrefillmodalDataArrayList.get(0).getAdjustTime());
        mBinding.pacuTime.setText(chargeInformationPrefillmodalDataArrayList.get(0).getPacuTime());
        if (!chargeInformationPrefillmodalDataArrayList.get(0).getAsa().equalsIgnoreCase("")) {
            String asa = chargeInformationPrefillmodalDataArrayList.get(0).getAsa();
            if (asa.equalsIgnoreCase("1")) {
                mBinding.txtOne.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
                mBinding.txtOne.setBackgroundResource(R.drawable.textviewfillsqr);

                mBinding.txtTwo.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtTwo.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtThree.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtThree.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFour.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFour.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFive.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFive.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtSix.setBackgroundResource(R.drawable.textviewsquire);

                asa_physical_status = "1";
                mBinding.checkboxSystem.setChecked(false);
            } else if (asa.equalsIgnoreCase("2")) {
                mBinding.txtOne.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtOne.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtTwo.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
                mBinding.txtTwo.setBackgroundResource(R.drawable.textviewfillsqr);

                mBinding.txtThree.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtThree.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFour.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFour.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFive.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFive.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtSix.setBackgroundResource(R.drawable.textviewsquire);

                asa_physical_status = "2";
                mBinding.checkboxSystem.setChecked(false);
            } else if (asa.equalsIgnoreCase("3")) {
                mBinding.txtOne.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtOne.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtTwo.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtTwo.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtThree.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
                mBinding.txtThree.setBackgroundResource(R.drawable.textviewfillsqr);

                mBinding.txtFour.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFour.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFive.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFive.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtSix.setBackgroundResource(R.drawable.textviewsquire);

                asa_physical_status = "3";
                mBinding.checkboxSystem.setChecked(false);
            } else if (asa.equalsIgnoreCase("4")) {
                mBinding.txtOne.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtOne.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtTwo.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtTwo.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtThree.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtThree.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFour.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
                mBinding.txtFour.setBackgroundResource(R.drawable.textviewfillsqr);

                mBinding.txtFive.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFive.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtSix.setBackgroundResource(R.drawable.textviewsquire);

                asa_physical_status = "4";
                mBinding.checkboxSystem.setChecked(false);
            } else if (asa.equalsIgnoreCase("5")) {
                mBinding.txtOne.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtOne.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtTwo.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtTwo.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtThree.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtThree.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFour.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtFour.setBackgroundResource(R.drawable.textviewsquire);

                mBinding.txtFive.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
                mBinding.txtFive.setBackgroundResource(R.drawable.textviewfillsqr);

                mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtSix.setBackgroundResource(R.drawable.textviewsquire);

                asa_physical_status = "5";
                mBinding.checkboxSystem.setChecked(false);
            } else if (asa.equalsIgnoreCase("6")) {
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

                mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
                mBinding.txtSix.setBackgroundResource(R.drawable.textviewfillsqr);

                asa_physical_status = "6";
                mBinding.checkboxSystem.setChecked(false);
            } else {
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

                mBinding.txtSix.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
                mBinding.txtSix.setBackgroundResource(R.drawable.textviewsquire);

                asa_physical_status = "E";
                mBinding.checkboxSystem.setChecked(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(Util.isNetworkConnected(context)){
            api_calling_place = "back";
            Util.confirmDialog(context, getString(R.string.do_you_want_to_save), getString(R.string.yes), getString(R.string.no), "", 0);
        }else
            finish();


    }
}

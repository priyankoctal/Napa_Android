package com.octalsoftaware.archi.views.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.data.DataManager;
import com.octalsoftaware.archi.data.events.ErrorEvent;
import com.octalsoftaware.archi.data.events.SucessEvent;
import com.octalsoftaware.archi.databinding.PatientDetailsBinding;
import com.octalsoftaware.archi.models.HomePageModal;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.SimpleGestureFilter;
import com.octalsoftaware.archi.utils.Util;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.activity.chargeinformation.ChargeInformation;
import com.octalsoftaware.archi.views.activity.images.ImagesListActivity;
import com.octalsoftaware.archi.views.activity.qualityinformation.QualityInformation;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import static com.octalsoftaware.archi.utils.Util.confirmDialog;
import static com.octalsoftaware.archi.utils.Util.showNoteDialog;
import static com.octalsoftaware.archi.utils.constants.S.patient_name;
import static com.octalsoftaware.archi.utils.constants.S.user_id;

/**
 * Created by anandj on 4/17/2017.
 */

public class PatientDetailsActivity extends BaseActivity implements View.OnClickListener {


    private PatientDetailsBinding mBinding;
    @Nullable
    private Context context = null;
    @Nullable
    private MyProgressDialog progressDialog = null;
    private String TAG = PatientDetailsActivity.class.getSimpleName();
    private String note_str = "";
    private String patient_id = "";
    private boolean note_status, send_note_statsu;
    Gson gson;
    HomePageModal homePageModal;
    private SimpleGestureFilter detector;
    private float x1, x2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // initialization context
        context = this;
        note_status = true;
        send_note_statsu = false;

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.patient_details);

        gson = new Gson();
        homePageModal = gson.fromJson(getIntent().getStringExtra(S.patient_details), HomePageModal.class);
        patient_id = homePageModal.getId();


        setTyface();
        setOnClickListner();


        // get Previous notes
        getNote();
    }

    @Override
    public void initEvent() {

    }

    private void getNote() {
        try {
            DataManager.getNotes(ConvertJsonToMap.jsonToMap(Util.getNoteParameter(MySharedPreferences.getPreferences(context, user_id), homePageModal.getId(), S.patien_detatisl_status)));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void setAutoFillData() {
        ///    Gson gson = new Gson();
       /* HomePageModal homePageModal = gson.fromJson(getIntent().getStringExtra(S.patient_details), HomePageModal.class);
        patient_id = homePageModal.getId();*/

        mBinding.txtPatientName.setText(homePageModal.getName());
        if (homePageModal.getGender().equalsIgnoreCase(S.gender_m))
            mBinding.txtGender.setText("(" + getString(R.string.male) + ")");
        else
            mBinding.txtGender.setText("(" + getString(R.string.female) + ")");

        mBinding.txtDob.setText(homePageModal.getDob());

        switch (Integer.parseInt(homePageModal.getStatus())) {
            case 1:
                //mBinding.llReopen.setDra(View.GONE);
                mBinding.imgReopen.setImageResource(R.drawable.reopengrey);
                mBinding.llReopen.setEnabled(false);
                break;
            case 6:
                mBinding.llReopen.setVisibility(View.VISIBLE);
                mBinding.llCharge.setVisibility(View.GONE);
                mBinding.llQi.setVisibility(View.GONE);
                mBinding.llImage.setVisibility(View.GONE);

                mBinding.imgReopen.setImageResource(R.drawable.reopengreen);
                mBinding.llReopen.setEnabled(true);
                break;
            default:
                mBinding.imgReopen.setImageResource(R.drawable.reopengrey);
                mBinding.llReopen.setEnabled(false);
        }

    }

    private void setTyface() {
        mBinding.txtPatientName.setTypeface(Util.setTypefaceBold(context));
        mBinding.txtGender.setTypeface(Util.setTypefaceRegular(context));
        mBinding.txtDob.setTypeface(Util.setTypefaceRegular(context));
        mBinding.txtBirthday.setTypeface(Util.setTypefaceRegular(context));
        mBinding.txtDos.setTypeface(Util.setTypefaceRegular(context));
        mBinding.txtDosText.setTypeface(Util.setTypefaceRegular(context));
        mBinding.txtMrnText.setTypeface(Util.setTypefaceRegular(context));
        mBinding.txtMrn.setTypeface(Util.setTypefaceRegular(context));
        mBinding.txtCharge.setTypeface(Util.setTypefaceRegular(context));
        mBinding.txtQi.setTypeface(Util.setTypefaceRegular(context));
        mBinding.txtImage.setTypeface(Util.setTypefaceRegular(context));
        mBinding.txtReopen.setTypeface(Util.setTypefaceRegular(context));
    }

    private void setOnClickListner() {
        mBinding.imgBack.setOnClickListener(this);
        mBinding.imgEdt.setOnClickListener(this);
        mBinding.llCharge.setOnClickListener(this);
        mBinding.llQi.setOnClickListener(this);
        mBinding.llImage.setOnClickListener(this);
        mBinding.llReopen.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.img_back:
               onBackPressed();
                break;
            case R.id.img_edt:
                // show note dialog
                showNoteDialog(context, note_str, patient_id, S.patien_detatisl_status);
                break;
            case R.id.ll_charge:
                Intent intent = new Intent(context, ChargeInformation.class);
                intent.putExtra(S.patient_details, patient_id);
                intent.putExtra(patient_name, homePageModal.getName());
                startActivity(intent);
                break;
            case R.id.ll_qi:
                Intent intent1 = new Intent(context, QualityInformation.class);
                intent1.putExtra(S.patient_details, patient_id);
                intent1.putExtra(patient_name, homePageModal.getName());
                startActivity(intent1);
                break;
            case R.id.ll_image:
                Intent intent2 = new Intent(context, ImagesListActivity.class);
                intent2.putExtra(S.patient_details, patient_id);
                intent2.putExtra(patient_name, homePageModal.getName());
                startActivity(intent2);
                break;
            case R.id.ll_reopen:
                confirmDialog(context, getString(R.string.reopen_confirm_message), getString(android.R.string.yes), getString(android.R.string.cancel), patient_id, 0);
                break;

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
        // set Data
        setAutoFillData();
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

    public void sendNote(@NonNull JSONObject jsonObject) throws JSONException {

        send_note_statsu = true;
        note_status = false;
        progressDialog = Util.showPrograsssDialog(context);
        DataManager.addNotes(ConvertJsonToMap.jsonToMap(jsonObject));
    }

    public void reopen(String patient_id) {
        try {

            note_status = false;
            send_note_statsu = false;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
            jsonObject.put(S.api_record_id, patient_id);

            if (Util.isNetworkConnected(context)) {
                progressDialog = Util.showPrograsssDialog(context);
                DataManager.reopenCase(ConvertJsonToMap.jsonToMap(jsonObject), 0);
            } else
                Util.showToast(context, getString(R.string.no_internet_connection));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }

    @Subscribe
    public void onSuccess(@NonNull SucessEvent event) {
        //  Log.d("patient", "event");
        Util.dismissPrograssDialog(progressDialog);
        String result = event.getResponce();
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result).getJSONObject(S.response);
                if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_get_notes)) {
                    if (jsonObject.getBoolean(S.status)) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject(S.data);
                        note_str = jsonObject1.getString(S.api_notes);
                    }
                } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_add_notes)) {
                    if (jsonObject.getBoolean(S.status)) {
                        Util.showToast(context, jsonObject.getString(S.message));
                        JSONObject jsonObject1 = jsonObject.getJSONObject(S.data);
                        note_str = jsonObject1.getString(S.api_notes);
                    } else
                        Util.showToast(context, jsonObject.getString(S.message));
                } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_reopen_record)) {
                    if (jsonObject.getBoolean(S.status)) {
                        Util.showToast(context, jsonObject.getString(S.message));
                        mBinding.imgReopen.setImageResource(R.drawable.reopengrey);
                        mBinding.llReopen.setEnabled(false);

                        mBinding.llCharge.setVisibility(View.VISIBLE);
                        mBinding.llQi.setVisibility(View.VISIBLE);
                        mBinding.llImage.setVisibility(View.VISIBLE);
                    } else
                        Util.showToast(context, jsonObject.getString(S.message));
                }

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }


        }
     /*   if (note_status) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result).getJSONObject(S.response);
                    if (jsonObject.getBoolean(S.status)) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject(S.data);
                        note_str = jsonObject1.getString(S.api_notes);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        } else if (send_note_statsu) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result).getJSONObject(S.response);
                    if (jsonObject.getBoolean(S.status)) {
                        Util.showToast(context, jsonObject.getString(S.message));
                        JSONObject jsonObject1 = jsonObject.getJSONObject(S.data);
                        note_str = jsonObject1.getString(S.api_notes);
                    } else
                        Util.showToast(context, jsonObject.getString(S.message));
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        } else {
            if (result != null) {
                try {
                    // parse data
                    JSONObject jsonObject = new JSONObject(result).getJSONObject(S.response);
                    if (jsonObject.getBoolean(S.status)) {
                        Util.showToast(context, jsonObject.getString(S.message));
                        mBinding.imgReopen.setImageResource(R.drawable.reopengrey);
                        mBinding.llReopen.setEnabled(false);

                        mBinding.llCharge.setVisibility(View.VISIBLE);
                        mBinding.llQi.setVisibility(View.VISIBLE);
                        mBinding.llImage.setVisibility(View.VISIBLE);
                    } else
                        Util.showToast(context, jsonObject.getString(S.message));

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }*/
    }

    @Subscribe
    public void onFailed(@NonNull ErrorEvent event) {
        Util.dismissPrograssDialog(progressDialog);
        Log.e(TAG, event.getMessage());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    onBackPressed();
                } else {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

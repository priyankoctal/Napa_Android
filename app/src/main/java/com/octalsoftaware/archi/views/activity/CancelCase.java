package com.octalsoftaware.archi.views.activity;

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
import com.octalsoftaware.archi.databinding.CancelCaseBinding;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.Util;
import com.octalsoftaware.archi.utils.Validations;
import com.octalsoftaware.archi.utils.constants.S;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import static com.octalsoftaware.archi.utils.Util.showNoteDialog;
import static com.octalsoftaware.archi.utils.constants.S.user_id;


/**
 * Created by anandj on 4/17/2017.
 */

public class CancelCase extends BaseActivity implements View.OnClickListener {

    private CancelCaseBinding mBinding;

    private static final String TAG = CancelCase.class.getSimpleName();
    @Nullable
    private Context context = null;
    @NonNull
    private String cancel_case_str = "";
    @Nullable
    private MyProgressDialog progressDialog = null;
    private boolean note_status, send_note_statsu;
    private String note_str = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization context
        context = this;
        note_status = true;
        send_note_statsu = false;
        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.cancel_case);

        // set Title
        mBinding.world1.toolbarheading.setText(getString(R.string.cancel_case));

        // set patient name
        mBinding.txtPatientname.setText(getIntent().getStringExtra(S.patient_name));


        setTypeface();
        setOnCLickListner();
        initEvent();


        mBinding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Validations.cancelCaseValdation(mBinding.checkboxBefore, mBinding.checkboxAfter, mBinding.checkboxSystem, mBinding.checkboxMedical, mBinding.checkboxPatient)) {
                        mBinding.txtSubmit.setEnabled(false);
                        note_status = false;
                        send_note_statsu = false;
                        // show progress dialog
                        progressDialog = new MyProgressDialog(context);
                        progressDialog.show();

                        String reasonStr = "";
                        if (mBinding.checkboxSystem.isChecked())
                            reasonStr = mBinding.checkboxSystem.getText().toString();
                        if (mBinding.checkboxMedical.isChecked()) {
                            if (!reasonStr.equals(""))
                                reasonStr = reasonStr.concat("," + mBinding.checkboxMedical.getText().toString());
                            else
                                reasonStr = mBinding.checkboxMedical.getText().toString();
                        }
                        if (mBinding.checkboxPatient.isChecked()) {
                            if (!reasonStr.equals(""))
                                reasonStr = reasonStr.concat("," + mBinding.checkboxPatient.getText().toString());
                            else
                                reasonStr = mBinding.checkboxPatient.getText().toString();
                        }
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
                        jsonObject.put(S.api_record_id, getIntent().getStringExtra(S.patient_id));
                        jsonObject.put(S.api_cancel_case, cancel_case_str);
                        jsonObject.put(S.api_reason, reasonStr);
                        if (Util.isNetworkConnected(context))
                            DataManager.cancelcase(ConvertJsonToMap.jsonToMap(jsonObject));
                        else
                            Util.showToast(context, getString(R.string.no_internet_connection));
                    } else {
                        Util.showToast(context, S.select_all_case);
                    }

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void onStart() {
        super.onStart();
        event.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        event.unregister(this);
    }

    public void sendNote(@NonNull JSONObject jsonObject) throws JSONException {

        send_note_statsu = true;
        note_status = false;
        progressDialog = Util.showPrograsssDialog(context);
        DataManager.addNotes(ConvertJsonToMap.jsonToMap(jsonObject));
    }

    // change font
    private void setTypeface() {
        mBinding.world1.toolbarheading.setTypeface(Util.setTypefaceRegular(context));
        mBinding.txtPatientname.setTypeface(Util.setTypefaceBold(context));
        mBinding.txtCancelcases.setTypeface(Util.setTypefaceBold(context));
        mBinding.txtSelectonly.setTypeface(Util.setTypefaceRegular(context));
        mBinding.checkboxBefore.setTypeface(Util.setTypefaceBold(context));
        mBinding.checkboxAfter.setTypeface(Util.setTypefaceBold(context));
        mBinding.txtReason.setTypeface(Util.setTypefaceBold(context));
        mBinding.txtSelectallapply.setTypeface(Util.setTypefaceRegular(context));
        mBinding.checkboxSystem.setTypeface(Util.setTypefaceBold(context));
        mBinding.checkboxMedical.setTypeface(Util.setTypefaceBold(context));
        mBinding.checkboxPatient.setTypeface(Util.setTypefaceBold(context));
        //  mBinding.txtSubmit.setTypeface(Util.setTypefaceBold(context));
    }

    private void setOnCLickListner() {
        mBinding.world1.toggleIcon.setOnClickListener(this);
        mBinding.world1.imgEdit.setOnClickListener(this);
        mBinding.checkboxBefore.setOnClickListener(this);
        mBinding.checkboxAfter.setOnClickListener(this);
        mBinding.txtSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.toggle_icon:
                finish();
                break;
            case R.id.img_edit:
                // show dialog
                showNoteDialog(context, note_str, getIntent().getStringExtra(S.patient_id), S.cancel_status);
                break;
            case R.id.checkbox_before:
                if (mBinding.checkboxBefore.isChecked())
                    mBinding.checkboxAfter.setChecked(false);

                cancel_case_str = getString(R.string.before_induction);
                break;
            case R.id.checkbox_after:
                if (mBinding.checkboxAfter.isChecked())
                    mBinding.checkboxBefore.setChecked(false);

                cancel_case_str = getString(R.string.after_induction);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            DataManager.getNotes(ConvertJsonToMap.jsonToMap(Util.getNoteParameter(MySharedPreferences.getPreferences(context, S.user_id), getIntent().getStringExtra(S.patient_id), S.cancel_status)));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Subscribe
    public void onSuccess(@NonNull SucessEvent event) {
        String result = event.getResponce();
        Util.dismissPrograssDialog(progressDialog);
        try {
            if (result != null && !result.equals("")) {
                JSONObject jsonObject = new JSONObject(result).getJSONObject(S.response);
                if (jsonObject.getBoolean(S.status)) {
                    if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_get_notes)) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject(S.data);
                        note_str = jsonObject1.getString(S.api_notes);
                    } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_add_notes)) {
                        Util.showToast(context, jsonObject.getString(S.message));
                        JSONObject jsonObject1 = jsonObject.getJSONObject(S.data);
                        note_str = jsonObject1.getString(S.api_notes);
                    } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_cancel_record)) {
                        Util.showToast(context, jsonObject.getString(S.message));
                        finish();
                    }
                } else {
                    if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_cancel_record))
                        Util.showToast(context, jsonObject.getString(S.message));
                }


            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }


    /*    if (note_status) {
            if (result != null && !result.equals("")) {
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
            mBinding.txtSubmit.setEnabled(true);

            if (result != null) {
                try {
                    // parse data
                    JSONObject jsonObject = new JSONObject(result).getJSONObject(S.response);
                    if (jsonObject.getBoolean(S.status)) {
                        Util.showToast(context, jsonObject.getString(S.message));
                        finish();
                    } else
                        Util.showToast(context, jsonObject.getString(S.message));

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }*/

        //Toast.makeText(context, "call cancel", Toast.LENGTH_SHORT).show();

    }

    @Subscribe
    public void onFailed(@NonNull ErrorEvent event) {
        Util.dismissPrograssDialog(progressDialog);
        mBinding.txtSubmit.setEnabled(true);
        Log.e(TAG, event.getMessage());
    }
}

package com.octalsoftaware.archi.views.activity.chargeinformation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.data.DataManager;
import com.octalsoftaware.archi.data.events.ErrorEvent;
import com.octalsoftaware.archi.data.events.SucessEvent;
import com.octalsoftaware.archi.databinding.ProcedureBinding;
import com.octalsoftaware.archi.models.ProceduresModal;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.Util;
import com.octalsoftaware.archi.utils.Validations;
import com.octalsoftaware.archi.utils.constants.I;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.activity.BaseActivity;
import com.octalsoftaware.archi.views.adapter.BookAutoCompleteAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by anandj on 5/8/2017.
 */

public class ProceduresActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    @Nullable
    private Context context = null;
    @Nullable
    private ProcedureBinding mBinding = null;
    private String patient_id = "";
    @Nullable
    private MyProgressDialog progressDialog = null;
    public static String TAG = ProceduresActivity.class.getSimpleName();
    private boolean mode_status = true;
    private String mode1 = "", mode2 = "", mode3 = "";
    @Nullable
    private ArrayList<ProceduresModal> proceduresModalArrayList= null;
    @Nullable
    private String my_icd1 = null,my_icd2= null,my_icd3= null,my_icd4= null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        patient_id = getIntent().getStringExtra(S.patient_details);

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.procedure);

        // set Title
        mBinding.mainToolbar.toolbarheading.setText(getString(R.string.procedure));
        mBinding.mainToolbar.imgEdit.setVisibility(View.GONE);

        proceduresModalArrayList = new ArrayList<>();

        setOnClickListener();


        // call api for set mode spinner
        getOpenCaseModifire();

        // iniliziation auto complete textview
        iniAutoAdapter();

    }

    private void iniSpinner(@NonNull String[] m1, @NonNull String[] m2, @NonNull String[] m3) {
        ArrayAdapter<String> spinner_adpater = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, m1);
        mBinding.spinnerMode1.setAdapter(spinner_adpater);

        ArrayAdapter<String> mode2_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, m2);
        mBinding.spinnerMode2.setAdapter(mode2_adapter);

        ArrayAdapter<String> mode3_adpter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, m3);
        mBinding.spinnerMode3.setAdapter(mode3_adpter);

        mode_status = false;
    }

    private void iniAutoAdapter() {
        mBinding.icd1.setThreshold(I.AUTOCOMPLETHRESHOLD);
        mBinding.icd1.setAdapter(new BookAutoCompleteAdapter(this, patient_id));
        mBinding.icd1.setLoadingIndicator(mBinding.pbIcd1);
        mBinding.icd1.setThreshold(I.AUTOCOMPLETHRESHOLD);

        mBinding.icd2.setAdapter(new BookAutoCompleteAdapter(this, patient_id));
        mBinding.icd2.setLoadingIndicator(mBinding.pbIcd2);
        mBinding.icd2.setThreshold(I.AUTOCOMPLETHRESHOLD);

        mBinding.icd3.setAdapter(new BookAutoCompleteAdapter(this, patient_id));
        mBinding.icd3.setLoadingIndicator(mBinding.pbIcd3);
        mBinding.icd3.setThreshold(I.AUTOCOMPLETHRESHOLD);

        mBinding.icd4.setAdapter(new BookAutoCompleteAdapter(this, patient_id));
        mBinding.icd4.setLoadingIndicator(mBinding.pbIcd4);
        mBinding.icd4.setThreshold(I.AUTOCOMPLETHRESHOLD);

       // mBinding.icd1.setOnItemClickListener(this);
        mBinding.icd1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull AdapterView<?> parent, View view, int position, long id) {
                ProceduresModal book = (ProceduresModal) parent.getItemAtPosition(position);
                mBinding.icd1.setText(book.getCode());
                my_icd1 = book.getCode();
            }
        });
        mBinding.icd2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull AdapterView<?> parent, View view, int position, long id) {
                ProceduresModal book = (ProceduresModal) parent.getItemAtPosition(position);
                mBinding.icd2.setText(book.getCode());
                my_icd2 = book.getCode();
            }
        });
        mBinding.icd3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull AdapterView<?> parent, View view, int position, long id) {
                ProceduresModal book = (ProceduresModal) parent.getItemAtPosition(position);
                mBinding.icd3.setText(book.getCode());
                my_icd3 = book.getCode();
            }
        });
        mBinding.icd4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull AdapterView<?> parent, View view, int position, long id) {
                ProceduresModal book = (ProceduresModal) parent.getItemAtPosition(position);
                mBinding.icd4.setText(book.getCode());
                my_icd4 = book.getCode();
            }
        });
     //   mBinding.icd2.setOnItemClickListener(this);
    //    mBinding.icd3.setOnItemClickListener(this);
     //   mBinding.icd4.setOnItemClickListener(this);



        mBinding.icd1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                my_icd1 = null;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBinding.icd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                my_icd2 = null;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBinding.icd3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                my_icd3 = null;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBinding.icd4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                my_icd4 = null;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    public void getOpenCaseModifire() {
        progressDialog = Util.showPrograsssDialog(context);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(S.api_record_id, patient_id);
            jsonObject.put(S.api_procedure_codes, "");
            DataManager.proceduresList(ConvertJsonToMap.jsonToMap(jsonObject));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void setOnClickListener() {
        mBinding.mainToolbar.toggleIcon.setOnClickListener(this);
        mBinding.btnAdd.setOnClickListener(this);

        mBinding.spinnerMode1.setOnItemSelectedListener(this);
        mBinding.spinnerMode2.setOnItemSelectedListener(this);
        mBinding.spinnerMode3.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.toggle_icon:
                onBackPressed();
                break;
            case R.id.btn_add:
                // save thr procedures
                saveProcedures();
                break;
        }
    }

    @Override
    public void initEvent() {
    }

    private void saveProcedures() {
        if (!Validations.proceduresValidation(mBinding.edtCode,mBinding.icd1,mBinding.icd2,mBinding.icd3,mBinding.icd4,my_icd1,my_icd2,my_icd3,my_icd4,context)) {
            return;
        }
        ProceduresModal pm = new ProceduresModal();
        pm.setCode_statis(mBinding.edtCode.getText().toString());
        pm.setMode1(mode1);
        pm.setMode2(mode2);
        pm.setMode3(mode3);
        pm.setIcd1(mBinding.icd1.getText().toString());
        pm.setIcd2(mBinding.icd2.getText().toString());
        pm.setIcd3(mBinding.icd3.getText().toString());
        pm.setIcd4(mBinding.icd4.getText().toString());

        proceduresModalArrayList.add(pm);

        makeemptyAllView();
    }

    private void makeemptyAllView(){
        mBinding.edtCode.setText(null);
        mBinding.spinnerMode1.setSelection(0);
        mBinding.spinnerMode2.setSelection(0);
        mBinding.spinnerMode3.setSelection(0);
        mBinding.icd1.setText(null);
        mBinding.icd2.setText(null);
        mBinding.icd3.setText(null);
        mBinding.icd4.setText(null);


        Util.confirmDialog(context,getString(R.string.save_procedures),getString(R.string.yes),getString(R.string.no),"",0);

        //Util.showToast(context,getString(R.string.save_procedures));
    }

    @Override
    public void onItemClick(@NonNull AdapterView<?> parent, @NonNull View view, int position, long id) {
        ProceduresModal book = (ProceduresModal) parent.getItemAtPosition(position);
        switch (view.getId()) {
            case R.id.icd1:
                mBinding.icd1.setText(book.getCode());
                my_icd1 = book.getCode();
                break;
            case R.id.icd2:
                mBinding.icd2.setText(book.getCode());
                my_icd2 = book.getCode();
                break;
            case R.id.icd3:
                mBinding.icd3.setText(book.getCode());
                my_icd3 = book.getCode();
                break;
            case R.id.icd4:
                mBinding.icd4.setText(book.getCode());
                my_icd4 = book.getCode();
                break;
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
                    if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_procedures_list)) {
                        JSONObject jsonObject1 = jsonObject.getJSONArray(S.data).getJSONObject(0);
                        if (mode_status) {
                            ArrayList<String> mode1 = new ArrayList<>();
                            ArrayList<String> mode2 = new ArrayList<>();
                            ArrayList<String> mode3 = new ArrayList<>();
                            JSONObject jsonObject2 = jsonObject1.getJSONObject(S.api_Open_Case_Modifire1);
                            Iterator<String> keys = jsonObject2.keys();
                            mode1.add("Mode 1");
                            while (keys.hasNext()) {
                                String key = keys.next();
                                mode1.add(jsonObject2.getString(key));
                            }
                            JSONObject jsonObject3 = jsonObject1.getJSONObject(S.api_Open_Case_Modifire2);
                            Iterator<String> keysmode2 = jsonObject3.keys();
                            mode2.add("Mode 2");
                            while (keysmode2.hasNext()) {
                                String key = keysmode2.next();
                                mode2.add(jsonObject3.getString(key));
                            }
                            JSONObject jsonObject4 = jsonObject1.getJSONObject(S.api_Open_Case_Modifire3);
                            Iterator<String> keysmode3 = jsonObject4.keys();
                            mode3.add("Mode 3");
                            while (keysmode3.hasNext()) {
                                String key = keysmode3.next();
                                mode3.add(jsonObject4.getString(key));
                            }

                            String[] m1 = new String[mode1.size()];
                            m1 = mode1.toArray(m1);

                            String[] m2 = new String[mode2.size()];
                            m2 = mode2.toArray(m2);

                            String[] m3 = new String[mode3.size()];
                            m3 = mode3.toArray(m3);
                            iniSpinner(m1, m2, m3);
                        }
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
    protected void onResume() {
        super.onResume();
        event.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        event.unregister(this);
    }

    @Override
    public void onItemSelected(@NonNull AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_mode1:
                if (position != 0)
                    mode1 = mBinding.spinnerMode1.getSelectedItem().toString();
                break;
            case R.id.spinner_mode2:
                if (position != 0)
                    mode2 = mBinding.spinnerMode2.getSelectedItem().toString();
                break;
            case R.id.spinner_mode3:
                if (position != 0)
                    mode3 = mBinding.spinnerMode3.getSelectedItem().toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(S.start_activity_result,"");
        intent.putExtra(S.check_id,proceduresModalArrayList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


}

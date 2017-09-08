package com.octalsoftaware.archi.views.activity.qualityinformation;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.data.DataManager;
import com.octalsoftaware.archi.data.events.ErrorEvent;
import com.octalsoftaware.archi.data.events.SucessEvent;
import com.octalsoftaware.archi.databinding.AdvancedQaBinding;
import com.octalsoftaware.archi.models.AdvancedQIModal;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.Util;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.activity.BaseActivity;
import com.octalsoftaware.archi.views.adapter.AdvanceQIAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.octalsoftaware.archi.utils.Util.showNoteDialog;
import static com.octalsoftaware.archi.utils.constants.S.user_id;

/**
 * Created by anandj on 4/18/2017.
 */

public class AdvancedQIActivity extends BaseActivity implements View.OnClickListener {

    @Nullable
    private AdvancedQaBinding mBinding = null;
    @Nullable
    private Context context = null;
    @Nullable
    private AdvanceQIAdapter advanceQIAdapter = null;
    @Nullable
    private ArrayList<AdvancedQIModal> advancedQIModalArrayList = null;
    @NonNull
    private ArrayList<AdvancedQIModal> mSaveQI = new ArrayList<>();
    @Nullable
    private MyProgressDialog progressDialog = null;
    private String note_str = "";
    private String patient_id = "";
    private String patient_name = "";
    private String TAG = AdvancedQIActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialization context
        context = this;

        patient_id = getIntent().getStringExtra(S.patient_details);
        patient_name = getIntent().getStringExtra(S.patient_name);
        mSaveQI = (ArrayList<AdvancedQIModal>) getIntent().getSerializableExtra(S.save_qi_data);

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.advanced_qa);

        // set patient name
        mBinding.txtPatientname.setText(patient_name);

        // set Title
        mBinding.mainToolbar.toolbarheading.setText(getString(R.string.advanced_qi));

        // onclick
        setOnClickListner();

        // iniadapter
        iniiadpter();


        // get Previous notes
        getNote();
    }

    private void getNote(){
        try {
            DataManager.getNotes(ConvertJsonToMap.jsonToMap(Util.getNoteParameter(MySharedPreferences.getPreferences(context, user_id), patient_id, S.qi_advanced_status)));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void iniiadpter(){
        advancedQIModalArrayList = new ArrayList<>();
        advanceQIAdapter = new AdvanceQIAdapter(advancedQIModalArrayList,context,patient_id,mSaveQI);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mBinding.recylarview.setLayoutManager(mLayoutManager);
        mBinding.recylarview.setItemAnimator(new DefaultItemAnimator());
        mBinding.recylarview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mBinding.recylarview.setAdapter(advanceQIAdapter);

        fillData();
    }

    private void fillData(){
        AdvancedQIModal advancedQIModal1 = new AdvancedQIModal();
        advancedQIModal1.setId("1");
        advancedQIModal1.setName(getString(R.string.airway_respiratory));
        advancedQIModalArrayList.add(advancedQIModal1);

        AdvancedQIModal advancedQIModal2 = new AdvancedQIModal();
        advancedQIModal2.setId("2");
        advancedQIModal2.setName(getString(R.string.cardiovascular));
        advancedQIModalArrayList.add(advancedQIModal2);

        AdvancedQIModal advancedQIModal3 = new AdvancedQIModal();
        advancedQIModal3.setId("3");
        advancedQIModal3.setName(getString(R.string.neurologic));
        advancedQIModalArrayList.add(advancedQIModal3);

        AdvancedQIModal advancedQIModal4 = new AdvancedQIModal();
        advancedQIModal4.setId("4");
        advancedQIModal4.setName(getString(R.string.regional));
        advancedQIModalArrayList.add(advancedQIModal4);

        AdvancedQIModal advancedQIModal5 = new AdvancedQIModal();
        advancedQIModal5.setId("5");
        advancedQIModal5.setName(getString(R.string.procedural));
        advancedQIModalArrayList.add(advancedQIModal5);

        AdvancedQIModal advancedQIModal6 = new AdvancedQIModal();
        advancedQIModal6.setId("6");
        advancedQIModal6.setName(getString(R.string.pharmacy_bloodbank));
        advancedQIModalArrayList.add(advancedQIModal6);

        AdvancedQIModal advancedQIModal6_a = new AdvancedQIModal();
        advancedQIModal6_a.setId("6_a");
        advancedQIModal6_a.setName(getString(R.string.patient_safety));
        advancedQIModalArrayList.add(advancedQIModal6_a);

        AdvancedQIModal advancedQIModal7 = new AdvancedQIModal();
        advancedQIModal7.setId("7");
        advancedQIModal7.setName(getString(R.string.morbidity_mortality));
        advancedQIModalArrayList.add(advancedQIModal7);

        AdvancedQIModal advancedQIModal8 = new AdvancedQIModal();
        advancedQIModal8.setId("8");
        advancedQIModal8.setName(getString(R.string.compliance));
        advancedQIModalArrayList.add(advancedQIModal8);


        advanceQIAdapter.notifyDataSetChanged();
    }

    private void setOnClickListner(){
        mBinding.mainToolbar.toggleIcon.setOnClickListener(this);
        mBinding.mainToolbar.imgEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.toggle_icon:
                finish();
                break;
            case R.id.img_edit:
                // show dialog note
                showNoteDialog(context, note_str, patient_id,S.qi_advanced_status);
                break;
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

    public void sendNote(@NonNull JSONObject jsonObject) throws JSONException {

        progressDialog = Util.showPrograsssDialog(context);
        DataManager.addNotes(ConvertJsonToMap.jsonToMap(jsonObject));
    }



    @Subscribe
    public void onSuccess(@NonNull SucessEvent event){
        Util.dismissPrograssDialog(progressDialog);
        String result = event.getResponce();
        if(result!=null){
            try {
                JSONObject jsonObject = new JSONObject(result).getJSONObject(S.response);
                if(jsonObject.getBoolean(S.status)){
                    if(jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_get_notes)){
                        JSONObject jsonObject1 = jsonObject.getJSONObject(S.data);
                        note_str = jsonObject1.getString(S.api_notes);
                    }
                    else if(jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_add_notes)){
                        Util.showToast(context, jsonObject.getString(S.message));
                        JSONObject jsonObject1 = jsonObject.getJSONObject(S.data);
                        note_str = jsonObject1.getString(S.api_notes);
                    }

                }
            }
            catch (Exception e){
                Log.e(TAG,e.toString());
            }
        }

    }
    @Subscribe
    public void onFailed(@NonNull ErrorEvent event){
        Util.dismissPrograssDialog(progressDialog);
        Log.e(TAG,event.getMessage());
    }

}

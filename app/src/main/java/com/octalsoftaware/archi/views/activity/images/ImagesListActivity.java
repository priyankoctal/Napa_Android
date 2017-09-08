package com.octalsoftaware.archi.views.activity.images;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.data.DataManager;
import com.octalsoftaware.archi.data.events.ErrorEvent;
import com.octalsoftaware.archi.data.events.SucessEvent;
import com.octalsoftaware.archi.databinding.ImagesListBinding;
import com.octalsoftaware.archi.models.ImagesListModal;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.Util;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.activity.BaseActivity;
import com.octalsoftaware.archi.views.adapter.ImagesListAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.octalsoftaware.archi.utils.Util.showNoteDialog;
import static com.octalsoftaware.archi.utils.constants.S.user_id;

/**
 * Created by anandj on 4/19/2017.
 */

public class ImagesListActivity extends BaseActivity implements View.OnClickListener {

    private ImagesListBinding mBinding = null;
    private Context context = null;
    private ArrayList<ImagesListModal> imagesListModalArrayList = null;
    private ImagesListAdapter imagesListAdapter = null;
    private String note_str = "";
    private String patient_id = "";
    private String TAG = ImagesListActivity.class.getSimpleName();
    private MyProgressDialog progressDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialization context
        context = this;

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.images_list);

        // set Title
        mBinding.mainToolbar.toolbarheading.setText(getString(R.string.images));

        mBinding.btnAdditional.setTypeface(Util.setTypefaceBold(context));

        // set on click listener
        setOnClickListner();

        imagesListModalArrayList = new ArrayList<>();

        // set adapter
        iniiadpter();


        patient_id = getIntent().getStringExtra(S.patient_details);

        // set patient name
        mBinding.txtPatientname.setText(getIntent().getStringExtra(S.patient_name));

        // get Previous notes
        getNote();
    }


    private void iniiadpter() {
        //imagesListModalArrayList = new ArrayList<>();
        imagesListAdapter = new ImagesListAdapter(imagesListModalArrayList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mBinding.recylerview.setLayoutManager(mLayoutManager);
        mBinding.recylerview.setItemAnimator(new DefaultItemAnimator());
        //    mBinding.recylerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mBinding.recylerview.setAdapter(imagesListAdapter);

        //    fillData();
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.toggle_icon:
                finish();
                break;
            case R.id.img_edit:
                // show note dialog
                showNoteDialog(context, note_str, patient_id, S.imageslist_status);
                break;
            case R.id.btn_additional:
                Intent intent = new Intent(ImagesListActivity.this, ImagesActivity.class);
                intent.putExtra(S.patient_details, patient_id);
                startActivity(intent);
                break;
        }
    }

    // here is code for delete patient image
    public void deletePatientImage(String image_id) {
        try {
            progressDialog = Util.showPrograsssDialog(context);
            //{"record_id":"993","user_id":"251","image_id":"142"}
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(S.api_record_id, patient_id);
            jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, S.user_id));
            jsonObject.put(S.api_image_id, image_id);

            DataManager.deletePatientImage(ConvertJsonToMap.jsonToMap(jsonObject));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void initEvent() {
    }

    private void setOnClickListner() {
        mBinding.mainToolbar.imgEdit.setOnClickListener(this);
        mBinding.mainToolbar.toggleIcon.setOnClickListener(this);
        mBinding.btnAdditional.setOnClickListener(this);
    }

    public void sendNote(@NonNull JSONObject jsonObject) throws JSONException {

        progressDialog = Util.showPrograsssDialog(context);
        DataManager.addNotes(ConvertJsonToMap.jsonToMap(jsonObject));
    }

    private void getNote() {
        try {
            DataManager.getNotes(ConvertJsonToMap.jsonToMap(Util.getNoteParameter(MySharedPreferences.getPreferences(context, user_id), patient_id, S.imageslist_status)));
            //  DataManager.
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void getImageList() {
        try {
            progressDialog = Util.showPrograsssDialog(context);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(S.api_record_id, patient_id);
            // call api for images listing
            DataManager.getImagesList(ConvertJsonToMap.jsonToMap(jsonObject));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        event.register(this);
        if (Util.isNetworkConnected(context)) {
          //  progressDialog = Util.showPrograsssDialog(context);
            getImageList();
        } else
            Util.showToast(context, getString(R.string.no_internet_connection));
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
    protected void onPause() {
        super.onPause();
        event.unregister(this);
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
                    } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_images_list)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(S.data);
                        imagesListModalArrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            ImagesListModal imagesListModal = new ImagesListModal();
                            imagesListModal.setId(jsonObject1.getString(S.api_id));
                            imagesListModal.setRecord_id(jsonObject1.getString(S.api_record_id));
                            imagesListModal.setImageLocation(jsonObject1.getString(S.api_ImageLocation));
                            imagesListModal.setImageSource(jsonObject1.getString(S.api_ImageSource));
                            imagesListModal.setUser_id(jsonObject1.getString(S.api_user_id));
                            imagesListModal.setUrl(jsonObject1.getString(S.api_url));
                            imagesListModal.setImageName(jsonObject1.getString(S.api_ImageName));
                            imagesListModal.setCreated(jsonObject1.getString(S.api_created));
                            imagesListModal.setExtension(jsonObject1.getString(S.api_extension));

                            imagesListModalArrayList.add(imagesListModal);
                        }
                        iniiadpter();
                    } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_images_delete)) {
                        Util.showToast(context, jsonObject.getString(S.message));
                        getImageList();
                    }

                } else {
                    if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_images_list)) {
                        imagesListModalArrayList.clear();
                        Util.showToast(context, jsonObject.getString(S.message));
                        iniiadpter();
                    } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_images_delete))
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

}

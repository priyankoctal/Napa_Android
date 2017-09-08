package com.octalsoftaware.archi.views.activity.images;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.data.DataManager;
import com.octalsoftaware.archi.data.events.ErrorEvent;
import com.octalsoftaware.archi.data.events.SucessEvent;
import com.octalsoftaware.archi.databinding.ImagesBinding;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.Util;
import com.octalsoftaware.archi.utils.Validations;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.activity.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.octalsoftaware.archi.utils.Util.showNoteDialog;
import static com.octalsoftaware.archi.utils.constants.S.user_id;

/**
 * Created by anandj on 4/19/2017.
 */

public class ImagesActivity extends BaseActivity implements View.OnClickListener {
    @Nullable
    private ImagesBinding mBinding = null;
    @Nullable
    private Context context = null;
    private String patient_id = "";
    public static String TAG = ImagesListActivity.class.getSimpleName();
    @Nullable
    private MyProgressDialog progressDialog = null;
    @Nullable
    HashMap<String, RequestBody> map = null;
    @Nullable
    MultipartBody.Part firstImage = null;
    @Nullable
    File f = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialization context
        context = this;

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.images);


        // set Title
        mBinding.mainToolbar.toolbarheading.setText(getString(R.string.images));

        mBinding.mainToolbar.imgEdit.setVisibility(View.GONE);

        setOnClickListner();

        patient_id = getIntent().getStringExtra(S.patient_details);


        EasyImage.openCamera(this, 0);


    }


    private void setOnClickListner() {
        mBinding.mainToolbar.imgEdit.setOnClickListener(this);
        mBinding.mainToolbar.toggleIcon.setOnClickListener(this);
        mBinding.imgCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.toggle_icon:
                onBackPressed();
                break;
            case R.id.img_edit:
                // show note dialog
                showNoteDialog(context, "", "", "");
                break;
            case R.id.img_camera:
                EasyImage.openCamera(this, 0);
               /* String title = mBinding.txtPatientname.getText().toString().trim();
                if (!TextUtils.isEmpty(title)) {
                    progressDialog = Util.showPrograsssDialog(context);
                    DataManager.addImage(map, firstImage);
                   // EasyImage.openCamera(this, 0);
                } else
                    Util.setError(mBinding.txtPatientname);*/

                break;
        }
    }

    public void saveImage() throws Exception {
        String title = mBinding.txtPatientname.getText().toString().trim();
        if (!Validations.saveImageValidation(mBinding.txtPatientname, firstImage, context)) {
            return;
            // EasyImage.openCamera(this, 0);
        }/* else
            Util.setError(mBinding.txtPatientname);*/
        if (Util.isNetworkConnected(context)) {
            progressDialog = Util.showPrograsssDialog(context);
            map = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(S.api_record_id, patient_id);
            jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
            jsonObject.put(S.api_image_name, title);
            RequestBody part_data = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.toString());
            map.put(S.api_data, part_data);
            DataManager.addImage(map, firstImage);


        } else
            Util.showToast(context, getString(R.string.no_internet_connection));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                //Handle the images
                onPhotosReturned(imagesFiles);
                //     File imgFile = new  File("/sdcard/Images/test_image.jpg");

                if (imagesFiles.get(0).exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imagesFiles.get(0).getAbsolutePath());

                    mBinding.patientImage.setImageBitmap(myBitmap);

                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                super.onCanceled(source, type);
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(ImagesActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotosReturned(@NonNull List<File> imFileList) {
        try {
           /* String title = mBinding.txtPatientname.getText().toString().trim();


            map = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(S.api_record_id, patient_id);
            jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
            jsonObject.put(S.api_image_name, title);
            RequestBody part_data = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.toString());
            map.put(S.api_data, part_data);*/

            Bitmap bitmap = BitmapFactory.decodeFile(imFileList.get(0).getPath());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            f = new File(context.getCacheDir(), S.file_name);
            f.createNewFile();

//Convert bitmap to byte array
            //  Bitmap bitmap = decoded;

            byte[] bitmapdata = out.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            //   mImageView.setImageBitmap(bitmap);

            RequestBody requestFileWall1 = RequestBody.create(MediaType.parse("multipart/form-data"), f);
            firstImage = MultipartBody.Part.createFormData(S.api_files, f.getName(), requestFileWall1);




          /*  RequestBody requestFileWall1 = RequestBody.create(MediaType.parse("multipart/form-data"), imFileList.get(0));
            firstImage = MultipartBody.Part.createFormData(S.api_files, imFileList.get(0).getName(), requestFileWall1);*/

        /*    if(imFileList.get(0)!=null)
                imFileList.get(0).delete();
*/
            //   DataManager.addImage(map, firstImage);


        } catch (Exception e) {
            Util.dismissPrograssDialog(progressDialog);
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void initEvent() {

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
    }

    @Subscribe
    public void onSuccess(@NonNull SucessEvent event) {
        Util.dismissPrograssDialog(progressDialog);
        String result = event.getResponce();
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result).getJSONObject(S.response);
                if (jsonObject.getBoolean(S.status)) {
                    if(f!=null){
                        if(f.exists())
                            f.delete();
                    }
                    Util.showToast(context, jsonObject.getString(S.message));
                    finish();
                } else {
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

    @Override
    public void onBackPressed() {
        Util.confirmDialog(context, getString(R.string.do_you_want_to_save), getString(R.string.yes), getString(R.string.no), "", 0);


    }
}



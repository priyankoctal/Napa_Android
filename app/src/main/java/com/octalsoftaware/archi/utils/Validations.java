package com.octalsoftaware.archi.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.models.HospitalModal;
import com.octalsoftaware.archi.utils.constants.S;

import okhttp3.MultipartBody;


/**
 * Created by anandj on 4/14/2017.
 */

public class Validations {

    public static boolean loginValidation(@NonNull EditText email, @NonNull EditText password, Context context) {
        String email_id = email.getText().toString().trim();
        if (TextUtils.isEmpty(email_id)) {
            Util.setError(email);
            return false;
        }
        if (!Util.isValidEmail(email_id)) {
            Util.setError(email, S.email_invalid);
            return false;
        }

        String password_text = password.getText().toString().trim();
        if (TextUtils.isEmpty(password_text)) {
            Util.setError(password);
            return false;
        }
        return true;
    }

    public static boolean cancelCaseValdation(@NonNull CheckBox before_induction, @NonNull CheckBox after_induction, @NonNull CheckBox system, @NonNull CheckBox medical, @NonNull CheckBox patient) {
        return (before_induction.isChecked() || after_induction.isChecked()) && (system.isChecked() || medical.isChecked() || patient.isChecked());
    }

    public static boolean addPatientValidation(@NonNull EditText first_name, @NonNull EditText last_name, @NonNull EditText dob, @NonNull EditText start_date, @NonNull EditText end_date, @NonNull EditText start_time, @NonNull EditText end_time, String select_gender, @Nullable HospitalModal select_location_detatils, @NonNull Context context) {
        String fname = first_name.getText().toString().trim();
        if (TextUtils.isEmpty(fname)) {
            Util.setError(first_name);
            return false;
        }
      /*  if(!Util.isValidName(fname)){
            Util.setError(first_name,context.getString(R.string.enter_valid_name));
            return false;
        }*/
        String lname = last_name.getText().toString().trim();
        if (TextUtils.isEmpty(lname)) {
            Util.setError(last_name);
            return false;
        }
        /*if(!Util.isValidName(lname)){
            Util.setError(last_name,context.getString(R.string.enter_valid_name));
            return false;
        }*/
        String birthday = dob.getText().toString().trim();
        if (TextUtils.isEmpty(birthday)) {
            Util.setError(dob);
            return false;
        }
        String sDate = start_date.getText().toString();
        if (TextUtils.isEmpty(sDate)) {
            Util.setError(start_date);
            return false;
        }
        String eDate = end_date.getText().toString();
        if (TextUtils.isEmpty(eDate)) {
            Util.setError(end_date);
            return false;
        }
        String stime = start_time.getText().toString();
        if (TextUtils.isEmpty(stime)) {
            Util.setError(start_time);
            return false;
        }
        String etime = end_time.getText().toString();
        if (TextUtils.isEmpty(etime)) {
            Util.setError(end_time);
            return false;
        }
        if (TextUtils.isEmpty(select_gender)) {
            Util.showToast(context, context.getString(R.string.select_gender));
            return false;
        }
        if (select_location_detatils == null) {
            Util.showToast(context, context.getString(R.string.select_location));
            return false;
        }
        return true;
    }

    public static boolean proceduresValidation(@NonNull EditText editText, @NonNull DelayAutoCompleteTextView icd1, @NonNull DelayAutoCompleteTextView icd2, @NonNull DelayAutoCompleteTextView icd3, @NonNull DelayAutoCompleteTextView icd4, @Nullable String my_icd1, @Nullable String my_icd2, @Nullable String my_icd3, @Nullable String my_icd4, @NonNull Context context) {
        String str = editText.getText().toString().trim();
        if (TextUtils.isEmpty(str)) {
            Util.setError(editText);
            return false;
        }
        if (!icd1.getText().toString().trim().equals("")) {
            if (my_icd1 == null) {
                Util.setError(icd1, context.getString(R.string.select_icd_code_error));
                return false;
            }
        }
        if (!icd2.getText().toString().trim().equals("")) {
            if (my_icd2 == null) {
                Util.setError(icd2, context.getString(R.string.select_icd_code_error));
                return false;
            }
        }
        if (!icd3.getText().toString().trim().equals("")) {
            if (my_icd3 == null) {
                Util.setError(icd3, context.getString(R.string.select_icd_code_error));
                return false;
            }
        }
        if (!icd4.getText().toString().trim().equals("")) {
            if (my_icd4 == null) {
                Util.setError(icd4, context.getString(R.string.select_icd_code_error));
                return false;
            }
        }

        return true;
    }

    public static boolean saveImageValidation(@NonNull EditText title, @Nullable MultipartBody.Part firstImage, @NonNull Context context){
        String edt_title = title.getText().toString().trim();
        if (TextUtils.isEmpty(edt_title)) {
            Util.setError(title);
            return false;
        }
        if(firstImage==null){
            Util.showToast(context,context.getString(R.string.please_add_image));
            return false;
        }
        return true;
    }
}

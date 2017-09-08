package com.octalsoftaware.archi.realm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.octalsoftaware.archi.models.AdvancedQIModal;
import com.octalsoftaware.archi.models.chargemodal.ChargeInformationPrefillmodalData;
import com.octalsoftaware.archi.models.chargemodal.DiagnoseList;
import com.octalsoftaware.archi.models.chargemodal.ProcedureList;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.constants.I;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.utils.CustomArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import static com.octalsoftaware.archi.utils.constants.S.save_charge_qi;
import static com.octalsoftaware.archi.utils.constants.S.user_id;

/**
 * Created by anandj on 5/22/2017.
 */

public class Database extends SQLiteOpenHelper {
    @Nullable
    Context mContext = null;


    public Database(Context context) {
        super(context, S.database_name, null, I.SCHEMAVERSION);
        mContext = context;
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(S.patient_record_table);
        db.execSQL(S.patient_charge_qi_record);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(S.drop_table + S.patient_list);
        db.execSQL(S.drop_table + save_charge_qi);
        onCreate(db);
    }

    // save patient list into database when home api is call
    public void savePatientList(@NonNull JSONObject jsonObject) {
        SQLiteDatabase tm = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(S.data, jsonObject.toString());
        tm.insert(S.patient_list, null, values);
        tm.close();
    }

    // claer patient list from database
    public void cleatPatientList() {
        SQLiteDatabase tm = this.getWritableDatabase();
        tm.execSQL(S.delete_patient_list);
        tm.close();
    }

    // get patient json from patient list table
    @Nullable
    public JSONObject getPatientList() throws Exception {
        JSONObject jsonObject = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(S.getPatientList, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                jsonObject = new JSONObject(cursor.getString(1));
            }
            cursor.close();
        }
        db.close();

        return jsonObject;
    }

    public void saveChargeAndQiDetails(String record_id, @NonNull JSONObject jsonObject, String api_name, int isOnline, String page_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(user_id, MySharedPreferences.getPreferences(mContext, user_id));
        contentValues.put(S.api_record_id, record_id);
        contentValues.put(S.save_data, jsonObject.toString());
        contentValues.put(S.api_APIname, api_name);
        contentValues.put(S.isSaved, isOnline);
        contentValues.put(S.api_page, page_name);
        db.insert(save_charge_qi, null, contentValues);
        db.close();
    }

    public void updateChargeAndQiDetails(String record_id, @NonNull JSONObject jsonObject, String api_name, String page_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(S.save_data, jsonObject.toString());
        db.update(S.save_charge_qi, contentValues, S.user_id + "=" + MySharedPreferences.getPreferences(mContext, S.user_id) + " and " + S.api_record_id + "=" + record_id + " and " + S.api_APIname + "='" + api_name + "' and " + S.api_page + "='" + page_name + "'", null);
        db.close();
    }
    public void updateQiDetails(String record_id, @NonNull JSONObject jsonObject, String api_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(S.save_data, jsonObject.toString());
        db.update(S.save_charge_qi, contentValues, S.user_id + "=" + MySharedPreferences.getPreferences(mContext, S.user_id) + " and " + S.api_record_id + "=" + record_id + " and " + S.api_APIname + "='" + api_name + "'", null);
        db.close();
    }
    public void savedQiDetails(String record_id, @NonNull JSONObject jsonObject, String api_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(user_id, MySharedPreferences.getPreferences(mContext, user_id));
        contentValues.put(S.api_record_id, record_id);
        contentValues.put(S.save_data, jsonObject.toString());
        contentValues.put(S.api_APIname, api_name);
        db.insert(save_charge_qi, null, contentValues);
        db.close();
    }
    @NonNull
    public CustomArrayList<AdvancedQIModal> getQIDetails(String record_id, String api_name) throws JSONException {
        JSONObject jsonObject = null;
        CustomArrayList<AdvancedQIModal> advancedQIModals = new CustomArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + S.save_charge_qi + " where " + S.user_id + "=" + MySharedPreferences.getPreferences(mContext, S.user_id) + " and " + S.api_record_id + "=" + record_id + " and " + S.api_APIname + "='" + api_name + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                jsonObject = new JSONObject(cursor.getString(3));
            }
            cursor.close();

            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray(S.data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    AdvancedQIModal advancedQIModal = new AdvancedQIModal();
                    advancedQIModal.setName(jsonObject1.getString(S.api_measure_name));
                    advancedQIModal.setId(jsonObject1.getString(S.api_value));
                    advancedQIModals.add(advancedQIModal);
                }
            }

        }
        db.close();
        return advancedQIModals;
    }
    @NonNull
    public CustomArrayList<AdvancedQIModal> getInvasiveLine(String record_id, String api_name) throws JSONException{
        JSONObject jsonObject = null;
        CustomArrayList<AdvancedQIModal> advancedQIModals = new CustomArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + S.save_charge_qi + " where " + S.user_id + "=" + MySharedPreferences.getPreferences(mContext, S.user_id) + " and " + S.api_record_id + "=" + record_id + " and " + S.api_APIname + "='" + api_name + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                jsonObject = new JSONObject(cursor.getString(3));
            }
            cursor.close();

            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray(S.data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    AdvancedQIModal invasiveModal = new AdvancedQIModal();
                    invasiveModal.setId(jsonObject1.getString(S.api_value));
                    invasiveModal.setName(jsonObject1.getString(S.api_charge_key));
                    advancedQIModals.add(invasiveModal);
                }
            }

        }
        db.close();
        return advancedQIModals;
    }
    @NonNull
    public ArrayList<ChargeInformationPrefillmodalData> getChargeDetails(String record_id, String api_name) throws JSONException {
        JSONObject jsonObject = null;
        ArrayList<ChargeInformationPrefillmodalData> chargeInformationPrefillmodalDataArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + S.save_charge_qi + " where " + S.user_id + "=" + MySharedPreferences.getPreferences(mContext, S.user_id) + " and " + S.api_record_id + "=" + record_id + " and " + S.api_APIname + "='" + api_name + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                jsonObject = new JSONObject(cursor.getString(3));
            }
            cursor.close();

            if (jsonObject != null) {
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

                 //   dialogsis_check_id.put(jsonObject2.getString(S.api_diagnose_id),jsonObject2.getString(S.api_diagnose_id));
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
            }

        }
        db.close();
        return chargeInformationPrefillmodalDataArrayList;
    }
    public boolean getSavedSatus(String record_id, String api_name) {
        boolean flag = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + S.save_charge_qi + " where " + S.user_id + "=" + MySharedPreferences.getPreferences(mContext, S.user_id) + " and " + S.api_record_id + "=" + record_id + " and " + S.api_APIname + "='" + api_name + "'", null);
        if (cursor != null) {
            flag = cursor.moveToFirst();
            cursor.close();
        }

        db.close();
        return flag;
    }

    public boolean getSavedSatus(String record_id, String api_name, String page_name) {
        boolean flag = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + S.save_charge_qi + " where " + S.user_id + "=" + MySharedPreferences.getPreferences(mContext, S.user_id) + " and " + S.api_record_id + "=" + record_id + " and " + S.api_APIname + "='" + api_name + "' and " + S.api_page + "='" + page_name + "'", null);
        if (cursor != null) {
            flag = cursor.moveToFirst();
            cursor.close();
        }

        db.close();
        return flag;
    }

    @NonNull
    public CustomArrayList<AdvancedQIModal> getChargeAndQIDetails(String record_id, String api_name, String page_name) throws JSONException {
        JSONObject jsonObject = null;
        CustomArrayList<AdvancedQIModal> advancedQIModals = new CustomArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + S.save_charge_qi + " where " + S.user_id + "=" + MySharedPreferences.getPreferences(mContext, S.user_id) + " and " + S.api_record_id + "=" + record_id + " and " + S.api_APIname + "='" + api_name + "' and " + S.api_page + "='" + page_name + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                jsonObject = new JSONObject(cursor.getString(3));
            }
            cursor.close();

            if (jsonObject != null) {
                JSONObject qi_measure = jsonObject.getJSONObject(S.api_qi_measures);
                Iterator<String> qi_mea = qi_measure.keys();
                while (qi_mea.hasNext()) {
                    String key = qi_mea.next();
                    AdvancedQIModal advancedQIModal = new AdvancedQIModal();
                    advancedQIModal.setName(key);
                    advancedQIModal.setId(qi_measure.getString(key));

                    advancedQIModals.add(advancedQIModal);
                }
            }

        }
        db.close();
        return advancedQIModals;
    }

    @NonNull
    public ArrayList<ChargeInformationPrefillmodalData> getChargeDetails(String record_id, String api_name, String page_name) throws JSONException {
        JSONObject jsonObject1 = null;
        ArrayList<ChargeInformationPrefillmodalData> chargeInformationPrefillmodalDataArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + S.save_charge_qi + " where " + S.user_id + "=" + MySharedPreferences.getPreferences(mContext, S.user_id) + " and " + S.api_record_id + "=" + record_id + " and " + S.api_APIname + "='" + api_name + "' and " + S.api_page + "='" + page_name + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                jsonObject1 = new JSONObject(cursor.getString(3));
            }
            cursor.close();

            if (jsonObject1 != null) {
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
            }

        }
        db.close();
        return chargeInformationPrefillmodalDataArrayList;
    }

}

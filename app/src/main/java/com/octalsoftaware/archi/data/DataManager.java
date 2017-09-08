package com.octalsoftaware.archi.data;

import android.support.annotation.NonNull;

import com.octalsoftaware.archi.data.remote.AuthAPI;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by anandj on 4/14/2017.
 */

public class DataManager {

    @NonNull
    private static AuthAPI sAuthAPI = new AuthAPI();

    public static void login(Map<String, Object> stringObjectMap) {
        sAuthAPI.login(stringObjectMap);
    }

    public static void homepage(Map<String, Object> stringObjectMap) {
        sAuthAPI.homepage(stringObjectMap);
    }

    public static void cancelcase(Map<String, Object> stringObjectMap) {
        sAuthAPI.cancelcase(stringObjectMap);
    }

    public static void reopenCase(Map<String, Object> stringObjectMap, int status) {
        sAuthAPI.reopenCase(stringObjectMap, status);
    }

    public static void addNotes(Map<String, Object> stringObjectMap) {
        sAuthAPI.addNote(stringObjectMap);
    }

    public static void getNotes(Map<String, Object> stringObjectMap) {
        sAuthAPI.getNote(stringObjectMap);
    }

    public static void getImagesList(Map<String, Object> stringObjectMap) {
        sAuthAPI.getImagesList(stringObjectMap);
    }

    public static void deletePatientImage(Map<String, Object> stringObjectMap) {
        sAuthAPI.deletePatientImage(stringObjectMap);
    }

    public static void addImage(Map<String, RequestBody> partMap, MultipartBody.Part file) {
        sAuthAPI.addImage(partMap, file);
    }

    public static void allList(Map<String, Object> stringObjectMap) {
        sAuthAPI.allList(stringObjectMap);
    }

    public static void modeOfAnesthesia(Map<String, Object> stringObjectMap) {
        sAuthAPI.modeOfAnesthesia(stringObjectMap);
    }

    public static void saveChargeInformation(Map<String, Object> stringObjectMap) {
        sAuthAPI.saveChargeInformation(stringObjectMap);
    }

    public static void saveQualityInformation(Map<String, Object> stringObjectMap) {
        sAuthAPI.saveQualityInformation(stringObjectMap);
    }

    public static void addPatient(Map<String, Object> stringObjectMap) {
        sAuthAPI.addPatient(stringObjectMap);
    }

    public static void proceduresList(Map<String, Object> stringObjectMap) {
        sAuthAPI.proceduresList(stringObjectMap);
    }

    public static void saveInvasiveCharge(Map<String, Object> stringObjectMap) {
        sAuthAPI.saveInvasiveCharge(stringObjectMap);
    }

    public static void getSaveQi(Map<String, Object> stringObjectMap) {
        sAuthAPI.getSaveQi(stringObjectMap);
    }

    public static void getSaveCharge(Map<String, Object> stringObjectMap) {
        sAuthAPI.getSaveCharge(stringObjectMap);
    }
    public static void getInvasiveLine(Map<String, Object> stringObjectMap) {
        sAuthAPI.getInvasiveLine(stringObjectMap);
    }

    public static void getHospitalLocation() {
        sAuthAPI.getHospitalLocation();
    }

}

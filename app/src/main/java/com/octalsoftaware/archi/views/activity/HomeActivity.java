package com.octalsoftaware.archi.views.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.octalsoftaware.archi.BuildConfig;
import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.data.DataManager;
import com.octalsoftaware.archi.data.events.ErrorEvent;
import com.octalsoftaware.archi.data.events.HomePageSuccessEvent;
import com.octalsoftaware.archi.databinding.HomePageBinding;
import com.octalsoftaware.archi.models.HomePageDaysModal;
import com.octalsoftaware.archi.models.HomePageModal;
import com.octalsoftaware.archi.realm.Database;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.MyProgressDialog;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.Util;
import com.octalsoftaware.archi.utils.constants.I;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.activity.chargeinformation.ChargeInformation;
import com.octalsoftaware.archi.views.activity.images.ImagesListActivity;
import com.octalsoftaware.archi.views.activity.qualityinformation.QualityInformation;
import com.octalsoftaware.archi.views.adapter.HomePageAdpter;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.octalsoftaware.archi.R.id.auto_comple;
import static com.octalsoftaware.archi.utils.Util.confirmDialog;
import static com.octalsoftaware.archi.utils.constants.I.ALERT_REOPEN;
import static com.octalsoftaware.archi.utils.constants.I.CANCEL_CASE;
import static com.octalsoftaware.archi.utils.constants.I.CHARGE;
import static com.octalsoftaware.archi.utils.constants.I.DATE_STATUS_CURRENT;
import static com.octalsoftaware.archi.utils.constants.I.DATE_STATUS_TOMORROW;
import static com.octalsoftaware.archi.utils.constants.I.DATE_STATUS_YESTERDAY;
import static com.octalsoftaware.archi.utils.constants.I.IMAGES;
import static com.octalsoftaware.archi.utils.constants.I.PATIENT_DETAIS;
import static com.octalsoftaware.archi.utils.constants.I.PLACE_PICKER_REQUEST;
import static com.octalsoftaware.archi.utils.constants.I.QI;
import static com.octalsoftaware.archi.utils.constants.S.user_id;


/**
 * Created by anandj on 4/17/2017.
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private HomePageBinding mBinding = null;

    private Context context = null;

    private ArrayList<HomePageModal> homePageModalArrayList = null;

    private HomePageAdpter homePageAdpter = null;
    private int date_status = DATE_STATUS_CURRENT;

    private MyProgressDialog progressDialog = null;

    private ArrayList<HomePageDaysModal> homePageDaysModals = null;

    private String[] day_str = new String[3];
    private boolean reopen_status = false;
    private int patient_current_pos = -1;
    //private String[] hospital_list;

    private ArrayList<String> hospital_location = null;

    private Database database;

    private JSONObject patient_json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialization context
        context = this;

        // Inflate the layout for this activty
        mBinding = DataBindingUtil.setContentView(this, R.layout.home_page);

        // set version
        mBinding.txtVersion.setText(getString(R.string.version) + " " + BuildConfig.VERSION_NAME);


        // get Database instance
        database = new Database(context);


        homePageModalArrayList = new ArrayList<>();
        homePageDaysModals = new ArrayList<>();
        day_str[0] = Util.getYesterdayDate();
        day_str[1] = Util.getTodayDate();
        day_str[2] = Util.getTommorrowDate();


        // on click
        setOnClickistner();

        mBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeRefresh.setRefreshing(true);
                reopen_status = false;
                patient_current_pos = -1;
                fetchHomeData(false);
            }
        });

        mBinding.autoComple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard();
                String search = (String) parent.getItemAtPosition(position);
                homePageAdpter.filter(search);
            }
        });
        // fetch patient details

        //  fetchHomeData(true);

        mBinding.autoComple.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(@NonNull CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    homePageAdpter.filter("");
                    S.hospital_name = "";
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void initEvent() {
    }


    @Override
    protected void onResume() {
        super.onResume();
        reopen_status = false;
     /*   try {
            patient_json = database.getPatientList();
            fillPatientList(patient_json, Util.getDate(1), 1);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }*/
        if (Util.isNetworkConnected(context))
            fetchHomeData(true);
        else {
            try {
                patient_json = database.getPatientList();
                fillPatientList(patient_json, Util.getDate(1), 1);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    private void fetchHomeData(boolean prograss_status) {
        reopen_status = false;
        if (prograss_status) {
            // show progress dialog
            progressDialog = Util.showPrograsssDialog(context);//new MyProgressDialog(context);
        }
        try {
            //  prepare jsonobject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
            DataManager.homepage(ConvertJsonToMap.jsonToMap(jsonObject));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        event.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        event.unregister(this);
    }

    private void iniadapter(@NonNull ArrayList<HomePageModal> arrayList) {
        homePageAdpter = new HomePageAdpter(arrayList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mBinding.recylerview.setLayoutManager(mLayoutManager);
        mBinding.recylerview.setItemAnimator(new DefaultItemAnimator());
        mBinding.recylerview.setAdapter(homePageAdpter);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, hospital_location);
        mBinding.autoComple.setAdapter(arrayAdapter);

        mBinding.autoComple.setThreshold(I.AUTOCOMPLETHRESHOLD);

        if (!S.hospital_name.equals("")) {
            mBinding.autoComple.setText(S.hospital_name);
            homePageAdpter.filter(mBinding.autoComple.getText().toString());
        }

        if (patient_current_pos != -1)
            mBinding.recylerview.scrollToPosition(patient_current_pos);

        mBinding.autoComple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard();
                S.hospital_name = (String) parent.getItemAtPosition(position);
                mBinding.autoComple.setText(S.hospital_name);
                homePageAdpter.filter(S.hospital_name);
            }
        });
    }


    private void setOnClickistner() {
        mBinding.toggleIcon.setOnClickListener(this);
        mBinding.imgRightarrow.setOnClickListener(this);
        mBinding.imgLeftarrow.setOnClickListener(this);
        mBinding.llLogout.setOnClickListener(this);
        mBinding.btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.toggle_icon:
                mBinding.drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.img_rightarrow:
                switch (date_status) {
                    case DATE_STATUS_CURRENT:
                        date_status = DATE_STATUS_TOMORROW;
                        mBinding.imgNav3.setImageResource(R.drawable.nav_select);
                        mBinding.imgNav1.setImageResource(R.drawable.nav_unselect);
                        mBinding.imgNav2.setImageResource(R.drawable.nav_unselect);

                        /*homePageModalArrayList.clear();
                        homePageModalArrayList.addAll(homePageDaysModals.get(2).getHomePageModals());
                        iniadapter(homePageModalArrayList);
                        mBinding.txtDate.setText(day_str[2]);*/
                        if (patient_json != null) {
                            try {
                                fillPatientList(patient_json, Util.getDate(2), 2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case DATE_STATUS_YESTERDAY:
                        date_status = DATE_STATUS_CURRENT;
                        mBinding.imgNav3.setImageResource(R.drawable.nav_unselect);
                        mBinding.imgNav1.setImageResource(R.drawable.nav_unselect);
                        mBinding.imgNav2.setImageResource(R.drawable.nav_select);

                        /*homePageModalArrayList.clear();
                        homePageModalArrayList.addAll(homePageDaysModals.get(1).getHomePageModals());
                        iniadapter(homePageModalArrayList);
                        mBinding.txtDate.setText(day_str[1]);*/
                        if (patient_json != null) {
                            try {
                                fillPatientList(patient_json, Util.getDate(1), 1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case DATE_STATUS_TOMORROW:
                        break;
                }
                break;
            case R.id.img_leftarrow:
                switch (date_status) {
                    case DATE_STATUS_CURRENT:
                        date_status = DATE_STATUS_YESTERDAY;
                        mBinding.imgNav3.setImageResource(R.drawable.nav_unselect);
                        mBinding.imgNav1.setImageResource(R.drawable.nav_select);
                        mBinding.imgNav2.setImageResource(R.drawable.nav_unselect);

                        /*homePageModalArrayList.clear();
                        homePageModalArrayList.addAll(homePageDaysModals.get(0).getHomePageModals());
                        iniadapter(homePageModalArrayList);
                        mBinding.txtDate.setText(day_str[0]);*/
                        if (patient_json != null) {
                            try {
                                fillPatientList(patient_json, Util.getDate(0), 0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case DATE_STATUS_YESTERDAY:
                        break;
                    case DATE_STATUS_TOMORROW:
                        date_status = DATE_STATUS_CURRENT;
                        mBinding.imgNav3.setImageResource(R.drawable.nav_unselect);
                        mBinding.imgNav1.setImageResource(R.drawable.nav_unselect);
                        mBinding.imgNav2.setImageResource(R.drawable.nav_select);

                      /*  homePageModalArrayList.clear();
                        homePageModalArrayList.addAll(homePageDaysModals.get(1).getHomePageModals());
                        iniadapter(homePageModalArrayList);
                        mBinding.txtDate.setText(day_str[1]);*/
                        if (patient_json != null) {
                            try {
                                fillPatientList(patient_json, Util.getDate(1), 1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }
                break;
            case R.id.ll_logout:
                mBinding.drawerLayout.closeDrawers();
                Util.confirmDialog(context, getString(R.string.are_you_sure_want_to_logout), getString(android.R.string.yes), getString(android.R.string.cancel), "", 0);
                break;
            case auto_comple:
                break;
            case R.id.btn_add:
                Intent intent = new Intent(context, AddPatientActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(Gravity.LEFT))
            mBinding.drawerLayout.closeDrawers();
        else
            finish();
    }

    public void startNewActivity(int activity_name, String id, int patient_current_pos, String patient_name) {
        Intent intent = null;
        this.patient_current_pos = patient_current_pos;
        switch (activity_name) {
            case PATIENT_DETAIS:
                intent = new Intent(context, PatientDetailsActivity.class);
                intent.putExtra(S.patient_details, id);
                break;
            case CANCEL_CASE:
                openCancelCaseACtivity(id,patient_name);
                /*intent = new Intent(context, CancelCase.class);
                intent.putExtra(S.patient_id, id);*/
                break;
            case ALERT_REOPEN:

                confirmDialog(context, getString(R.string.reopen_confirm_message), getString(android.R.string.yes), getString(android.R.string.cancel), id, patient_current_pos);
                break;
            case CHARGE:
                intent = new Intent(context, ChargeInformation.class);
                intent.putExtra(S.patient_details, id);
                intent.putExtra(S.patient_name, patient_name);
                break;
            case QI:
                intent = new Intent(context, QualityInformation.class);
                intent.putExtra(S.patient_details, id);
                intent.putExtra(S.patient_name, patient_name);
                break;
            case IMAGES:
                intent = new Intent(context, ImagesListActivity.class);
                intent.putExtra(S.patient_details, id);
                intent.putExtra(S.patient_name, patient_name);
                break;
        }
        if (intent != null)
            startActivity(intent);
    }

    private void openCancelCaseACtivity(String id,String patient_name) {
        Intent intent = new Intent(context, CancelCase.class);
        intent.putExtra(S.patient_id, id);
        intent.putExtra(S.patient_name, patient_name);
        startActivity(intent);
        //   startActivityForResult(intent,OPENCANCELCASEACTIVTY);
    }

    public void reopen(String id, int current_pos) {
        try {
            progressDialog = Util.showPrograsssDialog(context);
            reopen_status = true;
           // patient_current_pos = current_pos;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, user_id));
            jsonObject.put(S.api_record_id, id);

            if (Util.isNetworkConnected(context)) {
                DataManager.reopenCase(ConvertJsonToMap.jsonToMap(jsonObject), 1);
            } else
                Util.showToast(context, getString(R.string.no_internet_connection));


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(context, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Log.e("place", toastMsg);
                mBinding.autoComple.setText(place.getName());
            }
        } else if (requestCode == I.OPENLOCATIONSERVICES) {
            if (data != null) {
                String hospital_name = data.getStringExtra(S.start_activity_result);
                String hospital_id = data.getStringExtra(S.start_location_id);

                homePageAdpter.filter(hospital_id);

            }
        }
        /*else if(requestCode == OPENCANCELCASEACTIVTY) {
            if(data!=null){
                boolean status = data.getBooleanExtra(S.status,true);
                if(status){
                    reopen_status = false;
                    fetchHomeData(true);
                }

            }
        }*/
    }

    private void resetState() {
        date_status = DATE_STATUS_CURRENT;
        mBinding.imgNav3.setImageResource(R.drawable.nav_unselect);
        mBinding.imgNav1.setImageResource(R.drawable.nav_unselect);
        mBinding.imgNav2.setImageResource(R.drawable.nav_select);
    }


    private void fillPatientList(@NonNull JSONObject jsonArray, String day, int time) throws JSONException {
        homePageDaysModals.clear();
        homePageModalArrayList.clear();
     //   Iterator<String> iter = jsonArray.keys();
        //  while (iter.hasNext()) {
        HomePageDaysModal homePageDaysModal = new HomePageDaysModal();
        //   String key = iter.next();
        // if (!key.equalsIgnoreCase(S.api_ydate) && !key.equalsIgnoreCase(S.api_tdate) && !key.equalsIgnoreCase(S.api_tmdate)){

        if (jsonArray.has(day)) {


            JSONArray jsonArray1 = jsonArray.getJSONArray(day);
            ArrayList<HomePageModal> homePageDaysModalArrayList = new ArrayList<>();
            //      hospital_list = new String[jsonArray1.length()];
            hospital_location = new ArrayList<>();
            for (int j = 0; j < jsonArray1.length(); j++) {

                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                HomePageModal homePageModal = new HomePageModal();
                homePageModal.setId(jsonObject1.getString(S.api_id));
                homePageModal.setName(jsonObject1.getString(S.api_name));
                homePageModal.setDob(jsonObject1.getString(S.api_dob));
                homePageModal.setGender(jsonObject1.getString(S.api_gender));
                homePageModal.setStatus(jsonObject1.getString(S.status));
                homePageModal.setIs_completed(jsonObject1.getString(S.api_is_completed));
                homePageModal.setStatus3(jsonObject1.getString(S.api_status3));
                homePageModal.setSite(jsonObject1.getString(S.api_site));
                homePageModal.setIs_charged(jsonObject1.getString(S.api_is_charged));
                homePageModal.setIs_qi(jsonObject1.getString(S.api_is_qi));
                homePageModal.setCase_id(jsonObject1.getString(S.api_case_id));
                homePageModal.setTotalImages(jsonObject1.getString(S.api_totalImages));
                homePageModal.setStart_date(jsonObject1.getString(S.api_start_date));
                homePageModal.setLocation_id(jsonObject1.getString(S.api_location_id));
                homePageModal.setLocation_name(jsonObject1.getString(S.api_location_name));


                if (!hospital_location.contains(jsonObject1.getString(S.api_location_name)))
                    hospital_location.add(jsonObject1.getString(S.api_location_name));
                //hospital_list[j] = jsonObject1.getString(S.api_location_name);

                homePageDaysModalArrayList.add(homePageModal);
            }
            homePageDaysModal.setDays_name(day);
            homePageDaysModal.setHomePageModals(homePageDaysModalArrayList);
            homePageDaysModals.add(homePageDaysModal);
            homePageModalArrayList.addAll(homePageDaysModals.get(0).getHomePageModals());
        }
              /*  if(!jsonArray.getString(S.api_ydate).equalsIgnoreCase(day_str[0]))
                    homePageDaysModals.remove(0);
                else if(!jsonArray.getString(S.api_tdate).equalsIgnoreCase(day_str[1]))
                    homePageDaysModals.remove(1);
                else if(!jsonArray.getString(S.api_tmdate).equalsIgnoreCase(day_str[2]))
                    homePageDaysModals.remove(2);*/

        //   }
        // }


        mBinding.txtDate.setText(day_str[time]);
        if (time == 1)
            resetState();
        iniadapter(homePageModalArrayList);
    }

    @Subscribe
    public void onSuccess(@NonNull HomePageSuccessEvent event) {
        try {
            String result = event.getResponce();
            // parse data
            JSONObject jsonObject = new JSONObject(result).getJSONObject(S.response);
            if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_home)) {
                Util.dismissPrograssDialog(progressDialog);
                if (mBinding.swipeRefresh.isRefreshing())
                    mBinding.swipeRefresh.setRefreshing(false);
                try {
                    homePageDaysModals.clear();
                    patient_json = jsonObject.getJSONObject(S.data);

                    // show patient data
                    fillPatientList(patient_json, Util.getDate(date_status-1), date_status-1);

                    // before insert patient list into table first clear all previous data
                    database.cleatPatientList();
                    // set patient details into database
                    database.savePatientList(patient_json);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

            } else if (jsonObject.getString(S.api_APIname).equalsIgnoreCase(S.api_reopen_record)) {
                try {
                    fetchHomeData(false);
                    // parse data
                    if (jsonObject.getBoolean(S.status))
                        Util.showToast(context, jsonObject.getString(S.message));
                    else
                        Util.showToast(context, jsonObject.getString(S.message));

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Subscribe
    public void onFailed(@NonNull ErrorEvent event) {
        Log.e(TAG, event.getMessage());
        Util.dismissPrograssDialog(progressDialog);
        if (mBinding.swipeRefresh.isRefreshing())
            mBinding.swipeRefresh.setRefreshing(false);
    }


}
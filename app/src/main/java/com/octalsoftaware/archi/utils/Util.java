package com.octalsoftaware.archi.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.models.PostOpPainBlocksModal;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.activity.CancelCase;
import com.octalsoftaware.archi.views.activity.HomeActivity;
import com.octalsoftaware.archi.views.activity.LoginActivity;
import com.octalsoftaware.archi.views.activity.PatientDetailsActivity;
import com.octalsoftaware.archi.views.activity.chargeinformation.CardiacAndTEE;
import com.octalsoftaware.archi.views.activity.chargeinformation.ChargeInformation;
import com.octalsoftaware.archi.views.activity.chargeinformation.ChestAndAbdomenActivity;
import com.octalsoftaware.archi.views.activity.chargeinformation.InvasiveLinesActivity;
import com.octalsoftaware.archi.views.activity.chargeinformation.InvasiveLinesAndSpecialServices;
import com.octalsoftaware.archi.views.activity.chargeinformation.LowerExtremityActivity;
import com.octalsoftaware.archi.views.activity.chargeinformation.NEURAXIAL;
import com.octalsoftaware.archi.views.activity.chargeinformation.PostOpPlainBlocksActivity;
import com.octalsoftaware.archi.views.activity.chargeinformation.ProceduresActivity;
import com.octalsoftaware.archi.views.activity.chargeinformation.UpperExtremityActivity;
import com.octalsoftaware.archi.views.activity.images.ImagesActivity;
import com.octalsoftaware.archi.views.activity.images.ImagesListActivity;
import com.octalsoftaware.archi.views.activity.qualityinformation.AdvancedQIActivity;
import com.octalsoftaware.archi.views.activity.qualityinformation.CardiovascularActivity;
import com.octalsoftaware.archi.views.activity.qualityinformation.ComplianceActivity;
import com.octalsoftaware.archi.views.activity.qualityinformation.MorbidityMortality;
import com.octalsoftaware.archi.views.activity.qualityinformation.NeurologicActivity;
import com.octalsoftaware.archi.views.activity.qualityinformation.PatientSafety;
import com.octalsoftaware.archi.views.activity.qualityinformation.PharmacyBloodBank;
import com.octalsoftaware.archi.views.activity.qualityinformation.ProceduralActivity;
import com.octalsoftaware.archi.views.activity.qualityinformation.QIAirwayRespiratoryActivity;
import com.octalsoftaware.archi.views.activity.qualityinformation.QualityInformation;
import com.octalsoftaware.archi.views.activity.qualityinformation.RegionalActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by anandj on 4/14/2017.
 */

public class Util {

    public static void setError(@NonNull EditText text) {
        text.setError(S.error_empty_edittext);
        text.requestFocus();
    }

    public static void setError(@NonNull EditText text, String msg) {
        text.setError(msg);
        text.requestFocus();
    }

    public static void dismissPrograssDialog(@Nullable MyProgressDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * This method will return the JSONObject procedures , if it exists
     * If it doesn't exist it will return NULL
     */
    public static JSONObject getProcedures(@NonNull JSONObject json) {
        try {
            return json.getJSONObject(S.api_procedure_code);
        } catch (JSONException e) {
            // This could be triggered either because there is no q0
            //   or because the JSON structure is different from what was expected.
            return null;
        }
    }

    public static String convertRetrofitResponce(@NonNull Response<ResponseBody> responce) {
        try {
            InputStream inputStream = responce.body().byteStream();

            BufferedReader bR = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";

            StringBuilder responseStrBuilder = new StringBuilder();
            while ((line = bR.readLine()) != null) {

                responseStrBuilder.append(line);
            }
            inputStream.close();
            return responseStrBuilder.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static Typeface setTypefaceBold(@NonNull Context context) {
        return Typeface.createFromAsset(context.getAssets(), S.bold_font_path);
    }

    public static Typeface setTypefaceRegular(@NonNull Context context) {
        return Typeface.createFromAsset(context.getAssets(), S.regular_font_path);
    }

    @NonNull
    public static ArrayList<PostOpPainBlocksModal> prepareLowerExtremityData() {
        ArrayList<PostOpPainBlocksModal> arrayList = new ArrayList<>();
        PostOpPainBlocksModal modal = null;
        modal = new PostOpPainBlocksModal();
        modal.setName(S.femoral);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.adductor_canal);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.lumbar_plexus);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.sciatic);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.popliteal);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.popliteal_sciatic);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.saphenous);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.other);
        arrayList.add(modal);

        return arrayList;
    }

    @NonNull
    public static ArrayList<PostOpPainBlocksModal> prepareUpperExtremityData() {
        ArrayList<PostOpPainBlocksModal> arrayList = new ArrayList<>();
        PostOpPainBlocksModal modal = null;
        modal = new PostOpPainBlocksModal();
        modal.setName(S.brachial_plexus);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.supraclavicular);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.interscalene);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.axillary);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.other);
        arrayList.add(modal);

        return arrayList;
    }

    @NonNull
    public static ArrayList<PostOpPainBlocksModal> prepareNeuraxialData() {
        ArrayList<PostOpPainBlocksModal> arrayList = new ArrayList<>();
        PostOpPainBlocksModal modal = null;
        modal = new PostOpPainBlocksModal();
        modal.setName(S.thoracic_epidural);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.lumbar_epidural);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.spinal_single_shot);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.caudal_single_shot);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.continuous_caudal);
        arrayList.add(modal);

        return arrayList;
    }

    @NonNull
    public static ArrayList<PostOpPainBlocksModal> prepareChestAndAbdomenData() {
        ArrayList<PostOpPainBlocksModal> arrayList = new ArrayList<>();
        PostOpPainBlocksModal modal = null;
        modal = new PostOpPainBlocksModal();
        modal.setName(S.paravertebral_thoracic_single_level);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.paravertebral_thoracic_additional_level);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.interscalene_block);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.interscalene_block_multiple);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.tap_or_rectus_sheath_block);
        arrayList.add(modal);

        modal = new PostOpPainBlocksModal();
        modal.setName(S.other);
        arrayList.add(modal);

        return arrayList;
    }

    public static String[] getHourOfTime(@NonNull String time) {
        //  String[] arr = time.split(":");
        return time.split(":");
    }

    public static void showTimePicker(@NonNull final TextView txtStatrtime, @NonNull final Context context, @NonNull final String minhour, @NonNull final String min_minute, @NonNull final String max_hour, @NonNull final String max_minute) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        int second = mcurrentTime.get(Calendar.SECOND);
       /* TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                txtStatrtime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");

        mTimePicker.show();*/
        //TimePickerDialog
        if (!minhour.equals(""))
            hour = Integer.parseInt(minhour);
        if (!min_minute.equals(""))
            minute = Integer.parseInt(min_minute);

        CustomTimePickerDialog rangeTimePickerDialog;
        rangeTimePickerDialog = new CustomTimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                boolean validTime = true;
                if (!minhour.equals("") && !min_minute.equals("")) {

                    if (selectedHour < Integer.parseInt(minhour) || (selectedHour == Integer.parseInt(minhour) && selectedMinute < Integer.parseInt(min_minute))) {
                        validTime = false;
                    }
                }
                if (!max_hour.equals("") && !max_minute.equals("")) {
                    if (selectedHour > Integer.parseInt(max_hour) || (selectedHour == Integer.parseInt(max_hour) && selectedMinute > Integer.parseInt(max_minute))) {
                        validTime = false;
                    }
                }


                if (validTime) {
                    txtStatrtime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                } else {
                    showToast(context, context.getString(R.string.select_valid_time));
                }


            }
        }, hour, minute, true);//Yes 24 hour time
        rangeTimePickerDialog.setTitle("Select Time");
        if (!minhour.equals("") && !min_minute.equals(""))
            rangeTimePickerDialog.setMin(Integer.parseInt(minhour), Integer.parseInt(min_minute));
        if (!max_hour.equals("") && !max_minute.equals(""))
            rangeTimePickerDialog.setMax(Integer.parseInt(max_hour), Integer.parseInt(max_minute));

        rangeTimePickerDialog.show();
    }

    @NonNull
    public static JSONObject getDefaultJson(Context context, String patient_id) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(S.api_record_id, patient_id);
        jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, S.user_id));

        return jsonObject;
    }

    @NonNull
    public static String getUniqueImageName() {
        //will generate a random num
        //between 15-10000
        Random random = new Random();
        int num = random.nextInt(1000 + 15) + 15;
        return "img_" + num + ".png";
    }

    public static void confirmDialog(@NonNull final Context context, @NonNull final String msg, String positive_btn, String negative_btn, final String id, final int list_pos) {
        //android.R.string.cancel
        //android.R.string.yes
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);
        aBuilder.setTitle(msg)
                .setCancelable(false)
                .setNegativeButton(negative_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (context instanceof QualityInformation) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof QIAirwayRespiratoryActivity) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof CardiovascularActivity) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof NeurologicActivity) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof RegionalActivity) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof ProceduralActivity) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof PharmacyBloodBank) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof MorbidityMortality) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof ComplianceActivity) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof InvasiveLinesActivity) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof ProceduresActivity) {
                            ProceduresActivity qualityInformation = (ProceduresActivity) context;
                            try {
                                qualityInformation.onBackPressed();
                            } catch (Exception e) {
                                Log.e(ProceduresActivity.TAG, e.toString());
                            }
                            //((Activity) context).finish();
                        }
                        if (context instanceof PostOpPlainBlocksActivity) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof CardiacAndTEE) {
                            ((Activity) context).finish();
                        }
                       /* if (context instanceof PostOpPainBlocksOptions) {
                            ((Activity) context).finish();
                        }*/
                        if (context instanceof ImagesActivity) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof LowerExtremityActivity) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof UpperExtremityActivity) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof NEURAXIAL) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof ChestAndAbdomenActivity) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof PatientSafety) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof InvasiveLinesAndSpecialServices) {
                            ((Activity) context).finish();
                        }
                        if (context instanceof ChargeInformation) {
                            ((Activity) context).finish();
                        }
                    }
                }).setPositiveButton(positive_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (context instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) context;
                    if (msg.equalsIgnoreCase(context.getString(R.string.reopen_confirm_message))) {
                        homeActivity.reopen(id, list_pos);
                    } else {
                        MySharedPreferences.clearAll(context);
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                        // homeActivity.logout();
                    }

                } else if (context instanceof PatientDetailsActivity) {
                    PatientDetailsActivity patientDetailsActivity = (PatientDetailsActivity) context;
                    patientDetailsActivity.reopen(id);
                } else if (context instanceof ImagesListActivity) {
                    ImagesListActivity imagesListActivity = (ImagesListActivity) context;
                    imagesListActivity.deletePatientImage(id);
                } else if (context instanceof QualityInformation) {
                    QualityInformation qualityInformation = (QualityInformation) context;
                    try {
                        qualityInformation.submitQIDetails(id);
                    } catch (Exception e) {
                        Log.e(QualityInformation.TAG, e.toString());
                    }

                } else if (context instanceof QIAirwayRespiratoryActivity) {
                    QIAirwayRespiratoryActivity qualityInformation = (QIAirwayRespiratoryActivity) context;
                    try {
                        qualityInformation.submitQIAirway();
                    } catch (Exception e) {
                        Log.e(QIAirwayRespiratoryActivity.TAG, e.toString());
                    }
                }
                else if (context instanceof PatientSafety) {
                    PatientSafety qualityInformation = (PatientSafety) context;
                    try {
                        qualityInformation.submitQIAirway();
                    } catch (Exception e) {
                        Log.e(PatientSafety.TAG, e.toString());
                    }
                }
                else if (context instanceof CardiovascularActivity) {
                    CardiovascularActivity qualityInformation = (CardiovascularActivity) context;
                    try {
                        qualityInformation.submitQIAirway();
                    } catch (Exception e) {
                        Log.e(CardiovascularActivity.TAG, e.toString());
                    }
                } else if (context instanceof NeurologicActivity) {
                    NeurologicActivity qualityInformation = (NeurologicActivity) context;
                    try {
                        qualityInformation.submitQIAirway();
                    } catch (Exception e) {
                        Log.e(NeurologicActivity.TAG, e.toString());
                    }
                } else if (context instanceof RegionalActivity) {
                    RegionalActivity qualityInformation = (RegionalActivity) context;
                    try {
                        qualityInformation.submitQIAirway();
                    } catch (Exception e) {
                        Log.e(RegionalActivity.TAG, e.toString());
                    }
                } else if (context instanceof ProceduralActivity) {
                    ProceduralActivity qualityInformation = (ProceduralActivity) context;
                    try {
                        qualityInformation.submitQIAirway();
                    } catch (Exception e) {
                        Log.e(ProceduralActivity.TAG, e.toString());
                    }
                } else if (context instanceof PharmacyBloodBank) {
                    PharmacyBloodBank qualityInformation = (PharmacyBloodBank) context;
                    try {
                        qualityInformation.submitQIAirway();
                    } catch (Exception e) {
                        Log.e(PharmacyBloodBank.TAG, e.toString());
                    }
                } else if (context instanceof MorbidityMortality) {
                    MorbidityMortality qualityInformation = (MorbidityMortality) context;
                    try {
                        qualityInformation.submitQIAirway();
                    } catch (Exception e) {
                        Log.e(MorbidityMortality.TAG, e.toString());
                    }
                } else if (context instanceof ComplianceActivity) {
                    ComplianceActivity qualityInformation = (ComplianceActivity) context;
                    try {
                        qualityInformation.submitQIAirway();
                    } catch (Exception e) {
                        Log.e(ComplianceActivity.TAG, e.toString());
                    }
                } else if (context instanceof InvasiveLinesActivity) {
                    InvasiveLinesActivity invasiveLinesActivity = (InvasiveLinesActivity) context;
                    try {
                        invasiveLinesActivity.submitInvasiveLine();
                    } catch (Exception e) {
                        Log.e(InvasiveLinesActivity.TAG, e.toString());
                    }
                } else if (context instanceof PostOpPlainBlocksActivity) {
                    PostOpPlainBlocksActivity proceduresActivity = (PostOpPlainBlocksActivity) context;
                    try {
                        proceduresActivity.submitInvasiveLine();
                    } catch (Exception e) {
                        Log.e(PostOpPlainBlocksActivity.TAG, e.toString());
                    }
                } else if (context instanceof CardiacAndTEE) {
                    CardiacAndTEE proceduresActivity = (CardiacAndTEE) context;
                    try {
                        proceduresActivity.submitInvasiveLine();
                    } catch (Exception e) {
                        Log.e(CardiacAndTEE.TAG, e.toString());
                    }
                } /*else if (context instanceof PostOpPainBlocksOptions) {
                    PostOpPainBlocksOptions proceduresActivity = (PostOpPainBlocksOptions) context;
                    try {
                        proceduresActivity.submitInvasiveLine();
                    } catch (Exception e) {
                        Log.e(PostOpPainBlocksOptions.TAG, e.toString());
                    }
                }*/ else if (context instanceof ImagesActivity) {
                    ImagesActivity proceduresActivity = (ImagesActivity) context;
                    try {
                        proceduresActivity.saveImage();
                    } catch (Exception e) {
                        Log.e(ImagesActivity.TAG, e.toString());
                    }
                }
                else if (context instanceof LowerExtremityActivity) {
                    LowerExtremityActivity proceduresActivity = (LowerExtremityActivity) context;
                    try {
                        proceduresActivity.submitInvasiveLine();
                    } catch (Exception e) {
                        Log.e(LowerExtremityActivity.TAG, e.toString());
                    }
                } else if (context instanceof UpperExtremityActivity) {
                    UpperExtremityActivity proceduresActivity = (UpperExtremityActivity) context;
                    try {
                        proceduresActivity.submitInvasiveLine();
                    } catch (Exception e) {
                        Log.e(UpperExtremityActivity.TAG, e.toString());
                    }
                }
                else if (context instanceof NEURAXIAL) {
                    NEURAXIAL proceduresActivity = (NEURAXIAL) context;
                    try {
                        proceduresActivity.submitInvasiveLine();
                    } catch (Exception e) {
                        Log.e(NEURAXIAL.TAG, e.toString());
                    }
                }
                else if (context instanceof ChestAndAbdomenActivity) {
                    ChestAndAbdomenActivity proceduresActivity = (ChestAndAbdomenActivity) context;
                    try {
                        proceduresActivity.submitInvasiveLine();
                    } catch (Exception e) {
                        Log.e(ChestAndAbdomenActivity.TAG, e.toString());
                    }
                }
                else if (context instanceof InvasiveLinesAndSpecialServices) {
                    InvasiveLinesAndSpecialServices proceduresActivity = (InvasiveLinesAndSpecialServices) context;
                    try {
                        proceduresActivity.saveInvasiveCharge();
                    } catch (Exception e) {
                        Log.e(InvasiveLinesAndSpecialServices.TAG, e.toString());
                    }
                }
                else if (context instanceof ChargeInformation) {
                    ChargeInformation proceduresActivity = (ChargeInformation) context;
                    try {
                        proceduresActivity.saveChargeInformation("back");
                    } catch (Exception e) {
                        Log.e(InvasiveLinesAndSpecialServices.TAG, e.toString());
                    }
                }
            }
        });

        AlertDialog aleart = aBuilder.create();
        aleart.show();
        Button nbutton = aleart.getButton(DialogInterface.BUTTON_POSITIVE);
        nbutton.setTextColor(ContextCompat.getColor(context, R.color.charge_text_color));
        Button nbutton1 = aleart.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton1.setTextColor(ContextCompat.getColor(context, R.color.red_color));

        // return result[0];
    }

    @NonNull
    public static MyProgressDialog showPrograsssDialog(Context context) {
        MyProgressDialog progressDialog = new MyProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.show();

        return progressDialog;
    }

    public static void showDatePicker(@NonNull final TextView textView, @NonNull Context context, @NonNull String minDate, @NonNull String max_date) {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = S.api_date_format;
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                textView.setText(sdf.format(myCalendar.getTime()));

                //   updateLabel();
            }

        };

        if (minDate.equals("") && max_date.equals("")) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } else if (!max_date.equals("")) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        } else {

            long start_date = 0;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(S.api_date_format);
                Date datemin = sdf.parse(minDate);

                start_date = datemin.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(start_date);

            datePickerDialog.show();
        }
        /*new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
*/
    }

    public static void showNoteDialog(@NonNull final Context context, @NonNull final String msg, final String id, final String page_id) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.note_dialog);
        final EditText edt_space = (EditText) dialog.findViewById(R.id.edt_space);
        //edt_space.setImeOptions(EditorInfo.IME_ACTION_DONE);
        TextView txt_submit = (TextView) dialog.findViewById(R.id.txt_submit);
        if (!msg.equals("")) {
            edt_space.setText(msg);
            edt_space.setSelection(msg.length());
        }

        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@Nullable View view) {
                //{"user_id":"251","record_id":"993","page":"2","notes":"test note test note"}
                if (!edt_space.getText().toString().trim().equals("")) {
                    try {
                        if (view != null) {
                            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(S.api_user_id, MySharedPreferences.getPreferences(context, S.user_id));
                        jsonObject.put(S.api_record_id, id);
                        jsonObject.put(S.api_page, page_id);
                        jsonObject.put(S.api_notes, edt_space.getText().toString().trim());

                        if (context instanceof CancelCase) {
                            CancelCase cancelactivity = (CancelCase) context;
                            cancelactivity.sendNote(jsonObject);
                        } else if (context instanceof PatientDetailsActivity) {
                            PatientDetailsActivity patientDetailsActivity = (PatientDetailsActivity) context;
                            patientDetailsActivity.sendNote(jsonObject);
                        } else if (context instanceof ChargeInformation) {
                            ChargeInformation changeInformation = (ChargeInformation) context;
                            changeInformation.sendNote(jsonObject);
                        } else if (context instanceof InvasiveLinesAndSpecialServices) {
                            InvasiveLinesAndSpecialServices changeInformation = (InvasiveLinesAndSpecialServices) context;
                            changeInformation.sendNote(jsonObject);
                        } else if (context instanceof InvasiveLinesActivity) {
                            InvasiveLinesActivity changeInformation = (InvasiveLinesActivity) context;
                            changeInformation.sendNote(jsonObject);
                        } else if (context instanceof PostOpPlainBlocksActivity) {
                            PostOpPlainBlocksActivity changeInformation = (PostOpPlainBlocksActivity) context;
                            changeInformation.sendNote(jsonObject);
                        } else if (context instanceof CardiacAndTEE) {
                            CardiacAndTEE changeInformation = (CardiacAndTEE) context;
                            changeInformation.sendNote(jsonObject);
                        } else if (context instanceof QualityInformation) {
                            QualityInformation changeInformation = (QualityInformation) context;
                            changeInformation.sendNote(jsonObject);
                        } else if (context instanceof AdvancedQIActivity) {
                            AdvancedQIActivity changeInformation = (AdvancedQIActivity) context;
                            changeInformation.sendNote(jsonObject);
                        } else if (context instanceof ImagesListActivity) {
                            ImagesListActivity changeInformation = (ImagesListActivity) context;
                            changeInformation.sendNote(jsonObject);
                        }

                        dialog.dismiss();

                    } catch (Exception e) {
                        Log.e("Util.java", e.toString());
                    }
                } else {
                    Util.setError(edt_space);
                }

            }
        });

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }

    @NonNull
    public static JSONObject getNoteParameter(String user_id, String record_id, String page_id) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(S.api_user_id, user_id);
        jsonObject.put(S.api_record_id, record_id);
        jsonObject.put(S.api_page, page_id);

        return jsonObject;

    }



    public static boolean isValidEmail(@NonNull String target) {

        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public static boolean isValidName(@NonNull String target) {

        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return Pattern.compile("^([a-zA-Z]{2,}\\s[a-zA-z]{1,}'?-?[a-zA-Z]{2,}\\s?([a-zA-Z]{1,})?)").matcher(target).matches();
        }
    }

   /* public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }*/

    public static boolean isNetworkConnected(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @NonNull
    public static String getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        return getDateString(date);
    }
    @NonNull
    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return getDateString(date);
    }
    @NonNull
    public static String getTommorrowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        Date date = calendar.getTime();
        return getDateString(date);
    }
    @NonNull
    public static String getDateString(Date date){
       // String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        String day          = (String) DateFormat.format("dd",   date); // 20
        String monthString  = (String) DateFormat.format("MMMM",  date); // Jun
      //  String monthNumber  = (String) DateFormat.format("MM",   date); // 06
        String year         = (String) DateFormat.format("yyyy", date); // 2013

        return monthString+" "+day+", "+year;
    }
    public static String getDate(int day){
        Calendar calendar;
        Date date;
        SimpleDateFormat df;
        switch (day){
            case 0:
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -1);
                date = calendar.getTime();
                df = new SimpleDateFormat("yyyy-MM-dd");
                return  df.format(date.getTime());
            case 1:
                calendar = Calendar.getInstance();
                date = calendar.getTime();
                df = new SimpleDateFormat("yyyy-MM-dd");
                return  df.format(date.getTime());
            case 2:
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);
                date = calendar.getTime();
                df = new SimpleDateFormat("yyyy-MM-dd");
                return  df.format(date.getTime());
            default:
                return null;
        }
    }
}

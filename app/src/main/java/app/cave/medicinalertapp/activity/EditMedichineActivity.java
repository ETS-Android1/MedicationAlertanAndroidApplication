package app.cave.medicinalertapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.cave.medicinalertapp.R;
import app.cave.medicinalertapp.StaticVariables;
import app.cave.medicinalertapp.recicver.AlarmReceiver;
import app.cave.medicinalertapp.database.AlarmDatabase;
import app.cave.medicinalertapp.database.MedicineDatabase;
import app.cave.medicinalertapp.model.AlarmModel;
import app.cave.medicinalertapp.model.MedicineModel;
import app.cave.medicinalertapp.server.ImageSaver;

public class EditMedichineActivity extends AppCompatActivity {

    public SharedPreferences sharedPreferences;

    String tableName;
    int id;
    MedicineModel medicineModel, nMedicineModel;
    List<MedicineModel> medicineModelList;
    MedicineDatabase dbHelper;
    AlarmModel bAlarmModel;
    AlarmDatabase alarmDatabase;
    List<AlarmModel> alarmModelList;

    EditText medNameET;
    Spinner medTypeSP, numberOfSlotSP;
    TextView firstSlotTimeTV, secondSlotTimeTV, thirdSlotTimeTV;
    LinearLayout firstSlotLAYOUT, secondSlotLAYOUT, thirdSlotLAYOUT;
    RadioButton beforeMealRB, afterMealRB;
    ImageView takeSnapIV, medicineIV;
    Button editButton, retakeButton, cancelButton;
    CardView medicineImageCV;


    String bMedName, imagePath, formattedTime, bTableName;
    String aFirstSlotTime, aSecondSlotTime, aThirdSlotTime, aMedType, aMedName, aImagePath, aTableName;
    int bFirstRC, bSecondRC, bThirdRC,  bNumberOfSlot;
    int aNumberOfSlot, lastRequestCode, aFirstRC, aSecondRC, aThirdRC;
    boolean allPermission;
    Calendar myCalender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medichine__edit);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }
/*
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);*/


        sharedPreferences = getSharedPreferences("alarmRequestCode", MODE_PRIVATE);
        lastRequestCode = sharedPreferences.getInt("requestCodeValue", 1);

        checkMultiplePermissions();

        Intent intent = getIntent();
        tableName = intent.getStringExtra("tableName");
        id = intent.getIntExtra("id", -1);
        bTableName = tableName;

        medicineModel = new MedicineModel();
        nMedicineModel = new MedicineModel();
        medicineModelList = new ArrayList<>();
        dbHelper = new MedicineDatabase(this);
        bAlarmModel = new AlarmModel();
        alarmModelList = new ArrayList<>();
        alarmDatabase = new AlarmDatabase(this);


        if (id != -1) {

            medicineModel = dbHelper.getSingleMedicine(String.valueOf(id), tableName);

        } else {

            Log.d("data", "data not found");
        }


        init();
        setData();
        setOnClick();


        try {
            alarmDatabase = new AlarmDatabase(this);
            String searchKeywordForAlarm = medicineModel.getMedicineName() + medicineModel.getUniqueCode();
            alarmModelList = alarmDatabase.getSelectedAlarm(searchKeywordForAlarm);
            bAlarmModel = alarmModelList.get(0);

        }catch (Exception e){

            Toast.makeText(this, "There are an error in editing alarm", Toast.LENGTH_SHORT).show();
        }


    }

    private void init() {

        medNameET = (EditText) findViewById(R.id.medicine_name_ET);
        medTypeSP = (Spinner) findViewById(R.id.medicine_type_SP);
        numberOfSlotSP = (Spinner) findViewById(R.id.no_of_times_SP);
        firstSlotTimeTV = (TextView) findViewById(R.id.first_slot_TV);
        secondSlotTimeTV = (TextView) findViewById(R.id.second_slot_TV);
        thirdSlotTimeTV = (TextView) findViewById(R.id.third_slot_TV);
        firstSlotLAYOUT = (LinearLayout) findViewById(R.id.first_slot_LAYOUT);
        secondSlotLAYOUT = (LinearLayout) findViewById(R.id.second_slot_LAYOUT);
        thirdSlotLAYOUT = (LinearLayout) findViewById(R.id.third_slot_layout);
        beforeMealRB = (RadioButton) findViewById(R.id.before_meal_RB);
        afterMealRB = (RadioButton) findViewById(R.id.after_meal_RB);
        editButton = (Button) findViewById(R.id.set_BTN);
        retakeButton = (Button) findViewById(R.id.retakeBTN);
        cancelButton = (Button) findViewById(R.id.cancelBTN);
        medicineIV = (ImageView) findViewById(R.id.medicine_IV);
        takeSnapIV = (ImageView) findViewById(R.id.iv_take_snap);
        medicineImageCV = (CardView) findViewById(R.id.medicine_image_cv);
    }

    private void setData() {

        medNameET.setText(medicineModel.getMedicineName());
        setTypeSpinnerPosition(medicineModel.getMedicineType());

        if (medicineModel.getImagePath().equals("null")) {

            medicineImageCV.setVisibility(View.GONE);

        } else {

            medicineImageCV.setVisibility(View.VISIBLE);
            String imagePath = medicineModel.getImagePath();
            ImageSaver imageSaver = new ImageSaver(this, this);
            imageSaver.loadImage(imagePath, medicineIV, medicineModel.getMedicineType());
        }

        setSlotSpinnerPosition(medicineModel.getNumberOfSlot());

        if (tableName.contains("before")) {

            beforeMealRB.setChecked(true);

        } else if (tableName.contains("after")) {

            afterMealRB.setChecked(true);
        }


    }

    private void setTypeSpinnerPosition(String type) {

        if (type.equals("Tablet")) {

            medTypeSP.setSelection(1);

        } else if (type.equals("Capsule")) {

            medTypeSP.setSelection(2);

        } else if (type.equals("syrup")) {

            medTypeSP.setSelection(3);

        } else if (type.equals("Injection")) {

            medTypeSP.setSelection(4);

        } else if (type.equals("Ointment")) {

            medTypeSP.setSelection(5);

        } else if (type.equals("Eye Drop")) {

            medTypeSP.setSelection(6);

        }
    }

    private void setSlotSpinnerPosition(int numberOfSlot) {

        if (numberOfSlot == 1) {

            numberOfSlotSP.setSelection(0);
            firstSlotLAYOUT.setVisibility(View.VISIBLE);
            secondSlotLAYOUT.setVisibility(View.GONE);
            thirdSlotLAYOUT.setVisibility(View.GONE);

            firstSlotTimeTV.setText(medicineModel.getFirstSlotTime());

        } else if (numberOfSlot == 2) {

            numberOfSlotSP.setSelection(1);
            firstSlotLAYOUT.setVisibility(View.VISIBLE);
            secondSlotLAYOUT.setVisibility(View.VISIBLE);
            thirdSlotLAYOUT.setVisibility(View.GONE);

            firstSlotTimeTV.setText(medicineModel.getFirstSlotTime());
            secondSlotTimeTV.setText(medicineModel.getSecondSlotTime());

        } else if (numberOfSlot == 3) {

            numberOfSlotSP.setSelection(2);
            firstSlotLAYOUT.setVisibility(View.VISIBLE);
            secondSlotLAYOUT.setVisibility(View.VISIBLE);
            thirdSlotLAYOUT.setVisibility(View.VISIBLE);

            firstSlotTimeTV.setText(medicineModel.getFirstSlotTime());
            secondSlotTimeTV.setText(medicineModel.getSecondSlotTime());
            thirdSlotTimeTV.setText(medicineModel.getThirdSlotTime());
        }
    }

    private void setOnClick() {


        numberOfSlotSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                    firstSlotLAYOUT.setVisibility(View.VISIBLE);
                    secondSlotLAYOUT.setVisibility(View.GONE);
                    thirdSlotLAYOUT.setVisibility(View.GONE);

                } else if (position == 1) {

                    firstSlotLAYOUT.setVisibility(View.VISIBLE);
                    secondSlotLAYOUT.setVisibility(View.VISIBLE);
                    thirdSlotLAYOUT.setVisibility(View.GONE);

                } else if (position == 2) {

                    firstSlotLAYOUT.setVisibility(View.VISIBLE);
                    secondSlotLAYOUT.setVisibility(View.VISIBLE);
                    thirdSlotLAYOUT.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        firstSlotLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showHourPicker("Select first slot", 1);

            }
        });


        secondSlotLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showHourPicker("Select second slot", 2);
            }
        });


        thirdSlotLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showHourPicker("Select third slot", 3);
            }
        });


        takeSnapIV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {


                takePhoto();

            }
        });


        retakeButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                takePhoto();

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                medicineImageCV.setVisibility(View.GONE);
            }
        });


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= 23 && !allPermission) {


                    checkMultiplePermissions();

                } else {


                    mustExecute();

                }


            }
        });


    }

    private void mustExecute() {

        if (checkValidity()) {


            aMedName = medNameET.getText().toString();
            aMedType = getMedicineType();
            saveImageToDirectory();
            aNumberOfSlot = numberOfSlotSP.getSelectedItemPosition() + 1;
            getSlotTime();

            if (beforeMealRB.isChecked()){

                aTableName = "before_table";

            }else if (afterMealRB.isChecked()){

                aTableName = "after_table";

            }



            nMedicineModel.setId(id);
            nMedicineModel.setDate(medicineModel.getDate());
            nMedicineModel.setMedicineName(aMedName);
            nMedicineModel.setMedicineType(aMedType);
            nMedicineModel.setImagePath(aImagePath);
            nMedicineModel.setNumberOfSlot(aNumberOfSlot);
            nMedicineModel.setFirstSlotTime(aFirstSlotTime);
            nMedicineModel.setSecondSlotTime(aSecondSlotTime);
            nMedicineModel.setThirdSlotTime(aThirdSlotTime);
            nMedicineModel.setNumberOfDays(medicineModel.getNumberOfDays());
            nMedicineModel.setEveryday(medicineModel.isEveryday());
            nMedicineModel.setSpecificDaysOfWeek(medicineModel.isSpecificDaysOfWeek());
            nMedicineModel.setDaysInterval(medicineModel.isDaysInterval());
            nMedicineModel.setDaysNameOfWeek(medicineModel.getDaysNameOfWeek());
            nMedicineModel.setDaysInterval(medicineModel.getDaysInterval());
            nMedicineModel.setStartDate(medicineModel.getStartDate());
            nMedicineModel.setStatus(medicineModel.getStatus());
            nMedicineModel.setMedicineMeal(getMedicineMeal());
            nMedicineModel.setUniqueCode(medicineModel.getUniqueCode());


            if (bTableName.equals(aTableName)){

                dbHelper.updateData(nMedicineModel, aTableName);


            }else {

                dbHelper.insertData(nMedicineModel, aTableName);
                dbHelper.deleteData(medicineModel, bTableName);


            }


            updateAlarm(medicineModel.getDate(), aFirstSlotTime, aSecondSlotTime, aThirdSlotTime);


            startActivity(new Intent(EditMedichineActivity.this, MainActivity.class));
            finish();


        }
    }

    private void updateAlarm(String date, String aFirstSlotTime, String aSecondSlotTime, String aThirdSlotTime) {



        bFirstRC = bAlarmModel.getFirstSlotRequestCode();
        bSecondRC = bAlarmModel.getSecondSlotRequestCode();
        bThirdRC = bAlarmModel.getThirdSlotRequestCode();
        bNumberOfSlot = medicineModel.getNumberOfSlot();



        if (bNumberOfSlot == 1){

            String combine = medicineModel.getDate() + " " + medicineModel.getFirstSlotTime();
            deleteAlarm(combine, bFirstRC);

        }else if (bNumberOfSlot == 2){

            String combine = medicineModel.getDate() + " " + medicineModel.getFirstSlotTime();
            deleteAlarm(combine, bFirstRC);

            String combine2 = medicineModel.getDate() + " " + medicineModel.getSecondSlotTime();
            deleteAlarm(combine2, bSecondRC);

        }else if (bNumberOfSlot == 3){

            String combine = medicineModel.getDate() + " " + medicineModel.getFirstSlotTime();
            deleteAlarm(combine, bFirstRC);

            String combine2 = medicineModel.getDate() + " " + medicineModel.getSecondSlotTime();
            deleteAlarm(combine2, bSecondRC);

            String combine3 = medicineModel.getDate() + " " + medicineModel.getThirdSlotTime();
            deleteAlarm(combine3, bThirdRC);

        }



        setAlarm(aNumberOfSlot);
        createAlarmModelObject();



    }

    private void deleteAlarm(String combine, int requestCode) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");

        Calendar calendar = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();

        try {
            calendar.setTime(sdf.parse(combine));

            int dateForAlarm = calendar.get(Calendar.DAY_OF_MONTH);
            int monthForAlarm = calendar.get(Calendar.MONTH);
            int yearForAlarm = calendar.get(Calendar.YEAR);
            int hourForAlarm = calendar.get(Calendar.HOUR_OF_DAY);
            int minuteForAlarm = calendar.get(Calendar.MINUTE);
            int secondForAlarm = 0;

            cal.set(yearForAlarm, monthForAlarm, dateForAlarm, hourForAlarm, minuteForAlarm, secondForAlarm);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("medName", aMedName);
        intent.putExtra("imagePath", aImagePath);
        intent.putExtra("mealStatus", medicineModel.getStatus());
        intent.putExtra("time", combine);
        intent.putExtra("medType", aMedType);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);
    }



    private void setAlarm(int n){

        if (n == 1){

            String combine = medicineModel.getDate() + " " + aFirstSlotTime;
            aFirstRC = lastRequestCode + 1;
            lastRequestCode ++;
            setFinalAlarm(combine, aFirstRC);

        }else if (n == 2){

            String combine = medicineModel.getDate() + " " + aFirstSlotTime;
            aFirstRC = lastRequestCode + 1;
            lastRequestCode ++;
            setFinalAlarm(combine, aFirstRC);

            String combine2 = medicineModel.getDate() + " " + aSecondSlotTime;
            aSecondRC = lastRequestCode + 1;
            lastRequestCode ++;
            setFinalAlarm(combine2, aSecondRC);

        }else if(n == 3){

            String combine = medicineModel.getDate() + " " + aFirstSlotTime;
            aFirstRC = lastRequestCode + 1;
            lastRequestCode ++;
            setFinalAlarm(combine, aFirstRC);

            String combine2 = medicineModel.getDate() + " " + aSecondSlotTime;
            aSecondRC = lastRequestCode + 1;
            lastRequestCode ++;
            setFinalAlarm(combine2, aSecondRC);

            String combine3 = medicineModel.getDate() + " " + aThirdSlotTime;
            aThirdRC = lastRequestCode + 1;
            lastRequestCode ++;
            setFinalAlarm(combine3, aThirdRC);

        }

    }


    private void setFinalAlarm(String combine, int requestCode){


        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");

        Calendar calendar = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();

        try {
            calendar.setTime(sdf.parse(combine));

            int dateForAlarm = calendar.get(Calendar.DAY_OF_MONTH);
            int monthForAlarm = calendar.get(Calendar.MONTH);
            int yearForAlarm = calendar.get(Calendar.YEAR);
            int hourForAlarm = calendar.get(Calendar.HOUR_OF_DAY);
            int minuteForAlarm = calendar.get(Calendar.MINUTE);
            int secondForAlarm = 0;

            cal.set(yearForAlarm, monthForAlarm, dateForAlarm, hourForAlarm, minuteForAlarm, secondForAlarm);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("medName", aMedName);
        intent.putExtra("imagePath", aImagePath);
        intent.putExtra("mealStatus", medicineModel.getStatus());
        intent.putExtra("time", combine);
        intent.putExtra("medType", aMedType);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);


    }




    private void createAlarmModelObject() {


        AlarmModel nAlarmModel = new AlarmModel();
        nAlarmModel.setId(bAlarmModel.getId());
        nAlarmModel.setNdt(aMedName + medicineModel.getUniqueCode());
        nAlarmModel.setNumberOfSlot(aNumberOfSlot);
        nAlarmModel.setFirstSlotTime(aFirstSlotTime);
        nAlarmModel.setSecondSlotTime(aSecondSlotTime);
        nAlarmModel.setThirdSlotTime(aThirdSlotTime);
        nAlarmModel.setFirstSlotRequestCode(aFirstRC);
        nAlarmModel.setSecondSlotRequestCode(aSecondRC);
        nAlarmModel.setThirdSlotRequestCode(aThirdRC);

        AlarmDatabase alarmDatabase = new AlarmDatabase(this);
        alarmDatabase.updateAlarm(nAlarmModel);




        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("requestCodeValue", lastRequestCode);
        editor.commit();



    }


    private boolean checkValidity() {


        if (medNameET.getText().toString().equals("")) {

            medNameET.setError("Enter a medicine name");
            Toast.makeText(this, "Please enter a valid medicine name", Toast.LENGTH_SHORT).show();
            return false;


        } else if ((firstSlotLAYOUT.getVisibility() == View.VISIBLE &&
                secondSlotLAYOUT.getVisibility() == View.VISIBLE &&
                thirdSlotLAYOUT.getVisibility() == View.VISIBLE)
                &&
                ((firstSlotTimeTV.getText().toString().contains("Set"))
                        || secondSlotTimeTV.getText().toString().contains("Set")
                        || thirdSlotTimeTV.getText().toString().contains("Set"))) {


            if (firstSlotTimeTV.getText().toString().contains("Set")) {

                Toast.makeText(this, "Please enter a valid time in slot 1", Toast.LENGTH_SHORT).show();

            } else if (secondSlotTimeTV.getText().toString().contains("Set")) {

                Toast.makeText(this, "Please enter a valid time in slot 2", Toast.LENGTH_SHORT).show();

            } else if (thirdSlotTimeTV.getText().toString().contains("Set")) {

                Toast.makeText(this, "Please enter a valid time in slot 3", Toast.LENGTH_SHORT).show();
            }


            return false;


        } else if (((firstSlotLAYOUT.getVisibility() == View.VISIBLE &&
                secondSlotLAYOUT.getVisibility() == View.VISIBLE &&
                thirdSlotLAYOUT.getVisibility() == View.GONE))
                &&
                ((firstSlotTimeTV.getText().toString().contains("Set"))
                        || secondSlotTimeTV.getText().toString().contains("Set"))) {


            if (firstSlotTimeTV.getText().toString().contains("Set")) {

                Toast.makeText(this, "Please enter a valid time in slot 1", Toast.LENGTH_SHORT).show();

            } else if (secondSlotTimeTV.getText().toString().contains("Set")) {

                Toast.makeText(this, "Please enter a valid time in slot 2", Toast.LENGTH_SHORT).show();
            }


            return false;


        } else if (((firstSlotLAYOUT.getVisibility() == View.VISIBLE &&
                secondSlotLAYOUT.getVisibility() == View.GONE &&
                thirdSlotLAYOUT.getVisibility() == View.GONE))
                &&
                ((firstSlotTimeTV.getText().toString().contains("Set")))) {


            Toast.makeText(this, "Please enter a valid time in slot 1", Toast.LENGTH_SHORT).show();
            return false;


        } else if (medTypeSP.getSelectedItemPosition() == 0) {

            Toast.makeText(this, "Please select a valid medicine type", Toast.LENGTH_SHORT).show();

            return false;

        } else {

            return true;
        }


    }


    private String getMedicineType() {

        String type = "";

        if (medTypeSP.getSelectedItemPosition() == 1) {

            type = "Tablet";

        }
        if (medTypeSP.getSelectedItemPosition() == 2) {

            type = "Capsule";
        }
        if (medTypeSP.getSelectedItemPosition() == 3) {

            type = "syrup";
        }

        if (medTypeSP.getSelectedItemPosition() == 4) {

            type = "Injection";

        }
        if (medTypeSP.getSelectedItemPosition() == 5) {

            type = "Ointment";

        }
        if (medTypeSP.getSelectedItemPosition() == 6) {

            type = "Eye Drop";
        }

        return type;

    }


    private void takePhoto() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, StaticVariables.MY_CAMERA_PERMISSION_CODE);

            } else {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, StaticVariables.CAMERA_REQUEST);
            }

        } else {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, StaticVariables.CAMERA_REQUEST);
        }
    }

    private void saveImageToDirectory() {

        if (medicineImageCV.getVisibility() == View.VISIBLE) {

            aImagePath = aMedName.toUpperCase() + "(" + Calendar.getInstance().getTime() + ")";
            BitmapDrawable drawable = (BitmapDrawable) medicineIV.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            ImageSaver imageSaver = new ImageSaver(this, this);
            imageSaver.saveImage(bitmap, aImagePath);

        } else {

            aImagePath = "null";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case StaticVariables.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    allPermission = true;

                    return;
                } else {
                    // Permission Denied
                    if (Build.VERSION.SDK_INT >= 23) {
                        Toast.makeText(
                                this,
                                "This application cannot run without Camera and Storage " +
                                        "Permissions.\nYou may relaunch the App or allow permissions" +
                                        " from Application Settings",
                                Toast.LENGTH_LONG).show();

                        allPermission = false;


                        if (perms.get(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                        }


                        if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                        }

                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StaticVariables.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //  Bitmap bMapScaled = Bitmap.createScaledBitmap(photo, 330, 189, true);
            medicineImageCV.setVisibility(View.VISIBLE);
            medicineIV.setImageBitmap(photo);
        }
    }

    public void showHourPicker(String message, final int number) {


        myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);


        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);


                    try {
                        SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a");

                        String strTime = myCalender.get(Calendar.HOUR_OF_DAY) + ":" + myCalender.get(Calendar.MINUTE);
                        Date time = sdf24.parse(strTime);
                        formattedTime = sdf12.format(time);

                        if (number == 1) {

                            firstSlotTimeTV.setText(formattedTime);

                        } else if (number == 2) {

                            secondSlotTimeTV.setText(formattedTime);

                        } else if (number == 3) {

                            thirdSlotTimeTV.setText(formattedTime);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                myTimeListener,
                hour,
                minute,
                false);

        try {

            timePickerDialog.setTitle(message);
            timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            timePickerDialog.show();

        } catch (Exception e) {

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void checkMultiplePermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
                permissionsNeeded.add("Camera");
            }

            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("Storage");
            }


            Map<String, Integer> perms = new HashMap<String, Integer>();
            // Initial
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

            if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // All Permissions Granted

                allPermission = true;

            }


            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        StaticVariables.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23)

            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);

                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        return true;
    }

    private void getSlotTime() {

        if (numberOfSlotSP.getSelectedItemPosition() == 0) {

            aFirstSlotTime = firstSlotTimeTV.getText().toString();
            aSecondSlotTime = "null";
            aThirdSlotTime = "null";

        } else if (numberOfSlotSP.getSelectedItemPosition() == 1) {

            aFirstSlotTime = firstSlotTimeTV.getText().toString();
            aSecondSlotTime = secondSlotTimeTV.getText().toString();
            aThirdSlotTime = "null";

        } else if (numberOfSlotSP.getSelectedItemPosition() == 2) {

            aFirstSlotTime = firstSlotTimeTV.getText().toString();
            aSecondSlotTime = secondSlotTimeTV.getText().toString();
            aThirdSlotTime = thirdSlotTimeTV.getText().toString();

        }
    }

    private String getMedicineMeal() {

        String meal = "";

        if (beforeMealRB.isChecked()) {

            meal = "Before Meal";

        } else if (afterMealRB.isChecked()) {

            meal = "After Meal";
        }

        return meal;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                super.onBackPressed();

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}






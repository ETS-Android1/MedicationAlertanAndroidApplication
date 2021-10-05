package app.cave.medicinalertapp.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
*/

import app.cave.medicinalertapp.R;
import app.cave.medicinalertapp.server.ImageSaver;

public class DetailsMedichineActivity extends AppCompatActivity {

    TextView medNameTV, dateTimeTV, numberOfSlotTV,  firstSlotTV, secondSlotTV,
            thirdSlotTV, numberOfDaysTV,  startDateTV, daysIntervalTV, statusTV;
    ImageView medicineIV, optionIV;
    LinearLayout statusLAYOUT, secondSlotLAYOUT, thirdSlotLAYOUT;
    String medicineName, dateTime, numberOfSlot,  firstSlotTime, secondSlotTime,
            thirdSlotTime, numberOfDays,  startDate, daysInterval, status, imagePath, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medichine__details);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }
/*

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);
*/


        init();
        receiveIntent();
        setTextAndIMageIntoView();
        setOnClick();
    }

    private void init() {

        medNameTV = (TextView) findViewById(R.id.medicine_name_TV_DA);
        dateTimeTV = (TextView) findViewById(R.id.date_and_time_TV_DA);
        numberOfSlotTV = (TextView) findViewById(R.id.number_of_slot_TV_DA);
        firstSlotTV = (TextView) findViewById(R.id.first_slot_TV_DA);
        secondSlotTV = (TextView) findViewById(R.id.second_slot_TV_DA);
        thirdSlotTV = (TextView) findViewById(R.id.third_slot_TV_DA);
        numberOfDaysTV = (TextView) findViewById(R.id.number_of_days_TV_DA);
        startDateTV = (TextView) findViewById(R.id.start_date_TV_DA);
        daysIntervalTV = (TextView) findViewById(R.id.days_interval_TV_DA);
        statusTV = (TextView) findViewById(R.id.status_TV_DA);

        medicineIV = (ImageView) findViewById(R.id.medicine_IV_DA);
        optionIV = (ImageView) findViewById(R.id.option_IV_DA);

        statusLAYOUT = (LinearLayout) findViewById(R.id.status_LAYOUT_DA);
        secondSlotLAYOUT = (LinearLayout) findViewById(R.id.second_slot_LAYOUT_DA);
        thirdSlotLAYOUT = (LinearLayout) findViewById(R.id.third_slot_layout_DA);
    }

    private void receiveIntent() {

        Intent intent = getIntent();
        medicineName = intent.getStringExtra("medName");
        dateTime = intent.getStringExtra("dateTime");
        numberOfSlot = intent.getStringExtra("numberOfSlot");
        firstSlotTime = intent.getStringExtra("firstSlotTime");
        secondSlotTime = intent.getStringExtra("secondSlotTime");
        thirdSlotTime = intent.getStringExtra("thirdSlotTime");
        numberOfDays = intent.getStringExtra("numberOfDays");
        startDate = intent.getStringExtra("startDate");
        daysInterval = intent.getStringExtra("daysInterval");
        status = intent.getStringExtra("status");
        imagePath = intent.getStringExtra("imagePath");
        type = intent.getStringExtra("type");


    }

    private void setTextAndIMageIntoView() {

        medNameTV.setText(medicineName + " (" + type + ")");
        dateTimeTV.setText(dateTime);
        numberOfSlotTV.setText(numberOfSlot);
        numberOfDaysTV.setText(numberOfDays);
        firstSlotTV.setText(firstSlotTime);

        if (secondSlotTime.equals("null")){

            secondSlotLAYOUT.setVisibility(View.GONE);

        }else {

            secondSlotTV.setText(secondSlotTime);
        }


        if (thirdSlotTime.equals("null")){

            thirdSlotLAYOUT.setVisibility(View.GONE);

        }else {

            thirdSlotTV.setText(thirdSlotTime);
        }


        startDateTV.setText(startDate);
        daysIntervalTV.setText(daysInterval);
        statusTV.setText(status);

        ImageSaver imageSaver = new ImageSaver(DetailsMedichineActivity.this, this);
        imageSaver.loadImage(imagePath, medicineIV, type);

    }

    public void setOnClick(){

        optionIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

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

package com.classroom.attendancerecord;

import com.classroom.home.MainActivity;
import com.classroom.home.R;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AttendanceRecordMain extends AppCompatActivity implements DecoratedBarcodeView.TorchListener, View.OnClickListener {

    private Bundle bData;
    private String s_login,s_groupid,s_userid,s_name;
    private Intent intent;
    private InsertData insertData;
    private String lastResult;
    private TextView tvhUserid,tvhName,tvhGroupid;
    private TextView tvScanned,tvAttendanceDate,tvAttendanceId,tvUserid,tvName,tvAttendanceTime;
    CusSpinnerAdapter cusSpinnerAdapter;
    Spinner spinner;
    ArrayList<SpinnerItem> attendanceCodeItems;
    private ImageView imageView;
    private BeepManager beepManager;
    private CameraSettings cameraSettings;
    private CaptureManager captureManager;
    private Drawable turnOnTorch, turnOffTorch;
    private DecoratedBarcodeView decoratedBarcodeView;
    private String stringTurnOnTorch, stringTurnOffTorch;
    private Collection<BarcodeFormat> formats = Arrays.asList(
            BarcodeFormat.QR_CODE,
            BarcodeFormat.EAN_13,
            BarcodeFormat.EAN_8,
            BarcodeFormat.UPC_EAN_EXTENSION,
            BarcodeFormat.UPC_E,
            BarcodeFormat.UPC_A);
    private ImageButton btnToggleTorch, btnToggleDecoratedBarcodeView;
    private String stringPauseDecoratedBarcodeView, stringResumeDecoratedBarcodeView;
    private Drawable pauseDecoratedBarcodeView, resumeDecoratedBarcodeView;
    //---------------------------
//    private SQLiteDatabase db;
//    int id , code;
    List<AttendanceInsertFeeback> data_list;
    //---------------------------
    private BarcodeCallback barcodeCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

            if (result.getText() == null || result.getText().equals(lastResult))
                return;

            lastResult = result.getText();
            tvScanned.setText(lastResult);
            tvUserid.setText(lastResult);
            decoratedBarcodeView.setStatusText(lastResult);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
            String[] datai = new String[]{lastResult.toString(),tvAttendanceId.getText().toString(),s_userid,s_login,s_groupid,s_name};
            insertData = new InsertData(AttendanceRecordMain.this,datai);
            try {
                data_list = insertData.execute("https://webhost2503.000webhostapp.com/classroom/attendancerecord/attendance_insertfeeback.php").get();
                tvName.setText(data_list.get(0).getName());
                tvAttendanceTime.setText(data_list.get(0).getAttendancetime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_record);
        Log.i("!!AttendanceRecordMain==>","111");

        initView();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        AdView adView = findViewById(R.id.adView);
        tvScanned = findViewById(R.id.tvScanned);
        tvAttendanceDate = findViewById(R.id.tvAttendanceDate);
        Date currentTime = Calendar.getInstance().getTime();
        tvAttendanceDate.setText(DateFormat.format("yyyy-MM-dd", currentTime).toString());
        tvAttendanceId = findViewById(R.id.tvAttendanceId);
        tvUserid = findViewById(R.id.tvUserid);
        tvName = findViewById(R.id.tvName);
        tvAttendanceTime = findViewById(R.id.tvAttendanceTime);
        imageView = findViewById(R.id.ivScanned);
        btnToggleTorch = findViewById(R.id.btnToggleTorch);
        decoratedBarcodeView = findViewById(R.id.decoratedBarcodeView);
        btnToggleDecoratedBarcodeView = findViewById(R.id.btnToggleDecoratedBarcodeView);
        AttendanceIdSpinner();

        tvScanned.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                beepManager.playBeepSoundAndVibrate();
                toggleDecoratedBarcodeView(decoratedBarcodeView);
            }
        });

        cameraSettings = new CameraSettings();
        beepManager = new BeepManager(this);
        stringTurnOnTorch = getResources().getString(R.string.turnOnTorch);
        stringTurnOffTorch = getResources().getString(R.string.turnOffTorch);
        turnOnTorch = getResources().getDrawable(R.mipmap.turn_on_torch, null);
        turnOffTorch = getResources().getDrawable(R.mipmap.turn_off_torch, null);
        stringPauseDecoratedBarcodeView = getResources().getString(R.string.pauseDecoratedBarcodeView);
        stringResumeDecoratedBarcodeView = getResources().getString(R.string.resumeDecoratedBarcodeView);
        pauseDecoratedBarcodeView = getResources().getDrawable(R.mipmap.pause_barcode_view, null);
        resumeDecoratedBarcodeView = getResources().getDrawable(R.mipmap.resume_barcode_view, null);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(AttendanceRecordMain.this, SplashPermissionActivity.class));
            finish();
        }

        MobileAds.initialize(this, getString(R.string.googleAdsKey));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        cameraSettings.setRequestedCameraId(0);
        decoratedBarcodeView.setTorchListener(this);
        decoratedBarcodeView.getBarcodeView().setCameraSettings(cameraSettings);
        decoratedBarcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));

        decoratedBarcodeView.resume();

        if (!hasTorch())
            btnToggleTorch.setVisibility(View.GONE);

        decoratedBarcodeView.decodeContinuous(barcodeCallback);
    }

    @Override
    public void onTorchOn() {
        btnToggleTorch.setImageDrawable(turnOffTorch);
    }

    @Override
    public void onTorchOff() {
        btnToggleTorch.setImageDrawable(turnOnTorch);
    }

    private boolean hasTorch() {
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void toggleTorch(View view) {
        if (getString(R.string.turnOnTorch).contentEquals(btnToggleTorch.getContentDescription())) {
            decoratedBarcodeView.setTorchOn();
            btnToggleTorch.setContentDescription(stringTurnOffTorch);
        } else {
            decoratedBarcodeView.setTorchOff();
            btnToggleTorch.setContentDescription(stringTurnOnTorch);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return decoratedBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    public void toggleDecoratedBarcodeView(View view) {
        if (getString(R.string.pauseDecoratedBarcodeView).contentEquals(btnToggleDecoratedBarcodeView.getContentDescription())) {
            decoratedBarcodeView.pauseAndWait();
            btnToggleDecoratedBarcodeView.setImageDrawable(resumeDecoratedBarcodeView);
            btnToggleDecoratedBarcodeView.setContentDescription(stringResumeDecoratedBarcodeView);
        } else {
            decoratedBarcodeView.resume();
            btnToggleDecoratedBarcodeView.setImageDrawable(pauseDecoratedBarcodeView);
            btnToggleDecoratedBarcodeView.setContentDescription(stringPauseDecoratedBarcodeView);
        }
    }

    private void initView() {
        bData = this.getIntent().getExtras();
        if(bData == null || bData.getString("s_login").equals("false")){
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("s_login","false");
            startActivity(intent);
            this.finish();
        }
        else {
            s_login   = bData.getString("s_login");
            s_groupid = bData.getString("s_groupid");
            s_userid  = bData.getString("s_userid");
            s_name   = bData.getString("s_name");

            tvhUserid = findViewById(R.id.tvhUserid);
            tvhName = findViewById(R.id.tvhName);
            tvhGroupid = findViewById(R.id.tvhGroupid);
            tvhUserid.setText(s_userid);
            tvhName.setText(s_name);
            tvhGroupid.setText(s_groupid);
        }
    }

    private void AttendanceIdSpinner(){
        spinner = findViewById(R.id.spnAttendanceCode);

        attendanceCodeItems = new ArrayList<SpinnerItem>();
        attendanceCodeItems.add(new SpinnerItem("到班","I"));
        attendanceCodeItems.add(new SpinnerItem("離班","O"));
        String ww = tvAttendanceId.getText().toString();
        Log.i("DB_edt","ww"+tvAttendanceId.getText().toString());
        cusSpinnerAdapter = new CusSpinnerAdapter(this,attendanceCodeItems);
        spinner.setAdapter(cusSpinnerAdapter);
//        if(bData.getString("type").equals("edit"))
//            spinner.setSelection(getPostiton(edt_clolor.getText().toString(),attendanceCodeItems));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem attendanceCodeItem= attendanceCodeItems.get(position);
                tvAttendanceId.setText(attendanceCodeItem.code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int getPostiton(String findString,ArrayList<SpinnerItem> attendanceCodeItems) {
        Log.i("DB_findString",findString.toString());
        int i;
        for (i = 0; i < attendanceCodeItems.size(); i++) {
            SpinnerItem attendanceCodeItem = attendanceCodeItems.get(i);
            if (findString.equals(attendanceCodeItem.code))
                break;
        }
        if (i == attendanceCodeItems.size())
            return 0;
        else
            return i;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnAdd:
//                add();
//                break;
//            case R.id.btnDel:
//                delete();
//                break;
//            case R.id.btnQuery:
//                query();
//                break;
//        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    //--------------------------

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.attendancerecord_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id){
            case R.id.action_back:
//                changeView(AttendanceRecordMain.this, MainActivity.class);
                intent = new Intent(this, MainActivity.class);
                intentPutExtraS();
                startActivity(intent);
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void intentPutExtraS(){
        intent.putExtra("s_login",s_login);
        intent.putExtra("s_groupid",s_groupid);
        intent.putExtra("s_userid",s_userid);
        intent.putExtra("s_name",s_name);
    }
    //intent跳轉activity函式
    public void changeView(Context context, Class<?> cla){
        intent = intent.setClass(context, cla);
        startActivity(intent);
        this.finish();
    }
}

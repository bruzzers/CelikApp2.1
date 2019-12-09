package com.example.celikapp20;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.transform.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeReader extends AppCompatActivity implements View.OnClickListener, GestureDetector.OnGestureListener {

    private ZXingScannerView scannerView;
    private Button button;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private GestureDetector detector;
    private boolean mCameraPermissionGranted=false;
    BarcodeProductDB db=new BarcodeProductDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_reader_content);
        button=(Button) findViewById(R.id.barcode_button);
        detector=new GestureDetector(BarcodeReader.this);

        getCameraPermission();
        button.setOnClickListener(BarcodeReader.this);


       }


    @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.barcode_button:
                        scanCode(v);
            }
        }

    public void scanCode(View view){
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(new ZXingScannerResultHandler());

        setContentView(scannerView);
        scannerView.startCamera();
    }

    private void getCameraPermission(){
        String[] permission={Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            mCameraPermissionGranted=true;
        }else{
            ActivityCompat.requestPermissions(this, permission, 1234 );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        detector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getX()<e2.getX()){
            NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }


    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler{

        @Override
        public void handleResult(com.google.zxing.Result result) {
            final String resultCode= result.getText();
            Toast.makeText(BarcodeReader.this, resultCode, Toast.LENGTH_SHORT).show();




            myRef=database.getReference();
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    ArrayList<Product> array= new ArrayList<>();
                    array.clear();
                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        Prodotto value = ds.getValue(Prodotto.class);
                        if(value.getCodice().toString().equals(resultCode)) {
                            Product p = new Product(value.getNome(), value.getMarca(), String.valueOf(value.getPrezzo()), value.getLocalita(), String.valueOf(value.getCodice()), String.valueOf(value.getLatitudine()), String.valueOf(value.getLongitudine()));
                            array.add(p);
                        }
                    }
                    if(!array.isEmpty()){
                        if(!isFinishing()) {
                            scannerView.stopCamera();
                            finish();
                            Intent open=new Intent(BarcodeReader.this, BarcodeReader.class);
                            startActivity(open);
                            db.clean();
                            db.insert(array.get(0).getNome(), array.get(0).getMarca(), resultCode);

                            Intent opn=new Intent(BarcodeReader.this, BarcodeProduct.class);
                            startActivity(opn);


                        }
                    }
                    else {
                        Toast.makeText(BarcodeReader.this, "Il prodotto NON è senza glutine", Toast.LENGTH_LONG).show();
                        scannerView.stopCamera();
                        finish();
                        Intent open=new Intent(BarcodeReader.this, BarcodeReader.class);
                        startActivity(open);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("tag", "Failed to read value.", error.toException());
                }
            });




        }
    }







}

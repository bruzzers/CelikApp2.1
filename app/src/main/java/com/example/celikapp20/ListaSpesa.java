package com.example.celikapp20;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Notification.VISIBILITY_PUBLIC;

public class ListaSpesa extends AppCompatActivity {
    private ListView ListaSpesaListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_spesa);
        ArrayList<Product> array=new ArrayList<>();
        ProductListAdapter adapter=new ProductListAdapter(this, R.layout.adapter_view_layout, array);
        ListaSpesaListView=(ListView) findViewById(R.id.ListaSpesaListView);


        DB db=new DB(this);
        StringBuilder sb=new StringBuilder();
        db.openReadableDB();
        Cursor c=db.getProd();
        ArrayList<Product> listaspesa=new ArrayList<>();
        listaspesa.clear();
        if(c.moveToFirst()) {
            do {
                Product p=new Product(c.getString(1), c.getString(4), c.getString(2), c.getString(3));
                listaspesa.add(p);
            }while (c.moveToNext());
        }
        else
            Toast.makeText(this, "Nessun elemento trovato", Toast.LENGTH_SHORT).show();

        if(!listaspesa.isEmpty()){
            ListaSpesaAdapter lsadapter=new ListaSpesaAdapter(ListaSpesa.this, R.layout.lista_spesa_adapter_view_layout, listaspesa);
            ListaSpesaListView.setAdapter(lsadapter );

        }








         /* Intent i=new Intent(this, MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this, 0, i, 0);
        long[] pattern={250,250};

        NotificationCompat.Builder n=new NotificationCompat.Builder(this)
                .setContentTitle("Svuotare Lista della Spesa")
                .setContentText("Hai degli elementi nella lista della spesa")
                .setSmallIcon(R.drawable.ic_notify)
                .setContentIntent(pi)
                .setVisibility(VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(pattern)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);
        NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, n.build());*/



    }


}
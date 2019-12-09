package com.example.celikapp20;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MostraFragment extends Fragment {

    String nome;
    String brand;
    String code;
    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup frame_container, Bundle savedInstanceState){
        View rootView=inflater.inflate(R.layout.fragment_barcode, frame_container, false);
        BarcodeProductDB db=new BarcodeProductDB(MostraFragment.super.getContext());
        Cursor c=db.getProd();
        if(c.moveToFirst()){
            nome=c.getString(1);
            code=c.getString(3);
            brand=c.getString(2);
            TextView tvnome=(TextView) rootView.findViewById(R.id.barcode_name);
            TextView tvbrand=(TextView) rootView.findViewById(R.id.barcode_brand);
            TextView tvcode=(TextView) rootView.findViewById(R.id.barcode_code);
            tvcode.setText(code);
            tvnome.setText(nome);
            tvbrand.setText(brand);
            button=(Button) rootView.findViewById(R.id.barcode_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent open=new Intent(MostraFragment.super.getContext(), BarcodeReader.class);
                    startActivity(open);
                }
            });
        }


        return rootView;
    }


}

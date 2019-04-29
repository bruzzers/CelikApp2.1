package com.example.celikapp20;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private Context mContext;
    private int mResource;
    public ProductListAdapter( Context context, int resource, ArrayList<Product> objects) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        String nome=getItem(position).getNome();
        String localita=getItem(position).getLocalita();
        String prezzo=getItem(position).getPrezzo();
        String marca=getItem(position).getMarca();


        Product user=new Product(nome,marca, prezzo, localita);
        LayoutInflater inflater=LayoutInflater.from(mContext);

        convertView= inflater.inflate(mResource, parent, false);

        TextView tvPrezzo=(TextView) convertView.findViewById(R.id.tv1);
        TextView tvLocalita=(TextView) convertView.findViewById(R.id.tv2);
        TextView tvNome=(TextView) convertView.findViewById(R.id.tv3);
        TextView tvMarca=(TextView) convertView.findViewById(R.id.tv4);
        Button bt=(Button) convertView.findViewById(R.id.button1);

        tvPrezzo.setText(prezzo + "€");
        tvLocalita.setText(localita);
        tvNome.setText(nome);
        tvMarca.setText(marca);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Button was clicked for list item " + getItem(position).getNome(), Toast.LENGTH_SHORT).show();
                DB db=new DB(mContext);
                StringBuilder sb=new StringBuilder();
                db.Insert(getItem(position).getNome(), getItem(position).getPrezzo(), getItem(position).getLocalita(), getItem(position).getMarca());




            }
        });


        return convertView;
    }

}

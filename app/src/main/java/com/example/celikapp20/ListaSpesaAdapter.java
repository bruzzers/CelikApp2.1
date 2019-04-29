package com.example.celikapp20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaSpesaAdapter extends ArrayAdapter<Product> {
    private Context mContext;
    private int mResource;
    public ListaSpesaAdapter( Context context, int resource, ArrayList<Product> objects) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        String nome=getItem(position).getNome();
        String localita=getItem(position).getLocalita();
        String prezzo=getItem(position).getPrezzo();
        String marca=getItem(position).getMarca();

        Product l=new Product(nome,marca,prezzo, localita);
        LayoutInflater inflate=LayoutInflater.from(mContext);

        convertView=inflate.inflate(mResource, parent, false);

        TextView lsTvPrezzo=(TextView) convertView.findViewById(R.id.lstv1);
        TextView lsTvNome=(TextView) convertView.findViewById(R.id.lstv2);
        TextView lsTvLocalita=(TextView) convertView.findViewById(R.id.lstv3);
        TextView lsTvMarca=(TextView) convertView.findViewById(R.id.lstv4);
        Button lsButton=(Button) convertView.findViewById(R.id.lsbutton1);

        lsTvPrezzo.setText(prezzo);
        lsTvNome.setText(nome);
        lsTvLocalita.setText(localita);
        lsTvMarca.setText(marca);

        lsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB db=new DB(mContext);
                StringBuilder sb=new StringBuilder();
                db.Delete(getItem(position).getNome(), getItem(position).getLocalita(), getItem(position).getPrezzo(), getItem(position).getMarca());

            }
        });

        return convertView;
    }

}

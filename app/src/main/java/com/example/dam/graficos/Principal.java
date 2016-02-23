package com.example.dam.graficos;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.example.dam.graficos.util.Dialogo;
import com.example.dam.graficos.util.OnDialogoListener;

public class Principal extends AppCompatActivity {

    private RadioGroup rdgroup, rdgroupColor;
    private SeekBar sb;
    private ImageView img;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mnGuardar:
                new Vista(this).Cargar();
                /*try {
                    Log.v("estado", "a guardar 1");
                    Bitmap b = loadBitmapFromView(new Vista(this));
                    if(b ==null){
                        Log.v("estado", "putnas de pollas");
                    }
                    Log.v("estado", "a guardar 2");
                    img.setImageBitmap(b);
                    //new Vista(this).guardar(b);
                    Log.v("estado", "a guardar3");
                } catch (Exception e) {
                }*/
                break;
            case R.id.mnLimpiar:
                new Vista(this).limpiar();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        img = (ImageView)findViewById(R.id.imageView);
    }

    /************** ONCLICKS *************/
    public void setFiguraP(View v) {
        final OnDialogoListener odl = new OnDialogoListener() {
            @Override
            public void onPreShow(View v) {
                rdgroup = (RadioGroup)v.findViewById(R.id.rdGroup);
            }

            @Override
            public void onOkSelecter(View v) {
                int id = rdgroup.getCheckedRadioButtonId();
                switch (id){
                    case R.id.rdCirculo:
                        new Vista(Principal.this).setFigura("circulo");
                        break;
                    case R.id.rdRecta:
                        new Vista(Principal.this).setFigura("linea");
                        break;
                    case R.id.rdRectangulo:
                        new Vista(Principal.this).setFigura("rectangulo");
                        break;
                    case R.id.rdPath:
                        new Vista(Principal.this).setFigura("path");
                        break;
                }
            }

            @Override
            public void onCancelSelecter(View v) {
            }
        };
        Dialogo d = new Dialogo(Principal.this, R.layout.figura_layout_dialog, odl);
        d.show();
    }

    public void setGrosorP(View v){
        final OnDialogoListener odl = new OnDialogoListener() {
            @Override
            public void onPreShow(View v) {
                sb = (SeekBar)v.findViewById(R.id.sbgrosor);
            }

            @Override
            public void onOkSelecter(View v) {
                new Vista(Principal.this).setGrosor(sb.getProgress());
            }

            @Override
            public void onCancelSelecter(View v) {
            }
        };
        Dialogo d = new Dialogo(Principal.this, R.layout.grosor_layout_dialog, odl);
        d.show();
    }

    public void setColor(View v) {
        final OnDialogoListener odl = new OnDialogoListener() {
            @Override
            public void onPreShow(View v) {
                rdgroupColor = (RadioGroup)v.findViewById(R.id.rdgColor);
            }

            @Override
            public void onOkSelecter(View v) {
                int id = rdgroupColor.getCheckedRadioButtonId();
                switch (id){
                    case R.id.rbAmarillo:
                        new Vista(Principal.this).setColor(Color.YELLOW);
                        break;
                    case R.id.rbAzul:
                        new Vista(Principal.this).setColor(Color.BLUE);
                        break;
                    case R.id.rbNegro:
                        new Vista(Principal.this).setColor(Color.BLACK);
                        break;
                    case R.id.rbVerde:
                        new Vista(Principal.this).setColor(Color.GREEN);
                        break;
                }
            }

            @Override
            public void onCancelSelecter(View v) {
            }
        };
        Dialogo d = new Dialogo(Principal.this, R.layout.color_layout_dialog, odl);
        d.show();
    }
}
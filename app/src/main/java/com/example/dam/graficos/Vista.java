package com.example.dam.graficos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Vista extends View{

    private float x1, y1, x2, y2, radio;
    private int ancho,alto;
    private Bitmap mapaDeBits;
    private Canvas lienzoFondo;
    private Paint pincel;
    private Path rectaPoligonal = new Path();
    private Bitmap imagenFondo;
    public static String figura="path";
    private static int color=Color.GRAY;
    private static float grosor=5;

    public Vista(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Vista(Context context) {
        super(context);
    }

    public void setFigura(String f){
        figura=f;
    }

    public void setColor(int c){
        color=c;
    }

    public void setGrosor(float g){
        grosor=g;
    }

    public void limpiar(){
        mapaDeBits = Bitmap.createBitmap(432, 593, Bitmap.Config.ARGB_8888);
        lienzoFondo = new Canvas(mapaDeBits);
        invalidate();
    }

    public Bitmap loadBitmapFromView(View v) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(432, 593, Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =v.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        v.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        pincel = new Paint();
        pincel.setColor(color);
        pincel.setAntiAlias(true);
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(grosor);

        canvas.drawBitmap(mapaDeBits, 0, 0, null);

        switch (figura){
            case "linea":
                canvas.drawLine(x1, y1, x2, y2, pincel);
                break;
            case "circulo":
                radio = (float)Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
                canvas.drawCircle(x1,y1,radio,pincel);
                break;
            case "rectangulo":
                canvas.drawRect(x1, y1, x2, y2, pincel);
                break;
            case "path":
                canvas.drawPath(rectaPoligonal,pincel);
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ancho=w;
        alto=h;
        mapaDeBits = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        lienzoFondo = new Canvas(mapaDeBits);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (figura){
            case "linea":
                dibujarLinea(event);
                break;
            case "circulo":
                dibujarCirculo(event);
                break;
            case "rectangulo":
                dibujarRectangulo(event);
                break;
            case "path":
                path(event);
                break;
        }
        return true;
    }

    /*************** I/O ****************************/

    public void guardar(Bitmap b) throws FileNotFoundException {
        /******** Obtener bitmap **********/
        /*File carpeta=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath());
        File archivo = new File (carpeta, "dibujoo.jpg");
        FileOutputStream fos = new FileOutputStream (archivo);*/


        /*Bitmap b = Bitmap.createBitmap(432, 593, Bitmap.Config.ARGB_8888);
        lienzoFondo.drawBitmap(b,432,593,pincel);*/

        FileOutputStream salida;
        salida = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/cojjonescobdios.png");
        b.compress(Bitmap.CompressFormat.PNG, 90, salida);

        b.recycle();
    }

    public void Cargar(){
        File carpeta=new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());
        File archivo = new File (carpeta, "dibujito.jpg");
        imagenFondo = Bitmap.createBitmap(432,593, Bitmap.Config.ARGB_8888);
        if (archivo.exists()){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable=true;
            imagenFondo=BitmapFactory.decodeFile(archivo.getAbsolutePath(),options);
        }
        lienzoFondo = new Canvas(imagenFondo);
    }

    /************* PATH ************************/
    public void path(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = x2 = x;
                y1 = y2 = y;
                rectaPoligonal.reset();
                rectaPoligonal.moveTo(x1,y1);
                break;
            case MotionEvent.ACTION_MOVE:
                rectaPoligonal.quadTo(x2,y2,(x+x2)/2,(y+y2)/2);
                x2=x;
                y2=y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                lienzoFondo.drawPath(rectaPoligonal, pincel);
                rectaPoligonal.reset();
                invalidate();
                break;
        }
    }

    /************* LINEA ************************/
    public void dibujarLinea(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = x;
                y1 = y;
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = x;
                y2 = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                x2 = x;
                y2 = y;
                lienzoFondo.drawLine(x1,y1,x2,y2, pincel);
                invalidate();
                break;
        }
    }

    /************* CIRCULO ************************/
    public void dibujarCirculo(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = x;
                y1 = y;
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = x;
                y2 = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                x2=x;
                y2 = y;
                lienzoFondo.drawCircle(x1, y1, radio, pincel);
                invalidate();
                break;
        }
    }

    /************* RECTANGULO ************************/
    public void dibujarRectangulo(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = x;
                y1 = y;
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = x;
                y2 = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                x2=x;
                y2 = y;
                lienzoFondo.drawRect(x1, y1, x2, y2, pincel);
                invalidate();
                break;
        }
    }
}
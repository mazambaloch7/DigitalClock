package com.fn2fersh.digitalclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Time mTime;
    Handler handler;
    Runnable r;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTime = new Time();
        r = new Runnable() {
            @Override
            public void run() {
                mTime.setToNow();
                drawView dv = new drawView(MainActivity.this,
                        mTime.hour,mTime.minute, mTime.second,mTime.weekDay, mTime.monthDay, getBatteryLevel());
                setContentView(dv);
                handler.postDelayed(r,1000);
            }
        };

        handler = new Handler();
        handler.postDelayed(r,100);
    }
    public float getBatteryLevel(){
        Intent betteryInent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = betteryInent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int scale = betteryInent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);

        if (level == -1 ||scale == -1 ){
            return  50.0f;
        }
        return ((float)level / (float) scale * 100.0f);
    }
    public class drawView extends View{

        Typeface tf;
        Paint mBackgroundPaint, mTextPaint,mTextPaintBack;

        int hours, minutes, second, weekday, date;
        float battery;



        public drawView(Context context, int hours, int minutes, int second, int weekday, int date, float battery) {
            super(context);
            tf = Typeface.createFromAsset(getAssets(),"fonts.TTF");
//            tf = Typeface.createFromAsset(getAssets(),"lato_regular.ttf");



            mBackgroundPaint  = new Paint();
//            mBackgroundPaint.setColor(getResources().getColor(R.color.background));
            mBackgroundPaint.setColor(ContextCompat.getColor(getApplicationContext(),R.color.background));

            mTextPaint  = new Paint();
            mTextPaint.setColor(ContextCompat.getColor(getApplicationContext(),R.color.text));
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size));
            mTextPaint.setTypeface(tf);




            mTextPaintBack  = new Paint();
            mTextPaintBack.setColor(ContextCompat.getColor(getApplicationContext(),R.color.text_back));
            mTextPaintBack.setAntiAlias(true);
            mTextPaintBack.setTextAlign(Paint.Align.CENTER);
            mTextPaintBack.setTextSize(getResources().getDimension(R.dimen.text_size));
            mTextPaintBack.setTypeface(tf);


            this.hours = hours;
            this.minutes = minutes;
            this.second = second;
            this.weekday = weekday;
            this.date = date;
            this.battery = battery;


        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            float width = canvas.getWidth();
            float height = canvas.getHeight();

            canvas.drawRect(0,0,width,height,mBackgroundPaint);

            float centerX = width/2f;
            float centery = height/2f;

            int cursur_hours = hours;
            String cursur_ampm = "AM";

            if (cursur_hours == 0){
                cursur_hours = 12;
            }


            if (cursur_hours > 12){
                cursur_hours = cursur_hours-12;
                cursur_ampm = "PM";
            }

            String text =  String.format("%02d:%02d:%02d", cursur_hours,minutes,second);
                String day_of_week="";

            if (weekday==1){
                day_of_week = "MON";
            } else if (weekday==2){
                day_of_week = "TUE";
            } else if (weekday==3){
                day_of_week = "WED";
            } else if (weekday==4){
                day_of_week = "THU";
            } else if (weekday==5){
                day_of_week = "FRI";
            } else if (weekday==6){
                day_of_week = "SAT";
            } else if (weekday==7){
                day_of_week = "SUN";
            }

            String text2 = String.format("Date: %s %d", day_of_week,date );
            String betteryLevel = "BETTERY: "+(int)battery+"%";
            canvas.drawText("00 00 00", centerX, centery, mTextPaintBack);

            mTextPaint.setColor(ContextCompat.getColor(getApplicationContext(),R.color.text));
            mTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size));
            canvas.drawText(text, centerX, centery, mTextPaint);

            mTextPaint.setColor(ContextCompat.getColor(getApplicationContext(),R.color.text));
            mTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size_small));
            canvas.drawText(betteryLevel +"  "+
                    text2,
                    centerX,
                    centery+
                            getResources().getDimension(R.dimen.text_size_small), mTextPaint);



        }
    }
}
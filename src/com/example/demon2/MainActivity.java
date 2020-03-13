package com.example.demon2;

import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Button b;
    private TextView t;
    public TextView t2;
    public TextView t3;
    public  static String s1,s2;
    private EditText editText;
    public String res="0";
    //public StringBuffer res=new StringBuffer("enemy");
    public int times=0,temp=0;
    public int ringValue=40;
    public boolean flag=false;
    public int sec=0;
    RadioGroup radioGroup;
    
    private ColorArcProgressBar bar;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		b=(Button)findViewById(R.id.send);
        t=(TextView)findViewById(R.id.textview);
        t2=(TextView)findViewById(R.id.textview2);
        t3=(TextView)findViewById(R.id.textview3);
        editText=(EditText)findViewById(R.id.edittext);
        bar = (ColorArcProgressBar) findViewById(R.id.bar1);
        radioGroup=(RadioGroup)findViewById(R.id.rg_call_voice_desc);
        Timer time=new Timer();
        time.schedule(new TimerTask(){
          	     public void run()
          	     {
          	    	 Message message=new Message();
                     message.what=0;
                     mHandler.sendMessage(message);
          	     }
        	}, 0, 1000);
        b.setOnClickListener(new View.OnClickListener(){  //设置点击按钮
            public void onClick(View v){
            	flag=true; 
            	sec=0;
            	int count = radioGroup.getChildCount();
            	for(int i = 0 ;i < count;i++){
            	    RadioButton rb = (RadioButton)radioGroup.getChildAt(i);
            	    if(rb.isChecked()){
            	        s2=rb.getText().toString();
            	        break;
            	    }
            	}

            }
        });
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor type_accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // 传感器数据变化
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                //tv.setText("x="+x+",y="+y+",z="+z);
 
                if (Math.abs(x)+Math.abs(y)+Math.abs(z)>=ringValue&&flag==true){
                    times++;
                    temp++;
                    t2.setText("your score:"+Integer.toString(times));
//                    bar.setCurrentValues(times);
                    
                }
                
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // 传感器精度的变化
            }
        },type_accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }
	public void connect()
	{
		 new Thread(new Runnable() {   //新的线程来执行客户端
             @Override
             public void run() 
             {   
                 s1=Integer.toString(temp);
                 //s2=editText.getText().toString();
            	 Client c=  new Client(s2+s1); //新建客户端对象
                 c.send();
                 res=c.Return();//调用send方法
                 temp=0;
             }
         }).start();
	}
	private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            if(flag==true&&sec<=15)
                            {
                            	sec++;
                                connect();
                                t3.setText("enemy's score:"+res);
                                float of=(float) (times*1.0/(Integer.parseInt(res)+times)*100);
                                bar.setCurrentValues(of);
                            }
                            else if(sec>15)
                            {
                            	
                            	/*if(times>Integer.parseInt(res))
                            		build.setMessage("you win!");
                            	else
                            		build.setMessage("you lose!");
                            	build.setCancelable(false);
                            	build.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) 
                                    {
                                        dialog.dismiss();
                                    }
                                });
                                //设置反面按钮
                                build.setNegativeButton("不是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) 
                                    {
                                        //Toast.makeText(context, "你点击了不是", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog=build.create();
                                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);

                               // dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
                                dialog.show();*/
                            	if(times>Integer.parseInt(res))
                            	{
                            		t2.setText("You win!");
                            	}
                            	else
                            	{
                            		t2.setText("You lose!");
                            	}
                            	flag=false;
                            	times=0;
                            	sec=0;
                            	temp=0;
                            	
                            	t3.setText("");
                            	bar.setCurrentValues(0);
                            	editText.setText("请输入用户名");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }
    };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

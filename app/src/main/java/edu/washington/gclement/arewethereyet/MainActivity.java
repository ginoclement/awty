package edu.washington.gclement.arewethereyet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.EventListener;


public class MainActivity extends ActionBarActivity {
    private EditText input_message;
    private EditText input_interval;
    private EditText input_phone;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Button btn_start = (Button) findViewById(R.id.btn_start);
                btn_start.setEnabled(checkValues());
            }
        };
        Button btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn_start = (Button) v;
                String text = btn_start.getText().toString();
                Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
                alarmIntent.putExtra("message", input_phone.getText() + ": " + input_message.getText());
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1,
                        alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                if(text.equals("Start!")){
                    //Start the service
                    btn_start.setText("Stop!");
                    start();
                } else {
                    //Stop the service
                    btn_start.setText("Start!");
                    cancel();
                }
            }
        });
        input_message = (EditText) findViewById(R.id.input_message);
        input_message.addTextChangedListener(tw);
        input_phone = (EditText) findViewById(R.id.input_phone);
        input_phone.addTextChangedListener(tw);
        input_interval = (EditText) findViewById(R.id.input_interval);
        input_interval.addTextChangedListener(tw);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkValues(){
        Log.i("awty", "Checking values!");
        int interval = Integer.parseInt(input_interval.getText().toString());
        String phone = input_phone.getText().toString();
        String message = input_message.getText().toString();

//        This is the same idea as the return statement below (negated though). Commented this out
//              because it's not half as pretty.

//        if(interval <= 0){
//            return false;
//        } else if(phone.length() != 10){
//            //(123)456-7890
//            // 10 digits not including parentheses
//            return false;
//        } else if(message.equals("")) {
//            return false;
//        } else {
//            return true;
//        }
        return interval > 0 && phone.length() == 10 && !message.equals("");
    }

    public void start() {
        int interval = Integer.parseInt(input_interval.getText().toString()) * 1000;

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        String message = input_phone.getText().toString() + ": " + input_message.getText().toString();
        Log.i("awty", message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        pendingIntent.cancel();
        Toast.makeText(this, "Texting stopped", Toast.LENGTH_SHORT).show();
    }
}

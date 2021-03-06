package com.led.led;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;


public class ledControl extends AppCompatActivity {

    Button btnOnL1, btnOffL1,btnOnL2, btnOffL2,btnOnL3, btnOffL3, btnDis;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //view of the ledControl
        setContentView(R.layout.activity_led_control);

        //call the widgtes
        btnOnL1 = (Button)findViewById(R.id.button2);
        btnOffL1 = (Button)findViewById(R.id.button3);
        btnOnL2 = (Button)findViewById(R.id.button21);
        btnOffL2 = (Button)findViewById(R.id.button31);
        btnOnL3 = (Button)findViewById(R.id.button22);
        btnOffL3 = (Button)findViewById(R.id.button32);
        btnDis = (Button)findViewById(R.id.button4);

        new ConnectBT().execute(); //Call the class to connect

        //commands to be sent to bluetooth
        btnOnL1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                turnOnLed("B");      //method to turn on
                btnOnL1.setVisibility(View.GONE);
                btnOffL1.setVisibility(View.VISIBLE);
            }
        });

        btnOffL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                turnOnLed("A");   //method to turn off

                btnOffL1.setVisibility(View.GONE);
                btnOnL1.setVisibility(View.VISIBLE);
            }
        });

        btnOnL2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                turnOnLed("D");      //method to turn on
                btnOnL2.setVisibility(View.GONE);
                btnOffL2.setVisibility(View.VISIBLE);
            }
        });

        btnOffL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                turnOnLed("C");   //method to turn off
                btnOffL2.setVisibility(View.GONE);
                btnOnL2.setVisibility(View.VISIBLE);
            }
        });

        btnOnL3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                turnOnLed("F");      //method to turn on
                btnOnL3.setVisibility(View.GONE);
                btnOffL3.setVisibility(View.VISIBLE);
            }
        });

        btnOffL3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                turnOnLed("E");   //method to turn off
                btnOffL3.setVisibility(View.GONE);
                btnOnL3.setVisibility(View.VISIBLE);
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });

    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void turnOnLed(String key)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(key.getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_led_control, menu);
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

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                 myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                 BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                 btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                 BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}

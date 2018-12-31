package com.chooloo.www.callmanager.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chooloo.www.callmanager.R;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends ToolbarActivity {

    @BindView(R.id.numberInput) EditText numberInput;
    @BindView(R.id.callBtn) Button callBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        callBtn.setOnClickListener(v -> {
            if (numberInput.getText() == null) {
                Toast.makeText(getApplicationContext(), "Calling without a number huh? U little shit", Toast.LENGTH_LONG).show();
            } else {
                try {
                // Set the data
                String uri = "tel:" + numberInput.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));

                startActivity(callIntent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong... Fuck.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        // Ask for permissions
        // READ_PHONE_STATE
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        // CALL_PHONE
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }

        //Prompt the user with a dialog to select this app to be the default phone app
        String packageName = getApplicationContext().getPackageName();
        //noinspection ConstantConditions
        if (!getSystemService(TelecomManager.class).getDefaultDialerPackage().equals(packageName)) {
            startActivity(new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
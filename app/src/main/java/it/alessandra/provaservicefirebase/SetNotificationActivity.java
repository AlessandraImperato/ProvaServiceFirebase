package it.alessandra.provaservicefirebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;

public class SetNotificationActivity extends AppCompatActivity {

    private CheckBox checkBox;
    private boolean isCheck;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);
        checkBox = findViewById(R.id.checknotifiche);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.getBoolean("CHECKBOX",true);

        isCheck = checkBox.isChecked();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("CHECKBOX", isCheck);

        if (isCheck){ //se la checkbox delle notifiche è settata => notifiche attive (startService)
            Intent intent = new Intent(this, PushNotification.class);
            startService(intent);
        }else{ // se non è settata => disattiva notifiche (stopService)
            stopService(new Intent(this,PushNotification.class));
        }
    }
}

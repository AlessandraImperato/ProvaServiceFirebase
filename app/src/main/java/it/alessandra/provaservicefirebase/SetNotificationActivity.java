package it.alessandra.provaservicefirebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class SetNotificationActivity extends AppCompatActivity {

    private CheckBox checkBox;
    private boolean isCheck;
    private SharedPreferences preferences;
    private Button salva;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);

        Intent i = getIntent();

        checkBox = (CheckBox) findViewById(R.id.checknotifiche);
        salva = (Button) findViewById(R.id.bsave);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isCheck = preferences.getBoolean("CHECKBOX",true);
        user = preferences.getString("USER","");

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isCheck = checkBox.isChecked();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("CHECKBOX", isCheck);

                if (isCheck){ //se la checkbox delle notifiche è settata => notifiche attive (startService)
                    Log.i("result","check: " + isCheck);
                    Intent intent = new Intent(getApplicationContext(), PushNotification.class);
                    startService(intent);
                }else{ // se non è settata => disattiva notifiche (stopService)
                    Log.i("result","NOcheck: " + isCheck);
                    stopService(new Intent(getApplicationContext(),PushNotification.class));
                }
                Toast.makeText(getApplicationContext(),"Salvato",Toast.LENGTH_LONG).show();
            }
        });


    }
}

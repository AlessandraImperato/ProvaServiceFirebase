package it.alessandra.provaservicefirebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText editUser;
    private Button bRegistra;
    private static FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUser = (EditText)findViewById(R.id.edituser);
        bRegistra = (Button)findViewById(R.id.breg);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReferenceFromUrl("https://provaservicefirebase.firebaseio.com/Users");

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        bRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUser.getText().toString();
                if(username.equals("")){
                    Toast.makeText(getApplicationContext(),"Inserisci l'username",Toast.LENGTH_LONG).show();
                }else{
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("USER",username);
                    editor.commit();
                    databaseReference.child(username).child("id").setValue(username);
                    Intent i = new Intent(getApplicationContext(),SetNotificationActivity.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(),"Registrato",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

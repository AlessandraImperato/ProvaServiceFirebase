package it.alessandra.provaservicefirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText editUser;
    private Button bRegistra;
    private static FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUser = findViewById(R.id.edituser);
        bRegistra = findViewById(R.id.breg);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReferenceFromUrl("https://provaservicefirebase.firebaseio.com/Users");

        bRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUser.getText().toString();
                if(username.equals("")){
                    Toast.makeText(getApplicationContext(),"Inserisci l'username",Toast.LENGTH_LONG).show();
                }else{
                    databaseReference.child(username).setValue(username);
                    Toast.makeText(getApplicationContext(),"Registrato",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public int randomPosition(){
        Random random = new Random();
        int position = random.nextInt(100);
        return position;
    }
}

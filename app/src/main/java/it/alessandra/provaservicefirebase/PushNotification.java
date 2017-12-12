package it.alessandra.provaservicefirebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

/**
 * Created by utente7.academy on 12/12/2017.
 */

public class PushNotification extends Service{

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ChildEventListener handler;
    private DatabaseReference usersReference;// = database.getReferenceFromUrl("https://provaservicefirebase.firebaseio.com/Users");


    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = preferences.getString("USER","");
        usersReference = database.getReferenceFromUrl("https://provaservicefirebase.firebaseio.com/Users/"+username);
        handler = new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    activePushValidation(dataSnapshot.getKey()); //passa la chiave che ha ricevuto quella notifica
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersReference.addChildEventListener(handler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        usersReference.removeEventListener(handler); // elimino il listener!
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void activePushValidation(String commListener) { //crea un nuovo intent sulla setnotification activity
        Intent intent = new Intent(this, SetNotificationActivity.class); //su questa intent possiamo fare i put extra ad es per passare dei parametri
        sendNotification(intent, "Notifica", commListener); //questo intent determina dove ci porta la nostra notifica

    }

    public void sendNotification(Intent intent, String title, String body) { //codice che funziona sempre dalla 7.0 a scendere
        /*1*/Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher_round); //potremmo non crearlo, ci serve solo per il large icon
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

       /*2*/ PendingIntent activity = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);//PendingIntent indica dove porta la nostra notifica push, cioè dove andiamo quando clicchiamo sulla notifica (prende l'intent che abbiamo definito prima. è necessaria.

        Uri dsUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this); //crea l'oggetto notifica
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setAutoCancel(true);
        builder.setSound(dsUri); //è il default
        builder.setSmallIcon(R.mipmap.ic_launcher); //prende un intero se togliamo la small potrebbe non funzionare la push
        builder.setLargeIcon(bitmap); //prende un oggetto bitmap (decodificata sopra 107) se togliamo la large la notifica push funziona
        builder.setShowWhen(true);
        builder.setContentIntent(activity); //determina quale activity viene startata quando clicchiamo sulla notifica push

        /*3*/NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // creo l'oggetto che rende visibile la notifica push
        manager.notify(0, builder.build());
    }

}

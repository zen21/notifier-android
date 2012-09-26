package com.example.powiadamiacz;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	
	 @Override
	    public void onReceive(Context context, Intent intent) {
	      try {
	          
	    	Bundle bundle = intent.getExtras();
	        //String id = bundle.getString("id");
	        CharSequence tytul = bundle.getString("tytul");
	     	        
	        powiadom(tytul,"","",false, context, intent);
	        
	       } 
	      	  	      	      
	      catch (Exception e) {
	        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
	        e.printStackTrace();
	        System.out.println("Error!");
	    
	       }
	    }	
	 
	 
	    //@SuppressWarnings("deprecation")
		@SuppressWarnings("deprecation")
		public void powiadom(CharSequence powiadomienieTytul, CharSequence powiadomienieTekst, CharSequence powiadomienieWiadomosc, boolean Cancel, Context context, Intent intent) {
	    	//funkcja wyœwietla powiadomienie
	        
	    	
	        NotificationManager managerPowiadomien = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	        
	        int ikona = R.drawable.ic_launcher;
	      
	        long powiadomienieKiedy = System.currentTimeMillis();

	        Notification powiadomienie = new Notification(ikona, powiadomienieWiadomosc, powiadomienieKiedy);
	        
	        powiadomienie.flags = Notification.FLAG_ONGOING_EVENT;
	        
	        //Context context = getApplicationContext();
	  
	        //Intent powiadomienieCel = new Intent(this, MainActivity.class);
	       
	       // PendingIntent contentIntent = PendingIntent.getActivity(this, 0, powiadomienieCel, 0);
	        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
	        
	        powiadomienie.setLatestEventInfo(context, powiadomienieTytul, powiadomienieTekst, contentIntent);
	                      
	        final int HELLO_ID = 1;

	        
	        
	        if(Cancel)
	        	managerPowiadomien.cancelAll();
	        if(!Cancel) {
	        	managerPowiadomien.notify(HELLO_ID, powiadomienie);
	    	

	        }

	    }
	
	
	
	
}
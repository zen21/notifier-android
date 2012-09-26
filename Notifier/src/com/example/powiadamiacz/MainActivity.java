package com.example.powiadamiacz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;




public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);      
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
  
    
    public void startPowiadamiacz(View view) {
              
    	 try {
    		 
    	 start();
    	 
    	 Toast myToast;
    	 myToast = Toast.makeText(getApplicationContext(), 
                  "Powiadomienia zosta³y zaplanowane", 
                  Toast.LENGTH_SHORT);
    	 myToast.show();

    	 
    	 }
    	 
    	 catch (Exception e)
    	 {
    		 System.out.println("ERROR!");
    		 System.out.println(e);
    		 
    		 Toast mToast;
        	 mToast = Toast.makeText(getApplicationContext(), 
                      "ERROR!", 
                      Toast.LENGTH_SHORT);
        	 mToast.show();	   		 
    		 
    	 }
    	  
    	
    }
       
    
    public void exitPowiadamiacz(View view) {
    	
    	powiadom("A","A","A",true); //likwidujemy powiadomienie
    	
    	
    	try {
    	
    	String filename = "data.csv"; //nazwa pliku z danymi
    	
    	int size = iloscLinii(filename); //sprawdzamy wielkoœæ pliku

        String Tablica[][] = loadData(size, filename); //³adujemy dane z pliku do tablicy
    	
    	
    	for(int i = 0; i < size; i++) {
         	//odwo³ujemy alarmy
        	zaplanujPowiadomienie(Integer.parseInt(Tablica[i][0]), Integer.parseInt(Tablica[i][1]), Integer.parseInt(Tablica[i][2]), Tablica[i][3], true);
         }
    	
    	 }
    	 catch (Exception e) {
    		 System.out.println("ERROR while closing");
    	 }
    	
    	MainActivity.this.finish(); //zamykamy aplikacjê
    	
    }
    
    public void start() {    	
    	//Funkcja planuje wszystkie powiadomienia zapisane w CSV
    	    	
    	powiadom("Started","","", false);
    	
    	String filename = "data.csv"; //nazwa pliku z danymi
    	
    	int size = iloscLinii(filename); //sprawdzamy wielkoœæ pliku

        String Tablica[][] = loadData(size, filename); //³adujemy dane z pliku do tablicy
    	
    	
        for(int i = 0; i < size; i++) {
         	
        	zaplanujPowiadomienie(Integer.parseInt(Tablica[i][0]), Integer.parseInt(Tablica[i][1]), Integer.parseInt(Tablica[i][2]), Tablica[i][3], false);
         }

    	
    }
	
    public void zaplanujPowiadomienie(int id, int godzina, int minuta, final CharSequence tytul, boolean cancel) {
    	//Funkcja planuje powiadomienie
    	
    	AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
    	Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
    	intent.putExtra("id", id);
    	intent.putExtra("tytul", tytul);
    	PendingIntent sender = PendingIntent.getBroadcast(this, id+1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    	
    	if(!cancel) {
        	
    	long delay = kiedyUruchomic(godzina,minuta);
	
    	alarm.set(AlarmManager.RTC_WAKEUP,  (System.currentTimeMillis()+delay), sender);
    	
    	}

    	else if(cancel) 
    		alarm.cancel(sender);
		    

		}
    
    @SuppressWarnings("deprecation")
	public void powiadom(CharSequence powiadomienieTytul, CharSequence powiadomienieTekst, CharSequence powiadomienieWiadomosc, boolean Cancel) {
    	//funkcja wyœwietla powiadomienie
        
    	String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager managerPowiadomien = (NotificationManager) getSystemService(ns);
        
        int ikona = R.drawable.ic_launcher;
      
        long powiadomienieKiedy = System.currentTimeMillis();

		Notification powiadomienie = new Notification(ikona, powiadomienieWiadomosc, powiadomienieKiedy);
        
        powiadomienie.flags = Notification.FLAG_ONGOING_EVENT;
        
        Context context = getApplicationContext();
  
        Intent powiadomienieCel = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, powiadomienieCel, 0);

        powiadomienie.setLatestEventInfo(context, powiadomienieTytul, powiadomienieTekst, contentIntent);
                      
        final int HELLO_ID = 1;

        
        
        if(Cancel)
        	managerPowiadomien.cancelAll();
        if(!Cancel)
        	managerPowiadomien.notify(HELLO_ID, powiadomienie);
    	

    }
            
    public long kiedyUruchomic(int godzinaWyznaczona, int minutaWyznaczona) { 	
    	 
    	//Funkcja zwraca za ile w milisekundach uruchomiæ powiadomienie
    	
		   
		DateFormat dateFormat1 = new SimpleDateFormat("HH");
		DateFormat dateFormat2 = new SimpleDateFormat("mm");
		DateFormat dateFormat3 = new SimpleDateFormat("ss");
		//parametry dla formatowania godziny/sekund/minut
 
        Date date = new Date();
  
		  
		int godzinaObecna = Integer.parseInt(dateFormat1.format(date));
		//aktualna godzina jako liczba
	

		int doWyznaczonejgodziny = 0;
		
		if (godzinaObecna <= godzinaWyznaczona) 
			doWyznaczonejgodziny = (godzinaWyznaczona - godzinaObecna);
		else
			doWyznaczonejgodziny = (24 - godzinaObecna + godzinaWyznaczona );
		
		//jeœli godzina ju¿ minê³a, liczymy za ile bêdzie nastêpnego dnia 
	
		  
		long iloscMShour = doWyznaczonejgodziny*60*60*1000; //iloœæ milisekund do wyznaczonej godziny
		
		
		
		int sekundaObecna = Integer.parseInt(dateFormat3.format(date));
		
		  
		int minutaObecna = Integer.parseInt(dateFormat2.format(date));

		int doWyznaczonejminuty = (minutaWyznaczona - minutaObecna);		
		  
		long iloscMSminute = doWyznaczonejminuty*60*1000;
		
		long iloscMStotal = 0;
		
		
		if (iloscMSminute < 0) 	
			iloscMStotal = (iloscMShour - iloscMSminute);
		else
			iloscMStotal = (iloscMSminute + iloscMShour);
		
		
		if((iloscMStotal - (sekundaObecna*1000)) > 0)
			return (iloscMStotal - (sekundaObecna*1000));
		else
			return 0;
    }

    public String[][] loadData(int size, String filename) {

    	//Funkcja ³aduje dane z pliku csv (data.csv) do tablicy, dane s¹ przekazywane przez return
    	
    	File sdcard = Environment.getExternalStorageDirectory();	//Pobranie œcie¿ki
    	File file = new File(sdcard,filename); //plik z danymi
    	
    	String[][] tabl = new String[(size+3)][4]; //stworzenie tablicy na dane (na wszelki wypadek trochê wiêksza ni¿ potrzebna)
    
    	try {
    
    	BufferedReader bufRdr  = new BufferedReader(new FileReader(file));
    
    	//zmienne potrzebne do odczytu: line - pojedyncza linia z pliku, row i col - numery [][] w tablicy
    	String line = null;
    	int row = 0;
    	int col = 0;
    
    	//czytamy ka¿d¹ liniê z pliku, dopóki siê nie skoñcz¹
    	while((line = bufRdr.readLine()) != null)
    	{
    		col = 0;
    		StringTokenizer st = new StringTokenizer(line,","); //info sk¹d czytamy i jak wygl¹da delimiter
    	
    		while (st.hasMoreTokens())
    		{
    			tabl[row][col] = st.nextToken(); //zapisujemy w tablicy
    			col++;
    		}
    		row++;
    	}
    	 
    	//zamkniêcie bufora
    	bufRdr.close();
    	}
    	catch(IOException e) {
    		System.out.println("Error!"); 
        }
    	
    	return tabl; //zwracamy odczytan¹ tablicê
    	
    }
      
    public int iloscLinii(String fileName) {
    	
    	File sdcard = Environment.getExternalStorageDirectory();	//Pobranie œcie¿ki
    	File file = new File(sdcard, fileName); //plik z danymi
    	
    	int linenumber = 0;    	          	  	
		
    	try {
		FileReader fr = new FileReader(file);
	    LineNumberReader lnr = new LineNumberReader(fr);

		

	       while (lnr.readLine() != null)	{
	       	linenumber++;
	       }
	  
	   lnr.close();
    	}
    	catch(Exception e) {
    		
    	}
	   
	   return linenumber;
      

    
    
    }
    
    

}







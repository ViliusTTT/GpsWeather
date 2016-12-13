package com.easygo.vilius.pasiklydauapp;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Pagrindinio activicio klase, kurioje atvaizduojamas pagrindinis meniu
 */
public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    TextToSpeech tts;                   //Teksta apdorojantis objektas
    ImageButton startRecognizer;        //Mygtukas
    Spinner spinnerResult;              //Rezultati spinneris
    private static final int RQS_RECOGNITION =1;

    /**
     * onCreate metodas - nustato mygtukus ir layouta
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        startRecognizer=(ImageButton)findViewById(R.id.startrecognizer);
        startRecognizer.setEnabled(false);
        spinnerResult=(Spinner)findViewById(R.id.result);
        startRecognizer.setOnClickListener(startRecognizerOnClickListener);
        tts= new TextToSpeech(this,this);
    }

    /**
     * onClick metodas reaguoja i mygtuku play, about paspaudimus
     * @param v - View objektas
     */
    public void onClick(View v)
    {
        if(v.getId()==R.id.play_btn)
        {
            Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.about_btn)
        {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
    }
    private Button.OnClickListener startRecognizerOnClickListener = new Button.OnClickListener(){
        /**
         * onClick metodas apdorojantis balso komandas
         * @param arg0
         */
        @Override
        public void onClick(View arg0)
        {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra( RecognizerIntent.EXTRA_PROMPT,"Speech to Recognize");
            startActivityForResult(intent,RQS_RECOGNITION);
        }

    };

    /**
     * Apdoroja gautus duomenis is balso kontrolės
     * @param requestCode - uzklausos kodas
     * @param resultCode - rezultato kodas
     * @param data - duomenys
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if((requestCode==RQS_RECOGNITION )&&(resultCode== RESULT_OK)){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,result);
            if(result.contains("about"))
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            else if(result.contains("start"))
                startActivity(new Intent(MainActivity.this,MapsActivity.class));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerResult.setAdapter(adapter);
            spinnerResult.setOnItemSelectedListener(spinnerResultOnItemSelectedListener);
        }
    }

    /**
     * Metodas apdorojantis spinerio elementu parinkima
     */
    private Spinner.OnItemSelectedListener spinnerResultOnItemSelectedListener = new Spinner.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
            String selectedResult = parent.getItemAtPosition(position).toString();
            Toast.makeText(MainActivity.this,selectedResult,Toast.LENGTH_SHORT).show();
            tts.speak(selectedResult,TextToSpeech.QUEUE_ADD,null);

        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0){

        }
    };

    /**
     * Metodas paleidziantis balso ivesti
     * @param arg0
     */
    @Override
    public void onInit(int arg0){
        startRecognizer.setEnabled(true);
    }
}


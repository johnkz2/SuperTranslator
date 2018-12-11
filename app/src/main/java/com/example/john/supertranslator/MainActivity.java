package com.example.john.supertranslator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.os.AsyncTask;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import java.util.Arrays;
import java.util.HashMap;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;





public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SuperTranslator";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.translate);
        final EditText edittext = (EditText) findViewById(R.id.editText);
        final Spinner languages = findViewById(R.id.languages);
        final HashMap<String, String> languagesMap = new HashMap<>();
        languagesMap.put("English","en"); languagesMap.put("Spanish","es"); languagesMap.put("Mandarin","zh-Hans"); languagesMap.put("Italian","it"); languagesMap.put("French", "fr"); languagesMap.put("Russian","ru"); languagesMap.put("Arabic","ar");
        String[] fullLanguages = new String[]{"English", "Mandarin", "Spanish", "French", "Italian", "Russian", "Arabic"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, fullLanguages);
        languages.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String toTranslate = edittext.getText().toString();
                final String targetLanguage = languages.getSelectedItem().toString();
                Log.d(TAG, "Translate text");
                new TranslateHelper().execute(toTranslate, languagesMap.get(targetLanguage));
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });
    }
    private class TranslateHelper extends AsyncTask<String, Void, String> {
        //API KEY FOR GOOGLE TRANSLATE API
        private final String API_KEY = "apikeyhere";
        @Override
        //translate api runs in an AsyncTask because UI thread does not have network access
        protected String doInBackground(String... params) {

            String text = params[0];
            String targetLanguage = params[1];
            try {
                NetHttpTransport netHttpTransport = new NetHttpTransport();
                JacksonFactory jacksonFactory = new JacksonFactory();
                Translate translate = new Translate.Builder(netHttpTransport, jacksonFactory, null).build();
                Translate.Translations.List listToTranslate = translate.new Translations().list(Arrays.asList(text), targetLanguage).setKey(API_KEY);
                TranslationsListResponse response = listToTranslate.execute();
                return response.getTranslations().get(0).getTranslatedText();
            } catch (Exception e) {
                return "";
            }

        }

        @Override
        protected void onPostExecute(String translatedText) {
            TextView translatedbox = (TextView) findViewById(R.id.textView);
            translatedbox.setText(translatedText);
        }
    }

}

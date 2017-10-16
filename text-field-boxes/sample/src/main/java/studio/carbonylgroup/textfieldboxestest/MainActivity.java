package studio.carbonylgroup.textfieldboxestest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final SharedPreferences sharedPreferences = this.getSharedPreferences("theme", Context.MODE_PRIVATE);
        final boolean dark = sharedPreferences.getBoolean("dark", false);
        setTheme(dark ? R.style.AppThemeDark : R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button darkButton = findViewById(R.id.dark_button);
        darkButton.setText(dark ? "LIGHT SIDE" : "DARK SIDE");
        darkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sharedPreferences.edit().putBoolean("dark", !dark).apply();
//                Intent i = getBaseContext().getPackageManager()
//                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
            }
        });

        setTypeface(R.id.radioButton1);
        setTypeface(R.id.radioButton2);
        setTypeface(R.id.radioButton3);
        setTypeface(R.id.radioButton4);
        setTypeface(R.id.radioButton5);
        setTypeface(R.id.text_field_boxes1);
        setTypeface(R.id.text_field_boxes2);

    }
    Typeface face;

    private void setTypeface (@IdRes int resid) {
        TextView textView = findViewById(resid);

        if (face == null) {
            face=Typeface.createFromAsset(getAssets(),"fonts/roboto_thin.ttf");
        }
        textView.setTypeface(face);
    }
}

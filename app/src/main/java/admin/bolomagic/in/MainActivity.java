package admin.bolomagic.in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import admin.bolomagic.in.CustomDialouge.AddNewCard;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(v -> {
            AddNewCard addNewCard = new AddNewCard(MainActivity.this);
            addNewCard.setCanceledOnTouchOutside(false);
            addNewCard.show();
        });
    }
}
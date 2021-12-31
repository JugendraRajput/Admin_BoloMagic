package admin.bolomagic.in.CustomDialouge;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import admin.bolomagic.in.R;

public class AddNewGame extends Dialog {

    TextInputEditText gameNameEditText,gameDeveloperEditText, iconURLEditText;
    Button submitButton, closeButton, cancelButton;

    Context context;

    public AddNewGame(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.customdialog_add_new_game);

        gameNameEditText = findViewById(R.id.gameNameEditText);
        gameDeveloperEditText = findViewById(R.id.gameDeveloperEditText);
        iconURLEditText = findViewById(R.id.iconURLEditText);

        submitButton = findViewById(R.id.submitButton);
        closeButton = findViewById(R.id.closeButton);
        cancelButton = findViewById(R.id.cancelButton);

        closeButton.setOnClickListener(v -> {
            dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            cancel();
        });

        submitButton.setOnClickListener(v -> {
            String imageURL = Objects.requireNonNull(iconURLEditText.getText()).toString();
            String gameName = Objects.requireNonNull(gameNameEditText.getText()).toString();
            String gameDeveloper = Objects.requireNonNull(gameDeveloperEditText.getText()).toString();

            int status = 1;
            if (imageURL.equals("")){
                iconURLEditText.setError("Enter Icon URL");
                iconURLEditText.requestFocus();
                status = 0;
            }
            if (gameName.equals("")){
                gameNameEditText.setError("Enter Valid Game Name");
                gameNameEditText.requestFocus();
                status = 0;
            }
            if (gameDeveloperEditText.equals("")){
                gameDeveloperEditText.setError("Enter Valid Prize");
                gameDeveloperEditText.requestFocus();
                status = 0;
            }
            if (status == 1){
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("Icon URL", imageURL);
                hashMap.put("Name", gameName);
                hashMap.put("Developer", gameDeveloper);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyMMddHHmmss");
                String id = simpleDateFormat.format(new Date());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SPL/Gift Cards/Games");
                databaseReference.child(id).updateChildren(hashMap).addOnSuccessListener(unused -> {
                    gameNameEditText.setText("");
                    gameDeveloperEditText.setText("");
                    iconURLEditText.setText("");
                    Toast.makeText(context, "New Game Added", Toast.LENGTH_SHORT).show();
                    dismiss();
                });
            }
        });
    }
}

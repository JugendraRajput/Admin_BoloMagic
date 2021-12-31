package admin.bolomagic.in.CustomDialouge;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import admin.bolomagic.in.R;

public class AddNewCard extends Dialog {

    Spinner gameListSpinner;
    ArrayList<String> gameListArray = new ArrayList<>();

    TextInputEditText imageURLEditText,quantityEditText, prizeEditText, offerEditText, unitTypeEditText;
    Button submitButton, closeButton, cancelButton;

    boolean isReady = false;

    String selectedGame = "Default";

    Context context;

    TextView preTextView1, preTextView2;
    ImageView preImageView;
    Button preButton;

    String quantity = "";
    String prize = "";
    String offer = "";
    String unitType = "";

    public AddNewCard(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.customdialog_add_new_card);
        gameListSpinner = findViewById(R.id.gameListSpinner);

        imageURLEditText = findViewById(R.id.imageURLEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        prizeEditText = findViewById(R.id.prizeEditText);
        offerEditText = findViewById(R.id.offerEditText);
        unitTypeEditText = findViewById(R.id.unitTypeEditText);

        submitButton = findViewById(R.id.submitButton);
        closeButton = findViewById(R.id.closeButton);
        cancelButton = findViewById(R.id.cancelButton);
        isReady = true;

        gameListSpinner = findViewById(R.id.gameListSpinner);

        preButton = findViewById(R.id.preButton);
        preImageView = findViewById(R.id.preImageView);
        preTextView1 = findViewById(R.id.preTextView1);
        preTextView2 = findViewById(R.id.preTextView2);
        startTextChangeListner();

        gameListArray.add("Free Fire");

        DatabaseReference databaseReferenceGames = FirebaseDatabase.getInstance().getReference("SPL/Gift Cards/Games");
        databaseReferenceGames.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> iterable = snapshot.getChildren();
                gameListArray.clear();
                for (DataSnapshot next : iterable){
                    gameListArray.add(next.child("Name").getValue().toString());
                }
                gameListArray.add("Add New");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        ArrayAdapter spinnerAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, gameListArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameListSpinner.setAdapter(spinnerAdapter);

        gameListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (gameListArray.get(position).equals("Add New")){
                    if (position > 0){
                        selectedGame = gameListArray.get(position-1);
                        gameListSpinner.setSelection(position-1);
                    }
                    AddNewGame addNewGame = new AddNewGame(context);
                    addNewGame.setCanceledOnTouchOutside(false);
                    addNewGame.show();
                    return;
                }
                selectedGame = gameListArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        closeButton.setOnClickListener(v -> {
            dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            cancel();
        });

        submitButton.setOnClickListener(v -> {
            if (isReady){
                isReady = false;
                quantity = Objects.requireNonNull(quantityEditText.getText()).toString();
                prize = Objects.requireNonNull(prizeEditText.getText()).toString();
                offer = Objects.requireNonNull(offerEditText.getText()).toString();
                unitType = Objects.requireNonNull(unitTypeEditText.getText()).toString();
                String imageURL = Objects.requireNonNull(imageURLEditText.getText()).toString();
                int status = 1;
                if (imageURL.equals("")){
                    imageURLEditText.setError("Enter Icon URL");
                    imageURLEditText.requestFocus();
                    status = 0;
                }
                if (quantity.equals("") || quantity.equals("0")){
                    quantityEditText.setError("Enter Valid Quantity");
                    quantityEditText.requestFocus();
                    status = 0;
                }
                if (prize.equals("") || prize.equals("0")){
                    prizeEditText.setError("Enter Valid Prize");
                    prizeEditText.requestFocus();
                    status = 0;
                }
                if (offer.equals("") || offer.equals("0")){
                    offerEditText.setError("Enter Valid Quantity");
                    offerEditText.requestFocus();
                    status = 0;
                }
                if (unitType.equals("") || unitType.equals("0")){
                    unitTypeEditText.setError("Enter Valid Prize");
                    unitTypeEditText.requestFocus();
                    status = 0;
                }
                if (status == 1){
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Icon URL", imageURL);
                    hashMap.put("Quantity", quantity);
                    hashMap.put("Prize", prize);
                    hashMap.put("Offer Percent", offer);
                    hashMap.put("Unit Type", unitType);
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyMMddHHmmss");
                    String id = simpleDateFormat.format(new Date());
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SPL/Gift Cards/Game Cards/"+selectedGame+"/Cards");
                    databaseReference.child(id).updateChildren(hashMap).addOnSuccessListener(unused -> {
                        isReady = true;
                        quantityEditText.setText("");
                        prizeEditText.setText("");
                        offerEditText.setText("");
                        unitTypeEditText.setText("");
                        Toast.makeText(context, "New Card Added", Toast.LENGTH_SHORT).show();
                        dismiss();
                    });
                }else{
                    isReady = true;
                }
            }else {
                Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTextChangeListner(){
        imageURLEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                UpdatePreview();
            }
        });

        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                UpdatePreview();
            }
        });

        prizeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                UpdatePreview();
            }
        });

        offerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                UpdatePreview();
            }
        });

        unitTypeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                UpdatePreview();
            }
        });
    }

    private void UpdatePreview(){
        String imageURL = Objects.requireNonNull(imageURLEditText.getText()).toString();
        String quantity = Objects.requireNonNull(quantityEditText.getText()).toString();
        String prize = Objects.requireNonNull(prizeEditText.getText()).toString();
        String offer = Objects.requireNonNull(offerEditText.getText()).toString();
        String unitType = Objects.requireNonNull(unitTypeEditText.getText()).toString();
        if (!imageURL.equals("")){
            Picasso.get().load(imageURL).into(preImageView);
        }else {
            Picasso.get().load(R.drawable.ic_launcher_foreground).into(preImageView);
        }

        if (!quantity.equals("") && !offer.equals("")){
            int bonus = (Integer.valueOf(quantity)*Integer.valueOf(offer))/100;
            preTextView2.setText(quantity+" + Bonus "+bonus);
        }else {
            preTextView2.setText(R.string.loading);
        }
        if (!prize.equals("")){
            preButton.setText("₹ "+prize);
        }else{
            preButton.setText(R.string.loading);
        }
        if (!unitType.equals("")){
            preTextView1.setText(unitType);
        }else{
            preTextView1.setText(R.string.loading);
        }
    }
}

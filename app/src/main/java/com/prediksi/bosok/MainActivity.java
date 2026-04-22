package com.prediksi.bosok;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cardHK = findViewById(R.id.cardHK);
        CardView cardSD = findViewById(R.id.cardSD);
        CardView cardSG = findViewById(R.id.cardSG);
        CardView cardRiwayat = findViewById(R.id.cardRiwayat);

        cardHK.setOnClickListener(v -> bukaPrediksi("HONGKONG"));
        cardSD.setOnClickListener(v -> bukaPrediksi("SYDNEY"));
        cardSG.setOnClickListener(v -> bukaPrediksi("SINGAPURA"));
        cardRiwayat.setOnClickListener(v -> {
            startActivity(new Intent(this, RiwayatActivity.class));
        });
    }

    private void bukaPrediksi(String pasaran) {
        Intent i = new Intent(this, PrediksiActivity.class);
        i.putExtra("pasaran", pasaran);
        startActivity(i);
    }
}

package com.prediksi.bosok;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.List;

public class PrediksiActivity extends AppCompatActivity {

    private String pasaran;
    private TextView tvPasaran, tvAngkaIkut, tvColokBebas, tv4D, tv3D, tv2D, tvJitu, tvStatus;
    private Button btnGenerate;
    private ProgressBar progress;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediksi);

        pasaran = getIntent().getStringExtra("pasaran");
        db = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Prediksi " + pasaran);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        tvPasaran = findViewById(R.id.tvPasaran);
        tvAngkaIkut = findViewById(R.id.tvAngkaIkut);
        tvColokBebas = findViewById(R.id.tvColokBebas);
        tv4D = findViewById(R.id.tv4D);
        tv3D = findViewById(R.id.tv3D);
        tv2D = findViewById(R.id.tv2D);
        tvJitu = findViewById(R.id.tvJitu);
        tvStatus = findViewById(R.id.tvStatus);
        btnGenerate = findViewById(R.id.btnGenerate);
        progress = findViewById(R.id.progress);

        tvPasaran.setText("PASARAN: " + pasaran);

        btnGenerate.setOnClickListener(v -> generatePrediksi());
    }

    private void generatePrediksi() {
        progress.setVisibility(View.VISIBLE);
        tvStatus.setText("🤖 AI sedang menganalisa data...");
        btnGenerate.setEnabled(false);

        new android.os.Handler().postDelayed(() -> {
            List<String> riwayat = db.getNomorOnly(pasaran);
            PrediksiEngine engine = new PrediksiEngine(pasaran, riwayat);
            PrediksiEngine.HasilPrediksi hp = engine.generatePrediksi();

            tvAngkaIkut.setText(joinList(hp.angkaIkut, " • "));
            tvColokBebas.setText(joinList(hp.colokBebas, " • "));
            tv4D.setText(joinStr(hp.pasangan4D, "\n"));
            tv3D.setText(joinStr(hp.pasangan3D, "\n"));
            tv2D.setText(joinStr(hp.pasangan2D, " | "));
            tvJitu.setText(hp.angkaJitu);

            progress.setVisibility(View.GONE);
            tvStatus.setText("✅ Prediksi berhasil dibuat | Data riwayat: " + riwayat.size());
            btnGenerate.setEnabled(true);

            if (riwayat.size() < 3) {
                Toast.makeText(this, "💡 Tambahkan riwayat keluaran agar AI lebih akurat!", Toast.LENGTH_LONG).show();
            }
        }, 1500);
    }

    private String joinList(List<Integer> list, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(sep);
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    private String joinStr(List<String> list, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(sep);
            sb.append(list.get(i));
        }
        return sb.toString();
    }
}

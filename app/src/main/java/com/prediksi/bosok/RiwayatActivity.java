package com.prediksi.bosok;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RiwayatActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Spinner spinPasaran;
    private ListView listView;
    private Button btnTambah;
    private String currentPasaran = "HONGKONG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        db = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Riwayat Keluaran");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        spinPasaran = findViewById(R.id.spinPasaran);
        listView = findViewById(R.id.listView);
        btnTambah = findViewById(R.id.btnTambah);

        String[] pasaranList = {"HONGKONG", "SYDNEY", "SINGAPURA"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pasaranList);
        spinPasaran.setAdapter(adapter);

        spinPasaran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                currentPasaran = pasaranList[pos];
                loadData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnTambah.setOnClickListener(v -> dialogTambah());

        listView.setOnItemLongClickListener((parent, v, pos, id) -> {
            List<String[]> data = db.getByPasaran(currentPasaran);
            String idDel = data.get(pos)[0];
            new AlertDialog.Builder(this)
                .setTitle("Hapus")
                .setMessage("Hapus nomor ini?")
                .setPositiveButton("YA", (d, w) -> {
                    db.hapus(idDel);
                    loadData();
                })
                .setNegativeButton("Batal", null)
                .show();
            return true;
        });
    }

    private void dialogTambah() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Contoh: 1234");

        new AlertDialog.Builder(this)
            .setTitle("Tambah Keluaran " + currentPasaran)
            .setView(input)
            .setPositiveButton("Simpan", (d, w) -> {
                String nomor = input.getText().toString().trim();
                if (nomor.length() == 4) {
                    String tgl = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    db.tambahKeluaran(currentPasaran, nomor, tgl);
                    loadData();
                    Toast.makeText(this, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Nomor harus 4 digit", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Batal", null)
            .show();
    }

    private void loadData() {
        List<String[]> data = db.getByPasaran(currentPasaran);
        String[] display = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            display[i] = "🎯 " + data.get(i)[1] + "     📅 " + data.get(i)[2];
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, display));
    }
}

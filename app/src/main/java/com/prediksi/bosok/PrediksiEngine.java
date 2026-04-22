package com.prediksi.bosok;

import java.util.*;

public class PrediksiEngine {

    private String pasaran;
    private List<String> riwayat;
    private Random rand = new Random();

    public PrediksiEngine(String pasaran, List<String> riwayat) {
        this.pasaran = pasaran;
        this.riwayat = riwayat;
        if (this.riwayat == null) this.riwayat = new ArrayList<>();
    }

    // METODE MISTIK LAMA
    public Set<Integer> mistikLama() {
        Set<Integer> hasil = new HashSet<>();
        int[][] tabel = {
            {0,5},{1,6},{2,7},{3,8},{4,9}
        };
        for (String r : riwayat) {
            if (r.length() >= 4) {
                for (char c : r.toCharArray()) {
                    int d = Character.getNumericValue(c);
                    if (d >= 0 && d <= 9) {
                        for (int[] pair : tabel) {
                            if (pair[0] == d) hasil.add(pair[1]);
                            if (pair[1] == d) hasil.add(pair[0]);
                        }
                    }
                }
            }
        }
        while (hasil.size() < 5) hasil.add(rand.nextInt(10));
        return hasil;
    }

    // METODE MISTIK BARU
    public Set<Integer> mistikBaru() {
        Set<Integer> hasil = new HashSet<>();
        int[][] tabel = {
            {0,1},{2,3},{4,5},{6,7},{8,9}
        };
        for (String r : riwayat) {
            if (r.length() >= 4) {
                int as = Character.getNumericValue(r.charAt(0));
                int kop = Character.getNumericValue(r.charAt(1));
                int kepala = Character.getNumericValue(r.charAt(2));
                int ekor = Character.getNumericValue(r.charAt(3));
                int total = (as + kop + kepala + ekor) % 10;
                for (int[] p : tabel) {
                    if (p[0] == total) hasil.add(p[1]);
                    if (p[1] == total) hasil.add(p[0]);
                }
                hasil.add(total);
            }
        }
        while (hasil.size() < 5) hasil.add(rand.nextInt(10));
        return hasil;
    }

    // METODE INDEKS
    public Set<Integer> indeks() {
        Set<Integer> hasil = new HashSet<>();
        for (String r : riwayat) {
            if (r.length() >= 4) {
                int ekor = Character.getNumericValue(r.charAt(3));
                int kepala = Character.getNumericValue(r.charAt(2));
                int indeksAngka = (ekor + kepala) % 10;
                hasil.add(indeksAngka);
                hasil.add((indeksAngka + 3) % 10);
                hasil.add((indeksAngka + 7) % 10);
            }
        }
        while (hasil.size() < 5) hasil.add(rand.nextInt(10));
        return hasil;
    }

    // AI SIMULASI - Self Learning dari riwayat
    public Map<Integer, Double> aiAnalisa() {
        Map<Integer, Double> bobot = new HashMap<>();
        for (int i = 0; i < 10; i++) bobot.put(i, 0.0);

        double weight = 1.0;
        for (int i = riwayat.size() - 1; i >= 0; i--) {
            String r = riwayat.get(i);
            if (r.length() >= 4) {
                for (char c : r.toCharArray()) {
                    int d = Character.getNumericValue(c);
                    if (d >= 0 && d <= 9) {
                        bobot.put(d, bobot.get(d) + weight);
                    }
                }
                weight *= 0.85; // lebih baru = lebih penting
            }
        }
        return bobot;
    }

    // Gabungan semua metode + AI
    public HasilPrediksi generatePrediksi() {
        Set<Integer> lama = mistikLama();
        Set<Integer> baru = mistikBaru();
        Set<Integer> idx = indeks();
        Map<Integer, Double> ai = aiAnalisa();

        // Angka Ikut - gabungan 3 metode + top AI
        Set<Integer> angkaIkut = new TreeSet<>();
        angkaIkut.addAll(lama);
        angkaIkut.addAll(baru);
        angkaIkut.addAll(idx);

        // Filter pakai AI - ambil top 6
        List<Map.Entry<Integer, Double>> sorted = new ArrayList<>(ai.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        Set<Integer> aiTop = new LinkedHashSet<>();
        for (int i = 0; i < Math.min(6, sorted.size()); i++) {
            aiTop.add(sorted.get(i).getKey());
        }

        // Colok Bebas = top 4 AI
        List<Integer> colokBebas = new ArrayList<>(aiTop);
        if (colokBebas.size() > 4) colokBebas = colokBebas.subList(0, 4);

        HasilPrediksi hp = new HasilPrediksi();
        hp.angkaIkut = new ArrayList<>(angkaIkut);
        hp.colokBebas = colokBebas;
        hp.pasangan4D = generatePasang(4, 5, aiTop, angkaIkut);
        hp.pasangan3D = generatePasang(3, 5, aiTop, angkaIkut);
        hp.pasangan2D = generatePasang(2, 10, aiTop, angkaIkut);
        hp.angkaJitu = generateJitu(aiTop);

        return hp;
    }

    private List<String> generatePasang(int digit, int jumlah, Set<Integer> aiTop, Set<Integer> ikut) {
        Set<String> hasil = new LinkedHashSet<>();
        List<Integer> pool = new ArrayList<>(aiTop);
        pool.addAll(ikut);
        if (pool.size() < digit) {
            for (int i = 0; i < 10; i++) pool.add(i);
        }

        int maxTry = 200;
        while (hasil.size() < jumlah && maxTry-- > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digit; i++) {
                sb.append(pool.get(rand.nextInt(pool.size())));
            }
            hasil.add(sb.toString());
        }
        return new ArrayList<>(hasil);
    }

    private String generateJitu(Set<Integer> aiTop) {
        List<Integer> list = new ArrayList<>(aiTop);
        if (list.size() < 4) {
            while (list.size() < 4) list.add(rand.nextInt(10));
        }
        return "" + list.get(0) + list.get(1) + list.get(2) + list.get(3);
    }

    public static class HasilPrediksi {
        public List<Integer> angkaIkut;
        public List<Integer> colokBebas;
        public List<String> pasangan4D;
        public List<String> pasangan3D;
        public List<String> pasangan2D;
        public String angkaJitu;
    }
}

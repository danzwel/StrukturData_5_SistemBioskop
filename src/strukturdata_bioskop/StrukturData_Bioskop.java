package strukturdata_bioskop;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Queue;

public class StrukturData_Bioskop {

    //Record untuk merepresentasilkan informasi film
    public record Film(int studio, String judul, String sutradara, String negara, String rilis) {

    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Film[] infofilm = new Film[3];
        infofilm[0] = new Film(1, "Ancika: Dia yang bersamaku 1995", "Benni Setiawan", "Indonesia", "11 Jan 24");
        infofilm[1] = new Film(2, "Aquaman and the Lost Kingdom", "James Wan", "Amerika Serikat", "20 Des 23");
        infofilm[2] = new Film(3, "Siksa Neraka", "Anggy Umbara", "Indonesia", "14 Des 23");

        // Data peta kursi untuk setiap film dan jam tayang
        String[][][] kursiFilmJam = new String[infofilm.length][4][7 * 9];

        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n-----------------------------------------------MENU UTAMA----------------------------------------------");
            System.out.println("1. Pemesanan Tiket Film");
            System.out.println("2. Pengecekan Kursi Kosong");
            System.out.println("3. Keluar");
            System.out.print("Pilih menu (1-3): ");

            int menuChoice = input.nextInt();

            switch (menuChoice) {
                case 1:
                    pesanTiketFilm(input, infofilm, kursiFilmJam);
                    break;
                case 2:
                    tampilkanPetaKursi(infofilm, kursiFilmJam);
                    break;
                case 3:
                    System.out.println("Terima kasih. Program berakhir.");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan pilih lagi.");
            }
        }
    }
    
    private static void pesanTiketFilm(Scanner input, Film[] infofilm, String[][][] kursiFilmJam) {
        System.out.println("\n===================================DAFTAR FILM YANG TAYANG HARI INI====================================");
        System.out.printf("%-10s%-35s%-25s%-20s%-15s\n", "STUDIO", "|" + "JUDUL FILM", "|" + "SUTRADARA", "|" + "NEGARA", "|" + "RILIS");
        System.out.println("-------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < infofilm.length; i++) {
            System.out.printf("%-10s%-35s%-25s%-20s%-15s\n", infofilm[i].studio, "|" + infofilm[i].judul, "|" + infofilm[i].sutradara, "|" + infofilm[i].negara, "|" + infofilm[i].rilis);
        }

        System.out.print("\nMasukkan film yang ingin ditonton : ");
        int milihfilm = input.nextInt();

        if (milihfilm >= 1 && milihfilm <= infofilm.length) {
            System.out.print("Masukkan jam ingin menonton (14:00, 16:00, 18:30, 22:30): ");
            String pemilihanjam = input.next();

            if (jikajamsesuai(pemilihanjam)) {
                System.out.println("Anda telah memilih untuk menonton film " + infofilm[milihfilm - 1].judul + " pada jam " + pemilihanjam);

                System.out.print("\nMasukkan nama pemesan           : ");
                String nama = input.next();

                System.out.print("Berapa tiket yang ingin dipesan : ");
                int jumlahTiket = input.nextInt();

                if (jumlahTiket > 0) {
                    String[][] kursi = new String[7][9];
                    System.out.println("\nRincian kursi sekarang:");
                    tampilandenahkursi(kursiFilmJam[milihfilm - 1][getJamIndex(pemilihanjam)]);
                    System.out.println("Pilih kursi (format: baris(A-G) kolom(1-9)): ");

                    Queue<String> tiketList = new LinkedList<>();

                    for (int i = 0; i < jumlahTiket; i++) {
                        System.out.print("Tiket ke-" + (i + 1) + ": ");
                        String milihkursi = input.next();

                        if (jikakursisesuai(milihkursi) && kursitersedia(kursiFilmJam[milihfilm - 1][getJamIndex(pemilihanjam)], milihkursi)) {
                            kursitelahdipilih(kursiFilmJam[milihfilm - 1][getJamIndex(pemilihanjam)], milihkursi);

                            String tiketInfo = String.format(" Nama       : %s\n Film       : %s\n Jam        : %s\n Row: %s\n Col: %d\n Harga      : Rp%d",
                                    nama, infofilm[milihfilm - 1].judul, pemilihanjam, milihkursi.charAt(0), Integer.parseInt(milihkursi.substring(1)), 35000);
                            tiketList.add(tiketInfo);
                        } else {
                            System.out.println("Kursi tidak valid atau sudah terisi. Silakan pilih kursi lain.");
                            i--;
                        }
                    }

                    int hargaTiket = 35000;
                    int totalHarga = jumlahTiket * hargaTiket;
                    System.out.println("\nTotal Harga                : Rp" + totalHarga);

                    System.out.print("Masukkan jumlah pembayaran : Rp");
                    int pembayaran = input.nextInt();

                    int kembalian = pembayaran - totalHarga;
                    System.out.println("Kembalian                  : Rp" + kembalian);

                    if (kembalian >= 0) {
                        System.out.println("\n=====================================TIKET=====================================");
                        for (String tiket : tiketList) {
                            System.out.println(tiket);
                            System.out.println("===============================================================================");
                        }
                    } else {
                        System.out.println("Pembayaran tidak mencukupi.");
                    }

                } else {
                    System.out.println("Jumlah tiket tidak valid.");
                }
            } else {
                System.out.println("Jam tayang tidak valid.");
            }
        } else {
            System.out.println("Nomor studio tidak valid.");
        }
    }

    private static void tampilkanPetaKursi(Film[] infofilm, String[][][] kursiFilmJam) {
        System.out.println("\nPETA KURSI KOSONG:");
        for (int i = 0; i < infofilm.length; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println("\nFilm: " + infofilm[i].judul + " - Jam: " + getJamTayang(j));
                tampilandenahkursi(kursiFilmJam[i][j]);
            }
        }
    }

    private static String getJamTayang(int jamIndex) {
        switch (jamIndex) {
            case 0:
                return "14:00";
            case 1:
                return "16:00";
            case 2:
                return "18:30";
            case 3:
                return "22:30";
            default:
                return "";
        }
    }

    private static int getJamIndex(String jam) {
        switch (jam) {
            case "14:00":
                return 0;
            case "16:00":
                return 1;
            case "18:30":
                return 2;
            case "22:30":
                return 3;
            default:
                return -1;
        }
    }

    private static boolean jikajamsesuai(String time) {
        return time.matches("^(14:00|16:00|18:30|22:30)$");
    }

    private static boolean jikakursisesuai(String seat) {
        return seat.matches("^[A-Ga-g][1-9]$");
    }

    private static boolean kursitersedia(String[] kursi, String seat) {
        int row = seat.charAt(0) - 'A';
        int col = Integer.parseInt(seat.substring(1)) - 1;
        return kursi[row * 9 + col] == null || !kursi[row * 9 + col].equals("X");
    }

    private static void kursitelahdipilih(String[] kursi, String seat) {
        int row = seat.charAt(0) - 'A';
        int col = Integer.parseInt(seat.substring(1)) - 1;
        kursi[row * 9 + col] = "X";
    }

    private static void tampilandenahkursi(String[] kursi) {
        System.out.println("Peta Kursi:");

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print((kursi[i * 9 + j] != null ? "X" : "O") + " ");
            }
            System.out.println();
        }
    }
}
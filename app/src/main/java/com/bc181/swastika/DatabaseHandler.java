package com.bc181.swastika;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.security.PrivateKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 2;
    private final static String DATABASE_NAME = "db_bukuku";
    private final static String TABLE_BUKU = "t_buku";
    private final static String KEY_ID_BUKU = "ID_Buku";
    private final static String KEY_JUDUL = "Judul";
    private final static String KEY_TGL = "Tanggal";
    private final static String KEY_GAMBAR = "Gambar";
    private final static String KEY_CAPTION = "Caption";
    private final static String KEY_PENULIS = "Penulis";
    private final static String KEY_ISI_BUKU = "Isi_Buku";
    private final static String KEY_LINK = "Link";
    private SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
    private Context context;

    public DatabaseHandler(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION );
        this.context = ctx;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BUKU = "CREATE TABLE " + TABLE_BUKU
                + "(" + KEY_ID_BUKU + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDUL + " TEXT, " + KEY_TGL + " DATE, "
                + KEY_GAMBAR + " TEXT, " + KEY_CAPTION + " TEXT, "
                + KEY_PENULIS + " TEXT, " + KEY_ISI_BUKU + " TEXT, "
                + KEY_LINK + " TEXT);";

        db.execSQL(CREATE_TABLE_BUKU);
        inisialisasiBukuAwal(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_BUKU;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void tambahBuku(Buku dataBuku){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataBuku.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataBuku.getTanggal()));
        cv.put(KEY_GAMBAR, dataBuku.getGambar());
        cv.put(KEY_CAPTION, dataBuku.getCaption());
        cv.put(KEY_PENULIS, dataBuku.getPenulis());
        cv.put(KEY_ISI_BUKU, dataBuku.getIsiBuku());
        cv.put(KEY_LINK, dataBuku.getLink());

        db.insert(TABLE_BUKU, null, cv);
        db.close();
    }

    public void tambahBuku(Buku dataBuku, SQLiteDatabase db){
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataBuku.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataBuku.getTanggal()));
        cv.put(KEY_GAMBAR, dataBuku.getGambar());
        cv.put(KEY_CAPTION, dataBuku.getCaption());
        cv.put(KEY_PENULIS, dataBuku.getPenulis());
        cv.put(KEY_ISI_BUKU, dataBuku.getIsiBuku());
        cv.put(KEY_LINK, dataBuku.getLink());
        db.insert(TABLE_BUKU, null, cv);

    }
    public void editBuku(Buku dataBuku){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataBuku.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataBuku.getTanggal()));
        cv.put(KEY_GAMBAR, dataBuku.getGambar());
        cv.put(KEY_CAPTION, dataBuku.getCaption());
        cv.put(KEY_PENULIS, dataBuku.getPenulis());
        cv.put(KEY_ISI_BUKU, dataBuku.getIsiBuku());
        cv.put(KEY_LINK, dataBuku.getLink());

        db.update(TABLE_BUKU, cv, KEY_ID_BUKU + "=?", new String[]{String.valueOf(dataBuku.getIdBuku())});
        db.close();
    }

    public void hapusBuku(int idBuku){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_BUKU, KEY_ID_BUKU + "=?", new String[]{String.valueOf(idBuku)});
        db.close();
    }

    public ArrayList<Buku> getAllBuku() {
        ArrayList<Buku> dataBuku = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_BUKU;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if(csr.moveToFirst()){
            do {
                Date tempDate = new Date();
                try{
                    tempDate = sdFormat.parse(csr.getString(2));
                } catch (ParseException er){
                    er.printStackTrace();
                }

                Buku tempBuku = new Buku(
                        csr.getInt(0),
                        csr.getString(1),
                        tempDate,
                        csr.getString(3),
                        csr.getString(4),
                        csr.getString(5),
                        csr.getString(6),
                        csr.getString(7)
                );

                dataBuku.add(tempBuku);
            } while (csr.moveToNext());
        }
        return dataBuku;
    }

    private String storeImageFile(int id){
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id);
        location = inputActivity.saveImageToInternalStorage(image, context);
        return location;
    }

    private void inisialisasiBukuAwal(SQLiteDatabase db) {
        int idBuku = 0;
        Date tempDate = new Date();

        //Menambah Data Buku ke-1
        try {
            tempDate = sdFormat.parse("01/11/2011 06:22");
        } catch (ParseException er) {
            er.printStackTrace();
        }

        Buku buku1 = new Buku(
                idBuku,
                "Manusia Setengah Salmon: [chapter 1]",
                tempDate,
                storeImageFile(R.drawable.buku1),
                "GagasMedia",
                "Raditya Dika",
                "Nyokap memandangi penjuru kamar gue. Dia diam sebentar, tersenyum, lalu bertanya, ÔKamu takut ya? Makanya belom tidur?Õ\n" +
                        "\n" +
                        "ÔEnggak, kenapa harus takut?Õ\n" +
                        "\n" +
                        "ÔYa, siapa tahu rumah baru ini ada hantunya, hiiiiii...,Õ kata Nyokap, mencoba menakut-nakuti.\n" +
                        "\n" +
                        "ÔEnggak takut, Ma,Õ jawab gue.\n" +
                        "\n" +
                        "ÔKikkikikiki.Õ Nyokap mencoba menirukan suara kuntilanak, yang malah terdengar seperti ABG kebanyakan ngisep lem sewaktu hendak photobox. ÔKikikikikiki.Õ\n" +
                        "\n" +
                        "ÔAku enggak taÑÕ\n" +
                        "\n" +
                        "ÔKIKIKIKIKIKIKIKI!Õ Nyokap makin menjadi.\n" +
                        "\n" +
                        "ÔMa,Õ kata gue, Ôkata orang, kalo kita malem-malem niruin ketawa kuntilanak, dia bisa dateng lho.Õ\n" +
                        "\n" +
                        "ÔJANGAN NGOMONG GITU, DIKA!Õ Nyokap sewot. ÔKamu durhaka ya nakut-nakutin orang tua kayak gitu! Awas, ya, kamu, Dika!Õ\n" +
                        "\n" +
                        "ÔLah, tadi yang nakut-nakutin siapa, yang ketakutan siapa.Õ\n" +
                        "\n" +
                        "\n" +
                        "Manusia Setengah Salmon adalah kumpulan tulisan komedi Raditya Dika. Sembilan belas bab di dalam bercerita tentang pindah rumah, pindah hubungan keluarga, sampai pindah hati. Simak juga bab berisi tulisan galau, observasi ngawur, dan lelucon singkat khas Raditya Dika.\n" +
                        "\n" +
                        "-----------------------\n" +
                        "\n" +
                        "Chapter 1 dari 10 buku Manusia Setengah Salmon",
                "https://play.google.com/store/books/details/Raditya_Dika_Manusia_Setengah_Salmon?id=iv91DQAAQBAJ"
        );
        tambahBuku(buku1, db);
        idBuku++;

        // Data Buku ke-2
        try {
            tempDate = sdFormat.parse("01/02/2010 06:15");
        } catch (ParseException er) {
            er.printStackTrace();
        }

        Buku buku2 = new Buku(
                idBuku,
                "Tertawa Bersama Gus Dur: Humornya Kiai Indonesia",
                tempDate,
                storeImageFile(R.drawable.buku2),
                "PT Mizan Publika\n",
                "Muhammad Zikra",
                "??Kemenangan? Gus Dur atas ?lawan-lawan?-nya terutama karena dia punya rasa humor yang tinggi. Waktu menjadi presiden dan membuka pameran lukisan saya, dengan enteng Gus Dur berkata, ?Sudah tahu saya tidak bisa melihat, kok, disuruh membuka pameran ....? Buktikan sendiri dengan membaca buku ini.? ?K.H. Mustofa Bishri Seorang pandita Hindu, seorang pastor Katolik, dan seorang kiai Islam, memperdebatkan tentang siapa di antara mereka yang paling dekat dengan Tuhan. ?Kami!? ujar pandita Hindu. ?Kami memanggil Dia Om, seperti kami menyebut paman kami,? jawab pandita Hindu sambil merapatkan kedua tangan di dada. ?Om, shanti, shanti, Om.? ?Kalau begitu, kamilah yang jelas lebih dekat kepada Tuhan!? ujar pendeta Katolik, ?Kami memanggil Dia 'Bapa'. Bapa kami yang ada di Surga.? Kiai terdiam. ?Hm . . ., ? sang kiai merenung, ?sebenarnya kalau kami ingin memanggilnya, kami tinggal berteriak saja dari menara masjid . . . .? Berlatar belakang keluarga pesantren, Gus Dur dibesarkan oleh tradisi guyonan kalangan Nadhliyin yang blak-blakan. Setiap guyonan yang terlontar dari mulut Gus Dur adalah sebuah refleksi atas berbagai hal dan peristiwa. Dia pun tak ragu menjadikan dirinya sendiri sebagai bahan guyonan, dan dengan itu dia pun mengajarkan sikap self-criticism kepada pendengarnya. Buku Tertawa Bersama Gus Dur ini mengompilasi kembali berbagai guyonan Gus Dur yang selama ini telah membuat banyak orang tersenyum. Seperti Nasrudin Hoja dan Abu Nawas, humor Gus Dur tak hanya membuat orang tertawa, tetapi juga merenungi betapa kegetiran dunia ternyata bisa diselesaikan dengan humor ala sufi yang kritis. ?Sense of humor yang tinggi justru menunjukkan kapasitas spiritual intelligence yang tinggi dari seseorang. ?Prof. Dr. K.H. Jalaluddin Rakhmat ?Beliau adalah orang yang jago mencairkan suasana, guyonannya spontan dan disukai oleh siapa pun mulai dari orang Batak, Madura, LSM, hingga Lintas Agama.??Sulaiman (asisten pribadi Gus Dur) ?Seniman yang khusus menciptakan kesegaran humor dengan joke-joke cerdas.? ?Jaya Suprana [Mizan, Mizania, Inspirasi, Motivasi, Indonesia]",
                "https://play.google.com/store/books/details/Muhammad_Zikra_Tertawa_Bersama_Gus_Dur?id=dxGBAwAAQBAJ"
        );
        tambahBuku(buku2, db);
        idBuku++;

        //Data Buku ke-3
        try {
            tempDate = sdFormat.parse("13/10/2011 22:46");
        } catch (ParseException er) {
            er.printStackTrace();
        }

        Buku buku3 = new Buku(
                idBuku,
                "Facebook Hacker",
                tempDate,
                storeImageFile(R.drawable.buku3),
                "Lembar Langit Indonesia",
                "Arya Dipanegara",
                "Bisa dibilang ini adalah buku terlarang! Karena perbuatan meng-hack atau membajak adalah perbuatan yang merugikan pihak lain. Namun diluar itu semua, yang perlu diketahui, ini adalah sebuah pengetahuan atau ilmu. Sungguh tidak ada ruginya mempelajari atau mengetahui sebuah wawasan baru. Dalam buku terbitan JAL PUBLISHING ini memberikan Anda penjelasan mengenai apa itu hacker, seperti apa proses hacking itu, juga tentang perantasan media sosial Facebook.\n" +
                        "Sesungguhnya tidak ada senjata yang berbahaya. Pisau, pistol atau nuklir sekalipun hanyalah sesuatu yang biasa. Namun siapa yang menggunakannya itulah yang BERBAHAYA!\n" +
                        "Sebab dia menentukan ingin menggunakannya seperti apa?\n" +
                        "\n" +
                        "-Lembar Langit Indonesia Group-",
                "https://play.google.com/store/books/details/Arya_Dipanegara_Facebook_Hacker?id=Q8ZlCgAAQBAJ"
        );
        tambahBuku(buku3, db);
        idBuku++;

        //Data Buku ke-4
        try {
            tempDate = sdFormat.parse("06/01/2017 05:58");
        } catch (ParseException er) {
            er.printStackTrace();
        }

        Buku buku4 = new Buku(
                idBuku,
                "Belajar Bijak - Jurus Hidup Memenangi Pertarungan (Snackbook)",
                tempDate,
                storeImageFile(R.drawable.buku4),
                "Bentang Pustaka",
                "Whani Darmawan",
                "Jangan kau biarkan otak dan hatimu angkuh hendak memisahkan diri dari badanmu.\n" +
                        "Mengapa silat sering dikatakan sebagai media belajar budi pekerti dan kepribadian? Sudah sangat sering saya mendengar kalimat semacam itu, tetapi pengertiannya selama ini masih abstrak. Lalu, di manakah letak metode pembelajaran dan pemahamannya? Inilah cerita-cerita tentang hal tersebut.\n" +
                        "\n" +
                        "Berlatih silat memang bisa membuat seseorang menjadi bangga dan yakin karena setidaknya sadar bahwa diri kita telah punya bekal. Rasa sadar diri bisa saja “salah letak” dalam kepribadian yang kemudian mawujud pada sikap yang overacting atawa lebay. Padahal, para bijak yang telah menempuh perjalanan panjang keilmuan mengatakan, semakin masuk ke ilmu, kita akan semakin bodoh.\n" +
                        "\n" +
                        "[Mizan, Bentang Pustaka, Ebook, Buku Digital, Pendek, Ringan, Inspirasi, Indonesia]",
                "https://play.google.com/store/books/details/Whani_Darmawan_Belajar_Bijak_Jurus_Hidup_Memenangi?id=MjbYDQAAQBAJ"
        );
        tambahBuku(buku4, db);
        idBuku++;

    }
}


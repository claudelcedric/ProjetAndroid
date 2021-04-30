package com.example.mesfrigos4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/*
 * Création de la bdd
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // TABLE INFORMATTION
    public static final String TABLE_ALIMENT = "aliment";
    public static final String ALIMENT_ID = "_id";
    public static final String ALIMENT_NOM = "nom";
    public static final String ALIMENT_CATEGORIE = "categorie";
    public static final String ALIMENT_DATE_PEREMPTION = "date_peremption";

    // DATABASE INFORMATION
    static final String DB_NAME = "ALIMENTS.DB";
    static final int DB_VERSION = 1;

    // TABLE CREATION STATEMENT

    private static final String CREATE_TABLE = "create table " + TABLE_ALIMENT
            + "(" + ALIMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ALIMENT_NOM + " TEXT NOT NULL ," + ALIMENT_CATEGORIE
            + " TEXT NOT NULL ," + ALIMENT_DATE_PEREMPTION
            + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    //creation table aliment
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);
    }

    @Override
    //en cas de mis a jour lié au changement de version-drop et create
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALIMENT);
        onCreate(db);

    }

}

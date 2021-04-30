package com.example.mesfrigos4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/*
 * Classe permettant d'éxécuter les requetes sql sur la table membre
 */
public class Persistance {

    private DatabaseHelper dbhelper;
    private Context moncontext;
    private SQLiteDatabase database;

    // constructeur
    public Persistance(Context c) {
        moncontext = c;
    }

    // ouverture de la bdd
    public Persistance open() throws SQLException {
        dbhelper = new DatabaseHelper(moncontext);
        database = dbhelper.getWritableDatabase();
        return this;

    }

    // fermeture de la bdd
    public void close() {
        dbhelper.close();
    }

    // requete insertion
    public void insertData(Aliment_frigidaire a) {
        String nom = a.getName();
        String categorie = a.getCategory();
        String date_peremption = a.getExpiration_date();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.ALIMENT_NOM, nom);
        cv.put(DatabaseHelper.ALIMENT_CATEGORIE, categorie);
        cv.put(DatabaseHelper.ALIMENT_DATE_PEREMPTION, String.valueOf(date_peremption));
        database.insert(DatabaseHelper.TABLE_ALIMENT, null, cv);

    }

    // requete modification
    public void editData(Aliment_frigidaire a)
    {
        String id = a.getId();
        String nom = a.getName();
        String categorie = a.getCategory();
        String date_peremption = a.getExpiration_date();

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.ALIMENT_NOM, nom);
        cv.put(DatabaseHelper.ALIMENT_CATEGORIE, categorie);
        cv.put(DatabaseHelper.ALIMENT_DATE_PEREMPTION, String.valueOf(date_peremption));
        database.update(DatabaseHelper.TABLE_ALIMENT, cv, DatabaseHelper.ALIMENT_ID
                + " = " + id, null);

    }

    // requete de suppression
    public void deleteData(String id) {

        database.delete(DatabaseHelper.TABLE_ALIMENT, DatabaseHelper.ALIMENT_ID
                + " = " + id, null);
    }

    // requete de selection
    public Cursor select() {

        String[] allColumns = new String[] { DatabaseHelper.ALIMENT_ID,
                DatabaseHelper.ALIMENT_NOM, DatabaseHelper.ALIMENT_CATEGORIE,
                DatabaseHelper.ALIMENT_DATE_PEREMPTION };

        Cursor c = database.query(DatabaseHelper.TABLE_ALIMENT, allColumns,
                null, null, null, null, null);
        return c;

    }

}
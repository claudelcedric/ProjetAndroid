package com.example.mesfrigos4;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class FrigidaireFragment extends Fragment {

    EditText name,date;
    Spinner category;
    TextView res;

    String add_name,add_category,add_date;
    Persistance reqsql;
    ProgressDialog PD;

    CustomAdapter arrayAdapter;
    int category_number=0;

    int mYear, mMonth, mDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_frigidaire, container, false);
        // Inflate the layout for this fragment
        reqsql = new Persistance(getActivity());
        ArrayList<Aliment_frigidaire> foodList= new ArrayList<Aliment_frigidaire>();
        arrayAdapter = new CustomAdapter(getActivity(), foodList, new CustomAdapter.OnOptionsListener() {
            @Override

                //Afficher boîte dialogue inférieur pour élément sélectionné
            public void onOptions(int position) {
                View view2 = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
                BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
                dialog.setContentView(view2);
                dialog.show();

                    //Supprimer élément
                LinearLayout delete = view2.findViewById(R.id.delete_button);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            //cacher bottom dialog
                        dialog.dismiss();
                        new DelAsync(foodList.get(position).getId()).execute();
                    }
                });
                    //Modifier élément
                LinearLayout edit = view2.findViewById(R.id.edit_button);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            //cacher bottom dialog
                        dialog.dismiss();

                            //Déclaration Pop-Up de modification
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = LayoutInflater.from(getActivity());
                        View popupVue = inflater.inflate(R.layout.ajout_aliment,null);
                        ImageButton select_date = (ImageButton) popupVue.findViewById(R.id.SelectDate);
                        MaterialButton scan_barcode = (MaterialButton) popupVue.findViewById(R.id.barcode_button);

                            //Listener Sélection de la date
                        select_date.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                SelectDate();
                            }
                            });
                        scan_barcode.setVisibility(View.GONE);
                            //Listener scan code_barres
                        scan_barcode.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), BarcodeScanner.class);
                                startActivity(intent);
                            }
                        });

                        name = (EditText) popupVue.findViewById(R.id.edit_name);

                        name.setText(foodList.get(position).getName());
                        category = (Spinner) popupVue.findViewById(R.id.edit_category);
                            //Récupérer le nom de la catégorie d'après le fichier array.xml
                        String[] cat_food = getResources().getStringArray(R.array.categories);

                            //Récupère le nom de la catégorie de l'élément sélectionné
                        String sel_categ=foodList.get(position).getCategory();

                            //Compare élément selectionné avec array.xml
                        if(sel_categ.equals(cat_food[0]))
                        {
                            category_number=0;
                        }
                        else if(sel_categ.equals(cat_food[1]))
                        {
                            category_number=1;
                        }
                        else if(sel_categ.equals(cat_food[2]))
                        {
                            category_number=2;
                        }
                        else if(sel_categ.equals(cat_food[3]))
                        {
                            category_number=3;
                        }
                        else if(sel_categ.equals(cat_food[4]))
                        {
                            category_number=4;
                        }

                        category.setSelection(category_number);
                        date = (EditText) popupVue.findViewById(R.id.edit_date);
                        date.setText(foodList.get(position).getExpiration_date());

                            //Affichage de la Pop-Up
                        builder.setView(popupVue)
                                .setTitle("Ajouter un produit")
                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, null);
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        //.show();
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                add_name=name.getText().toString();
                                add_category=category.getSelectedItem().toString();
                                add_date=date.getText().toString();
                                // write check code
                                if (add_name.equals("")){
                                    Toast.makeText(getActivity(),"Tous les champs ne sont pas complétés",Toast.LENGTH_SHORT).show();
                                }else{
                                    dialog.dismiss();
                                    new EditAsync(foodList.get(position).getId()).execute();
                                }
                            }
                        });
                    }
                });
            }
        });

        ListView simpleList = rootView.findViewById(R.id.simpleListView);
        BuildTable();
        simpleList.setAdapter(arrayAdapter);

            //Ajouter un produit sans code barre
        FloatingActionButton monBouton = rootView.findViewById(R.id.floatingActionButton);
        monBouton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addProduct("");
            }
        });

        Bundle arguments = getArguments();
        if (arguments != null) {
            String code= getArguments().getString("barcode_from_activity");
            addProduct(code);
        }

        return rootView;
    }

    private void addProduct(String code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View popupVue = inflater.inflate(R.layout.ajout_aliment, null);
        name = (EditText) popupVue.findViewById(R.id.edit_name);
        if (code != null) {
            name.setText(code);
        }
        category = (Spinner) popupVue.findViewById(R.id.edit_category);

        //Spinner avec texte "Sélectionner une catégorie"
        List<String> list = new ArrayList<String>();
        list.add(getResources().getStringArray(R.array.categories)[0]);
        list.add(getResources().getStringArray(R.array.categories)[1]);
        list.add(getResources().getStringArray(R.array.categories)[2]);
        list.add(getResources().getStringArray(R.array.categories)[3]);
        list.add(getResources().getStringArray(R.array.categories)[4]);
        list.add("[Sélectionner une catégorie]");
        final int listsize = list.size() - 1;
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list) {
            @Override
            public int getCount() {
                return (listsize); // Truncate the list
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(dataAdapter);
        category.setSelection(listsize);
        ImageButton select_date = (ImageButton) popupVue.findViewById(R.id.SelectDate);
        MaterialButton scan_barcode = (MaterialButton) popupVue.findViewById(R.id.barcode_button);
        //Listener Sélection de la date
        select_date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SelectDate();
            }
        });

        //Listener scan code_barres
        scan_barcode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BarcodeScanner.class);
                startActivity(intent);
            }
        });
        date = (EditText) popupVue.findViewById(R.id.edit_date);
        builder.setView(popupVue)
                .setTitle("Ajouter un produit")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null);
        //.setIcon(android.R.drawable.ic_dialog_alert)
        //.show();
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_name=name.getText().toString();
                add_category=category.getSelectedItem().toString();
                add_date=date.getText().toString();
                // write check code
                if (add_category.equals("") ||
                        add_category.equals("[Sélectionner une catégorie]") ||
                        add_date.equals("")) {
                    Toast.makeText(getActivity(),"Tous les champs ne sont pas complétés",Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    new AddAsync().execute();
                }
            }
        });
    }


    private void SelectDate(){
        Locale.setDefault(Locale.FRANCE);
        // Récupérer date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String text = year + "-";
                        if (monthOfYear < 9) {
                            text += "0";
                        }
                        text += (monthOfYear + 1) + "-";
                        if (dayOfMonth < 10) {
                            text += "0";
                        }
                        text += dayOfMonth;

                        date.setText(text);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void BuildTable() {
        // ouverture de bdd
        // selection des enregistrements
        reqsql.open();
        Cursor c = reqsql.select();

        arrayAdapter.clear();

        // placement du cursor au debut
        while(c.moveToNext()) {
            String id = c.getString(0);
            String name= c.getString(1);
            String category= c.getString(2);
            String expiration_date = c.getString(3);
            arrayAdapter.add(new Aliment_frigidaire(id, name,category,expiration_date));
        }
        reqsql.close();
    }

        //Ajouter élément dans BDD
    private class AddAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // progresbar
            PD = new ProgressDialog(getActivity());
            PD.setTitle("Veuillez patienter");
            PD.setMessage("Chargement en cours...");
            PD.setCancelable(false);
            PD.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // insertion data
            reqsql.open();
            // instanciation dansla classe Aliment_frigidaire
            Aliment_frigidaire m = new Aliment_frigidaire(null, add_name, add_category, add_date);
            // insertion en bdd
            reqsql.insertData(m);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            PD.dismiss();
                //Actualisation de la liste (notamment pour le code barre).
            ListView simpleList = getActivity().findViewById(R.id.simpleListView);
            BuildTable();
            simpleList.setAdapter(arrayAdapter);
        }
    }

        //Modifier élément dans BDD
    private class EditAsync extends AsyncTask<Void, Void, Void> {
        String position;

        public EditAsync(String position) {
            this.position=position;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // progresbar
            PD = new ProgressDialog(getActivity());
            PD.setTitle("Veuillez patienter");
            PD.setMessage("Chargement en cours...");
            PD.setCancelable(false);
            PD.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // recuperation des value nom prenom pour insertion

            // insertion data
            reqsql.open();
            // instanciation dansla classe membre
            Aliment_frigidaire m = new Aliment_frigidaire(position, add_name, add_category, add_date);
            // insertion en bdd
            reqsql.editData(m);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            BuildTable();
            PD.dismiss();
        }
    }

        //Supprimer élément dans BDD
    private class DelAsync extends AsyncTask<Void, Void, Void> {
        String position;

        public DelAsync(String position) {
            this.position=position;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // progresbar
            PD = new ProgressDialog(getActivity());
            PD.setTitle("Veuillez patienter");
            PD.setMessage("Chargement en cours...");
            PD.setCancelable(false);
            PD.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            // ouverture bdd
            reqsql.open();

            // suppression en bdd
            reqsql.deleteData(position);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            BuildTable();
            PD.dismiss();
        }
    }
}
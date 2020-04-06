package com.example.listasimple;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lista;
    List<String> items;
    ArrayAdapter ADP;

    /*Guardar datos en android utilizando sharedpreferences*/
    //*Context context = getApplicationContext();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editTarea = findViewById(R.id.editTarea);
        final Button btnAgregar = findViewById(R.id.btnAgregar);
        lista = findViewById(R.id.lista);
        items = new ArrayList<>();

        SharedPreferences sharprefs = getSharedPreferences("ArchivoSP", Context.MODE_PRIVATE);
        /*Llamar al JSON*/
        String mostrarJSON = sharprefs.getString("ListaJSON", "");

        if (mostrarJSON == ""){
            // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.dialog_message)
                    .setTitle("Lista de Tareas");

            // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
            AlertDialog dialog = builder.create();
        }else {
            /*Deserializacion del JSON de string a ARRAY*/
            items = new Gson().fromJson(mostrarJSON, new TypeToken<List<String>>(){}.getType());
        }

        /*Monta los items dentro de un layout para luego montarlo sobre el contror de lista*/
        ADP = new ArrayAdapter(getApplicationContext(), R.layout.list_item_modlist, items);
        lista.setAdapter(ADP);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.add(editTarea.getText().toString());
                SharedPreferences sharprefs = getSharedPreferences("ArchivoSP", Context.MODE_PRIVATE);
                /*crea variable de editor que me permita editar el ArchivoSP*/
                SharedPreferences.Editor editorArchivoSP = sharprefs.edit();

                /*proceso de formato json para convertir de tablas a tipo string*/
                /*Serializacion del JSON*/
                String SerializeJSON = new Gson().toJson(items);
                editorArchivoSP.putString("ListaJSON", SerializeJSON);
                /*guardar los cambios*/
                editorArchivoSP.apply();
                ADP.notifyDataSetChanged();
            }
        });
        /*evento para eliminar la palaba de la lista*/
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences sharprefs = getSharedPreferences("ArchivoSP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorArchivoSP = sharprefs.edit();
                items.remove(position);
                String SerializeJSON = new Gson().toJson(items);
                editorArchivoSP.putString("ListaJSON", SerializeJSON);
                /*guardar los cambios*/
                editorArchivoSP.apply();
                ADP.notifyDataSetChanged();
                return false;
            }
        });
    }
}

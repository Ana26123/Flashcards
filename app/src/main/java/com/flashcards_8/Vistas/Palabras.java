package com.flashcards_8.Vistas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flashcards_8.R;
import com.flashcards_8.Utilidades.SharedPreferencesUtil;
import com.flashcards_8.Utilidades.Utilidades;
import com.flashcards_8.db.DbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Palabras extends AppCompatActivity {

    private ExpandableListView expandableListViewNivel1, expandableListViewNivel2, expandableListViewNivel3;
    private List<String> listDataHeaderNivel1, listDataHeaderNivel2, listDataHeaderNivel3;
    private HashMap<String, List<String>> listDataChildNivel1, listDataChildNivel2, listDataChildNivel3;
    private DbHelper dbHelper;
    private String selectedWord;
    private String selectedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palabras);

        expandableListViewNivel1 = findViewById(R.id.expandableListViewNivel1);
        expandableListViewNivel2 = findViewById(R.id.expandableListViewNivel2);
        expandableListViewNivel3 = findViewById(R.id.expandableListViewNivel3);
        dbHelper = new DbHelper(this, Utilidades.DATABASE_NAME, null, Utilidades.DATABASE_VERSION);

        // Preparar datos
        prepareListData();

        ExpandableListAdapter adapterNivel1 = new ExpandableListAdapter(this, listDataHeaderNivel1, listDataChildNivel1);
        ExpandableListAdapter adapterNivel2 = new ExpandableListAdapter(this, listDataHeaderNivel2, listDataChildNivel2);
        ExpandableListAdapter adapterNivel3 = new ExpandableListAdapter(this, listDataHeaderNivel3, listDataChildNivel3);

        expandableListViewNivel1.setAdapter(adapterNivel1);
        expandableListViewNivel2.setAdapter(adapterNivel2);
        expandableListViewNivel3.setAdapter(adapterNivel3);

        // Agregar listeners para los ExpandableListViews
        setOnChildClickListener(expandableListViewNivel1, "Nivel 1");
        setOnChildClickListener(expandableListViewNivel2, "Nivel 2");
        setOnChildClickListener(expandableListViewNivel3, "Nivel 3");

        findViewById(R.id.btnAgregarPalabra).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Palabras.this, Agregar_palabras.class);
                startActivity(intent);
            }
        });



        findViewById(R.id.btnEliminarPalabra).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // lógica para eliminar palabras
                if (selectedWord != null && selectedGroup != null) {
                    eliminarPalabraSeleccionada();
                } else {
                    Toast.makeText(Palabras.this, "Seleccione una palabra para eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btnPalabrasVolver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void prepareListData() {
        listDataHeaderNivel1 = new ArrayList<>();
        listDataHeaderNivel2 = new ArrayList<>();
        listDataHeaderNivel3 = new ArrayList<>();

        listDataChildNivel1 = new HashMap<>();
        listDataChildNivel2 = new HashMap<>();
        listDataChildNivel3 = new HashMap<>();

        // Añadir encabezados de los niveles
        listDataHeaderNivel1.add("Nivel 1");
        listDataHeaderNivel2.add("Nivel 2");
        listDataHeaderNivel3.add("Nivel 3");

        // Obtener palabras eliminadas
        Set<String> eliminatedWords = SharedPreferencesUtil.getEliminatedWords(this);

        // Añadir datos de cada nivel
        List<String> nivel1 = cargarPalabrasNivel(Utilidades.TABLE_PALABRAS_NIVEL1, eliminatedWords);
        List<String> nivel2 = cargarPalabrasNivel(Utilidades.TABLE_PALABRAS_NIVEL2, eliminatedWords);
        List<String> nivel3 = cargarPalabrasNivel(Utilidades.TABLE_PALABRAS_NIVEL3, eliminatedWords);

        // Vincular los datos de cada nivel con su encabezado
        listDataChildNivel1.put(listDataHeaderNivel1.get(0), nivel1);
        listDataChildNivel2.put(listDataHeaderNivel2.get(0), nivel2);
        listDataChildNivel3.put(listDataHeaderNivel3.get(0), nivel3);
    }

    private List<String> cargarPalabrasNivel(String tableName, Set<String> eliminatedWords) {
        List<String> palabras = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + Utilidades.CAMPO_PALABRA + " FROM " + tableName, null);
        if (cursor.moveToFirst()) {
            do {
                String palabra = cursor.getString(cursor.getColumnIndexOrThrow(Utilidades.CAMPO_PALABRA));
                if (!eliminatedWords.contains(palabra)) {
                    palabras.add(palabra);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return palabras;
    }

    //Logica para eliminar palabras
    private void eliminarPalabraSeleccionada() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName;

        switch (selectedGroup) {
            case "Nivel 1":
                tableName = Utilidades.TABLE_PALABRAS_NIVEL1;
                break;
            case "Nivel 2":
                tableName = Utilidades.TABLE_PALABRAS_NIVEL2;
                break;
            case "Nivel 3":
                tableName = Utilidades.TABLE_PALABRAS_NIVEL3;
                break;
            default:
                tableName = null;
                break;
        }

        if (tableName != null) {
            db.delete(tableName, Utilidades.CAMPO_PALABRA + "=?", new String[]{selectedWord});
            SharedPreferencesUtil.addEliminatedWord(this, selectedWord);  // Registrar la palabra eliminada en SharedPreferences
            db.close();
            Toast.makeText(this, "Palabra eliminada: " + selectedWord, Toast.LENGTH_SHORT).show();
            // Actualizar la lista después de la eliminación
            prepareListData();
            ((BaseExpandableListAdapter) expandableListViewNivel1.getExpandableListAdapter()).notifyDataSetChanged();
            ((BaseExpandableListAdapter) expandableListViewNivel2.getExpandableListAdapter()).notifyDataSetChanged();
            ((BaseExpandableListAdapter) expandableListViewNivel3.getExpandableListAdapter()).notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Error al eliminar la palabra", Toast.LENGTH_SHORT).show();
        }
    }


    private void setOnChildClickListener(ExpandableListView expandableListView, final String nivel) {
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                selectedWord = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                selectedGroup = nivel;
                Toast.makeText(Palabras.this, "Palabra seleccionada: " + selectedWord, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private final Palabras context;
        private final List<String> listDataHeader;
        private final HashMap<String, List<String>> listDataChild;

        public ExpandableListAdapter(Palabras context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.listDataChild = listChildData;
        }

        @Override
        public int getGroupCount() {
            return this.listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.listDataHeader.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                convertView = context.getLayoutInflater().inflate(R.layout.titulo_font, parent, false);
            }

            TextView lblListHeader = convertView.findViewById(R.id.groupTitle);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final String childText = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                convertView = context.getLayoutInflater().inflate(R.layout.child_item, parent, false);
            }

            TextView txtListChild = convertView.findViewById(R.id.childTitle);
            txtListChild.setText(childText);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}

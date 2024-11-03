package com.example.saldoseguro;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION_BASE_DATOS = 1;
    private static final String NOMBRE_BASE_DATOS = "SaldoSeguro.db";
    private static final String NOMBRE_TABLA = "Sesion";
    private static final String COLUMNA_USER = "user";
    private static final String COLUMNA_PASSWORD = "password";
    private static final String COLUMNA_ID_USUARIO = "idUsuario";

    private static final String CREAR_TABLA =
            "CREATE TABLE "+ NOMBRE_TABLA + " ("+
                    COLUMNA_USER + " TEXT, "+
                    COLUMNA_ID_USUARIO + " TEXT, "+
                    COLUMNA_PASSWORD + " TEXT);";

    public DataBaseHelper (Context contexto){
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREAR_TABLA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(db);
    }

    // Método para agregar un usuario (Create)
    public void agregarSesion(String user, String password, String idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COLUMNA_USER, user);
        valores.put(COLUMNA_PASSWORD, password);
        valores.put(COLUMNA_ID_USUARIO, idUsuario);
        db.insert(NOMBRE_TABLA, null, valores);
        db.close();
    }

    @SuppressLint("Range")
    public String obtenerSesion() {
        SQLiteDatabase db = this.getReadableDatabase();
        String usuario = null;

        // Consulta para obtener el usuario en sesión
        Cursor cursor = db.query(NOMBRE_TABLA, new String[]{COLUMNA_ID_USUARIO}, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                usuario = cursor.getString(cursor.getColumnIndex(COLUMNA_ID_USUARIO));
            }
            cursor.close(); // Cierra el cursor después de usarlo
        }

        db.close(); // Cierra la base de datos después de la consulta
        return usuario; // Devuelve el usuario encontrado o null si no hay sesión
    }

    public void cerrarSesion() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOMBRE_TABLA, null, null);
        db.close();
    }
}

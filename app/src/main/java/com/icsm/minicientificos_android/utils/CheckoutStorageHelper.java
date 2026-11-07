package com.icsm.minicientificos_android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.icsm.minicientificos_android.models.DatosEnvioCliente;

public class CheckoutStorageHelper {

    private static final String PREF_NAME = "prefs_checkout_envio";
    private static final String KEY_NOMBRE = "nombre_completo";
    private static final String KEY_CELULAR = "celular";
    private static final String KEY_CORREO = "correo";
    private static final String KEY_DIRECCION = "direccion";
    private static final String KEY_DISTRITO = "distrito";
    private static final String KEY_PROVINCIA = "provincia";
    private static final String KEY_DEPARTAMENTO = "departamento";
    private static final String KEY_RECORDAR = "recordar_datos";

    public static void guardarDatosEnvio(Context context, DatosEnvioCliente datos) {
        if (context == null || datos == null) return;
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (datos.isRecordarDatos()) {
            editor.putString(KEY_NOMBRE, datos.getNombreCompleto());
            editor.putString(KEY_CELULAR, datos.getCelular());
            editor.putString(KEY_CORREO, datos.getCorreo());
            editor.putString(KEY_DIRECCION, datos.getDireccion());
            editor.putString(KEY_DISTRITO, datos.getDistrito());
            editor.putString(KEY_PROVINCIA, datos.getProvincia());
            editor.putString(KEY_DEPARTAMENTO, datos.getDepartamento());
            editor.putBoolean(KEY_RECORDAR, true);
        } else {
            editor.clear();
        }
        editor.apply();
    }

    public static DatosEnvioCliente obtenerDatosEnvioGuardados(Context context) {
        if (context == null) return new DatosEnvioCliente();
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        boolean recordar = sp.getBoolean(KEY_RECORDAR, false);
        if (!recordar) {
            return new DatosEnvioCliente();
        }

        return new DatosEnvioCliente(
                sp.getString(KEY_NOMBRE, ""),
                sp.getString(KEY_CELULAR, ""),
                sp.getString(KEY_CORREO, ""),
                sp.getString(KEY_DIRECCION, ""),
                sp.getString(KEY_DISTRITO, ""),
                sp.getString(KEY_PROVINCIA, ""),
                sp.getString(KEY_DEPARTAMENTO, ""),
                true
        );
    }
}
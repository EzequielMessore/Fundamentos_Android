package com.br.fundamentosandroid.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

/**
 * Created by c1283796 on 25/09/2015.
 */
public class MaskUtil implements TextWatcher {

    private final EditText mEditText;
    private String current;
    private static Context context;

    public MaskUtil(EditText mEditText, String current, Context context) {
        super();
        this.mEditText = mEditText;
        this.current = current;
    }

    @Override
    public void afterTextChanged(Editable arg0) {
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
        if (!s.toString().equals(current)) {
            mEditText.removeTextChangedListener(this);

            String cleanString = s.toString().replaceAll("[R$,.]", "");

            double parsed = Double.parseDouble(cleanString);
            String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));

            current = formatted;

            mEditText.setText(formatted);
            mEditText.setSelection(formatted.length());
            mEditText.addTextChangedListener(this);
        }

    }

    public static double stringMonetarioToDouble(String str) {
        double retorno = 0;
        try {
            boolean hasMask = ((str.indexOf("R$") > -1 || str.indexOf("$") > -1) && (str.indexOf(".") > -1 || str.indexOf(",") > -1));
            // Verificamos se existe máscara
            if (hasMask) {
                // Retiramos a mascara.
                str = str.replaceAll("[R$]", "").replaceAll("[$]", "").replaceAll("[.]", "").replaceAll("[,]", ".");
            }
            // Transformamos o número que está escrito no EditText em double.
            retorno = Double.parseDouble(str);

        } catch (NumberFormatException e) {

        }
        return retorno;

    }
}

package com.duomizhibo.phonelive.widget.nicespinner;

import android.text.Spannable;

public interface SpinnerTextFormatter<T> {

    Spannable format(T item);
}

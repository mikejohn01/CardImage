package com.example.glushkovskiy.cardimage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by glushkovskiy on 31.03.2017.
 */

public class ImageViewL extends ImageView{

    int letter;

    public ImageViewL(Context context) {
        super(context);
    }

    public ImageViewL(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewL(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getLetter() {
        return letter;
    }

    public void setLetter(int letter) {
        this.letter = letter;
    }
}

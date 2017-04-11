package com.example.glushkovskiy.cardimage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "myLogs";
    private static Integer lastLetter = 0;
    private static Integer nowLetter = 0;
    final Random rnd = new Random();
    private static int i = 0;
    private static int j = 0;
    TableLayout tb_Layout;
    Button bn_1, bn_2, bn_3, bn_4; //кнопки установки размеров поля
    ArrayList<TableRow> ArrListRows = new ArrayList<>(); //лист рядов
    ArrayList<ImageViewL> ArrListCard = new ArrayList<>(); //лист ImageView
    ArrayList<Integer> ArrListID_View = new ArrayList<>(); //лист номеров карточек

    //если не делать в кастомном классе ImageView вложенный класс карточек, а попробовать идти через массив картинок с буквами:
    ArrayList<Integer> ArrListLetter = new ArrayList<> (Arrays.asList(new Integer [] {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f,
            R.drawable.g, R.drawable.h, R.drawable.i, R.drawable.j, R.drawable.k, R.drawable.l, R.drawable.m, R.drawable.n, R.drawable.o, R.drawable.v} ));
    ArrayList<Integer> ArrListLetter_used = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tb_Layout = (TableLayout) findViewById(R.id.tableLayout);

        bn_1 = (Button) findViewById(R.id.button);
        bn_2 = (Button) findViewById(R.id.button2);
        bn_3 = (Button) findViewById(R.id.button3);
        bn_4 = (Button) findViewById(R.id.button4);

        bn_1.setOnClickListener(this);
        bn_2.setOnClickListener(this);
        bn_3.setOnClickListener(this);
        bn_4.setOnClickListener(this);

        createTable(2, 2);
    }

    public void createTable(int countX, int countY) {
        ArrListRows.clear();
        ArrListCard.clear();
        ArrListID_View.clear();
        int index = 0;
        int half = (countX * countY) / 2;
        TableRow.LayoutParams lParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int y = 0; y < countY; y++) {
            ArrListRows.add(new TableRow(this));
            for (int x = 0; x < countX; x++) {
                ArrListCard.add(new ImageViewL(this));
                int temp = generateViewId(); //записываем номер imageView в переменную temp
                ArrListCard.get(index).setId(temp);
                ArrListCard.get(index).setOnClickListener(this);
                ArrListCard.get(index).setImageResource(R.drawable.out);
                //сетим в ImageView номер буквы: Проверяем, что поле letter == null, и потом сетим букву, и так в половину ячеек.
                setImageLetter(ArrListCard.get(index), index, half);

                ArrListID_View.add(temp); //записываем id ImageView в лист

                ArrListRows.get(y).addView(ArrListCard.get(index), lParams);
                index++;
            }
            tb_Layout.addView(ArrListRows.get(y), lParams);
        }
    }

    private void setImageLetter (ImageViewL imL, int index, int half) {
        if (i < half) {
            if (ArrListCard.get(index).letter == 0) {
                Integer let = ArrListLetter.get(rnd.nextInt(15));
                ArrListCard.get(index).setLetter(let);
                // Использованные буквы записываем в массив. Потом теми же буквами (их тоже в еще один массив) заполняем оставшиеся ячейки.
                ArrListLetter_used.add(let);
                Log.d(TAG, "ArrListLetter_used" + ArrListLetter_used);
            }
            i++;
        } else if (j<half){
            //сетим 2-ю половину карточек.
            if (ArrListCard.get(index).letter == 0) {
                int let2 = ArrListLetter_used.get(j);
                //Log.d(TAG, "let2" + let2);
                ArrListCard.get(index).setLetter(let2);
            };
            j++;
        }
    }

    public synchronized int generateViewId() {
        Random rand = new Random();
        int id;
        while (findViewById(id = rand.nextInt(Integer.MAX_VALUE) + 1) != null);
        return id;
    }

    @Override
    public void onClick(final View v) {
        if (ArrListID_View.contains(v.getId())) {   //Contains сравнивает значение массива с id с id нажатого элемента.
            //Если в массиве такой id есть, то пишем лог.
            //Log.d(TAG, "Press id_iv = " + v.getId());
            //запмсываем букву нажатой карточки:
            nowLetter = ArrListCard.get(ArrListID_View.indexOf(v.getId())).getLetter();
            Log.d(TAG, "nowLetter=" + nowLetter);

            //показываем букву
            ArrListCard.get(ArrListID_View.indexOf(v.getId())).setImageResource(nowLetter);

            //проверка на совпадение
            control(nowLetter, lastLetter, v, ArrListCard);

            lastLetter = nowLetter;
        }

        switch (v.getId()) {
            case R.id.button:
                Log.d(TAG, "Press button");
                tb_Layout.removeAllViews();
                createTable(2, 2);
                break;
            case R.id.button2:
                Log.d(TAG, "Press button2");
                tb_Layout.removeAllViews();
                createTable(2, 3);
                break;
            case R.id.button3:
                Log.d(TAG, "Press button3");
                tb_Layout.removeAllViews();
                createTable(4, 4);
                break;
            case R.id.button4:
                Log.d(TAG, "Press button4");
                tb_Layout.removeAllViews();
                //createTable(5, 5);
                break;
        }
    }

    private void control (Integer nowLetter, Integer lastLetter, View v, ArrayList<ImageViewL> ArrListCard) {
        if (nowLetter.equals(lastLetter)) {
            ArrListCard.get(ArrListID_View.indexOf(v.getId())).setImageResource(R.drawable.win);

            //сделать переворачивание прошлой карточки

//        выполняется слишком быстро!
                } else {
            delay(v, ArrListCard);
            //ArrListCard.get(ArrListID_View.indexOf(v.getId())).setImageResource(R.drawable.out);
        }
    }

    private void delay (final View v, final ArrayList<ImageViewL> ArrListCard) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ArrListCard.get(ArrListID_View.indexOf(v.getId())).setImageResource(R.drawable.out);
            }
        }, 1000);
    }

}
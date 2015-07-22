package com.yellow.evgeny.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

//С MainActivity начинается выполнение любой аппликации. В этом классе содержится от одного(по
//умолчанию) до нескольких фрагментов,выполняющих функциональность.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//две вспомогательные колбэк функции(обратный вызов) - часть ассинхронных действий
    @Override //построение меню
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//в инфлатер передаётся ресурс menu_main и
        //говорим какой объект -menu меню туда положить
        return true;
    }
//реакция на нажатие выбранного элемента меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

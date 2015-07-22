package com.yellow.evgeny.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    //============================================================
   //На уровне фрагмента есть ф-ция, кот отслеживает когда началось создание самого фрагмента ещё до
    //того,как мы обратились к Активити
    //везде где написано Override означает, что функция принадлежит Отцовскому классу,Чтобы пере
    //определить - ctrl+O . И внутри вызываем ф-цию фрагмента setHasOptionsMenu(), кот на уровне
    //отцовск.класса выставляет флаг (true) что внутри фрагмента тоже есть меню и нужно пройти по
    //колбэкам кот связаны с опциональным меню
    @Override
    public void onCreate(Bundle savedInstanceState) {//(видео-запись 17_07_15_sdv_0435<24:25>)
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    //две вспомогательные колбэк функции(обратный вызов) - часть ассинхронных действий
    @Override //построение меню. Создаётся фрагмент, во фрагменте будет получен меню,меню инфлатер от
    //активити,используя их надо дописать в сущ. меню один айтем,кот находится в menu_main.xml
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_fragment_forecast, menu);//в инфлатер передаётся ресурс menu_main и
        //говорим какой объект -menu меню туда положить
        super.onCreateOptionsMenu(menu, inflater);
    }
    //реакция на нажатие выбранного элемента меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//объект item описан внутри xml item
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();//этот id получаем из item с пом. getItemId
       if(id==R.id.action_refresh){//статическая переменная action_refresh прописанная внутри
           //xml из стат.класса id, в статич. классе R, сравнивается с полученной из item, если
           //они совпадают вызываем executeTask  и делаем return, в super не идём и refresh не
           //отлавливаем
           new FetchWeatherTask().execute();
           return true; //(видео-запись 17_07_15_sdv_0435<26:25>)
       }
        return super.onOptionsItemSelected(item);//возвращаемся в суперкласс и проходим по колбэк
        //пока не отловим нужный айтем item(видео-запись от 14_07_15_sdv_0434<47:03>)
    }

   //===============================================================
//Эта функция перезаписывает ф-цию базового класса Fragment,  здесь создаётся реальный объект
        //фрагмента с помощью вспомогат эл-а inflater получающего в качестве параметра имя
        // конкретного лейаута - R.layout.fragment_main - это декларативный ресурс в виде тексового
        //файла, необрабатываемого компилятором в бинарную информацию
        //"R."- Resurse дополнительный статический класс создаваемый в структ. проекта при работе
        // аппликации, и кот. может содержать доп статические классы  в зависимости от папок входящих
        // в директор. resources
        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            String[] forecastArray = {
                    "Today - Sunny - 88 / 53",
                    "Tomorrow - Foggy - 70 / 46",
                    "Wed - Cloudy - 72 / 63",
                    "Thu - Rainy - 64 / 51",
                    "Fri - Foggy - 70 / 46",
                    "Sat - Sunny - 76 / 68",

            };

            ArrayAdapter<String> forecastAdapter = new ArrayAdapter<String>(
                    //создаём контекст
                    getActivity(), //передаем активити (в данном случае MainActivity) в кот лежит
                    //фрагмент
                    R.layout.list_item_forecast, //дальше передаём layout в кот лежит элемент наз
                    //list_item_forecast
                    R.id.list_item_forecast_txtvw,//передаем текствью по id кот наз list_item_forecast_txtvw
                    forecastArray);//передаём набор роуинформации

            //Этот ArrayAdapter передаём визуальному элементу кот наз ListView   //listview_forecast//
            ListView forcastList = (ListView)rootView.findViewById(R.id.listview_forecast);

            //Создаём объект класса FetchWeatherTask для создания нашего запроса(doInBackground()) и его запуска
            //new FetchWeatherTask().execute();



            forcastList.setAdapter(forecastAdapter);
            return rootView;
        }

    //создаём стат. класс расщиряющий стандартный абстрактный класс AsyncTask для созд. запроса содержащий параметры
    //запроса
    public static class FetchWeatherTask extends AsyncTask<Void,Void,Void>{//<Void,Void,Void>дженерик типы<входные дан,
    //тип данных кот мы хотим передать в прогресс , тип данных на выходе >(видео-запись 17_07_15_sdv_0435<5:59>)

        private static final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
        @Override
        protected Void doInBackground(Void... params) {//(Void... params)-это подставляется из <Void,Void,Void> при имплементации
            //AsyncTask. Многоточие означает тип данных "неявный массив"
        //эту функцию мы обязаны переопределить при создании класса FetchWeatherTask  из абстрактного класса AsyncTask
            //а затем результаты её работы передать в GUI(MainThread), но чтобы синхронизироваться с ним надо переопределить
            //ещё три функции-колбэка(видео-запись 17_07_15_sdv_0435<14:25>): 1)onPreExecute();например ProgressBar вывести
            // в ноль ло начала действия doInBackground.   2)onProgressUpdate(); индикация на ProgressBar это второй элемент
            //в AsyncTask<Void,"int или long",Void> обычно int  или long это % от выполненого действия. 3)onPostExecute(String[]);
            //это третий элемент в AsyncTask<Void,"int или long",String[]> в нашем случае массив стрингов String[]
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                 URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=Rehovot,il&mode=json&units=metric&cnt=7");

               // URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                forecastJsonStr = buffer.toString();//переводим бафер в стринг ихотим показать его
                //в логере после присвоения переменной forecastJsonStr

                Log.v(LOG_TAG, "Forecast JASON string is:" + forecastJsonStr );
                //видео-запись от 14_07_15_img_0949.mov<3:49>
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);

                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.

                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return null;
        }
    }
    }

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
   //�� ������ ��������� ���� �-���, ��� ����������� ����� �������� �������� ������ ��������� ��� ��
    //����,��� �� ���������� � ��������
    //����� ��� �������� Override ��������, ��� ������� ����������� ���������� ������,����� ����
    //���������� - ctrl+O . � ������ �������� �-��� ��������� setHasOptionsMenu(), ��� �� ������
    //�������.������ ���������� ���� (true) ��� ������ ��������� ���� ���� ���� � ����� ������ ��
    //�������� ��� ������� � ������������ ����
    @Override
    public void onCreate(Bundle savedInstanceState) {//(�����-������ 17_07_15_sdv_0435<24:25>)
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    //��� ��������������� ������ �������(�������� �����) - ����� ������������ ��������
    @Override //���������� ����. �������� ��������, �� ��������� ����� ������� ����,���� �������� ��
    //��������,��������� �� ���� �������� � ���. ���� ���� �����,��� ��������� � menu_main.xml
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_fragment_forecast, menu);//� �������� ��������� ������ menu_main �
        //������� ����� ������ -menu ���� ���� ��������
        super.onCreateOptionsMenu(menu, inflater);
    }
    //������� �� ������� ���������� �������� ����
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//������ item ������ ������ xml item
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();//���� id �������� �� item � ���. getItemId
       if(id==R.id.action_refresh){//����������� ���������� action_refresh ����������� ������
           //xml �� ����.������ id, � ������. ������ R, ������������ � ���������� �� item, ����
           //��� ��������� �������� executeTask  � ������ return, � super �� ��� � refresh ��
           //�����������
           new FetchWeatherTask().execute();
           return true; //(�����-������ 17_07_15_sdv_0435<26:25>)
       }
        return super.onOptionsItemSelected(item);//������������ � ���������� � �������� �� ������
        //���� �� ������� ������ ����� item(�����-������ �� 14_07_15_sdv_0434<47:03>)
    }

   //===============================================================
//��� ������� �������������� �-��� �������� ������ Fragment,  ����� �������� �������� ������
        //��������� � ������� ��������� ��-� inflater ����������� � �������� ��������� ���
        // ����������� ������� - R.layout.fragment_main - ��� ������������� ������ � ���� ���������
        //�����, ����������������� ������������ � �������� ����������
        //"R."- Resurse �������������� ����������� ����� ����������� � ������. ������� ��� ������
        // ����������, � ���. ����� ��������� ��� ����������� ������  � ����������� �� ����� ��������
        // � ��������. resources
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
                    //������ ��������
                    getActivity(), //�������� �������� (� ������ ������ MainActivity) � ��� �����
                    //��������
                    R.layout.list_item_forecast, //������ ������� layout � ��� ����� ������� ���
                    //list_item_forecast
                    R.id.list_item_forecast_txtvw,//�������� �������� �� id ��� ��� list_item_forecast_txtvw
                    forecastArray);//������� ����� �������������

            //���� ArrayAdapter ������� ����������� �������� ��� ��� ListView   //listview_forecast//
            ListView forcastList = (ListView)rootView.findViewById(R.id.listview_forecast);

            //������ ������ ������ FetchWeatherTask ��� �������� ������ �������(doInBackground()) � ��� �������
            //new FetchWeatherTask().execute();



            forcastList.setAdapter(forecastAdapter);
            return rootView;
        }

    //������ ����. ����� ����������� ����������� ����������� ����� AsyncTask ��� ����. ������� ���������� ���������
    //�������
    public static class FetchWeatherTask extends AsyncTask<Void,Void,Void>{//<Void,Void,Void>�������� ����<������� ���,
    //��� ������ ��� �� ����� �������� � �������� , ��� ������ �� ������ >(�����-������ 17_07_15_sdv_0435<5:59>)

        private static final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
        @Override
        protected Void doInBackground(Void... params) {//(Void... params)-��� ������������� �� <Void,Void,Void> ��� �������������
            //AsyncTask. ���������� �������� ��� ������ "������� ������"
        //��� ������� �� ������� �������������� ��� �������� ������ FetchWeatherTask  �� ������������ ������ AsyncTask
            //� ����� ���������� � ������ �������� � GUI(MainThread), �� ����� ������������������ � ��� ���� ��������������
            //��� ��� �������-�������(�����-������ 17_07_15_sdv_0435<14:25>): 1)onPreExecute();�������� ProgressBar �������
            // � ���� �� ������ �������� doInBackground.   2)onProgressUpdate(); ��������� �� ProgressBar ��� ������ �������
            //� AsyncTask<Void,"int ��� long",Void> ������ int  ��� long ��� % �� ����������� ��������. 3)onPostExecute(String[]);
            //��� ������ ������� � AsyncTask<Void,"int ��� long",String[]> � ����� ������ ������ �������� String[]
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

                forecastJsonStr = buffer.toString();//��������� ����� � ������ ������ �������� ���
                //� ������ ����� ���������� ���������� forecastJsonStr

                Log.v(LOG_TAG, "Forecast JASON string is:" + forecastJsonStr );
                //�����-������ �� 14_07_15_img_0949.mov<3:49>
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

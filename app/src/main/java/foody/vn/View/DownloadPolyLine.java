package foody.vn.View;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadPolyLine extends AsyncTask<String, Void, String> {
    StringBuffer stringBuffer = new StringBuffer();
    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stringBuffer.toString();
    }

    @Override
    protected void onPostExecute(String dataJSON) {
        super.onPostExecute(dataJSON);
    }
}

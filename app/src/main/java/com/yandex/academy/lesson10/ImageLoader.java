package com.yandex.academy.lesson10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

class ImageLoader {

    private List<String> mImageUrls = new ArrayList<>();

    @Nullable
    String getImageUrl() {
        final List<String> imageUrls = getImageUrls();
        if (imageUrls.isEmpty() == false) {
            final int index = new Random().nextInt(mImageUrls.size());
            return imageUrls.get(index);
        } else {
            return null;
        }
    }

    @NonNull
    private List<String> getImageUrls() {
        if (mImageUrls.isEmpty()) {
            try {
                final Calendar calendar = Calendar.getInstance();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                final String formattedDate = dateFormat.format(calendar.getTime());

                final String stringUrl = "http://api-fotki.yandex.ru/api/podhistory/poddate;" + formattedDate + "T12:00:00Z/?limit=100";
                final URL url = new URL(stringUrl);
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    final InputStream stream = connection.getInputStream();
                    final XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(stream, null);
                    String imgUrl;
                    while ((imgUrl = processEntry(parser)) != null) {
                        mImageUrls.add(imgUrl);
                    }
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }

        return mImageUrls;
    }

    @Nullable
    private String processEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        //https://issues.jenkins-ci.org/browse/JENKINS-14502
        int eventType = parser.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG
                    && "img".equals(parser.getName())) {
                for (int i = 1; i < parser.getAttributeCount(); i++) {
                    if ("size".equals(parser.getAttributeName(i))) {
                        if ("XXXL".equals(parser.getAttributeValue(i))) {
                            return parser.getAttributeValue(i-1);
                        }
                    }
                }
            }
            eventType = parser.next();
        }

        return null;
    }

    @Nullable
    Bitmap loadBitmap(final String srcUrl) {
        try {
            URL url = new URL(srcUrl);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] bytes = buffer.toByteArray();
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

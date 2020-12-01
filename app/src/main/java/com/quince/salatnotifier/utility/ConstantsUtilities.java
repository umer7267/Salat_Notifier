package com.quince.salatnotifier.utility;

public class ConstantsUtilities {

    public static final String CHANNEL_ID = "first";
    public static final String NOTI_CHANNEL_NAME = "Salat Notifications";
    public static final String NOTI_CHANNEL_DESC = "This channel is used to deliver notifications of the Salat Time, so User don't forget to pray Salat in their Near by Mosque";

    public static final String BASE_NEARBY_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    public static final String API_KEY = "AIzaSyC8QGoOyFmhCS2dfD65wXZAl9XlkgWmRAM";
    public static final int RADIUS = 1500; //1.5 KM
    public static final String TYPE = "mosque";

    public static final String NAMAZ_TIMING = "https://api.aladhan.com/v1/timingsByAddress";

    public static final String SURAH_LIST_BASE = "https://api.alquran.cloud/v1/surah";
    public static final String SURAH_WITH_TRANSLATION = "/editions/quran-simple,ur.jalandhry";

    public static final String API_BASE_URL = "https://alquran.coderpakistan.tk/public/api/";
    public static final String IMG_BASE_URL = "https://alquran.coderpakistan.tk/public/uploads/images/";
    public static final String BOOK_BASE_URL = "https://alquran.coderpakistan.tk/public/uploads/books/";
}

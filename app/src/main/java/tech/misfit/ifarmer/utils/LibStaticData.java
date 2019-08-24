package tech.misfit.ifarmer.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LibStaticData {

    public static final DateFormat monthFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);

    /*Normal Request*/
    public static final int IMAGE_CAPTURE_PERMISSION = 300;
    public static final int GALLERY_IMAGE_REQUEST = 301;
    public static final int WRITE_EXTERNAL_STORAGE = 302;
    public static final int WRITE_EXTERNAL_STORAGE_FOR_GALLERY = 303;

    /*Run Time Permission code*/
    public static final int CAMERA_REQUEST = 400;

    public static final long SPLASH_DURATION = 2 * 1000;

}

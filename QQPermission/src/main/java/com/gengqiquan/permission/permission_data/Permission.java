package com.gengqiquan.permission.permission_data;

/**
 * Permission
 *
 * @author panfei.pf
 * @since 2021/3/18 10:50
 */
public class Permission {

    public static final String CAMERA = "android.permission.CAMERA";

    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    public static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";

    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    public static final String ACCESS_BACKGROUND_LOCATION = "android.permission.ACCESS_BACKGROUND_LOCATION";

    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";

    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String CALL_PHONE = "android.permission.CALL_PHONE";
    public static final String READ_PHONE_NUMBERS = "android.permission.READ_PHONE_NUMBERS";
    public static final String ANSWER_PHONE_CALLS = "android.permission.ANSWER_PHONE_CALLS";

    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    public static final class Group {

        public static final String[] CAMERA = new String[]{Permission.CAMERA};

        public static final String[] CONTACTS = new String[]{Permission.READ_CONTACTS, Permission.WRITE_CONTACTS, Permission.GET_ACCOUNTS};

        public static final String[] LOCATION = new String[]{Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION,
                Permission.ACCESS_BACKGROUND_LOCATION};

        public static final String[] MICROPHONE = new String[]{Permission.RECORD_AUDIO};

        public static final String[] PHONE = new String[]{Permission.READ_PHONE_STATE,
                Permission.READ_PHONE_NUMBERS, Permission.ANSWER_PHONE_CALLS};

        public static final String[] CALL_PHONE = new String[] {Permission.CALL_PHONE};

        public static final String[] STORAGE = new String[]{Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE};
    }

}

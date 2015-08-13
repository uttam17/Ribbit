package com.uttamapps.ribbit;

/**
 * Created by Uttam Kumaran on 8/8/2015.
 */
public final class ParseConstants {
    //Class names
    public static final String CLASS_MESSAGES = "Messages"; //classes begin with capital letters and field with lowercase

    //Field names
    public static final String KEY_USERNAME = "username";  //public can be accessed anywhere, static can be accesed without a parseConstant object, final can not be modified
    public static final String KEY_FRIENDS_RELATION = "friendsRelation";
    public static final String KEY_RECIPIENTS_IDS = "recipientsIds";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_FILE = "file";
    public static final String KEY_FILE_TYPE = "fileType";

    //Misc Values
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_VIDEO = "video";
}

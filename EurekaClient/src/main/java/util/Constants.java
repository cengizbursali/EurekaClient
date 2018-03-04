package util;

public class Constants {
    public static final String COLLECTION_NAME = "numbers";
    public static final String DATABASE_URL = "mongodb://admin:admin@ds151508.mlab.com:51508/numberdb";

    public static final String PROPERTY_NUMBER = "number";
    public static final String PROPERTY_INSERT_DATE = "insert_date";

    public static final String ORDER_ASCENDING = "ascending";
    public static final String ORDER_DESCENDING = "descending";

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String MESSAGE_UNIQUE = "This number already exists!";
    public static final String MESSAGE_NOT_FOUND = "Could not find the number you are trying to delete!";

    public static final String MESSAGE_SUCCESS_INSERT = "Number has been inserted successfully!";
    public static final String MESSAGE_SUCCESS_DELETE = "Number has been deleted successfully!";
}

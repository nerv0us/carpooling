package com.telerik.carpoolingapplication.models.constants;

public class Constants {
    // Trip constants
    public static final String NOT_AVAILABLE_TRIPS = "There are no trips available!";
    public static final String TRIP_CREATED = "Trip created!";
    public static final String TRIP_UPDATED = "Trip updated!";
    public static final String UNAUTHORIZED = "Unauthorized!";
    public static final String INVALID_ID_SUPPLIED = "Invalid ID supplied!";
    public static final String TRIP_NOT_FOUND = "Trip not found!";
    public static final String NO_SUCH_STATUS = "Status doesn't exist!";
    public static final String COMMENT_ADDED = "Comment added!";
    public static final String APPLIED = "You have successfully applied for this trip";
    public static final String YOUR_OWN_TRIP = "You cannot apply for your own trip";
    public static final String TRIP_STATUS_CHANGED = "Trip status changed!";
    public static final String UNAUTHORIZED_MESSAGE = "Not authorized!";
    public static final String ALREADY_APPLIED = "You have already applied for this trip!";
    public static final String PASSENGER_STATUS_CHANGED = "Passenger status changed!";
    public static final String YOU_DO_NOT_PARTICIPATE = "You do not participate in this trip!";
    public static final String TRIP_NOT_FINISHED = "You cannot add comments before trip is finished!!";
    public static final String NO_SUCH_PASSENGER = "Passenger does not participate in this trip!";
    public static final String RATING_NOT_ALLOWED_BEFORE_TRIP_IS_DONE = "You cannot rate before trip is over!";
    public static final String NOT_A_NUMBER = "Value is not a number!";
    public static final String NOT_A_BOOLEAN = "Value is not a boolean";
    public static final String ILLEGAL_VALUE = "Illegal sorting value!";

    // User constants
    public static final String USER_NOT_FOUND = "User with ID %d not found!";
    public static final String USERNAME_NOT_FOUND = "User with username %s not found!";
    public static final String USERNAME_ALREADY_EXIST = "User with username %s already exist!";
    public static final String EMAIL_ALREADY_EXIST = "Email %s already exist";
    public static final String USER_CREATED = "User created!";
    public static final String USER_UPDATED = "User updated!";
    public static final String DRIVER_RATED = "You have successfully rated this driver!";
    public static final String PASSENGER_RATED = "You have successfully rated this passenger!";
    public static final String DEFAULT_USER_IMAGE_NAME = "defaultUserPhoto";
    public static final String USERNAME_CANNOT_BE_CHANGED_MESSAGE = "Username cannot be changed!";
    public static final String RATE_YOURSELF = "You cannot rate yourself!";

    // Storage constants
    public static final String FAILED_TO_STORE_FILE_MESSAGE = "Failed to store file";
    public static final String STORAGE_ROUTE = "/tmp/images/users/";
    public static final int MAX_FILE_SIZE = 5242880;
}

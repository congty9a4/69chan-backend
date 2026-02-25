package com.congty9a4.backend.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


// add more!
@Getter
public enum ErrorCode {
    // --- BAD REQUEST (400) ---

    // General
    INVALID_INPUT(40000, "Invalid input. Please check your data", HttpStatus.BAD_REQUEST),

    // Authentication & User
    INVALID_CREDENTIALS(40001, "Invalid credentials. Please check your username and password.", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTS(40010, "A user with this username or email already exists.", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(40011, "A user with this email address already exists.", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_ALREADY_EXISTS(40012, "A user with this phone number already exists.", HttpStatus.BAD_REQUEST),
    SAME_PASSWORD(40013, "Your new password cannot be the same as your old password.", HttpStatus.BAD_REQUEST),
    ACCOUNT_BLOCKED(40014, "Your account has been blocked. Please contact support for assistance.", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT(40015, "The email address you entered is not valid.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(40016, "The user you are looking for could not be found.", HttpStatus.BAD_REQUEST),


    // Post & Content
    POST_NOT_FOUND(40030, "The post you are looking for could not be found.", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_FOUND(40031, "The comment you are looking for could not be found.", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_POST(40032, "You do not have permission to delete this post.", HttpStatus.BAD_REQUEST),
    CANNOT_EDIT_POST(40033, "You do not have permission to edit this post.", HttpStatus.BAD_REQUEST),

    // Profile
    PROFILE_NOT_FOUND(40040, "The profile you are looking for could not be found.", HttpStatus.BAD_REQUEST),
    PROFILE_ALREADY_EXISTS(40041, "A profile already exists for this user.", HttpStatus.BAD_REQUEST),
    PROFILE_FULLNAME_ALREADY_EXISTS(40042, "This full name is already taken.", HttpStatus.BAD_REQUEST),

    // File Upload
    FILE_TOO_LARGE(40050, "The uploaded file is too large. Please upload a smaller file.", HttpStatus.BAD_REQUEST),
    INVALID_FILE_TYPE(40051, "The uploaded file type is not supported.", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(40052, "File upload failed. Please try again.", HttpStatus.BAD_REQUEST),
    FILE_DELETE_FAILED(40053, "File deletion failed. Please try again.", HttpStatus.BAD_REQUEST),
    // Friends & Relationships
    FRIEND_REQUEST_ALREADY_SENT(40060, "You have already sent a friend request to this user.", HttpStatus.BAD_REQUEST),
    ALREADY_FRIENDS(40061, "You are already friends with this user.", HttpStatus.BAD_REQUEST),
    FRIEND_REQUEST_NOT_FOUND(40062, "Friend request not found.", HttpStatus.BAD_REQUEST),
    USER_BLOCKED(40063, "This user is blocked.", HttpStatus.BAD_REQUEST),
    CANNOT_INTERACT_WITH_BLOCKED_USER(40064, "You cannot interact with a user you have blocked or who has blocked you.", HttpStatus.BAD_REQUEST),


    // --- UNAUTHENTICATED (401) ---
    UNAUTHENTICATED(40100, "You must be logged in to perform this action.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(40101, "Invalid or expired token. Please log in again.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN(40102, "Your session has expired. Please log in again.", HttpStatus.UNAUTHORIZED),

    // --- FORBIDDEN (403) ---
    FORBIDDEN(40300, "You do not have permission to access this resource.", HttpStatus.FORBIDDEN),

    // --- NOT FOUND (404) ---
    RESOURCE_NOT_FOUND(40400, "No endpoint found for this request", HttpStatus.NOT_FOUND),
    VERIFICATION_TOKEN_NOT_FOUND(40404, "The verification token is invalid or has expired.", HttpStatus.NOT_FOUND);


    private final int code;
    private final String detailedMessage;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.detailedMessage = message;
        this.statusCode = statusCode;
    }

}

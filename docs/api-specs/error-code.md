# API Error Code Specifications

This document describes all standardized error codes returned by the API. Each error includes a numeric code, HTTP status, and a human-readable message.

---

## Error Code Table

| Enum Name | Code | HTTP Status | Status Code | Message |
|---|---|---|---|---|
| **GENERAL** |||||
| `INVALID_INPUT` | 40000 | 400 Bad Request | `BAD_REQUEST` | Invalid input. Please check your data. |
| **AUTHENTICATION & USER** |||||
| `INVALID_CREDENTIALS` | 40001 | 400 Bad Request | `BAD_REQUEST` | Invalid credentials. Please check your username and password. |
| `USER_ALREADY_EXISTS` | 40010 | 400 Bad Request | `BAD_REQUEST` | A user with this username or email already exists. |
| `EMAIL_ALREADY_EXISTS` | 40011 | 400 Bad Request | `BAD_REQUEST` | A user with this email address already exists. |
| `PHONE_NUMBER_ALREADY_EXISTS` | 40012 | 400 Bad Request | `BAD_REQUEST` | A user with this phone number already exists. |
| `SAME_PASSWORD` | 40013 | 400 Bad Request | `BAD_REQUEST` | Your new password cannot be the same as your old password. |
| `ACCOUNT_BLOCKED` | 40014 | 400 Bad Request | `BAD_REQUEST` | Your account has been blocked. Please contact support for assistance. |
| `INVALID_EMAIL_FORMAT` | 40015 | 400 Bad Request | `BAD_REQUEST` | The email address you entered is not valid. |
| `USER_NOT_FOUND` | 40016 | 400 Bad Request | `BAD_REQUEST` | The user you are looking for could not be found. |
| `GOOGLE_TOKEN_INVALID` | 40017 | 400 Bad Request | `BAD_REQUEST` | The Google token is invalid or expired. |
| **POST & CONTENT** |||||
| `POST_NOT_FOUND` | 40030 | 400 Bad Request | `BAD_REQUEST` | The post you are looking for could not be found. |
| `COMMENT_NOT_FOUND` | 40031 | 400 Bad Request | `BAD_REQUEST` | The comment you are looking for could not be found. |
| `CANNOT_DELETE_POST` | 40032 | 400 Bad Request | `BAD_REQUEST` | You do not have permission to delete this post. |
| `CANNOT_EDIT_POST` | 40033 | 400 Bad Request | `BAD_REQUEST` | You do not have permission to edit this post. |
| **PROFILE** |||||
| `PROFILE_NOT_FOUND` | 40040 | 400 Bad Request | `BAD_REQUEST` | The profile you are looking for could not be found. |
| `PROFILE_ALREADY_EXISTS` | 40041 | 400 Bad Request | `BAD_REQUEST` | A profile already exists for this user. |
| `PROFILE_FULLNAME_ALREADY_EXISTS` | 40042 | 400 Bad Request | `BAD_REQUEST` | This full name is already taken. |
| **FILE UPLOAD** |||||
| `FILE_TOO_LARGE` | 40050 | 400 Bad Request | `BAD_REQUEST` | The uploaded file is too large. Please upload a smaller file. |
| `INVALID_FILE_TYPE` | 40051 | 400 Bad Request | `BAD_REQUEST` | The uploaded file type is not supported. |
| `FILE_UPLOAD_FAILED` | 40052 | 400 Bad Request | `BAD_REQUEST` | File upload failed. Please try again. |
| `FILE_DELETE_FAILED` | 40053 | 400 Bad Request | `BAD_REQUEST` | File deletion failed. Please try again. |
| **FRIENDS & RELATIONSHIPS** |||||
| `FRIEND_REQUEST_ALREADY_SENT` | 40060 | 400 Bad Request | `BAD_REQUEST` | You have already sent a friend request to this user. |
| `ALREADY_FRIENDS` | 40061 | 400 Bad Request | `BAD_REQUEST` | You are already friends with this user. |
| `FRIEND_REQUEST_NOT_FOUND` | 40062 | 400 Bad Request | `BAD_REQUEST` | Friend request not found. |
| `USER_BLOCKED` | 40063 | 400 Bad Request | `BAD_REQUEST` | This user is blocked. |
| `CANNOT_INTERACT_WITH_BLOCKED_USER` | 40064 | 400 Bad Request | `BAD_REQUEST` | You cannot interact with a user you have blocked or who has blocked you. |
| `ALREADY_FOLLOWING` | 40065 | 400 Bad Request | `BAD_REQUEST` | You are already following this user. |
| **CHAT & MESSAGING** |||||
| `MESSAGE_NOT_FOUND` | 40070 | 400 Bad Request | `BAD_REQUEST` | The message you are looking for could not be found. |
| `CONVERSATION_ALREADY_EXISTS` | 40071 | 400 Bad Request | `BAD_REQUEST` | A conversation between these users already exists. |
| `CANNOT_SEND_MESSAGE` | 40072 | 400 Bad Request | `BAD_REQUEST` | You cannot send a message to this user. |
| `CANNOT_ACCESS_CONVERSATION` | 40073 | 400 Bad Request | `BAD_REQUEST` | You do not have permission to access this conversation. |
| `CONVERSATION_NOT_FOUND` | 40074 | 400 Bad Request | `BAD_REQUEST` | The conversation you are looking for could not be found. |
| **UNAUTHENTICATED (401)** |||||
| `UNAUTHENTICATED` | 40100 | 401 Unauthorized | `UNAUTHORIZED` | You must be logged in to perform this action. |
| `INVALID_TOKEN` | 40101 | 401 Unauthorized | `UNAUTHORIZED` | Invalid or expired token. Please log in again. |
| `EXPIRED_TOKEN` | 40102 | 401 Unauthorized | `UNAUTHORIZED` | Your session has expired. Please log in again. |
| **FORBIDDEN (403)** |||||
| `FORBIDDEN` | 40300 | 403 Forbidden | `FORBIDDEN` | You do not have permission to access this resource. |
| **NOT FOUND (404)** |||||
| `RESOURCE_NOT_FOUND` | 40400 | 404 Not Found | `NOT_FOUND` | No endpoint found for this request. |
| `VERIFICATION_TOKEN_NOT_FOUND` | 40404 | 404 Not Found | `NOT_FOUND` | The verification token is invalid or has expired. |

---

## Error Response Structure

All API errors return a consistent JSON response body:

```json
{
  "isSuccess": false,
  "status": 40000,
  "message": "Invalid input. Please check your data",
  "errors": {
    "email": "Not in email format"
  },
  "detail": "My custom message"
}
```

| Field | Type | Description |
|---|---|---|
| `isSuccess` | `boolean` | Always `false` for errors |
| `status` | `integer` | Application-specific numeric error code |
| `message` | `string` | Human-readable description of the error |
| `errors` | `map` | Field-level validation errors, when available |
| `detail` | `string` | Context-specific message |

---

## HTTP Status Code Summary

| HTTP Status | Range | Description |
|---|---|---|
| `400 Bad Request` | `400xx` | Client sent invalid or conflicting data |
| `401 Unauthorized` | `401xx` | Authentication is required or has failed |
| `403 Forbidden` | `403xx` | Authenticated but lacking permission |
| `404 Not Found` | `404xx` | Requested resource or endpoint does not exist |

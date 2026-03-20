# [SPEC] Home Feed

→ Displays sorted list of posts from users that the current user follows by creation time.

## Feature requirements

- Users can view a feed of posts from the users they follow.
- The feed is sorted in reverse chronological order (newest posts first).
- The feed supports cursor-based pagination for infinite scrolling.

## Problem

A user's home feed needs to be populated with content from the people they follow. A simple approach of fetching all posts from all followed users and sorting them can be slow and inefficient, especially as the number of followed users and posts grows. This can lead to a poor user experience with long load times for the home feed.

## Solution

To address the performance issues, we will implement a "fan-out on read" pattern with cursor-based pagination.

1.  **Fan-out on Read**: When a user requests their home feed, the system will:

    * Fetch the list of user IDs that the current user follows.

    * Query the `posts` collection for posts created by these user IDs.

2.  **Cursor-based Pagination**: To enable infinite scrolling and efficient data fetching, we will use a cursor based on the timestamp of the posts.
    *   The initial request will fetch the first page of posts.
    *   Subsequent requests will include a `cursor` (the timestamp of the last post from the previous page) to fetch the next set of posts created before that time.

This approach avoids large joins and complex sorting operations at the database level for every request and provides a scalable way to deliver the home feed.

## Feature’s Diagram

```
+----------+           +------------------+           +-----------------+
|          |           |                  |           |                 |
|  Client  |---------> |  Backend Service |---------> |  Post Database  |
|          |           |                  |           | (MongoDB)       |
+----------+           +------------------+           +-----------------+
     |                        |                                |
     | 1. GET /api/feeds   |                                |
     |    (with/without cursor)|                                |
     |                        |                                |
     |                        | 2. Get followed user IDs       |
     |                        |    for the current user.       |
     |                        |                                |
     |                        | 3. Query posts from followed   |
     |                        |    users, using the cursor     |
     |                        |    if provided.                |
     |                        |                                |
     |                        |                                | 4. Return posts
     |                        |                                |
     |                        | 5. Format and return posts     |
     |                        |    to the client.              |
     |                        |                                |
     |<-----------------------|                                |
     |                        |                                |

```

## Endpoint

### GET  /api/feeds?limit=20&after=2023-10-27T09:55:00

-   **Description**: Get the user's home feed.
-   **Query Parameters**:
    -   `after` (optional, string): An `Instant` value representing the `createdAt` timestamp of the last post from the previous page. If not provided, it fetches the first page.
    -   `limit` (optional, integer, default=20): The number of posts to return per page.
- **Request**:
```http
GET /api/feeds?limit=4   // First page, no cursor
GET /api/feeds?limit=20&after=2023-10-27T09:55:00   // Next page, with cursor
```
- **Success Response (200 OK)**:
    ```json
   {
      "data": [
        {
          "id": "string",
          "tags": [
            "string"
          ],
          "caption": "string",
          "medias": [
            {
              "id": "string",
              "url": "string",
              "type": "string",
              "uploaded_at": "2026-03-19T13:15:23.459Z"
            }
          ],
          "user": {
            "username": "string",
            "avatar": "string",
            "id": "string",
            "fullname": "string"
          },
          "scope": "string",
          "likes": 0,
          "comments": 0,
          "user_liked": true,
          "created_at": "2026-03-19T13:15:23.459Z",
          "updated_at": "2026-03-19T13:15:23.459Z"
        }
      ],
      "page_info": {
        "next_cursor": "string",
        "has_next": true
      }
   }
    ```

# Changelog
- 2024-07-25: Initial draft of the technical specification.


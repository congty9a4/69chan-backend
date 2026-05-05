# [SPEC] Real-time Notifications System

→ Hệ thống cung cấp thông báo tức thời cho người dùng về các tương tác (Like, Comment, Follow, Message) và quản lý trạng thái thông báo với hiệu năng cao.

---

## Feature requirements

- **Real-time Delivery**: Người dùng nhận thông báo ngay lập tức thông qua WebSocket mà không cần reload trang.
- **Persistence**: Thông báo được lưu trữ trong MongoDB để có thể xem lại lịch sử.
- **Unread Tracking**: Sử dụng Redis để lưu số lượng thông báo chưa đọc với độ phức tạp O(1).
- **Online Presence**: Chỉ push WebSocket khi user đang online nhằm tiết kiệm tài nguyên.
- **Mark as Read**: Cho phép cập nhật trạng thái đã đọc và đồng bộ với Redis.
- **Low Latency**: Đảm bảo phản hồi nhanh ngay cả khi số lượng notification lớn.

---

## Problem

Trong hệ thống mạng xã hội như **69chan**, việc triển khai notification gặp các vấn đề:

- **Polling inefficiency**:
  - Client phải liên tục gọi API để kiểm tra thông báo mới.
  - Gây load lớn lên server và database.
  - Trải nghiệm người dùng bị delay.

- **Unread count bottleneck**:
  - Query `count` trực tiếp từ MongoDB chậm khi dữ liệu lớn.
  - Không phù hợp với hệ thống có hàng triệu bản ghi.

---

## Solution

Hệ thống kết hợp ba thành phần chính:

### 1. WebSocket (STOMP)

- Thiết lập kết nối real-time giữa server và client.
- Push notification ngay lập tức nếu user online.

---

### 2. Redis (Caching & Atomic Counter)

- Lưu unread count: `unread_count:{userId}`
- Sử dụng lệnh: `INCR` / `DECR`
- Độ phức tạp: **O(1)** (constant time)
- TTL:
  - Tránh stale data
  - Giảm memory usage

## Feature’s Diagram

```text
+----------+          +------------------+          +-----------------+
|  Sender  |--[API]-->|  Backend Service |---(1)--->|   MongoDB       |
| (Client) |          | (Spring Boot)    |          | (Persist Noti)  |
+----------+          +---------+--------+          +-----------------+
                        |     |
                       (2)   (3)
                        |     |
                        |     v
                        |  +-----------------+
                        |  |      Redis      |
                        |  +-----------------+
                        |
                        v
                  +------------------+          +----------+
                  | WebSocket Broker |---(4)--->| Receiver |
                  | (STOMP)          |          | (Client) |
                  +------------------+          +----------+

1. Save notification to MongoDB.
2. Increment unread count in Redis.
3. Check presence (online/offline).
4. Push notification via WebSocket if online.
Endpoints
1. Get Notifications
GET /api/notifications

Description: Lấy danh sách thông báo của user hiện tại.

Response (200 OK):

{
    "body": [
        {
            "id": "69f41f01601fcb5146b0d3fa",
            "sender": {
                "username": "quockhanh",
                "id": "5100a604-7f90-4199-841c-ba805d0279fb"
            },
            "type": "MESSAGE",
            "targetId": null,
            "message": "sent you a new message.",
            "createdAt": "2026-05-01T10:33:21.044",
            "read": true
        },
    ]
}
2. Get Unread Count
GET /api/notifications/unread-count

Description: Lấy số lượng notification chưa đọc (ưu tiên Redis).

Response (200 OK):

{
  "body": 1,
  "timestamp": "2026-05-01T09:53:17",
  "success": true
}
3. Mark as Read
PATCH /api/notifications/{id}/read

Description: Đánh dấu notification là đã đọc và cập nhật Redis.
```

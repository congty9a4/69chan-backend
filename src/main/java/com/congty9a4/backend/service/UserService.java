package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.req.user.UserCreationRequest;
import com.congty9a4.backend.dto.req.user.UserUpdationRequest;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.UserResponse;
import com.congty9a4.backend.entity.Infochan;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.util.AppPageable;

import java.util.List;
import java.util.UUID;

public interface UserService {

    /**
     * Retrieves a paginated list of all users in the system.
     *
     * @param pageable the pagination parameters including page number, size, and sorting criteria
     */
    PageResponse<List<UserResponse>> getAllUsers(AppPageable pageable);

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier (UUID) of the user
     * @return the {@link Userchan} entity corresponding to the given ID
     */
    Userchan getUserById(UUID id);

    /**
     * Creates a new user in the system.
     *
     * <p>This method processes the user creation request, validates the input data,
     * and persists the new user to the database. The password will be encrypted
     * before storage.</p>
     *
     * @param user the {@link UserCreationRequest} containing user details
     * @return the newly created {@link Userchan} entity
     */
    Userchan createUser(UserCreationRequest user);

    /**
     * Updates an existing user's information.
     *
     * <p>This method updates the user's profile information based on the provided
     * request. Only non-null fields in the request will be updated.</p>
     *
     * @param id the unique identifier (UUID) of the user to update
     * @param user the {@link UserUpdationRequest} containing updated user details
     * @return the updated {@link Userchan} entity
     */
    Userchan updateUser(UUID id, UserUpdationRequest user);

    /**
     * Deletes a user from the system.
     *
     * <p>This operation performs a soft or hard delete based on the application
     * configuration. Related data may be handled according to cascade rules.</p>
     *
     * @param id the unique identifier (UUID) of the user to delete
     * @throws com.congty9a4.backend.exception.error.AppException if the user is not found
     * @throws IllegalArgumentException if id is null
     */
    void deleteUser(UUID id);

    /**
     * Retrieves a user by their email address.
     *
     * <p>This method is commonly used for authentication and user lookup operations.</p>
     *
     * @param email the email address of the user
     * @return the {@link Userchan} entity corresponding to the given email
     * @throws com.congty9a4.backend.exception.error.AppException if the user is not found
     * @throws IllegalArgumentException if email is null or empty
     */
    Userchan getUserByEmail(String email);

    /**
     * Retrieves detailed information about a user.
     *
     * <p>This method returns comprehensive user information including profile details,
     * statistics, and other related data encapsulated in the Infochan entity.</p>
     *
     * @param userId the unique identifier of the user as a String
     * @return the {@link Infochan} entity containing detailed user information
     * @throws com.congty9a4.backend.exception.error.AppException if the user is not found
     * @throws IllegalArgumentException if userId is null or empty
     */
    Infochan userInfo(String userId);

    /**
     * Handles the follow/unfollow action for a user.
     *
     * <p>This method toggles the follow status between the currently authenticated user
     * and the target user specified by the ID. If the current user is already following
     * the target user, this will unfollow them. Otherwise, it will create a new follow
     * relationship.</p>
     *
     * @param targetUserId the unique identifier (UUID) of the user to follow or unfollow
     * @throws com.congty9a4.backend.exception.error.AppException if the target user is not found
     *         or if the user attempts to follow themselves
     * @throws IllegalArgumentException if id is null
     */
    void handleFollow(String targetUserId);

    void handleUnfollow(String id);
}


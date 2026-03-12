package org.example.new_auth.service.impl;

import org.example.new_auth.model.domain.Permission;
import org.example.new_auth.model.domain.User;
import org.example.new_auth.model.dto.response.batch.BatchError;
import org.example.new_auth.model.dto.response.batch.BatchResult;
import org.example.new_auth.model.dto.response.batch.UserItem;
import org.example.new_auth.service.BatchService;
import org.example.new_auth.service.PermissionService;
import org.example.new_auth.service.UserQueryService;
import org.example.new_auth.util.BatchProcessor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.new_auth.util.UserUtils.*;

@Service
public class BatchServiceImpl implements BatchService {

    private final UserQueryService userService;
    private final PermissionService permissionService;
    private final BatchProcessor processor;

    public BatchServiceImpl(UserQueryService userService, PermissionService permissionService, BatchProcessor processor) {
        this.userService = userService;
        this.permissionService = permissionService;
        this.processor = processor;
    }

    @Override
    public BatchResult<User> findUsersByUsernames(List<String> usernames) {
        return processor.batchMap(
                nonNullStream(usernames).toList(), userService::getUserByUsername, Function.identity(), nullExtractor()
        );
    }

    @Override
    public BatchResult<User> findUsersByIds(List<Long> ids) {
        return processor.batchMap(nonNullStream(ids).toList(), userService::getUserById, nullExtractor(), String::valueOf);
    }

    @Override
    public BatchResult<User> grantPermissions(List<String> usernames, List<Permission> permissions) {
        return modifyAndSaveUsers(usernames, user -> permissionService.grantPermissions(user, permissions));
    }

    @Override
    public BatchResult<User> revokeAreas(List<String> usernames, List<String> areas) {
        return modifyAndSaveUsers(usernames, user -> permissionService.revokeAreas(user, areas));
    }

    @Override
    public BatchResult<User> revokeOperations(List<String> usernames, List<String> operations) {
        return modifyAndSaveUsers(usernames, user -> permissionService.revokeOperations(user, operations));
    }

    @Override
    public BatchResult<User> clearPermissions(List<String> usernames) {
        return modifyAndSaveUsers(usernames, permissionService::clearPermissions);
    }

    @Override
    public BatchResult<String> filterUsernamesByAreas(List<String> usernames, List<String> areas) {
        return findByRequired(
                usernames, areas, this::findUsersByUsernames, Permission::area, user -> user.getUsernames().stream()
        );
    }

    @Override
    public BatchResult<Long> filterUserIdsByAreas(List<Long> ids, List<String> areas) {
        return findByRequired(
                ids, areas, this::findUsersByIds, Permission::area, user -> Stream.of(user.getId())
        );
    }

    @Override
    public BatchResult<Long> filterUserIdsByOperations(List<Long> ids, List<String> operations) {
        return findByRequired(
                ids, operations, this::findUsersByIds, Permission::operation, user -> Stream.of(user.getId())
        );
    }

    @Override
    public BatchResult<String> extractAreasByUsernames(List<String> usernames) {
        return findAndExtract(usernames, Permission::area);
    }

    @Override
    public BatchResult<String> extractOperationsByUsernames(List<String> usernames) {
        return findAndExtract(usernames, Permission::operation);
    }

    @Override
    public BatchResult<User> saveUsers(List<User> users) {
        return processor.batchMap(
                nonNullStream(users).toList(), userService::saveUser, nullExtractor(), user -> String.valueOf(user.getId())
        );
    }

    private BatchResult<User> modifyAndSaveUsers(Collection<String> usernames, Function<User, User> modifier) {
        BatchResult<UserItem> itemsBatch = processor.batchMap(
                nonNullStream(usernames).toList(),
                username -> new UserItem(username, userService.getUserByUsername(username)),
                Function.identity(),
                nullExtractor());

        BatchResult<User> savedBatch = processor.batchMap(
                nonNullStream(itemsBatch.getSuccess()).toList(),
                item -> userService.saveUser(modifier.apply(item.user())),
                UserItem::username,
                item -> String.valueOf(item.user().getId()));

        savedBatch.addErrors(itemsBatch.getErrors());

        return savedBatch;
    }

    private <T> BatchResult<T> findByRequired(Collection<T> source,
                                              Collection<String> required,
                                              Function<List<T>, BatchResult<User>> findUsers,
                                              Function<Permission, String> permissionMapper,
                                              Function<User, Stream<T>> valueExtractor) {

        Set<T> sourceSet = ofNullableStream(source).collect(Collectors.toSet());
        if (sourceSet.isEmpty()) return new BatchResult<>(List.of(), List.of());

        Set<String> requiredSet = ofNullableStream(required).collect(Collectors.toSet());
        BatchResult<User> usersBatch = findUsers.apply(new ArrayList<>(sourceSet));

        List<T> success = usersBatch.getSuccess().stream()
                .filter(user -> hasAllRequired(user, requiredSet, permissionMapper))
                .flatMap(valueExtractor)
                .filter(sourceSet::contains)
                .distinct()
                .toList();

        return new BatchResult<>(success, usersBatch.getErrors());
    }

    private <T> BatchResult<T> findAndExtract(List<String> usernames, Function<Permission, T> extractor) {
        BatchResult<User> usersBatch = findUsersByUsernames(usernames);
        List<User> users = usersBatch.getSuccess();

        List<T> success = extractUniqueFromUsers(users, extractor);
        List<BatchError> errors = new ArrayList<>(usersBatch.getErrors());

        return new BatchResult<>(success, errors);
    }

    private <T> Collection<T> safeList(Collection<T> source) {
        return nonNullStream(source).toList();
    }

    private <T> Function<T, String> nullExtractor() {
        return t -> null;
    }

}

package org.example.new_auth.service.impl;

import jakarta.annotation.Nullable;
import org.example.new_auth.client.AuthClient;
import org.example.new_auth.client.TokenManager;
import org.example.new_auth.exception.ApiException;
import org.example.new_auth.model.domain.Permission;
import org.example.new_auth.model.domain.User;
import org.example.new_auth.model.dto.response.batch.BatchError;
import org.example.new_auth.model.dto.response.batch.BatchResult;
import org.example.new_auth.model.dto.response.batch.UserItem;
import org.example.new_auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthClient authClient;
    private final TokenManager tokenManager;

    public AuthServiceImpl(AuthClient authClient, TokenManager tokenManager) {
        this.authClient = authClient;
        this.tokenManager = tokenManager;
    }

    @Override
    public User getUserById(Long id) {
        return tokenManager.withTokenRetryOnce(token -> authClient.getUser(id, token));
    }

    @Override
    public User getUserByUsername(String username) {
        return tokenManager.withTokenRetryOnce(token -> authClient.getUser(username, token));
    }

    @Override
    public BatchResult<User> findUsersByUsernames(List<String> usernames) {
        return batchMap(nonNullStream(usernames), this::getUserByUsername, Function.identity(), null);
    }

    @Override
    public BatchResult<User> findUsersByIds(List<Long> ids) {
        return batchMap(nonNullStream(ids), this::getUserById, null, String::valueOf);
    }

    @Override
    public BatchResult<String> findUsernamesByRequiredAreas(List<String> usernames, List<String> areas) {
        Set<String> rawRequestedAreas = streamOfNullableList(areas).collect(Collectors.toSet());
        Set<String> rawRequestedUsernames = streamOfNullableList(usernames).collect(Collectors.toSet());
        BatchResult<User> usersBatch = findUsersByUsernames(usernames);

        List<String> success = usersBatch.getSuccess().stream().filter(user -> hasAllRequired(user, rawRequestedAreas, Permission::area)).flatMap(user -> streamOfNullableList(user.getUsernames())).filter(rawRequestedUsernames::contains).distinct().toList();

        return findByRequired(usernames, areas, this::findUsersByUsernames, Permission::area, user -> user.getUsernames().stream());
    }

    @Override
    public BatchResult<Long> findUserIdsByRequiredAreas(List<Long> ids, List<String> areas) {
        return findByRequired(ids, areas, this::findUsersByIds, Permission::area, user -> Stream.of(user.getId()));
    }

    @Override
    public BatchResult<Long> findUserIdsByRequiredOperations(List<Long> ids, List<String> operations) {
        return findByRequired(ids, operations, this::findUsersByIds, Permission::operation, user -> Stream.of(user.getId()));
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
    public List<User> filterUsersByUsernames(List<User> users, List<String> usernames) {
        Set<String> rawRequestedUsernames = streamOfNullableList(usernames).collect(Collectors.toSet());

        return nonNullStream(users).filter(user -> streamOfNullableList(user.getUsernames()).anyMatch(rawRequestedUsernames::contains)).toList();
    }

    @Override
    public List<String> extractAreasFromUsers(List<User> users) {
        return extractUniqueFromUsers(users, Permission::area);
    }

    @Override
    public List<String> extractOperationsFromUsers(List<User> users) {
        return extractUniqueFromUsers(users, Permission::operation);
    }

    @Override
    public User saveUser(User user) {
        return tokenManager.withTokenRetryOnce(token -> authClient.saveUser(user, token));
    }

    @Override
    public BatchResult<User> saveUsers(List<User> users) {
        return batchMap(nonNullStream(users), this::saveUser, null, user -> String.valueOf(user.getId()));
    }

    @Override
    public BatchResult<User> grantPermissions(List<String> usernames, List<Permission> permissions) {
        return modifyAndSaveUsers(usernames, user -> user.grantPermissions(nonNullStream(permissions).toList()));
    }

    @Override
    public BatchResult<User> revokeAreas(List<String> usernames, List<String> areas) {
        return modifyAndSaveUsers(usernames, user -> user.revokePermissionsByAreas(streamOfNullableList(areas).toList()));
    }

    @Override
    public BatchResult<User> revokeOperations(List<String> usernames, List<String> operations) {
        return modifyAndSaveUsers(usernames, user -> user.revokePermissionsByOperations(streamOfNullableList(operations).toList()));
    }

    private <T> BatchResult<T> findAndExtract(List<String> usernames, Function<Permission, T> extractor) {
        BatchResult<User> usersBatch = findUsersByUsernames(usernames);
        List<User> users = usersBatch.getSuccess();

        List<T> success = extractUniqueFromUsers(users, extractor);
        List<BatchError> errors = new ArrayList<>(usersBatch.getErrors());

        return new BatchResult<>(success, errors);
    }

    private boolean hasAllRequired(User user, Set<String> required, Function<Permission, String> mapper) {
        Set<String> userValues = nonNullStream(user.getPermissions()).map(mapper).collect(Collectors.toSet());

        if (userValues.isEmpty()) return true;

        return userValues.containsAll(required);
    }

    private BatchResult<User> modifyAndSaveUsers(List<String> usernames, Consumer<User> modifier) {
        BatchResult<UserItem> itemsBatch = batchMap(nonNullStream(usernames), username -> new UserItem(username, getUserByUsername(username)), Function.identity(), null);

        BatchResult<User> savedBatch = batchMap(nonNullStream(itemsBatch.getSuccess()), item -> {
            modifier.accept(item.user());
            return saveUser(item.user());
        }, UserItem::username, item -> String.valueOf(item.user().getId()));

        savedBatch.addErrors(itemsBatch.getErrors());

        return savedBatch;
    }

    private <T> List<T> extractUniqueFromUsers(List<User> users, Function<Permission, T> extractor) {
        return nonNullStream(users).flatMap(user -> nonNullStream(user.getPermissions())).map(extractor).distinct().toList();
    }

    private <T> Stream<T> nonNullStream(List<T> source) {
        return streamOfNullableList(source).filter(Objects::nonNull);
    }

    private <T> Stream<T> streamOfNullableList(List<T> source) {
        return Stream.ofNullable(source).flatMap(List::stream);
    }

    private <T, R> BatchResult<R> batchMap(Stream<T> source, Function<T, R> mapper, @Nullable Function<T, String> itemExtractor, @Nullable Function<T, String> refExtractor) {

        Function<T, String> nullableIE = Objects.requireNonNullElse(itemExtractor, t -> null);
        Function<T, String> nullableRE = Objects.requireNonNullElse(refExtractor, t -> null);

        List<R> success = new ArrayList<>();
        List<BatchError> errors = new ArrayList<>();

        source.forEach(t -> {
            try {
                success.add(mapper.apply(t));

            } catch (Exception e) {
                BatchError batchError = BatchError.fromException(t, e, nullableIE, nullableRE);
                errors.add(batchError);

                if (e instanceof ApiException ae && ae.getStatus() == HttpStatus.NOT_FOUND) {
                    log.debug("Batch error item='{}' ref='{}' status={} msg={}", batchError.item(), batchError.ref(), batchError.error().status(), batchError.error().message());

                } else {
                    log.warn("Batch error item='{}' ref='{}' status={} msg={}", batchError.item(), batchError.ref(), batchError.error().status(), batchError.error().message(), e);
                }
            }
        });

        log.debug("Batch processing: {} success, {} errors", success.size(), errors.size());
        return new BatchResult<>(success, errors);
    }

    private <T> BatchResult<T> findByRequired(List<T> source, List<String> required, Function<List<T>, BatchResult<User>> findUsers, Function<Permission, String> permissionMapper, Function<User, Stream<T>> valueExtractor) {

        Set<T> sourceSet = streamOfNullableList(source).collect(Collectors.toSet());
        if (sourceSet.isEmpty()) return new BatchResult<>(List.of(), List.of());

        Set<String> requiredSet = streamOfNullableList(required).collect(Collectors.toSet());
        BatchResult<User> usersBatch = findUsers.apply(new ArrayList<>(sourceSet));

        List<T> success = usersBatch.getSuccess().stream().filter(user -> hasAllRequired(user, requiredSet, permissionMapper)).flatMap(valueExtractor).filter(sourceSet::contains).distinct().toList();

        return new BatchResult<>(success, usersBatch.getErrors());
    }

}

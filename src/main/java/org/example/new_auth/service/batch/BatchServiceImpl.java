package org.example.new_auth.service.batch;

import org.example.new_auth.batch.BatchError;
import org.example.new_auth.batch.BatchProcessor;
import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.batch.UserItem;
import org.example.new_auth.domain.Permission;
import org.example.new_auth.domain.User;
import org.example.new_auth.domain.UserIdProductIds;
import org.example.new_auth.domain.UsernameUserId;
import org.example.new_auth.dto.request.SandRecreateRequest;
import org.example.new_auth.dto.response.UsernameMatchResponse;
import org.example.new_auth.service.permission.PermissionService;
import org.example.new_auth.service.user.UserQueryService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.new_auth.util.AuthUtils.*;

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
    public BatchResult<User> getUsersByUsernames(List<String> usernames) {
        return processor.batchMap(safeList(usernames), userService::getUserByUsername, Function.identity(), nullExtractor());
    }

    @Override
    public BatchResult<User> getUsersByIds(List<Long> ids) {
        return processor.batchMap(safeList(ids), userService::getUserById, nullExtractor(), String::valueOf);
    }

    @Override
    public BatchResult<Long> getUserIdsByUsernames(List<String> usernames) {
        return extract(getUsersByUsernames(usernames), user -> Stream.of(user.getId()), Function.identity());
    }

    @Override
    public BatchResult<UsernameUserId> getUserIdsByUsernamesLinked(List<String> usernames) {
        return extract(
                getUserItemsByUsernames(usernames),
                userItem -> Stream.of(new UsernameUserId(userItem.item(), userItem.user().getId())),
                Function.identity()
        );
    }

    @Override
    public BatchResult<String> getAreasByUsernames(List<String> usernames) {
        return extract(getUsersByUsernames(usernames), user -> nonNullStream(user.getPermissions()), Permission::area);
    }

    @Override
    public BatchResult<String> getAreasByUserIds(List<Long> ids) {
        return extract(getUsersByIds(ids), user -> nonNullStream(user.getPermissions()), Permission::area);
    }

    @Override
    public BatchResult<String> getOperationsByUsernames(List<String> usernames) {
        return extract(getUsersByUsernames(usernames), user -> nonNullStream(user.getPermissions()), Permission::operation);
    }

    @Override
    public BatchResult<String> getOperationsByUserIds(List<Long> ids) {
        return extract(getUsersByIds(ids), user -> nonNullStream(user.getPermissions()), Permission::operation);
    }

    @Override
    public BatchResult<String> filterUsernamesByAreas(List<String> usernames, List<String> areas) {
        return findByRequired(usernames, areas, this::getUsersByUsernames, Permission::area, user -> user.getUsernames().stream());
    }

    @Override
    public BatchResult<String> filterUsernamesByOperations(List<String> usernames, List<String> operations) {
        return findByRequired(usernames, operations, this::getUsersByUsernames, Permission::operation, user -> user.getUsernames().stream());
    }

    @Override
    public BatchResult<Long> filterUserIdsByAreas(List<Long> ids, List<String> areas) {
        return findByRequired(ids, areas, this::getUsersByIds, Permission::area, user -> Stream.of(user.getId()));
    }

    @Override
    public BatchResult<Long> filterUserIdsByOperations(List<Long> ids, List<String> operations) {
        return findByRequired(ids, operations, this::getUsersByIds, Permission::operation, user -> Stream.of(user.getId()));
    }

    @Override
    public BatchResult<UsernameMatchResponse> groupUserIdentitiesByUsernames(List<String> usernames) {
        BatchResult<UserItem> itemsBatch = getUserItemsByUsernames(usernames);

        List<UsernameMatchResponse> matched = itemsBatch.success().stream()
                .filter(userItem -> userItem.user().getUsernames().size() > 1)
                .map(userItem -> new UsernameMatchResponse(userItem.item(), userItem.user().getId(), userItem.user().getUsernames()))
                .toList();

        return new BatchResult<>(matched, itemsBatch.errors());
    }

    @Override
    public BatchResult<User> saveUsers(List<User> users) {
        return processor.batchMap(safeList(users), userService::saveUser, nullExtractor(), user -> String.valueOf(user.getId()));
    }

    @Override
    public BatchResult<User> grantPermissionsByUsernames(List<String> usernames, List<Permission> permissions) {
        return modifyAndSaveUsers(getUserItemsByUsernames(usernames), user -> permissionService.addPermissions(user, permissions));
    }

    @Override
    public BatchResult<User> grantPermissionsByUserIds(List<Long> ids, List<Permission> permissions) {
        return modifyAndSaveUsers(getUserItemsByIds(ids), user -> permissionService.addPermissions(user, permissions));
    }

    @Override
    public BatchResult<User> grantOldProductsByUserIds(List<UserIdProductIds> userIdsProductIds) {
        return processor.batchMap(
                safeList(userIdsProductIds),
                userIdProductIds -> userService.saveUser(
                        permissionService.addPermissions(
                                userService.getUserById(userIdProductIds.userId()),
                                userIdProductIds.productIds()
                                        .stream()
                                        .distinct()
                                        .map(productId -> new Permission("old", "product", productId.toString()))
                                        .toList()
                        )
                ),
                nullExtractor(),
                userIdProductIds -> userIdProductIds.userId().toString()
        );
    }

    @Override
    public BatchResult<User> revokeAreasByUsernames(List<String> usernames, List<String> areas) {
        return modifyAndSaveUsers(getUserItemsByUsernames(usernames), user -> permissionService.revokeAreas(user, areas));
    }

    @Override
    public BatchResult<User> revokeOperationsByUsernames(List<String> usernames, List<String> operations) {
        return modifyAndSaveUsers(getUserItemsByUsernames(usernames), user -> permissionService.revokeOperations(user, operations));
    }

    @Override
    public BatchResult<User> revokeAreasByUserIds(List<Long> ids, List<String> areas) {
        return modifyAndSaveUsers(getUserItemsByIds(ids), user -> permissionService.revokeAreas(user, areas));
    }

    @Override
    public BatchResult<User> revokeOperationsByUserIds(List<Long> ids, List<String> operations) {
        return modifyAndSaveUsers(getUserItemsByIds(ids), user -> permissionService.revokeOperations(user, operations));
    }

    @Override
    public BatchResult<User> clearPermissionsByUsernames(List<String> usernames) {
        return modifyAndSaveUsers(getUserItemsByUsernames(usernames), permissionService::clearPermissions);
    }

    @Override
    public BatchResult<User> clearPermissionsByUserIds(List<Long> ids) {
        return modifyAndSaveUsers(getUserItemsByIds(ids), permissionService::clearPermissions);
    }

    @Override
    public BatchResult<UsernameUserId> sandNotMatchesByUsernames(List<UsernameUserId> list) {
        return processor.batchMap(
                safeList(list),
                u -> {
                    User user = userService.getUserByUsername(u.username());
                    if (!Objects.equals(user.getId(), u.userId())) return new UsernameUserId(u.username(), u.userId());
                    return new UsernameUserId(u.username(), null);
                },
                UsernameUserId::username,
                nullExtractor()
        );
    }

    @Override
    public BatchResult<User> sandUsersRecreate(List<SandRecreateRequest> list) {
        BatchResult<UserItem> itemsBatch = processor.batchMap(
                safeList(list),
                sand -> {
                    System.out.println("processing oAuth: " + sand.userId());

                    User user = userService.getUserById(sand.userId());

                    if (user.getUsernames().contains(sand.baseName()) && user.getUsernames().contains(sand.recreateName())) {
                        System.out.println("sand user recreated");
                    }

                    if (!user.getUsernames().contains(sand.baseName())) {
                        System.out.println("sand user not baseName");
                    }

                    if (!user.getUsernames().contains(sand.recreateName())) {
                        System.out.println("sand user not recreateName");
                    }

                    return new UserItem(Objects.toString(sand.userId()), user);
                },
                nullExtractor(),
                Objects::toString
        );

        return new BatchResult<>(itemsBatch.success().stream().map(UserItem::user).toList(), itemsBatch.errors());
    }

    private BatchResult<User> modifyAndSaveUsers(BatchResult<UserItem> itemsBatch, Function<User, User> modifier) {
        BatchResult<User> savedBatch = processor.batchMap(
                safeList(itemsBatch.success()),
                item -> userService.saveUser(modifier.apply(item.user())),
                UserItem::item,
                item -> String.valueOf(item.user().getId()));

        savedBatch.addErrors(itemsBatch.errors());

        return savedBatch;
    }

    private BatchResult<UserItem> getUserItemsByUsernames(Collection<String> usernames) {
        return processor.batchMap(
                safeList(usernames),
                username -> new UserItem(username, userService.getUserByUsername(username)),
                Function.identity(),
                nullExtractor());
    }

    private BatchResult<UserItem> getUserItemsByIds(Collection<Long> ids) {
        return processor.batchMap(
                safeList(ids),
                id -> new UserItem(Objects.toString(id), userService.getUserById(id)),
                nullExtractor(),
                Objects::toString);
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

        List<T> success = usersBatch.success().stream()
                .filter(user -> hasAllRequired(user, requiredSet, permissionMapper))
                .flatMap(valueExtractor)
                .filter(sourceSet::contains)
                .distinct()
                .toList();

        return new BatchResult<>(success, usersBatch.errors());
    }

    private boolean hasAllRequired(User user, Set<String> required, Function<Permission, String> mapper) {
        if (required == null || required.isEmpty()) return true;

        Set<String> userValues = nonNullStream(user.getPermissions())
                .map(mapper)
                .collect(Collectors.toSet());

        return userValues.containsAll(required);
    }

    private <R, T, I> BatchResult<T> extract(BatchResult<I> batchResult, Function<I, Stream<R>> extractor, Function<R, T> mapper) {
        List<I> list = batchResult.success();

        List<T> success = extractUniqueFromList(list, extractor, mapper);
        List<BatchError> errors = new ArrayList<>(batchResult.errors());

        return new BatchResult<>(success, errors);
    }

    private <T> Collection<T> safeList(Collection<T> source) {
        return nonNullStream(source).toList();
    }

    private <T> Function<T, String> nullExtractor() {
        return t -> null;
    }

}

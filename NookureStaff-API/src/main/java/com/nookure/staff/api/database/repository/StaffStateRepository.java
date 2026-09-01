package com.nookure.staff.api.database.repository;

import static java.util.Objects.requireNonNull;

import com.nookure.staff.api.database.model.StaffStateModel;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StaffStateRepository {
    /**
     * Run the migrations to prepare the StaffState table
     */
    void migrate();

    /**
     * Retrieves a StaffStateModel by its UUID.
     *
     * @param uuid The UUID of the staff member.
     * @return The StaffStateModel associated with the given UUID, or null if not found.
     */
    @Nullable StaffStateModel fromUUID(@NotNull final UUID uuid);

    /**
     * Retrieves a StaffStateModel by its ID.
     *
     * @param id The ID of the staff member.
     * @return The StaffStateModel associated with the given ID, or null if not found.
     */
    @Nullable StaffStateModel fromID(int id);

    /**
     * Saves a StaffStateModel to the database.
     *
     * @param model The StaffStateModel to save.
     */
    void savePlayerModel(@NotNull final StaffStateModel model);

    /**
     * Deletes a StaffStateModel from the database.
     *
     * @param model The StaffStateModel to delete.
     */
    void deletePlayerModel(@NotNull final StaffStateModel model);

    /**
     * Updates a StaffStateModel in the database.
     *
     * @param model The StaffStateModel to update.
     */
    void updatePlayerModel(@NotNull final StaffStateModel model);

    /**
     * Saves or updates a StaffStateModel in the database.
     *
     * @param model The StaffStateModel to save or update.
     * @return true if the model was saved or updated successfully, false otherwise.
     */
    default boolean saveOrUpdateModel(@NotNull final StaffStateModel model) {
        requireNonNull(model, "Model cannot be null");
        requireNonNull(model.uuid(), "The UUID of the model can't be null");

        StaffStateModel fromDB = fromUUID(model.uuid());

        if (fromDB == null) {
            savePlayerModel(model);
            return true;
        }

        updatePlayerModel(model);
        return true;
    }

    /**
     * Asynchronously retrieves a StaffStateModel by its UUID.
     *
     * @param uuid The UUID of the staff member.
     * @return A CompletableFuture containing the StaffStateModel associated with the given UUID, or null if not found.
     */
    default CompletableFuture<StaffStateModel> fromUUIDAsync(@NotNull final UUID uuid) {
        return CompletableFuture.supplyAsync(() -> fromUUID(uuid));
    }

    /**
     * Asynchronously retrieves a StaffStateModel by its ID.
     *
     * @param id The ID of the staff member.
     * @return A CompletableFuture containing the StaffStateModel associated with the given ID, or null if not found.
     */
    default CompletableFuture<StaffStateModel> fromIDAsync(int id) {
        return CompletableFuture.supplyAsync(() -> fromID(id));
    }

    /**
     * Asynchronously saves a StaffStateModel to the database.
     *
     * @param model The StaffStateModel to save.
     * @return A CompletableFuture that completes when the save operation is done.
     */
    default CompletableFuture<Void> savePlayerModelAsync(@NotNull final StaffStateModel model) {
        return CompletableFuture.runAsync(() -> savePlayerModel(model));
    }

    /**
     * Asynchronously deletes a StaffStateModel from the database.
     *
     * @param model The StaffStateModel to delete.
     * @return A CompletableFuture that completes when the delete operation is done.
     */
    default CompletableFuture<Void> deletePlayerModelAsync(@NotNull final StaffStateModel model) {
        return CompletableFuture.runAsync(() -> deletePlayerModel(model));
    }

    /**
     * Asynchronously updates a StaffStateModel in the database.
     *
     * @param model The StaffStateModel to update.
     * @return A CompletableFuture that completes when the update operation is done.
     */
    default CompletableFuture<Void> updatePlayerModelAsync(@NotNull final StaffStateModel model) {
        return CompletableFuture.runAsync(() -> updatePlayerModel(model));
    }

    /**
     * Asynchronously saves or updates a StaffStateModel in the database.
     *
     * @param model The StaffStateModel to save or update.
     * @return A CompletableFuture containing true if the model was saved or updated successfully, false otherwise.
     */
    default CompletableFuture<Boolean> saveOrUpdateModelAsync(@NotNull final StaffStateModel model) {
        return CompletableFuture.supplyAsync(() -> saveOrUpdateModel(model));
    }
}

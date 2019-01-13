package services;

import model.Entity;
import services.sto.StateHolderSaveEntity;

import java.util.BitSet;

import static common.ResourceManager.NULL_ID;

public class ServiceHelper {
    public static byte getStateMask(Long entityId, Entity tempEntity) {
        BitSet bs = new BitSet(4);
        bs.set(3, entityId != NULL_ID);
        bs.set(2, tempEntity == null);
        bs.set(1, tempEntity != null && tempEntity.getId() == null);
        bs.set(0, tempEntity != null && entityId.equals(tempEntity.getId()));
        byte[] bytes = bs.toByteArray();
        return (bytes.length > 0 ? bytes[0] : 0);
    }

    public static <T extends Entity> void setSavedEntity(StateHolderSaveEntity<T> state, T newEntity) {
        if (newEntity == null || newEntity.getId() == null || newEntity.getId().equals(NULL_ID)) {
            state.setResultState(StateHolderSaveEntity.State.ERROR_ENTITY_NOT_SAVED);
            return;
        }
        state.setEntity(newEntity);
        state.setResultState(StateHolderSaveEntity.State.SUCCESS);
    }
}

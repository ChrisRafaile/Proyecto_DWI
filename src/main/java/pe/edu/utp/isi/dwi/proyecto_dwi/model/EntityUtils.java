package pe.edu.utp.isi.dwi.proyecto_dwi.model;

import java.util.List;

public class EntityUtils {

    // Método para buscar una entidad por su ID en una lista
    public static <T extends BaseEntity> T findById(List<T> entities, int id) {
        for (T entity : entities) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    // Método para actualizar una entidad en una lista
    public static <T extends BaseEntity> void updateEntity(List<T> entities, T updatedEntity) {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getId() == updatedEntity.getId()) {
                entities.set(i, updatedEntity);
                break;
            }
        }
    }
}

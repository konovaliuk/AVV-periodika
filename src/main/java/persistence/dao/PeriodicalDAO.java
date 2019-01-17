package persistence.dao;

import model.Periodical;
import persistence.database.DbContext;

import java.util.List;

public interface PeriodicalDAO extends GenericDAO<Periodical, Long> {
    List<Periodical> findAllByCategoryWithLimit(Long categoryId, Long offset, Long limit, DbContext context)
            throws DAOException;

    Long countByCategory(Long categoryId, DbContext context) throws DAOException;
}

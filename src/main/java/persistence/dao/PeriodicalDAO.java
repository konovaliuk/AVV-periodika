package persistence.dao;

import model.Periodical;
import persistence.database.DBContext;

import java.util.List;

public interface PeriodicalDAO extends GenericDAO<Periodical, Long> {
    List<Periodical> findAllByCategoryWithLimit(Long categoryId, Long offset, Long limit, DBContext context)
            throws DAOException;

    Long countByCategory(Long categoryId, DBContext context) throws DAOException;
}

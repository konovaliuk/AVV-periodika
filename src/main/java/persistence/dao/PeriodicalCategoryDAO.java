package persistence.dao;

import model.PeriodicalCategory;
import persistence.database.DBContext;

import java.util.List;

public interface PeriodicalCategoryDAO extends GenericDAO<PeriodicalCategory, Long> {
    List<PeriodicalCategory> findAllByType(Integer type, DBContext context) throws DAOException;
}

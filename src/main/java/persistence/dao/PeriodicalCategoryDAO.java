package persistence.dao;

import model.CategoryType;
import model.PeriodicalCategory;
import persistence.database.DbContext;

import java.util.List;

public interface PeriodicalCategoryDAO extends GenericDAO<PeriodicalCategory, Long> {
    List<PeriodicalCategory> findAllByType(CategoryType type, DbContext context) throws DAOException;
}

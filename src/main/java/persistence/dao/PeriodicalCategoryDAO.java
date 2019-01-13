package persistence.dao;

import model.CategoryType;
import model.PeriodicalCategory;
import persistence.database.DBContext;

import java.util.List;

public interface PeriodicalCategoryDAO extends GenericDAO<PeriodicalCategory, Long> {
    List<PeriodicalCategory> findAllByType(CategoryType type, DBContext context) throws DAOException;
}

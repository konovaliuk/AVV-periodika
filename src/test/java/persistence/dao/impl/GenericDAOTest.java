package persistence.dao.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import persistence.dao.GenericReadonlyDAO;
import model.Entity;

import java.sql.Connection;

@RunWith(Parameterized.class)
public abstract class GenericDAOTest<Context> {

    /**
     * Класс тестируемого дао объекта
     */
    protected Class daoClass;

    /**
     * Экземпляр доменного объекта, которому не соответствует запись в системе хранения
     */
    protected Entity notPersistedDto;

    public GenericDAOTest(Class clazz, Entity<Long> notPersistedDto) {
        this.daoClass = clazz;
        this.notPersistedDto = notPersistedDto;
    }

    /**
     * Экземпляр тестируемого дао объекта
     */
    public abstract GenericReadonlyDAO dao();

    /**
     * Контекст взаимодействия с системой хранения данных
     */
    public abstract Connection context();
}

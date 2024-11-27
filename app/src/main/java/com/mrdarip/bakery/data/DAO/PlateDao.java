package com.mrdarip.bakery.data.DAO;

import com.mrdarip.bakery.data.entity.Plate;

import java.util.List;

/**
 * @author mrdarip
 */
public abstract class PlateDao {
    public abstract Plate getPlate(int id);

    public abstract List<Plate> getPlatesPage(int page, int orderBy);

    public abstract Plate upsert(Plate plate);

    public abstract void delete(Plate plate);
}

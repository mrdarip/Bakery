package com.mrdarip.bakery.navigation;

import com.mrdarip.bakery.data.entity.Plate;

public interface PlateDependantNavigable extends Navigable {
    void setPlateContext(Plate plateContext);

    void setPlateRequiredPlate(Plate requiredPlate);
}

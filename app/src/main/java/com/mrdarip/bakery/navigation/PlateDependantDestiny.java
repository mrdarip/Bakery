package com.mrdarip.bakery.navigation;

import com.mrdarip.bakery.data.entity.Plate;

public interface PlateDependantDestiny extends Navigable {
    void setPlateContext(Plate plateContext);
}

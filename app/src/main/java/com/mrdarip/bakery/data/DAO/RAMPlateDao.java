package com.mrdarip.bakery.data.DAO;

import com.mrdarip.bakery.data.entity.Plate;
import java.util.ArrayList;
import java.util.List;

public class RAMPlateDao extends PlateDao {
    private List<Plate> plates = new ArrayList();

    public RAMPlateDao() {
        fillWithSample();
    }

    @Override
    public Plate getPlate(int id) {
        return plates.get(id);
    }

    @Override
    public List<Plate> getPlatesPage(int page, int orderBy) {
        int pageSize = 6;
        return plates.subList(page * pageSize, Math.min(plates.size(), (page + 1) * pageSize));
    }

    /**
     * @param plate Plate to register in the database
     * @return upserted plate
     */
    @Override
    public Plate upsert(Plate plate) {
        if(plate == null){
            return null;
        }

        plate.setRequiredPlate(this.upsert(plate.getRequiredPlate()));

        if(plate.isRegistered()) {
            plates.set(plate.getId(), plate);
        }else{
            plate.setId(plates.size());
            plates.add(plate);
        }

        return plate;
    }
    
    
    private void fillWithSample(){
        String image = "data:image/jpeg;base64,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";

        Plate plate1 = new Plate("Plate 1", 5, null, image);
        Plate plate2 = new Plate("Plate 2", 4, plate1, image);
        Plate plate3 = new Plate("Plate 3", 3, plate2, image);
        Plate plate4 = new Plate("Plate 4", 2, plate3, image);
        Plate plate5 = new Plate("Plate 5", 1, plate4, image);
        Plate plate6 = new Plate("Plate 6", 0, plate5, image);
        Plate plate7 = new Plate("Plate 7", 0, plate6, image);

        this.upsert(plate1);
        this.upsert(plate2);
        this.upsert(plate3);
        this.upsert(plate4);
        this.upsert(plate5);
        this.upsert(plate6);
        this.upsert(plate7);
    }
}

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

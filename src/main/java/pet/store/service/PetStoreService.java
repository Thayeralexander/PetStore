package pet.store.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.store.controller.model.PetStoreData;
import pet.store.dao.PetStoreDao;
import pet.store.entity.PetStore;
import java.util.NoSuchElementException;
@Service
public class PetStoreService {
    @Autowired
    private PetStoreDao petStoreDao;
    public PetStoreData savePetStore(PetStoreData petStoreData) {
        PetStore petStore = findOrCreatePetStore(petStoreData.getPetStoreId());
        copyPetStoreFields(petStore, petStoreData);
        return new PetStoreData(petStoreDao.save(petStore));
    }
    private PetStore findOrCreatePetStore(Long petStoreId) {
        if (petStoreId == null) {
            return new PetStore();
        }
        return petStoreDao.findById(petStoreId).orElseThrow(() ->
            new NoSuchElementException("Pet store with ID " + petStoreId + " not found"));
    }
    private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
        petStore.setPetStoreName(petStoreData.getPetStoreName());
        petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
        petStore.setPetStoreCity(petStoreData.getPetStoreCity());
        petStore.setPetStoreState(petStoreData.getPetStoreState());
        petStore.setPetStoreZip(petStoreData.getPetStoreZip());
        petStore.setPetStorePhone(petStoreData.getPetStorePhone());
    }
}
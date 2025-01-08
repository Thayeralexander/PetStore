package pet.store.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.service.PetStoreService;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
    @Autowired
    private PetStoreService petStoreService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetStoreData createPetStore(@RequestBody PetStoreData petStoreData) {
        log.info("Creating pet store: {}", petStoreData);
        return petStoreService.savePetStore(petStoreData);
    }
    @PutMapping("/{petStoreId}")
    public PetStoreData updatePetStore(@PathVariable Long petStoreId, @RequestBody PetStoreData petStoreData) {
        log.info("Updating pet store with ID {}: {}", petStoreId, petStoreData);
        petStoreData.setPetStoreId(petStoreId);
        return petStoreService.savePetStore(petStoreData);
    }
    @PostMapping("/{petStoreId}/employee")
    @ResponseStatus(HttpStatus.CREATED)
    public PetStoreEmployee addEmployeeToPetStore(@PathVariable Long petStoreId, @RequestBody PetStoreEmployee petStoreEmployee) {
        log.info("Adding employee to pet store ID {}: {}", petStoreId, petStoreEmployee);
        return petStoreService.saveEmployee(petStoreId, petStoreEmployee);
    }
    @PostMapping("/{petStoreId}/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public PetStoreCustomer addCustomerToPetStore(@PathVariable Long petStoreId, @RequestBody PetStoreCustomer petStoreCustomer) {
        log.info("Adding customer to pet store ID {}: {}", petStoreId, petStoreCustomer);
        return petStoreService.saveCustomer(petStoreId, petStoreCustomer);
    }
    @GetMapping
    public List<PetStoreData> retrieveAllPetStores() {
        log.info("Retrieving all pet stores");
        return petStoreService.retrieveAllPetStores();
    }
    @GetMapping("/{petStoreId}")
    public PetStoreData retrievePetStoreById(@PathVariable Long petStoreId) {
        log.info("Retrieving pet store with ID: {}", petStoreId);
        return petStoreService.retrievePetStoreById(petStoreId);
    }
    @DeleteMapping("/{petStoreId}")
    public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId) {
        log.info("Deleting pet store with ID: {}", petStoreId);
        petStoreService.deletePetStoreById(petStoreId);
        return Map.of("message", "Pet store deleted successfully");
    }
}










package pet.store.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
@Service
public class PetStoreService {
    @Autowired
    private PetStoreDao petStoreDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private CustomerDao customerDao;
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
    public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
        PetStore petStore = findPetStoreById(petStoreId);
        Employee employee = findOrCreateEmployee(petStoreEmployee.getEmployeeId(), petStoreId);
        copyEmployeeFields(employee, petStoreEmployee);
        employee.setPetStore(petStore);
        petStore.getEmployees().add(employee);
        employeeDao.save(employee);
        return new PetStoreEmployee(employee);
    }
    public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
        PetStore petStore = findPetStoreById(petStoreId);
        Customer customer = findOrCreateCustomer(petStoreCustomer.getCustomerId(), petStoreId);
        copyCustomerFields(customer, petStoreCustomer);
        customer.getPetStores().add(petStore);
        petStore.getCustomers().add(customer);
        customerDao.save(customer);
        return new PetStoreCustomer(customer);
    }
    @Transactional
    public List<PetStoreData> retrieveAllPetStores() {
        return petStoreDao.findAll().stream()
                .map(PetStoreData::new)
                .peek(petStoreData -> {
                    petStoreData.setCustomers(null);
                    petStoreData.setEmployees(null);
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public PetStoreData retrievePetStoreById(Long petStoreId) {
        return new PetStoreData(findPetStoreById(petStoreId));
    }
    public void deletePetStoreById(Long petStoreId) {
        PetStore petStore = findPetStoreById(petStoreId);
        petStoreDao.delete(petStore);
    }
    private PetStore findPetStoreById(Long petStoreId) {
        return petStoreDao.findById(petStoreId).orElseThrow(() ->
                new NoSuchElementException("Pet store with ID " + petStoreId + " not found"));
    }
    private Employee findOrCreateEmployee(Long employeeId, Long petStoreId) {
        if (employeeId == null) {
            return new Employee();
        }
        return findEmployeeById(petStoreId, employeeId);
    }
    private Employee findEmployeeById(Long petStoreId, Long employeeId) {
        Employee employee = employeeDao.findById(employeeId).orElseThrow(() ->
                new NoSuchElementException("Employee with ID " + employeeId + " not found"));
        if (!employee.getPetStore().getPetStoreId().equals(petStoreId)) {
            throw new IllegalArgumentException("Employee does not belong to pet store ID " + petStoreId);
        }
        return employee;
    }
    private Customer findOrCreateCustomer(Long customerId, Long petStoreId) {
        if (customerId == null) {
            return new Customer();
        }
        return findCustomerById(petStoreId, customerId);
    }
    private Customer findCustomerById(Long petStoreId, Long customerId) {
        Customer customer = customerDao.findById(customerId).orElseThrow(() ->
                new NoSuchElementException("Customer with ID " + customerId + " not found"));
        boolean petStoreExists = customer.getPetStores().stream()
                .anyMatch(petStore -> petStore.getPetStoreId().equals(petStoreId));
        if (!petStoreExists) {
            throw new IllegalArgumentException("Customer is not associated with pet store ID " + petStoreId);
        }
        return customer;
    }
    private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
        petStore.setPetStoreName(petStoreData.getPetStoreName());
        petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
        petStore.setPetStoreCity(petStoreData.getPetStoreCity());
        petStore.setPetStoreState(petStoreData.getPetStoreState());
        petStore.setPetStoreZip(petStoreData.getPetStoreZip());
        petStore.setPetStorePhone(petStoreData.getPetStorePhone());
    }
    private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
        employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
        employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
        employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
        employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
    }
    private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
        customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
        customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
        customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
    }
}
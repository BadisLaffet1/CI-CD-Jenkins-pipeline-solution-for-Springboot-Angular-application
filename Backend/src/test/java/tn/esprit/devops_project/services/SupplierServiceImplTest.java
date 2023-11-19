package tn.esprit.devops_project.services;


import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.repositories.SupplierRepository;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;


@ExtendWith(MockitoExtension.class)
@Slf4j
public class SupplierServiceImplTest {

    @InjectMocks
    SupplierServiceImpl supplierService;

    @Mock
    SupplierRepository supplierRepository;



    @Test
    void testAddSupplier() {
        // Create a Supplier instance and set its properties
        Supplier addedSupplier = new Supplier();
        addedSupplier.setLabel("Supplier Label");

        // Mock the behavior of your repository to return the addedSupplier when save is called
        Mockito.when(supplierRepository.save(Mockito.any(Supplier.class))).thenReturn(addedSupplier);

        // Call the service method you want to test
        Supplier savedSupplier = supplierService.addSupplier(addedSupplier);

        // Assertions
        assertNotNull(savedSupplier);
        // Add more specific assertions based on the behavior you expect
    }



    @Test
    void testRetrieveSupplier() {
        // Create a Supplier instance and set its properties
        Supplier addedSupplier = new Supplier();
        addedSupplier.setIdSupplier(1L);
        addedSupplier.setLabel("Supplier Label");

        // Mock the behavior of your repository to return a Supplier when findById is called
        Mockito.when(supplierRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(addedSupplier));

        // Call the service method you want to test
        Supplier retrievedSupplier = supplierService.retrieveSupplier(1L);

        // Assertions
        assertNotNull(retrievedSupplier);
        // Add more specific assertions based on the behavior you expect
    }

    @Test
    void testDeleteSupplier() {
        // Create a Supplier instance and set its properties
        Supplier supplierToDelete = new Supplier();
        supplierToDelete.setIdSupplier(1L);
        supplierToDelete.setLabel("Supplier Label");

        // Mock the behavior of your repository to return the supplierToDelete when findByIdSupplier is called
        //  Mockito.when(supplierRepository.findByIdSupplier(1L)).thenReturn(supplierToDelete);

        // Mock the deleteById method to do nothing
        doNothing().when(supplierRepository).deleteById(1L);

        // Call the service method you want to test
        supplierService.deleteSupplier(1L);

        // Assertions
        // Verify that the deleteById method is called with the specific ID
        Mockito.verify(supplierRepository, Mockito.times(1)).deleteById(1L);
    }

    /*@Test
    void testRetrieveAllSuppliers() {
        // Create a list of Supplier instances to return when findAll is called
        List<Supplier> supplierList = new ArrayList<>();
        supplierList.add(new Supplier(1L, "Supplier 1"));
        supplierList.add(new Supplier(2L, "Supplier 2"));

        // Mock the behavior of your repository to return the list when findAll is called
        //  Mockito.when(supplierRepository.findAll()).thenReturn(supplierList);

        // Call the service method you want to test
        List<Supplier> retrievedSuppliers = supplierService.retrieveAllSuppliers();

        // Assertions
        assertNotNull(retrievedSuppliers);
        assertEquals(supplierList.size(), retrievedSuppliers.size());

        // Verify that the findAll method was called once
        Mockito.verify(supplierRepository, Mockito.times(1)).findAll();
    }
*/

}
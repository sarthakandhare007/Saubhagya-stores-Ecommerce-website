package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Dto.UserDto;
import com.example.demo.models.Cart;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.ProductService;
import com.example.demo.services.UserServices;

@DisplayName("ECart Project Test Cases")
public class ProjectTestCases {

    // ================= USER =================

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServices userServices;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ================= USER TEST =================

    @Test
    void testUserRegistrationSuccess() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");

        User saved = new User();
        saved.setUsername("testuser");
        saved.setEmail("test@example.com");

        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = userServices.registeruser(userDto);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    // ================= PRODUCT TEST =================

    @Test
    void testGetProductsByCategory() {
        List<Product> expected = Arrays.asList(new Product(), new Product());

        when(productRepository.findAll(
                ArgumentMatchers.<Example<Product>>any(),
                any(Sort.class)
        )).thenReturn(expected);

        List<Product> result = productService.getProductsByCriteria(
                "Electronics", null, null, null, "price"
        );

        assertEquals(2, result.size());
    }

    @Test
    void testFilterProductsByPrice() {
        Product p = new Product();
        p.setPrice(15000);

        when(productRepository.findAll(
                ArgumentMatchers.<Example<Product>>any(),
                any(Sort.class)
        )).thenReturn(Arrays.asList(p));

        List<Product> result = productService.getProductsByCriteria(
                null, null, 20000, null, "price"
        );

        assertTrue(result.get(0).getPrice() <= 20000);
    }

    @Test
    void testSearchProductsByName() {
        Product p = new Product();
        p.setName("Samsung");

        when(productRepository.findAll(
                ArgumentMatchers.<Example<Product>>any(),
                any(Sort.class)
        )).thenReturn(Arrays.asList(p));

        List<Product> result = productService.getProductsByCriteria(
                null, "Samsung", null, null, "price"
        );

        assertTrue(result.get(0).getName().contains("Samsung"));
    }

    @Test
    void testSortAscending() {
        Product p1 = new Product(); p1.setPrice(100);
        Product p2 = new Product(); p2.setPrice(200);

        when(productRepository.findAll(
                ArgumentMatchers.<Example<Product>>any(),
                any(Sort.class)
        )).thenReturn(Arrays.asList(p1, p2));

        List<Product> result = productService.getProductsByCriteria(
                null, null, null, "asc", "price"
        );

        assertTrue(result.get(0).getPrice() < result.get(1).getPrice());
    }

    @Test
    void testSortDescending() {
        Product p1 = new Product(); p1.setPrice(200);
        Product p2 = new Product(); p2.setPrice(100);

        when(productRepository.findAll(
                ArgumentMatchers.<Example<Product>>any(),
                any(Sort.class)
        )).thenReturn(Arrays.asList(p1, p2));

        List<Product> result = productService.getProductsByCriteria(
                null, null, null, "desc", "price"
        );

        assertTrue(result.get(0).getPrice() > result.get(1).getPrice());
    }

    // ================= MODEL TEST =================

    @Test
    void testProductModel() {
        Product p = new Product();
        p.setName("Test");
        p.setPrice(1000);

        assertEquals("Test", p.getName());
        assertEquals(1000, p.getPrice());
    }

    @Test
    void testCartModel() {
        Cart c = new Cart();
        c.setQuantity(5);

        assertEquals(5, c.getQuantity());
    }
}
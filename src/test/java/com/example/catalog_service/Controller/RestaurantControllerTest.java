package com.example.catalog_service.Controller;

import com.example.catalog_service.Exceptions.InvalidRestaurantRegistrationCredentials;
import com.example.catalog_service.Exceptions.RestaurantAlreadyExistException;
import com.example.catalog_service.Model.Restaurant;
import com.example.catalog_service.Service.RestaurantService;
import com.example.catalog_service.dto.RestaurantRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Restaurant> restaurantList;

    @BeforeEach
    void setUp() {
        restaurantList = Arrays.asList(
                new Restaurant("Restaurant1", "Location1"),
                new Restaurant("Restaurant2", "Location2")
        );
    }

    @Test
void testGetRestaurants() throws Exception {
    // Verify that the mock is set up correctly
    Mockito.when(restaurantService.getRestaurants()).thenReturn(restaurantList);

    // Perform the request and log the response for debugging
    mockMvc.perform(get("/catalog/restaurants"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(restaurantList)))
            .andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()));
}

    @Test
    void testAddRestaurant() throws Exception {
        RestaurantRequestDTO restaurantRequestDTO = new RestaurantRequestDTO();
        restaurantRequestDTO.setName("New Restaurant");
        restaurantRequestDTO.setLocation("New Location");

        Mockito.doNothing().when(restaurantService).addRestaurant(anyString(), anyString());

        mockMvc.perform(post("/catalog/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Restaurant added successfully"));
    }

    @Test
    void testAddRestaurantFailure_EmptyName() throws Exception {
        // Mock the service to throw an exception for invalid restaurant credentials
        doThrow(new InvalidRestaurantRegistrationCredentials("Name and location cannot be empty"))
                .when(restaurantService).addRestaurant(anyString(), anyString());

        // Perform the request with invalid data (empty name or location)
        mockMvc.perform(post("/catalog/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\", \"location\": \"New Location\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Name and location cannot be empty"));

        verify(restaurantService,times(1)).addRestaurant(anyString(),anyString());
    }

    @Test
    void testAddRestaurantFailure_EmptyLocation() throws Exception {
        // Mock the service to throw an exception for invalid restaurant credentials
        doThrow(new InvalidRestaurantRegistrationCredentials("Name and location cannot be empty"))
                .when(restaurantService).addRestaurant(anyString(), anyString());

        mockMvc.perform(post("/catalog/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New\", \"location\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Name and location cannot be empty"));
        verify(restaurantService,times(1)).addRestaurant(anyString(),anyString());

    }

    @Test
    void testAddRestaurantFailure_RestaurantAlreadyExistWithSameNameAndLocation() throws Exception {
        // Mock the service to throw an exception for invalid restaurant credentials
        doThrow(new RestaurantAlreadyExistException("Restaurant with same name and location alreadyExists"))
                .when(restaurantService).addRestaurant(anyString(), anyString());

        mockMvc.perform(post("/catalog/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New\", \"location\": \"New Location\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Restaurant with same name and location alreadyExists"));
        verify(restaurantService,times(1)).addRestaurant(anyString(),anyString());

    }

}
package com.example.p4backend.UnitTests;

import com.example.p4backend.models.ActionImage;
import com.example.p4backend.repositories.ActionImageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ActionImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActionImageRepository actionImageRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private ActionImage generateActionImage() {
        return new ActionImage("https://http.cat/400.jpg", "action1");
    }

    private List<ActionImage> generateActionImagesList(){
        ActionImage image1 = new ActionImage("https://http.cat/400.jpg", "action1");
        ActionImage image2 = new ActionImage("https://http.cat/401.jpg", "action2");
        ActionImage image3 = new ActionImage("https://http.cat/402.jpg", "action3");
        ActionImage image4 = new ActionImage("https://http.cat/403.jpg", "action4");
        ActionImage image5 = new ActionImage("https://http.cat/404.jpg", "action5");
        ActionImage image6 = new ActionImage("https://http.cat/405.jpg", "action6");
        ActionImage image7 = new ActionImage("https://http.cat/406.jpg", "action7");

        List<ActionImage> imageList = new ArrayList<>();
        imageList.add(image1);
        imageList.add(image2);
        imageList.add(image3);
        imageList.add(image4);
        imageList.add(image5);
        imageList.add(image6);
        imageList.add(image7);

        return imageList;
    }

    @Test
    void givenActionImages_whenGetActionImages_thenReturnJsonActionImages() throws Exception {
        List<ActionImage> imageList = generateActionImagesList();
        given(actionImageRepository.findAll()).willReturn(imageList);

        mockMvc.perform(get("/actionimages"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)))
                .andExpect(jsonPath("$[0].fileLocation", is("https://http.cat/400.jpg")))
                .andExpect(jsonPath("$[0].actionId", is("action1")))
                .andExpect(jsonPath("$[3].fileLocation", is("https://http.cat/403.jpg")))
                .andExpect(jsonPath("$[3].actionId", is("action4")))
                .andExpect(jsonPath("$[4].fileLocation", is("https://http.cat/404.jpg")))
                .andExpect(jsonPath("$[4].actionId", is("action5")));
    }

    @Test
    void givenActionImage_whenGetActionImage_thenReturnJsonActionImage() throws Exception {
        ActionImage image = generateActionImage();
        image.setId("1");
        given(actionImageRepository.findById("1")).willReturn(java.util.Optional.of(image));

        mockMvc.perform(get("/actionimages/{id}", image.getId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileLocation", is("https://http.cat/400.jpg")))
                .andExpect(jsonPath("$.actionId", is("action1")));

    }
}

package org.example.file_storage_system.controllers;

import org.example.file_storage_system.services.FileService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    public void testUploadFile() throws Exception {
        mockMvc.perform(multipart("/upload").file("file", "content".getBytes()))
                .andExpect(status().isOk());

    }
    @Test
    public void testDownloadFile() throws Exception {
        mockMvc.perform(get("/download/testfile.txt"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSendFile() throws Exception {
        mockMvc.perform(post("/send")
                        .param("recipient", "user@example.com")
                        .param("filename", "testfile.txt"))
                .andExpect(status().isOk());
    }
}
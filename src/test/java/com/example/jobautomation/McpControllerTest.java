package com.example.jobautomation;

import com.example.jobautomation.controller.McpController;
import com.example.jobautomation.service.DraftService;
import com.example.jobautomation.service.JobDiscoveryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(McpController.class)
@ActiveProfiles("test")
class McpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobDiscoveryService discoveryService;

    @MockBean
    private DraftService draftService;

    @Test
    void toolsListShouldReturnSearchAndDraftTools() throws Exception {
        mockMvc.perform(post("/mcp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"tools/list\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.tools[0].name").value("search_jobs"));
    }
}

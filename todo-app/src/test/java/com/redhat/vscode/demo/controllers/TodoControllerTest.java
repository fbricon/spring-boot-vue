package com.redhat.vscode.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.vscode.demo.model.Todo;
import com.redhat.vscode.demo.repositories.TodoRepository;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Basic integration tests for service demo application.
 *
 * @author Dave Syer
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = TodoController.class)
public class TodoControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private TodoRepository repository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private List<Todo> todos;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Todo notDone = new Todo("Not done");
        notDone.setId("one");
        Todo done = new Todo("Done", true);
        done.setId("two");
        todos = Lists.newArrayList(notDone, done);
        given(this.repository.findAll()).willReturn(todos);
        todos.forEach(t -> {
            given(this.repository.findById(t.getId())).willReturn(Optional.of(t));
        });
    }

    @Test
    public void testGetTodos() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/todos")).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String json = mvcResult.getResponse().getContentAsString();
        Todo[] results = mapFromJson(json, Todo[].class);
        for (int i = 0; i < results.length;i++) {
            assertEquals(todos.get(i), results[i]);
        }
    }

    @Test
    public void testGetTodo() throws Exception {
        for( Todo t : todos) {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/todos/"+t.getId())).andReturn();
            assertEquals(200, mvcResult.getResponse().getStatus());
        }
    }

    @Test
    public void testGetMissingTodo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/todos/none")).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}

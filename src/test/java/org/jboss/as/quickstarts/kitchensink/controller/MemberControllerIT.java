package org.jboss.as.quickstarts.kitchensink.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

   private Member testMember;
    private final List<String> testMemberIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        testMember = new Member();
        testMember.setName("Jane Doe");
        testMember.setEmail("jane@mailinator.com");
        testMember.setPhoneNumber("2125551234");
    }

    @AfterEach
    void tearDown() {
        testMemberIds.forEach(id -> memberRepository.deleteById(id));
        testMemberIds.clear();
    }
    @Test
    void testRegisterMember() throws Exception {
        String memberJson = objectMapper.writeValueAsString(testMember);

        String responseContent = mockMvc.perform(post("/rest/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(memberJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Member savedMember = objectMapper.readValue(responseContent, Member.class);
        testMemberIds.add(savedMember.getId()); // Track the new member ID

        // Verify the member was saved correctly
        Member storedMember = memberRepository.findById(savedMember.getId())
                .orElseThrow(() -> new AssertionError("Member not found"));
        assertEquals(testMember.getName(), storedMember.getName());
        assertEquals(testMember.getEmail(), storedMember.getEmail());
        assertEquals(testMember.getPhoneNumber(), storedMember.getPhoneNumber());
    }

    @Test
    void testRegisterMemberWithDuplicateEmail() throws Exception {
        // First registration
        Member savedMember = memberRepository.save(testMember);
        testMemberIds.add(savedMember.getId()); // Track the first member

        // Try to register another member with the same email
        Member duplicateMember = new Member();
        duplicateMember.setName("John Doe");
        duplicateMember.setEmail(testMember.getEmail()); // Same email as testMember
        duplicateMember.setPhoneNumber("2125559876");

        String memberJson = objectMapper.writeValueAsString(duplicateMember);

        mockMvc.perform(post("/rest/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(memberJson))
                .andExpect(status().isConflict());
    }

    @Test
    void testGetAllMembers() throws Exception {
        Member savedMember = memberRepository.save(testMember);
        testMemberIds.add(savedMember.getId()); // Track the member

        mockMvc.perform(get("/rest/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*]").isArray())
                .andExpect(jsonPath("$[?(@.id=='" + savedMember.getId() + "')]").exists())
                .andExpect(jsonPath("$[?(@.email=='" + testMember.getEmail() + "')]").exists())
                .andExpect(jsonPath("$[?(@.name=='" + testMember.getName() + "')]").exists())
                .andExpect(jsonPath("$[?(@.phoneNumber=='" + testMember.getPhoneNumber() + "')]").exists());
    }
}

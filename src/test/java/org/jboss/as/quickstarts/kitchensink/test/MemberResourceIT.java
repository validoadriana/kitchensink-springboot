package org.jboss.as.quickstarts.kitchensink.test;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MemberResourceIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MemberRepository memberRepository;

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
    void testRegisterMember() {
        webTestClient.post()
            .uri("/rest/members")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(testMember))
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").value(id -> testMemberIds.add((String) id))
            .jsonPath("$.name").isEqualTo(testMember.getName())
            .jsonPath("$.email").isEqualTo(testMember.getEmail())
            .jsonPath("$.phoneNumber").isEqualTo(testMember.getPhoneNumber());
    }

    @Test
    void testRegisterMemberWithDuplicateEmail() {
        // First registration
        Member savedMember = memberRepository.save(testMember);
        testMemberIds.add(savedMember.getId());

        // Try to register another member with the same email
        Member duplicateMember = new Member();
        duplicateMember.setName("John Doe");
        duplicateMember.setEmail(testMember.getEmail()); // Same email as testMember
        duplicateMember.setPhoneNumber("2125559876");

        webTestClient.post()
            .uri("/rest/members")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(duplicateMember))
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody(String.class).isEqualTo("{\"email\":\"Email taken\"}");
    }

    @Test
    void testGetAllMembers() {
        Member savedMember = memberRepository.save(testMember);
        testMemberIds.add(savedMember.getId());

        webTestClient.get()
            .uri("/rest/members")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Member.class)
            .contains(testMember);
    }
}
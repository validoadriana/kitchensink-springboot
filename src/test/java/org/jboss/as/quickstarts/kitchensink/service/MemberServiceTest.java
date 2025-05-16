package org.jboss.as.quickstarts.kitchensink.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.validation.ValidationException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member();
        testMember.setName("Jane Doe");
        testMember.setEmail("jane@mailinator.com");
        testMember.setPhoneNumber("2125551234");
    }

    @Test
    void testRegisterMemberSuccess() {
        when(memberRepository.findByEmail(testMember.getEmail())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        Member registered = memberService.register(testMember);

        assertNotNull(registered);
        assertEquals("Jane Doe", registered.getName());
        assertEquals("jane@mailinator.com", registered.getEmail());
        verify(memberRepository).findByEmail(testMember.getEmail());
        verify(memberRepository).save(testMember);
    }

    @Test
    void testRegisterMemberWithDuplicateEmail() {
        when(memberRepository.findByEmail(testMember.getEmail())).thenReturn(Optional.of(testMember));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            memberService.register(testMember);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(memberRepository).findByEmail(testMember.getEmail());
        verify(memberRepository, never()).save(any(Member.class));
    }
}

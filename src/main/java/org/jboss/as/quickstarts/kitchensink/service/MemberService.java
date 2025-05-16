package org.jboss.as.quickstarts.kitchensink.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.repository.MemberRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member register(Member member) {
        log.info("Registering {}", member.getName());
        
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new ValidationException("Email already exists");
        }
        
        return memberRepository.save(member);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAllByOrderByNameAsc();
    }

    public Member getMemberById(String id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Member not found"));
    }
}

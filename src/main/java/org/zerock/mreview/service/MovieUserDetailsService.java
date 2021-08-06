package org.zerock.mreview.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.mreview.dto.AuthMemberDTO;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.repository.MemberRepository;

import java.util.stream.Collectors;

@Service
@Log4j2
public class MovieUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MovieUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("----------------------------");
        log.info(username);

        Member member = memberRepository.findByEmail(username);

        AuthMemberDTO authMemberDTO = new AuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.getRoleSet().stream().map(role ->
                        new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList()));

        authMemberDTO.setMid(member.getMid());
        authMemberDTO.setNickname(member.getNickname());

        return authMemberDTO;
    }

}

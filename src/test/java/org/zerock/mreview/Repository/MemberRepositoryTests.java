package org.zerock.mreview.Repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.MemberRole;
import org.zerock.mreview.entity.Review;
import org.zerock.mreview.repository.MemberRepository;
import org.zerock.mreview.repository.ReviewRepository;

import java.util.List;
import java.util.stream.IntStream;

@Transactional
@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    public void insertMembers() {

        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder()
                    .email("r"+i +"@zerock.org")
                    .password(passwordEncoder.encode("1111"))
                    .nickname("reviewer"+i).build();

            member.addMemberRole(MemberRole.USER);

            if(i > 80){
                member.addMemberRole(MemberRole.MANAGER);
            }

            if(i > 90){
                member.addMemberRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        });
    }


    @Transactional
    @Test
    public void testDeleteMember() {

        Long mid = 1L; //Member의 mid

        Member member = Member.builder().mid(mid).build();

        //기존
        //memberRepository.deleteById(mid);
        //reviewRepository.deleteByMember(member);

        List<Review> result = reviewRepository.findByMember(member);
        for(Review review : result){
            System.out.println("========================================");
            System.out.println(review);
        }

        //순서 주의
        reviewRepository.deleteByMember(member);
        memberRepository.deleteById(mid);


        Assertions.assertThat(reviewRepository.findByMember(member)).isEmpty();
    }

    @Test
    void testFindByEmail(){
        Member member = memberRepository.findByEmail("r10@zerock.org");
        System.out.println("member: " + member);
    }


}

package com.codestates.member.service;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.entity.Member;
import com.codestates.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * V2
 *  - 메서드 구현
 *  - DI 적용
 */
@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) { // MemberRepository 의존성 부여
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail()); // 이미 등록된 이메일인지 확인
        return memberRepository.save(member); // 회원 정보 저장
    }

    public Member updateMember(Member member) {
        Member findMember = findVerifiedMember(member.getMemberId()); // 존재하는 회원인지 확인
        Optional.ofNullable(member.getName())  // 이름 정보와 휴대폰 번호 정보 업데이트
                .ifPresent(name -> findMember.setName(name));
        Optional.ofNullable(member.getPhone())
                .ifPresent(phone -> findMember.setPhone(phone));
        return memberRepository.save(findMember); // 회원 정보 업데이트
    }

    public Member findMember(long memberId) { // 특정 회원 정보 조회
        return findVerifiedMember(memberId);
    }

    public List<Member> findMembers() { // 모든 회원 정보 조회
        return (List<Member>) memberRepository.findAll(); // findAll() = CrudRepository 인터페이스에 있는 메서드
    }

    public void deleteMember(long memberId) { // 특정 회원 정보 삭제
        Member findMember = findVerifiedMember(memberId); //존재하는 회원인지 확인하고
        memberRepository.delete(findMember); // 삭제
    }

    public Member findVerifiedMember(long memberId) { // 이미 존재하는 회원인지를 확인하는 메서드
        Optional<Member> optionalMember =
                memberRepository.findById(memberId);
        Member findMember =
                optionalMember.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return findMember;
    }

    private void verifyExistsEmail(String email) { // 이미 등록된 이메일 주소인지 확인하는 메서드
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) // isPresent() = Optional클래스에 있는 값이 존재하는지의 여부를 리턴하는 메서드
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }
}

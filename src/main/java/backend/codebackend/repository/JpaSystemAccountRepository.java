package backend.codebackend.repository;

import backend.codebackend.domain.SystemAccount;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class JpaSystemAccountRepository implements SystemAccountRepository{
    private final EntityManager em;

    @Override       //시스템 계좌 정보 조회
    public SystemAccount getSystemAccountInfo() {
        String a = "select sa from SystemAccount sa";
        return em.createQuery(a, SystemAccount.class).getSingleResult();
    }

    @Override       //송금 처리(시스템 -> 방장(호스트))
    public boolean sendMoneyToHost(Long amount) {
        return false;
    }
}

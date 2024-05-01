package club.c1sec.c1ctfplatform.dao;

import club.c1sec.c1ctfplatform.po.Attachment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AttachmentDao extends JpaRepository<Attachment, Long> {
    List<Attachment> findAllByChallengeId(Long challengeId);

    Attachment findByAttachmentId(Long attachmentId);

    Attachment findByFlag(String flag);

    void deleteByAttachmentId(Long attachmentId);

    int countByChallengeId(Long challengeId);

    List<Attachment> findAttachmentByChallengeId(Long challengeId, Pageable pageable);
}

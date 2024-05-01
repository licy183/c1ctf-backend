package club.c1sec.c1ctfplatform.services;

import club.c1sec.c1ctfplatform.dao.AttachmentDao;
import club.c1sec.c1ctfplatform.po.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentService {
    @Autowired
    AttachmentDao attachmentDao;

    public int countByChallengeId(Long challengeId) {
        return attachmentDao.countByChallengeId(challengeId);
    }

    public Attachment findAttachmentByChallengeIdWithIndex(Long challengeId, Integer index) {
        Pageable pageable = PageRequest.of(index, 1, Sort.by(Sort.Direction.DESC, "attachmentId"));
        List<Attachment> attachments = attachmentDao.findAttachmentByChallengeId(challengeId, pageable);
        if (attachments.size() >= 1) {
            return attachments.get(0);
        } else {
            return null;
        }
    }

    public List<Attachment> findAllByChallengeId(Long challengeId) {
        return attachmentDao.findAllByChallengeId(challengeId);
    }

    public Attachment findByAttachmentId(Long attachmentId) {
        return attachmentDao.findByAttachmentId(attachmentId);
    }

    public Attachment findByFlag(String flag) {
        return attachmentDao.findByFlag(flag);
    }

    public void deleteByAttachmentId(Long attachmentId) {
        attachmentDao.deleteByAttachmentId(attachmentId);
    }

    public void addAttachment(Attachment attachment) {
        attachmentDao.save(attachment);
    }
}

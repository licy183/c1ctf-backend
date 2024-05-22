package club.c1sec.c1ctfplatform.controllers;

import club.c1sec.c1ctfplatform.checkers.AdminChecker;
import club.c1sec.c1ctfplatform.interceptor.InterceptCheck;
import club.c1sec.c1ctfplatform.po.Attachment;
import club.c1sec.c1ctfplatform.services.AttachmentService;
import club.c1sec.c1ctfplatform.vo.attachment.DeleteAttachmentRequest;
import club.c1sec.c1ctfplatform.vo.attachment.EditAttachmentRequest;
import club.c1sec.c1ctfplatform.vo.attachment.GetAttachmentRequest;
import club.c1sec.c1ctfplatform.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@InterceptCheck(checkers = {AdminChecker.class})
@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {
    @Autowired
    AttachmentService attachmentService;

    @PostMapping("/get_attachment")
    public Response<List<Attachment>> getAttachment(@RequestBody @Valid GetAttachmentRequest getAttachmentRequest,
                                                    BindingResult bindingResult) {
        Response<List<Attachment>> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }
        Long challengeId = getAttachmentRequest.getChallengeId();
        List<Attachment> attachments = attachmentService.findAllByChallengeId(challengeId);
        response.success("", attachments);
        return response;
    }

    @PostMapping("/edit_attachment")
    public Response<Long> editAttachment(@RequestBody EditAttachmentRequest editAttachmentRequest) {
        Response<Long> response = new Response<>();
        Attachment attachment = null;

        if (editAttachmentRequest.getAttachmentId() != null) {
            attachment = attachmentService.findByAttachmentId(editAttachmentRequest.getAttachmentId());
            if (attachment == null) {
                response.fail("此附件 ID 不存在");
                return response;
            }
        } else {
            attachment = new Attachment();
        }

        if (editAttachmentRequest.getUrl() != null) {
            attachment.setUrl(editAttachmentRequest.getUrl());
        }
        if (editAttachmentRequest.getFlag() != null) {
            attachment.setFlag(editAttachmentRequest.getFlag());
        }
        if (editAttachmentRequest.getChallengeId() != null) {
            attachment.setChallengeId(editAttachmentRequest.getChallengeId());
        }
        attachmentService.addAttachment(attachment);
        response.success("修改/添加成功", attachment.getAttachmentId());
        return response;
    }

    @PostMapping("/delete_attachment")
    public Response<String> deleteAttachment(@RequestBody @Valid DeleteAttachmentRequest deleteAttachmentRequest,
                                             BindingResult bindingResult) {
        Response<String> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }
        Long attachmentId = deleteAttachmentRequest.getAttachmentId();
        Attachment attachment = attachmentService.findByAttachmentId(attachmentId);
        if (attachment != null) {
            attachmentService.deleteByAttachmentId(attachmentId);
            response.success("删除成功");
        } else {
            response.fail("此附件 ID 不存在");
        }
        return response;
    }
}

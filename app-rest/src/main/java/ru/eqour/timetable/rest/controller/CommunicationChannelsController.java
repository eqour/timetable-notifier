package ru.eqour.timetable.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import ru.eqour.timetable.rest.exception.MessageSenderNotFoundException;
import ru.eqour.timetable.rest.exception.SendCodeException;
import ru.eqour.timetable.rest.model.channel.*;
import ru.eqour.timetable.rest.service.UserAccountService;
import ru.eqour.timetable.rest.service.CommunicationChannelsService;
import ru.eqour.timetable.rest.service.code.CodeService;
import ru.eqour.timetable.rest.service.code.payload.UpdateChannelPayload;
import ru.eqour.timetable.rest.utils.MessageSenderFactory;

@RestController
@RequestMapping("/api/v1/communication-channels")
public class CommunicationChannelsController {

    private UserAccountService userAccountService;
    private CommunicationChannelsService channelsService;
    private CodeService<UpdateChannelPayload> codeService;
    private MessageSenderFactory senderFactory;

    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Autowired
    public void setChannelsService(CommunicationChannelsService channelsService) {
        this.channelsService = channelsService;
    }

    @Autowired
    public void setCodeService(CodeService<UpdateChannelPayload> codeService) {
        this.codeService = codeService;
    }

    @Autowired
    public void setSenderFactory(MessageSenderFactory senderFactory) {
        this.senderFactory = senderFactory;
    }

    @PostMapping("{channelId}/code")
    public ResponseEntity<?> code(@PathVariable String channelId,
                                  @RequestBody CodeRequest request,
                                  @CurrentSecurityContext SecurityContext context) {
        if (request == null || request.getRecipient() == null || channelId == null
                || userAccountService.channelTypeIsInvalid(channelId)) {
            return ResponseEntity.badRequest().build();
        }
        String email = context.getAuthentication().getPrincipal().toString();
        codeService.registerCode(email, new UpdateChannelPayload(channelId, request.getRecipient()),
                request.getRecipient(), senderFactory.getById(channelId));
        return ResponseEntity.ok().build();
    }

    @PutMapping("{channelId}/id")
    public ResponseEntity<?> updateChannelRecipient(@PathVariable String channelId,
                                                    @RequestBody UpdateChannelRecipientRequest request,
                                                    @CurrentSecurityContext SecurityContext context) {
        if (request == null || request.getRecipient() == null || request.getCode() == null || channelId == null
                || userAccountService.channelTypeIsInvalid(channelId)) {
            return ResponseEntity.badRequest().build();
        }
        String email = context.getAuthentication().getPrincipal().toString();
        UpdateChannelPayload payload = new UpdateChannelPayload(channelId, request.getRecipient());
        if (codeService.verifyCode(email, request.getCode(), payload)) {
            channelsService.updateChannelRecipient(email, channelId, request.getRecipient());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("{channelId}/active")
    public ResponseEntity<?> updateActive(@PathVariable String channelId,
                                          @RequestBody UpdateChannelActiveRequest request,
                                          @CurrentSecurityContext SecurityContext context) {
        if (request == null || channelId == null || userAccountService.channelTypeIsInvalid(channelId)) {
            return ResponseEntity.badRequest().build();
        }
        String email = context.getAuthentication().getPrincipal().toString();
        channelsService.setActive(email, channelId, request.isActive());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{channelId}")
    public ResponseEntity<?> delete(@PathVariable String channelId,
                                    @CurrentSecurityContext SecurityContext context) {
        if (channelId == null || userAccountService.channelTypeIsInvalid(channelId)) {
            return ResponseEntity.badRequest().build();
        }
        String email = context.getAuthentication().getPrincipal().toString();
        channelsService.deleteChannel(email, channelId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(SendCodeException.class)
    private ResponseEntity<?> handleSendCodeException(SendCodeException exception) {
        return ResponseEntity.unprocessableEntity().build();
    }

    @ExceptionHandler(MessageSenderNotFoundException.class)
    private ResponseEntity<?> handleNotifierNotFoundException(MessageSenderNotFoundException exception) {
        return ResponseEntity.badRequest().build();
    }
}

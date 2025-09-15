package com.wedding.backend.wedding_app.controller;

import com.wedding.backend.wedding_app.annotations.InvitationApiDocs;
import com.wedding.backend.wedding_app.dto.InvitationCodeResponseDTO;
import com.wedding.backend.wedding_app.dto.InvitationValidationResponseDTO;
import com.wedding.backend.wedding_app.entity.InvitationCodeEntity;
import com.wedding.backend.wedding_app.service.InvitationCodeService;
import com.wedding.backend.wedding_app.service.QRCodeService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.PNG_EXTENSION;
import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.QR_CODE_ATTACHMENT_HEADER;
import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.QR_CODE_DEFAULT_FILENAME;
import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.SPACE;

@RestController
@RequestMapping("/v1/api/invitation")
@Tag(name = "Invitation Management", description = "Endpoints for managing invitation codes and QR codes")
@Slf4j
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationCodeService invitationCodeService;
    private final QRCodeService qrCodeService;
    
    @InvitationApiDocs.GenerateCode
    @PostMapping("/code/generate/{guestId}")
    public ResponseEntity<InvitationCodeResponseDTO> generateCode(
            @Parameter(description = "The guest ID", required = true)
            @PathVariable Long guestId,
            
            @Parameter(description = "The type of code (PRIMARY, REPLACEMENT, etc.)")
            @RequestParam(defaultValue = "PRIMARY") String codeType) {
            
        log.info("BEGIN - Generating {} invitation code for guest ID: {}", codeType, guestId);
        
        InvitationCodeEntity code = invitationCodeService.createInvitationCode(guestId, codeType);
        
        InvitationCodeResponseDTO response = InvitationCodeResponseDTO.builder()
                .id(code.getId())
                .code(code.getCode())
                .guestId(code.getGuest().getId())
                .guestName(code.getGuest().getFirstName() + SPACE + code.getGuest().getLastName())
                .createdDate(code.getCreatedDate())
                .expiryDate(code.getExpiryDate())
                .used(code.getUsed())
                .codeType(code.getCodeType())
                .build();
                
        log.info("END - Generated invitation code: {}", code.getCode());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @InvitationApiDocs.GetQRCode
    @GetMapping(value = "/qrcode/{code}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQRCode(
            @Parameter(description = "The invitation code", required = true)
            @PathVariable String code,
            
            @Parameter(description = "Filename for the downloaded QR code")
            @RequestParam(defaultValue = "invitation-qrcode") String filename) throws IOException {
        
        log.info("BEGIN - Generating QR code for invitation code: {}", code);
        
        invitationCodeService.validateCode(code);
        
        byte[] qrCode = qrCodeService.generateQRCodeForInvitation(code);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData(QR_CODE_ATTACHMENT_HEADER, filename + PNG_EXTENSION);
        
        log.info("END - Generated QR code for invitation code: {}", code);
        return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
    }
    
    @InvitationApiDocs.ValidateCode
    @GetMapping("/validate/{code}")
    public ResponseEntity<InvitationValidationResponseDTO> validateInvitationCode(
            @Parameter(description = "The invitation code to validate", required = true)
            @PathVariable String code) {

        log.info("BEGIN - Validating invitation code and retrieving RSVP data: {}", code);

        InvitationValidationResponseDTO response = invitationCodeService.validateInvitationAndRetrieveRSVP(code);

        log.info("END - Validated invitation code for guest: {}", response.getPrimaryGuest().getId());
        return ResponseEntity.ok(response);
    }
    
    @InvitationApiDocs.GetCodesForGuest
    @GetMapping("/codes/guest/{guestId}")
    public ResponseEntity<List<InvitationCodeResponseDTO>> getCodesForGuest(
            @Parameter(description = "The guest ID", required = true)
            @PathVariable Long guestId) {
        
        log.info("BEGIN - Getting invitation codes for guest ID: {}", guestId);
        
        List<InvitationCodeEntity> codes = invitationCodeService.getCodesForGuest(guestId);
        
        List<InvitationCodeResponseDTO> response = codes.stream()
                .map(code -> InvitationCodeResponseDTO.builder()
                        .id(code.getId())
                        .code(code.getCode())
                        .guestId(code.getGuest().getId())
                        .guestName(code.getGuest().getFirstName() + " " + code.getGuest().getLastName())
                        .createdDate(code.getCreatedDate())
                        .expiryDate(code.getExpiryDate())
                        .used(code.getUsed())
                        .codeType(code.getCodeType())
                        .build())
                .toList();
        
        log.info("END - Found {} invitation codes for guest ID: {}", codes.size(), guestId);
        return ResponseEntity.ok(response);
    }
    
    @InvitationApiDocs.GenerateReplacementCode
    @PostMapping("/code/replacement/{guestId}")
    public ResponseEntity<InvitationCodeResponseDTO> generateReplacementCode(
            @Parameter(description = "The guest ID", required = true)
            @PathVariable Long guestId) {
        
        log.info("BEGIN - Generating replacement invitation code for guest ID: {}", guestId);
        
        InvitationCodeEntity code = invitationCodeService.generateReplacementCode(guestId);
        
        InvitationCodeResponseDTO response = InvitationCodeResponseDTO.builder()
                .id(code.getId())
                .code(code.getCode())
                .guestId(code.getGuest().getId())
                .guestName(code.getGuest().getFirstName() + " " + code.getGuest().getLastName())
                .createdDate(code.getCreatedDate())
                .expiryDate(code.getExpiryDate())
                .used(code.getUsed())
                .codeType(code.getCodeType())
                .build();
                
        log.info("END - Generated replacement invitation code: {}", code.getCode());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @InvitationApiDocs.MarkCodeAsUsed
    @PutMapping("/code/mark-used/{code}")
    public ResponseEntity<Void> markCodeAsUsed(
            @Parameter(description = "The invitation code", required = true)
            @PathVariable String code) {
        
        log.info("BEGIN - Marking invitation code as used: {}", code);
        
        invitationCodeService.markCodeAsUsed(code);
        
        log.info("END - Marked invitation code as used: {}", code);
        return ResponseEntity.noContent().build();
    }
    
    @InvitationApiDocs.GenerateQRCodeForUrl
    @PostMapping(value = "/qrcode/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCodeForUrl(
            @Parameter(description = "The URL to encode in the QR code", required = true)
            @RequestParam String url,
            
            @Parameter(description = "Filename for the downloaded QR code")
            @RequestParam(required = false) String filename) {
        
        log.info("BEGIN - Generating QR code for URL: {}", url);
        
        byte[] qrCode = qrCodeService.generateQRCodeForUrl(url);

        String downloadFilename = StringUtils.isNotBlank(filename) ? filename : QR_CODE_DEFAULT_FILENAME;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData(QR_CODE_ATTACHMENT_HEADER, downloadFilename + PNG_EXTENSION);
        
        log.info("END - Generated QR code for URL: {}", url);
        return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
    }
}
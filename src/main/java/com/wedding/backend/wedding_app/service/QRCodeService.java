package com.wedding.backend.wedding_app.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.BASE_URL;
import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.PNG;
import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.QR_CODE_SIZE;

@Slf4j
@Service
public class QRCodeService {


    /**
     * Core helper method to generate QR code from any content string
     * @param content The content to encode in the QR code
     * @param logContext Context for logging (e.g., "invitation code", "URL")
     * @return QR code image as byte array
     * @throws WeddingAppException if QR code generation fails
     */
    private byte[] generateQRCodeCore(String content, String logContext) {
        if (StringUtils.isBlank(content)) {
            throw WeddingAppException.invalidParameter("Content cannot be blank");
        }

        log.info("Begin - Generating QR Code for {}: {}", logContext, content);

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE);
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, PNG, out);
            
            log.info("End - Generated QR Code for {} successfully!", logContext);
            return out.toByteArray();
        } catch (WriterException e) {
            log.error("Exception while generating QR code for {}: {}", logContext, content, e);
            throw WeddingAppException.internalError("Failed to generate QR code: " + e.getMessage());
        } catch (IOException e) {
            log.error("Exception while writing QR code image for {}: {}", logContext, content, e);
            throw WeddingAppException.internalError("Failed to create QR code image: " + e.getMessage());
        }
    }

    public byte[] generateQRCodeImage(String code) throws IOException {
        String url = BASE_URL + code;
        return generateQRCodeCore(url, "invitation code");
    }

    public byte[] generateQRCodeForInvitation(String invitationCode) throws IOException {
        return generateQRCodeImage(invitationCode);
    }

    /**
     * Generate QR code for any URL
     * @param url The URL to encode in the QR code
     * @return QR code image as byte array
     * @throws WeddingAppException if URL is invalid or QR code generation fails
     */
    public byte[] generateQRCodeForUrl(String url) {
        return generateQRCodeCore(url, "URL");
    }
}

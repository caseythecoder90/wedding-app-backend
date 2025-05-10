package com.wedding.backend.wedding_app.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.BASE_URL;
import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.PNG;
import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.QR_CODE_SIZE;

@Service
public class QRCodeService {

    private final Logger log = LoggerFactory.getLogger(QRCodeService.class);

    public byte[] generateQRCodeImage(String code) throws IOException {

        log.info("Begin - Generating QR Code Image - Code: {}", code);

        String url = BASE_URL + code;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;

        try {
            bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE);
        } catch (WriterException e) {
            log.error("Exception while generating QR code", e);
            throw new IOException(e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, PNG, out);

        log.info("End - Generated QR Code Image successfully!");
        return out.toByteArray();
    }

    public byte[] generateQRCodeForInvitation(String invitationCode) throws IOException {
        return generateQRCodeImage(invitationCode);
    }
}

package com.janwes.qrcode.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @author Janwes
 * @version 1.0
 * @package com.janwes.qrcode.utils
 * @date 2021/9/6 16:06
 * @description
 */
public class QRCodeGenerator {

    /**
     * 二维码生成图片存储路径
     */
    private static final String QR_CODE_IMAGE_PATH = "\\Users\\HZWJa\\Desktop\\QRCode.png";

    /**
     * 二维码宽度
     */
    private static final int width = 350;

    /**
     * 二维码高度
     */
    private static final int height = 350;

    public static void generateQRCodeImage(String text, int width, int height, String filePath) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            Path path = FileSystems.getDefault().getPath(filePath);

            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.janwes.qrcode.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @author Janwes
 * @version 1.0
 * @package com.janwes.qrcode.utils
 * @date 2021/9/8 12:10
 * @description
 */
public class QRCodeUtil {

    /**
     * 字符编码类型
     */
    private static final String CHARSET = "utf-8";

    /**
     * 格式化名称
     */
    private static final String FORMAT_NAME = "JPG";

    /**
     * 二维码宽度
     */
    private static final int QRCODE_WIDTH = 300;

    /**
     * 二维码高度
     */
    private static final int QRCODE_HEIGHT = 300;

    /**
     * logo宽度
     */
    private static final int LOGO_WIDTH = 60;

    /**
     * logo高度
     */
    private static final int LOGO_HEIGHT = 60;

    private static BufferedImage createImage(String content, String imagePath, boolean needCompress) throws Exception {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // 设置字符编码类型
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        // 设置校错级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置二维码边距，单位像素，值越小，二维码距离四周越近
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        // 设置二维码图片颜色
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0XFFFFFFFF);
            }
        }
        if (StringUtils.isEmpty(imagePath)) {
            return bufferedImage;
        }
        // 插入logo图片
        insertImage(bufferedImage, imagePath, needCompress);
        return bufferedImage;
    }

    /**
     * 插入logo图片
     *
     * @param source       二维码图片
     * @param imagePath    logo图片路径
     * @param needCompress 是否压缩logo
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imagePath, boolean needCompress) throws Exception {
        File file = new File(imagePath);
        if (!file.exists()) {
            throw new IllegalArgumentException(imagePath + "file path is not found!");
        }
        Image src = ImageIO.read(new File(imagePath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) {
            if (width > LOGO_WIDTH) {
                width = LOGO_WIDTH;
            }

            if (height > LOGO_HEIGHT) {
                height = LOGO_HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 绘制图像
            Graphics graphics = bufferedImage.getGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            src = image;
        }
        // 插入logo
        Graphics2D graphic = source.createGraphics();
        int x = (QRCODE_WIDTH - width) / 2;
        int y = (QRCODE_HEIGHT - height) / 2;
        graphic.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, height, 6, 6);
        graphic.setStroke(new BasicStroke(3f));
        graphic.draw(shape);
        graphic.dispose();
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param imagePath    logo图片路径
     * @param destPath     存放路径
     * @param needCompress 是否压缩logo
     * @return
     * @throws Exception
     */
    public static String encode(String content, String imagePath, String destPath, boolean needCompress) throws Exception {
        BufferedImage bufferedImage = createImage(content, imagePath, needCompress);
        // 创建目录
        mkdirs(destPath);
        // 文件名称
        String fileName = UUID.randomUUID().toString() + FORMAT_NAME.toLowerCase();
        ImageIO.write(bufferedImage, FORMAT_NAME, new File(destPath + "/" + fileName));
        return fileName;
    }

    /**
     * 生成二维码(内嵌LOGO) 指定二维码文件名
     *
     * @param content      内容
     * @param imagePath    logo图片路径
     * @param fileName     二维码文件名
     * @param destPath     存放路径
     * @param needCompress 是否压缩logo
     * @return
     * @throws Exception
     */
    public static String encode(String content, String imagePath, String fileName, String destPath, boolean needCompress) throws Exception {
        BufferedImage bufferedImage = createImage(content, imagePath, needCompress);
        // 创建目录
        mkdirs(destPath);
        // 文件名称
        fileName = fileName + FORMAT_NAME.toLowerCase();
        ImageIO.write(bufferedImage, FORMAT_NAME, new File(destPath + "/" + fileName));
        return fileName;
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content   内容
     * @param imagePath logo图片路径
     * @param destPath  目标存放路径
     * @throws Exception
     */
    public static void encode(String content, String imagePath, String destPath) throws Exception {
        encode(content, imagePath, destPath, false);
    }

    /**
     * 生成二维码
     *
     * @param content      内容
     * @param destPath     目标存放路径
     * @param needCompress 是否压缩logo
     * @throws Exception
     */
    public static void encode(String content, String destPath, boolean needCompress) throws Exception {
        encode(content, null, destPath, needCompress);
    }

    /**
     * 生成二维码
     *
     * @param content  内容
     * @param destPath 目标存放路径
     * @throws Exception
     */
    public static void encode(String content, String destPath) throws Exception {
        encode(content, null, destPath, false);
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param imagePath    logo图片路径
     * @param outputStream 输出流
     * @param needCompress 是否压缩logo
     * @throws Exception
     */
    public static void encode(String content, String imagePath, OutputStream outputStream, boolean needCompress) throws Exception {
        BufferedImage bufferedImage = createImage(content, imagePath, needCompress);
        ImageIO.write(bufferedImage, FORMAT_NAME, outputStream);
    }

    /**
     * 生成二维码
     *
     * @param content      内容
     * @param outputStream 输出流
     * @throws Exception
     */
    public static void encode(String content, OutputStream outputStream) throws Exception {
        encode(content, null, outputStream, false);
    }

    /**
     * 解析二维码
     *
     * @param file 二维码图片
     * @return
     */
    public static String decode(File file) {
        BufferedImage image;
        try {
            image = ImageIO.read(file);
            if (Objects.isNull(image)) {
                return null;
            }
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
            hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
            hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            Result result = new MultiFormatReader().decode(bitmap, hints);
            // 获取二维码文本
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析二维码
     *
     * @param path 二维码图片地址
     * @return
     */
    public static String decode(String path) {
        return decode(new File(path));
    }

    /**
     * 当文件夹不存在创建多级目录(mkdir如果父目录不存在则会抛出异常)
     *
     * @param destPath 目标存放路径
     */
    private static void mkdirs(String destPath) {
        File file = new File(destPath);
        if (!file.exists() && !file.isDirectory()) {
            if (!file.mkdirs()) throw new RuntimeException("created directory is failure");
        }
    }
}

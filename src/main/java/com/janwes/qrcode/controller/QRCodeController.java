package com.janwes.qrcode.controller;

import com.janwes.qrcode.utils.QRCodeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;

/**
 * @author Janwes
 * @version 1.0
 * @package com.janwes.qrcode.controller
 * @date 2021/9/8 11:15
 * @description
 */
@RestController
@RequestMapping("/qrcode")
public class QRCodeController {

    @GetMapping("/generate")
    public void generateQRCode(@RequestParam("content") String content, HttpServletResponse response) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            QRCodeUtil.encode(content, byteArrayOutputStream);

            // 转换成字节数组
            byte[] bytes = byteArrayOutputStream.toByteArray();
            // 禁止缓存
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            // 设置响应过期时间
            response.setDateHeader("Expires", 0);
            // 设置响应类型格式
            response.setContentType("image/jpeg");

            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

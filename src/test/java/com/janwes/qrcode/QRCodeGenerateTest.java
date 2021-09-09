package com.janwes.qrcode;

import com.google.zxing.WriterException;
import com.janwes.qrcode.utils.QRCodeGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author Janwes
 * @version 1.0
 * @package com.janwes.qrcode
 * @date 2021/9/6 16:00
 * @description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class QRCodeGenerateTest {

    @Test
    public void codeTest() throws IOException, WriterException {
        QRCodeGenerator.generateQRCodeImage("this is my qr code", 350, 350, "C:\\Users\\HZWJa\\Desktop\\QRCode.png");
    }
}

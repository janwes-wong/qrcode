package com.janwes.qrcode;

import com.janwes.qrcode.utils.QRCodeGenerator;
import com.janwes.qrcode.utils.QRCodeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

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
    public void codeTest() {
        QRCodeGenerator.generateQRCodeImage("this is my qr code", 350, 350, "C:\\Users\\HZWJa\\Desktop\\QRCode.png");
    }

    @Test
    public void testCode() {
        System.out.println(QRCodeUtil.decode(new File("C:\\Users\\HZWJa\\Desktop\\20210909113820.jpg")));
    }
}

package fpt.android.com.appnauan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void convertImageToBitMap() throws Exception{
        FileInputStream in = new FileInputStream("C:\\Users\\Son\\AndroidStudioProjects\\AppNauAn\\app\\src\\main\\res\\drawable\\ai_icon.png");
        BufferedInputStream buf = new BufferedInputStream(in);
        byte[] bMapArray= new byte[buf.available()];
        buf.read(bMapArray);
        Bitmap bMap = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
        System.out.println(bMap);
    }
}
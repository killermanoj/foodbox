package sample.foody;
import java.util.Random;

public class otp
{
    public static String otpp()
    {
        int len = 4;
        String numbers = "1234567890";
       Random random = new Random();
      char[] otp = new char[length];

      for(int i = 0; i< length ; i++) {
         otp[i] = numbers.charAt(random.nextInt(numbers.length()));
      }
      String ret =String.valueOf(otp);
      return ret;
    }
}

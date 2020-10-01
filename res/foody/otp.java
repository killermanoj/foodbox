package sample.foody;
import java.util.Random;

public class otp
{
    public static String otpp()
    {
        int len = 2;
        String numbers = "5288287458274582750120937";
        Random rndm_method = new Random();
        int[] arr= new int [len];
        for (int i = 0; i < len; i++)
        {
            arr[i]=0;
            arr[i] =numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        String a="";
        for (int i = 0; i < len; i++)
        {
            String t = String.valueOf(arr[i]);
            a=a+t;
        }
        return a;
    }
}

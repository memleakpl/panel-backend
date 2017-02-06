package pl.memleak.panel.bll.utils;

import java.util.Random;

/**
 * Created by wigdis on 21.01.17.
 */
public class RandomSequenceGenerator {
     private static Random random = new Random();

    static public String generate(int n, String allowedSigns){
        char[] chars = allowedSigns.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}

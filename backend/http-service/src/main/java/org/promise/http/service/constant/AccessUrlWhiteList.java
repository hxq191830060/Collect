package org.promise.http.service.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author:黄相淇
 * @date:2022年05月12日19:25
 * @description:
 */
public class AccessUrlWhiteList {

    private static List<String> patternList;

    static {
        patternList=new ArrayList<> ();
        patternList.add ("/promise/monitor/?[\\s\\S]*");
        patternList.add ("/task/test/?");
        patternList.add ("/user/login/?");
        patternList.add ("/user/register/?");
        patternList.add("/user/getVerificationCode/?");
        patternList.add("/recommend/test1/?");
        patternList.add("/recommend/test2/?");
    }

    public static boolean allowUrl(String url){
        for(String pattern:patternList){
            if(Pattern.matches (pattern,url)){
                return true;
            }
        }
        return false;
    }

    public static void main(String[]args){
        Scanner in=new Scanner (System.in);
        while(in.hasNextLine ()){
            String context=in.nextLine ();
            System.out.println (allowUrl (context) );
        }
    }
}

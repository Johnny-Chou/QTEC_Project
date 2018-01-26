package com.example.test;

import java.io.BufferedReader;

public class myClass {

    private static BufferedReader reader;

    public static void main(String[] args) {
//        try {
//            File file = new File("/Users/zhouyanglei/Desktop/expression.txt");
//            reader = new BufferedReader(new FileReader(file));
//            String b = null;
//            File file1 = new File("/Users/zhouyanglei/Desktop/expression1.txt");
//            boolean mkdir = file1.createNewFile();
//            BufferedWriter writer = new BufferedWriter(new FileWriter(file1));
//            //writer.newLine();
//            int y = 0;
//            while ((b = reader.readLine()) != null) {
//                System.out.println(b);
//                String substring = b.trim();
//                substring = "\"" + substring + "\",";
//                writer.write(substring);//输出字符串
//                writer.newLine();//换行
//                writer.flush();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                reader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        String s = "45717d76-8f7e-4d49-b6c0-21184281da2a";
//        System.out.println(s.length() + "" + s.getBytes().length);
        char []ss={'a','b','c','d'};
        permutation(ss,0);
    }

    public static void permutation(char[]ss,int i){

        for (int j = i; j < ss.length; j++) {
            char tem = ss[j];
            ss[j] = ss[i];
            ss[i] = tem;
            permutation(ss,i+1);
            char tem1 = ss[j];
            ss[j] = ss[i];
            ss[i] = tem1;
        }
        if (i == ss.length - 1){
            System.out.println(new String(ss));
        }
    }

}




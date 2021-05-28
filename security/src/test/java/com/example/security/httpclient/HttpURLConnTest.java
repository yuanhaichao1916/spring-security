package com.example.security.httpclient;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HttpURLConnTest {

    @Test
    public void test() {
        String url = "https://www.baidu.com/";

    }

    @Test
    public void test1(){
        // 创建密码解析器
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        //对密码进行加密
        String haichaoyuan = bCryptPasswordEncoder.encode("haichaoyuan");
        //打印加密后的密码
        System.out.println(haichaoyuan);

        //判断元字符加密后和加密前是否匹配
        boolean pwd = bCryptPasswordEncoder.matches("haichaoyuan", haichaoyuan);
        System.out.println("比较结果：\t"+pwd);
    }
}

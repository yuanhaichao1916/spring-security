package com.example.security.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.pool.PoolStats;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class GameHttpClient {
//    private static Logger logger = LoggerFactory.getLogger(GameHttpClient.class);
    // 池化管理
    private static PoolingHttpClientConnectionManager poolConnManager = null;
 
    private static CloseableHttpClient httpClient;// 它是线程安全的，所有的线程都可以使用它一起发送http请求
    static {
        try {
            System.out.println("初始化HttpClientTest~~~开始");
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
            // 配置同时支持 HTTP 和 HTPPS
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
            // 初始化连接管理器
            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            poolConnManager.setMaxTotal(640);// 同时最多连接数
            // 设置最大路由
            poolConnManager.setDefaultMaxPerRoute(320);
            System.out.println("连接池最大连接数：" + poolConnManager.getMaxTotal());
            System.out.println("连接池最大路由数：" + poolConnManager.getDefaultMaxPerRoute());
            PoolStats totalStats = poolConnManager.getTotalStats();
            System.out.println("连接池Stats最大连接数：" + totalStats.getMax());
            System.out.println("连接池被占用连接数：" + totalStats.getLeased());
            System.out.println("连接池可用连接数：" + totalStats.getAvailable());

            // 此处解释下MaxtTotal和DefaultMaxPerRoute的区别：
            // 1、MaxtTotal是整个池子的大小；
            // 2、DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；比如：
            // MaxtTotal=400 DefaultMaxPerRoute=200
            // 而我只连接到http://www.abc.com时，到这个主机的并发最多只有200；而不是400；
            // 而我连接到http://www.bac.com 和
            // http://www.ccd.com时，到每个主机的并发最多只有200；即加起来是400（但不能超过400）；所以起作用的设置是DefaultMaxPerRoute
            // 初始化httpClient
            httpClient = getConnection();
 
            System.out.println("初始化HttpClientTest~~~结束");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
 
    public static CloseableHttpClient getConnection() {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000).setSocketTimeout(5000).build();
        CloseableHttpClient httpClient = HttpClients.custom()
                // 设置连接池管理
                .setConnectionManager(poolConnManager)
                .setDefaultRequestConfig(config)
                // 设置重试次数
                .setRetryHandler(new DefaultHttpRequestRetryHandler(2, false)).build();
        return httpClient;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String url = "https://api.powerbi.com/v1.0/myorg/admin/groups?$top=5000&$filter=isOnDedicatedCapacity+eq+true";
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Im5PbzNaRHJPRFhFSzFqS1doWHNsSFJfS1hFZyIsImtpZCI6Im5PbzNaRHJPRFhFSzFqS1doWHNsSFJfS1hFZyJ9.eyJhdWQiOiJodHRwczovL2FuYWx5c2lzLndpbmRvd3MubmV0L3Bvd2VyYmkvYXBpIiwiaXNzIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvNWM3ZDBiMjgtYmRmOC00MTBjLWFhOTMtNGRmMzcyYjE2MjAzLyIsImlhdCI6MTYyMTg0OTg4NywibmJmIjoxNjIxODQ5ODg3LCJleHAiOjE2MjE4NTM3ODcsImFjY3QiOjAsImFjciI6IjEiLCJhaW8iOiJBU1FBMi84VEFBQUFpaWZyb09BbVVhb2V1dFNsMFpsckFybktEamtvNExOS3V5eEhxUk5TT0lZPSIsImFtciI6WyJ3aWEiXSwiYXBwaWQiOiI0YjZiYjZmNC1hYmU2LTQ4OTctYjA2Zi0wNGQ5NmQ3YmYzNDIiLCJhcHBpZGFjciI6IjEiLCJmYW1pbHlfbmFtZSI6IkNoZW4iLCJnaXZlbl9uYW1lIjoiU2l3ZWkiLCJpcGFkZHIiOiI1Ny4xOTcuNTguMjAiLCJuYW1lIjoiU2l3ZWkgU1cxMSBDaGVuIiwib2lkIjoiYTQ1YTMwY2YtMzA3MS00N2NmLWEzZDItYmEyZmI4MTkyOTE3Iiwib25wcmVtX3NpZCI6IlMtMS01LTIxLTg5MzIxOTY2OS0xNTA4NDU3ODItMTU4OTg2NTkxNS03MjAxOTciLCJwdWlkIjoiMTAwMzIwMDA0MUU1QTU5OSIsInJoIjoiMC5BVDRBS0F0OVhQaTlERUdxazAzemNyRmlBX1MyYTB2bXE1ZElzRzhFMlcxNzgwSS1BRTAuIiwic2NwIjoiQXBwLlJlYWQuQWxsIERhc2hib2FyZC5SZWFkLkFsbCBEYXRhc2V0LlJlYWQuQWxsIERhdGFzZXQuUmVhZFdyaXRlLkFsbCBSZXBvcnQuUmVhZC5BbGwgVGVuYW50LlJlYWQuQWxsIFRlbmFudC5SZWFkV3JpdGUuQWxsIFdvcmtzcGFjZS5SZWFkLkFsbCBXb3Jrc3BhY2UuUmVhZFdyaXRlLkFsbCIsInN1YiI6Ik5LOGh2Zl9RWVg0YUJhTHdQUV8yVmFEbEdHRmJPVlFoX28tcHRfLU9FYVUiLCJ0aWQiOiI1YzdkMGIyOC1iZGY4LTQxMGMtYWE5My00ZGYzNzJiMTYyMDMiLCJ1bmlxdWVfbmFtZSI6ImNoZW5zdzExQExlbm92by5jb20iLCJ1cG4iOiJjaGVuc3cxMUBMZW5vdm8uY29tIiwidXRpIjoiRDhnWUNwWVdXa2VCUlZGWEszd2FBUSIsInZlciI6IjEuMCIsIndpZHMiOlsiYTllYTg5OTYtMTIyZi00Yzc0LTk1MjAtOGVkY2QxOTI4MjZjIiwiYjc5ZmJmNGQtM2VmOS00Njg5LTgxNDMtNzZiMTk0ZTg1NTA5Il19.A91e4TzulgpAqkwo22BvORvHYmFdPZ4uXB9cg_VNQG9mpOhj-lHNOkSPxxpde5bUBLGqPQYSQ6CdPynxUqV66-wurfoGod5YREJx3BfyFIXOyye4e8yw_LpTPpSCMbzyd3YeEoFQ6zchqfwPxeexlbORof9By_yU2e-1IM7ymwqvo2ACUEQiJ7aFhFSVoDG0v8EKYehsgPcNVRYQYm3xa2cEFvD1BQkoFuF8pUBcEpjVeYk8JgfC7TMEgle7L9r65GbJ03e1gRneCIB-OUmBDbM8jnt5FggsuRCiU1CJglDuispN8cgOT8ykPYYlo86fyCFo-TOd-4NjlcPIrdKKhw";
            httpGet(url, token);
        }

    }
     
    public static String httpGet(String url,String token) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        httpGet.addHeader(new BasicHeader("Authorization", String.format("Bearer %s", token)));
        httpGet.addHeader(new BasicHeader("Content-Type", "content-type=application/json,charset=utf-8"));
        System.out.println("连接池最大连接数：" + poolConnManager.getMaxTotal());
        System.out.println("连接池最大路由数：" + poolConnManager.getDefaultMaxPerRoute());
        PoolStats totalStats = poolConnManager.getTotalStats();
        System.out.println("连接池Stats最大连接数：" + totalStats.getMax());
        System.out.println("连接池被占用连接数：" + totalStats.getLeased());
        System.out.println("连接池可用连接数：" + totalStats.getAvailable());

        try {
            response = httpClient.execute(httpGet);
            String result = EntityUtils.toString(response.getEntity());
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                return result;
            } else {
                log.error("请求{}返回错误码：{},{}", url, code,result);
                return null;
            }
        } catch (IOException e) {
            log.error("http请求异常，{}",url,e);
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
//    public static String post(String uri, Object params, Header... heads) {
//        HttpPost httpPost = new HttpPost(uri);
//        CloseableHttpResponse response = null;
//        try {
//            StringEntity paramEntity = new StringEntity(JSON.toJSONString(params));
//            paramEntity.setContentEncoding("UTF-8");
//            paramEntity.setContentType("application/json");
//            httpPost.setEntity(paramEntity);
//            if (heads != null) {
//                httpPost.setHeaders(heads);
//            }
//             response = httpClient.execute(httpPost);
//            int code = response.getStatusLine().getStatusCode();
//            String result = EntityUtils.toString(response.getEntity());
//            if (code == HttpStatus.SC_OK) {
//                return result;
//            } else {
//                logger.error("请求{}返回错误码:{},请求参数:{},{}", uri, code, params,result);
//                return null;
//            }
//        } catch (IOException e) {
//            logger.error("收集服务配置http请求异常", e);
//        } finally {
//            try {
//               if(response != null) {
//                   response.close();
//               }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
 
}
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.junit.Test;
//
//import java.io.IOException;
//
///**
// * Created by hu on 2018-05-31.
// */
//public class HttpClientTest {
//
//    /**
//     * 执行get请求
//     */
//    @Test
//    public void doGet(){
//        //创建一个httpclient对象
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        //创建一个GET对象
//        HttpGet get = new HttpGet("http://www.baidu.com");
//        //执行请求
//        CloseableHttpResponse response = null;
//        try {
//            response = httpClient.execute(get);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //取响应结果
//        int statusCode = response.getStatusLine().getStatusCode();
//        System.out.println(statusCode);
//        HttpEntity entity = response.getEntity();
//        try {
//            String string = EntityUtils.toString(entity, "utf-8");
//            System.out.println(string);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //关闭httpclient
//        try {
//            response.close();
//            httpClient.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 执行get请求带参数
//     * @throws Exception
//     */
//    @Test
//    public void doGetWithParam() throws Exception{
//        //创建一个httpclient对象
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        //创建一个uri对象
//        URIBuilder uriBuilder = new URIBuilder("http://www.sogou.com/web");
//        uriBuilder.addParameter("query","刘德华");
//        HttpGet get = new HttpGet(uriBuilder.build());
//        //执行请求
//        CloseableHttpResponse response = httpClient.execute(get);
//        //取得响应结果
//        int statusCode = response.getStatusLine().getStatusCode();
//        System.out.println(statusCode);
//        HttpEntity entity = response.getEntity();
//        String string = EntityUtils.toString(entity, "utf-8");
//        System.out.println(string);
//        //关闭httpclient
//        response.close();
//        httpClient.close();
//    }
//
//}

package club.c1sec.c1ctfplatform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;

@Service
public class HttpService {

    private final Proxy proxy;

    @Autowired
    public HttpService(@Value("${container.proxy_host}") String proxyHost,
                       @Value("${container.proxy_port}") Integer proxyPort) {
        if (proxyHost == null || proxyPort == null) {
            this.proxy = Proxy.NO_PROXY;
        }
        else {
            this.proxy = new java.net.Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        }
    }

    /**
     * 向目的URL发送 POST 请求
     *
     * @param url    目的url
     * @param params 发送的参数
     * @return String
     */
    public String sendPostRequest(String url, MultiValueMap<String, String> params) {
        return sendPostRequest(url, params, new HttpHeaders());
    }

    /**
     * 向目的URL发送 POST 请求
     *
     * @param url     目的url
     * @param params  发送的参数
     * @param headers 发送的http头
     * @return String
     */
    public String sendPostRequest(String url, MultiValueMap<String, String> params, HttpHeaders headers) {
        RestTemplate client = createRestTemplate();
        HttpMethod method = HttpMethod.POST;
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * 向目的URL发送 GET 请求
     *
     * @param url     目的url
     * @param params  发送的参数
     * @return String
     */
    public <R> R sendGetRequest(String url, Map<String, String> params, Class<R> retType) {
        return sendGetRequest(url, params, new HttpHeaders(), retType);
    }

    /**
     * 向目的URL发送 GET 请求
     *
     * @param url     目的url
     * @param params  发送的参数
     * @param headers 发送的http头
     * @return String
     */
    public <R> R sendGetRequest(String url, Map<String, String> params, HttpHeaders headers, Class<R> retType) {
        RestTemplate client = createRestTemplate();
        HttpMethod method = HttpMethod.GET;
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<R> response = client.exchange(url, method, requestEntity, retType);
        return response.getBody();
    }

    /**
     * 向目的URL发送 POST 请求, 返回指定类型的 Json 实体
     *
     * @param url       目的 url
     * @param param     发送的参数
     * @param retType   返回的实体类型
     * @return 请求成功的 Json 实体，或者 null
     */
    public <T, R> R sendPostRequest(String url, T param, Class<R> retType) {
        return sendPostRequest(url, param, new HttpHeaders(), retType);
    }

    /**
     * 向目的URL发送 POST 请求, 返回指定类型的 Json 实体
     *
     * @param url       目的 url
     * @param param     发送的参数
     * @param headers   发送的 http 头
     * @param retType   返回的实体类型
     * @return 请求成功的 Json 实体，或者 null
     */
    public <T, R> R sendPostRequest(String url, T param, HttpHeaders headers, Class<R> retType) {
        RestTemplate client = createRestTemplate();
        HttpMethod method = HttpMethod.POST;
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<T> requestEntity = new HttpEntity<>(param, headers);
        ResponseEntity<R> response = client.exchange(url, method, requestEntity, retType);
        return response.getBody();
    }

    /**
     * 创建一个使用代理的 RestTemplate
     *
     * @return RestTemplate
     */
    private RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setProxy(this.proxy);
        return new RestTemplate(factory);
    }
}
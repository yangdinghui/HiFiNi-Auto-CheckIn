package cloud.ohiyou.utils;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @author yangdh
 * @date 2024/6/17 11:30
 */
public class FeiShuUtils {
    private static final Logger log = Logger.getLogger(FeiShuUtils.class);

    private static final String FEI_SHU_WEBHOOK = System.getenv("FEI_SHU_WEBHOOK");

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public static void sendNotice(String msg) {
        sendNotice(msg, FEI_SHU_WEBHOOK);
    }

    public static void sendNotice(String msg, String webhook) {
        if (webhook == null || webhook.isEmpty()) {
            System.out.println("FEI_SHU_WEBHOOK 飞书环境变量未设置");
            return;
        }
        String json = String.format("{ \"msg_type\":\"text\", \"content\":{\"text\":\"%s\"} }", msg);
        try {
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(webhook)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            try (Response response = call.execute()) {
                assert response.body() != null;
                log.info("飞书消息发送成功：" + response.body().string());
            }
        } catch (Exception e) {
            log.error("飞书消息发送失败", e);
        }
    }

    public static void main(String[] args) {
        sendNotice("Hello, World!");
    }
}

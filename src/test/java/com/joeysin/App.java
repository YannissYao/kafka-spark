package com.joeysin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class})
public class App {
    private WebClient client = null;

    @Before
    public void init() {
        client = WebClient.create("http://127.0.0.1:8080");
    }

    @Test
    public void t1() {
        Mono<Map> map = client.get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON).retrieve()
                .bodyToMono(Map.class);
        System.out.println(map.block().get("key"));
    }


}

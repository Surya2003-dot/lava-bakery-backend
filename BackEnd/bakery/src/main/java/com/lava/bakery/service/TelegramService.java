package com.lava.bakery.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramService {

    private final String BOT_TOKEN = "8345477303:AAF8P7jvfrDKw-hINexbc5iSkj0fn5uVaqM";
    private final String CHAT_ID = "5257063971";

    public void sendMessage(String text){

        String url = "https://api.telegram.org/bot" + BOT_TOKEN
                + "/sendMessage?chat_id=" + CHAT_ID
                + "&text=" + text;

        RestTemplate rest = new RestTemplate();
        rest.getForObject(url, String.class);
    }
}
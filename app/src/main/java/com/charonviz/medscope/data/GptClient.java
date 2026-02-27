/* Copyright (c) 2023-2025 Maksim Nikonov. MIT License. */
package com.charonviz.medscope.data;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Client for the OpenAI Chat Completions API.
 * Maintains conversation history so GPT can reference earlier messages.
 * The system prompt constrains the model to act as a medical consultant.
 */
public class GptClient {

    private final String apiKey;
    private final String systemPrompt;
    private final List<JSONObject> history = new ArrayList<>();

    public interface Callback {
        void onSuccess(String response);
        void onError(String error);
    }

    public GptClient(String apiKey, String systemPrompt) {
        this.apiKey = apiKey;
        this.systemPrompt = systemPrompt;
    }

    public void sendMessage(String userMessage, Callback callback) {
        new Thread(() -> {
            try {
                history.add(new JSONObject()
                    .put("role", "user")
                    .put("content", userMessage));

                JSONArray msgs = new JSONArray();
                msgs.put(new JSONObject()
                    .put("role", "system")
                    .put("content", systemPrompt));
                for (JSONObject m : history) msgs.put(m);

                JSONObject body = new JSONObject();
                body.put("model", "gpt-4o-mini");
                body.put("messages", msgs);
                body.put("max_tokens", 800);
                body.put("temperature", 0.7);

                URL url = new URL("https://api.openai.com/v1/chat/completions");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + apiKey);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(body.toString().getBytes(StandardCharsets.UTF_8));
                os.close();

                Scanner sc = new Scanner(conn.getInputStream(), "UTF-8")
                    .useDelimiter("\\A");
                String resp = sc.hasNext() ? sc.next() : "";
                sc.close();

                String content = new JSONObject(resp)
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

                history.add(new JSONObject()
                    .put("role", "assistant")
                    .put("content", content));
                callback.onSuccess(content);

            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }

    public void clearHistory() { history.clear(); }
}

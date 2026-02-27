/* Copyright (c) 2023-2025 Maksim Nikonov. MIT License. */
package com.charonviz.medscope.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.charonviz.medscope.R;
import com.charonviz.medscope.data.GptClient;
import com.charonviz.medscope.model.ChatMessage;
import com.charonviz.medscope.util.PrefsManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * GPT-powered AI consultation screen.
 * The system prompt constrains GPT to act strictly as a medical consultant.
 * Requires an OpenAI API key configured in Settings.
 */
public class AiChatActivity extends AppCompatActivity {

    private static final String SYSTEM_PROMPT =
        "You are MedScope AI, a medical consultation assistant. "
        + "Help users understand their symptoms and provide preliminary guidance. "
        + "Always remind that your responses are not a substitute for professional advice. "
        + "Be thorough, empathetic, and evidence-based. Suggest emergency care when appropriate.";

    private final List<ChatMessage> messages = new ArrayList<>();
    private ChatAdapter adapter;
    private EditText etMessage;
    private GptClient gptClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("AI Consultation");

        RecyclerView rv = findViewById(R.id.recycler_chat);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(messages);
        rv.setAdapter(adapter);
        etMessage = findViewById(R.id.et_message);

        String apiKey = new PrefsManager(this).getApiKey();
        View noKeyPanel = findViewById(R.id.no_key_panel);

        if (apiKey == null || apiKey.isEmpty()) {
            noKeyPanel.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
            etMessage.setEnabled(false);
            findViewById(R.id.btn_go_settings).setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
        } else {
            noKeyPanel.setVisibility(View.GONE);
            gptClient = new GptClient(apiKey, SYSTEM_PROMPT);
            messages.add(new ChatMessage(
                "Hello! I'm MedScope AI. Describe your symptoms and I'll help you " +
                "understand what might be going on.", ChatMessage.Role.ASSISTANT));
            adapter.notifyDataSetChanged();
        }

        ((FloatingActionButton) findViewById(R.id.fab_send)).setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String text = etMessage.getText().toString().trim();
        if (text.isEmpty() || gptClient == null) return;
        messages.add(new ChatMessage(text, ChatMessage.Role.USER));
        adapter.notifyItemInserted(messages.size() - 1);
        etMessage.setText("");

        messages.add(new ChatMessage("Thinking...", ChatMessage.Role.TYPING));
        adapter.notifyItemInserted(messages.size() - 1);

        RecyclerView rv = findViewById(R.id.recycler_chat);
        rv.smoothScrollToPosition(messages.size() - 1);

        gptClient.sendMessage(text, new GptClient.Callback() {
            @Override public void onSuccess(String response) {
                runOnUiThread(() -> {
                    messages.remove(messages.size() - 1);
                    messages.add(new ChatMessage(response, ChatMessage.Role.ASSISTANT));
                    adapter.notifyDataSetChanged();
                    rv.smoothScrollToPosition(messages.size() - 1);
                });
            }
            @Override public void onError(String error) {
                runOnUiThread(() -> {
                    messages.remove(messages.size() - 1);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(AiChatActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    @Override public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}

/* Copyright (c) 2023-2025 Maksim Nikonov. MIT License. */
package com.charonviz.medscope.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.charonviz.medscope.R;
import com.charonviz.medscope.model.ChatMessage;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VH> {
    private final List<ChatMessage> msgs;
    public ChatAdapter(List<ChatMessage> m) { msgs = m; }

    @Override public int getItemViewType(int pos) {
        return msgs.get(pos).role == ChatMessage.Role.USER ? 0 : 1;
    }
    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
        int l = t == 0 ? R.layout.item_chat_user : R.layout.item_chat_ai;
        return new VH(LayoutInflater.from(p.getContext()).inflate(l, p, false));
    }
    @Override public void onBindViewHolder(@NonNull VH h, int p) { h.tv.setText(msgs.get(p).text); }
    @Override public int getItemCount() { return msgs.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tv;
        VH(View v) { super(v); tv = v.findViewById(R.id.tv_message); }
    }
}

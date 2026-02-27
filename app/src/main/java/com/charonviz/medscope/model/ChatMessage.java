/* Copyright (c) 2023-2025 Maksim Nikonov. MIT License. */
package com.charonviz.medscope.model;

public class ChatMessage {
    public enum Role { USER, ASSISTANT, TYPING }
    public String text;
    public Role role;
    public ChatMessage(String text, Role role) {
        this.text = text;
        this.role = role;
    }
}

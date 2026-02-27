# AI Consultation — GPT-4o Integration

## Overview

MedScope integrates with OpenAI's GPT-4o-mini model to provide evidence-based medical consultation via a messenger-style chat interface. The user provides their own API key.

## Architecture

- **GptClient** (`data/GptClient.java`) — handles HTTP communication with the OpenAI `/v1/chat/completions` endpoint. Maintains full conversation history for multi-turn context.
- **AiChatActivity** (`ui/AiChatActivity.java`) — manages the RecyclerView chat UI, input bar, and loading states.
- **ChatAdapter** (`ui/ChatAdapter.java`) — RecyclerView adapter with two view types: user bubbles (blue gradient, right-aligned) and AI bubbles (gray, left-aligned).
- **ChatMessage** (`model/ChatMessage.java`) — POJO with `text` and `role` (user, assistant, typing).

## System Prompt

The GPT client sends a medical system prompt that instructs the model to:

1. Act as a medical information assistant (not a doctor)
2. Ask clarifying questions before suggesting possible conditions
3. Flag emergency symptoms with explicit "call emergency services" guidance
4. Never provide definitive diagnoses — always recommend professional consultation
5. Base responses on established medical literature

## API Configuration

- **Model:** gpt-4o-mini
- **Temperature:** 0.3 (low for factual accuracy)
- **Max tokens:** 1024
- **Endpoint:** `https://api.openai.com/v1/chat/completions`

## Security

- The API key is stored in SharedPreferences (local device only)
- The key is never transmitted to any server other than OpenAI
- No conversation data is stored persistently

## Chat Bubble Design

User messages use a blue gradient (`#5B8DEF → #3A6FE8` at 135°) with rounded corners (16dp top, 4dp bottom-right). AI messages use a neutral card background. Both are compact messenger-style with 8dp vertical padding and 12dp horizontal padding.

package com.lotterydev.chat;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatMessage;
import io.reactivex.Flowable;

public interface ChatService {
    ChatMessage sendMessage(ChatMessage message);

    Flowable<ChatCompletionChunk> sendMessageStream(ChatMessage message);
}

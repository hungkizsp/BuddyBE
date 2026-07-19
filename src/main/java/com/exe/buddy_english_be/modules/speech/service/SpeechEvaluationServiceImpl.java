package com.exe.buddy_english_be.modules.speech.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.exe.buddy_english_be.modules.conversation.service.GeminiService;
import com.exe.buddy_english_be.modules.speech.dto.SpeechEvaluationRequest;
import com.exe.buddy_english_be.modules.speech.dto.SpeechEvaluationResponse;
import com.exe.buddy_english_be.shared.exception.GeminiQuotaExceededException;
import com.exe.buddy_english_be.shared.exception.GeminiUnavailableException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeechEvaluationServiceImpl implements SpeechEvaluationService {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final String SYSTEM_PROMPT = """
            Bạn là giáo viên luyện phát âm tiếng Anh có nhiều kinh nghiệm dành cho trẻ em Việt Nam từ 6–10 tuổi.
            Hãy phân tích kết quả phát âm một cách khách quan dựa DUY NHẤT trên dữ liệu điểm phát âm được cung cấp.
            Quy tắc:
            - Không được tự suy diễn lỗi nếu dữ liệu không thể hiện lỗi đó.
            - Luôn bắt đầu bằng lời khen ngắn gọn, tích cực.
            - Chỉ nêu NHỮNG lỗi quan trọng nhất.
            - Không liệt kê tất cả lỗi nhỏ.
            - Nếu phát âm đã tốt, chỉ cần động viên tiếp tục luyện tập.
            - Không sử dụng thuật ngữ ngôn ngữ học hoặc IPA quá phức tạp.
            - Sử dụng câu ngắn, dễ hiểu, phù hợp với trẻ em 6–10 tuổi.
            - Mỗi câu tối đa khoảng 12–15 từ.
            - overallFeedback chỉ gồm 1 câu.
            - strengths tối đa 2 ý.
            - weaknesses chỉ tối đa 1 ý.
            - improvementTips chỉ tối đa 1 ý.
            - wordFeedback chỉ trả về DUY NHẤT 1 từ cần luyện nhất.
            - Nếu không có từ nào cần cải thiện thì trả về mảng rỗng.
            - Chỉ tập trung vào lỗi ảnh hưởng nhiều nhất đến khả năng hiểu.
            - Ưu tiên xưng hô bạn
            Chỉ trả về JSON hợp lệ.
            Không được thêm markdown.
            Không được bọc kết quả trong ```json.
            Response schema:
            {
              "overallFeedback": "Một câu động viên ngắn.",
              "strengths": [
                "Điểm mạnh 1",
                "Điểm mạnh 2"
              ],
              "weaknesses": [
                "Điểm cần cải thiện"
              ],
              "improvementTips": [
                "Một lời khuyên ngắn"
              ],
              "wordFeedback": [
                {
                  "word": "where",
                  "problem": "Mô tả ngắn gọn vấn đề.",
                  "tip": "Cách luyện ngắn gọn."
                }
              ]
            }
            """;

    @Override
    public SpeechEvaluationResponse evaluate(Long userId, SpeechEvaluationRequest request) {
        try {
            String userPrompt = objectMapper.writeValueAsString(request);
            log.info("Sending speech evaluation request to Gemini for userId: {}", userId);

            String responseStr = geminiService.generateResponse(SYSTEM_PROMPT, userPrompt);
            log.info("Received raw speech evaluation response from Gemini for userId: {}", userId);

            String cleanedJson = cleanJson(responseStr);
            return objectMapper.readValue(cleanedJson, SpeechEvaluationResponse.class);

        } catch (GeminiQuotaExceededException e) {
            log.warn("Gemini quota exceeded during speech evaluation for userId: {}. Returning fallback response.", userId, e);
            return getFallbackResponse();

        } catch (GeminiUnavailableException e) {
            log.warn("Gemini unavailable during speech evaluation for userId: {}. Returning fallback response.", userId, e);
            return getFallbackResponse();

        } catch (Exception e) {
            log.error("Unexpected error evaluating speech with Gemini for userId: {}. Returning fallback response.", userId, e);
            return getFallbackResponse();
        }
    }

    private String cleanJson(String rawResponse) {
        if (rawResponse == null) {
            return "";
        }
        String cleaned = rawResponse.trim();
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceAll("^```(?:json)?\\s*", "");
            cleaned = cleaned.replaceAll("\\s*```$", "");
        }
        return cleaned.trim();
    }

    private SpeechEvaluationResponse getFallbackResponse() {
        return SpeechEvaluationResponse.builder()
                .overallFeedback(
                        "Great job trying to read the sentence! Keep practicing to make your English sound even more natural.")
                .strengths(List.of("Wonderful effort in speaking and practicing!"))
                .weaknesses(List.of("Some words or sounds can be spoken more clearly."))
                .improvementTips(List.of(
                        "Try repeating the words slowly after listening to them.",
                        "Practice reading along with your AI buddy often!"))
                .wordFeedback(List.of())
                .build();
    }
}
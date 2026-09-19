package com.exe.buddy_english_be.modules.studymode.service;

import com.exe.buddy_english_be.modules.studymode.dto.SessionResultResponse;
import com.exe.buddy_english_be.modules.studymode.dto.StartSessionRequest;
import com.exe.buddy_english_be.modules.studymode.dto.StartSessionResponse;
import com.exe.buddy_english_be.modules.studymode.dto.SubmitAnswerRequest;

public interface StudyModeService {

    /** Start a new study session for a given category and mode. Returns shuffled vocab list. */
    StartSessionResponse startSession(StartSessionRequest request);

    /** Submit an answer for one vocabulary item, updates spaced-repetition data. */
    void submitAnswer(Long sessionId, SubmitAnswerRequest request);

    /** Finish the session and return the final result. */
    SessionResultResponse finishSession(Long sessionId);
}

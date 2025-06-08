package university.jala.gumaapi.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import university.jala.gumaapi.dtos.request.ChatDTO;
import university.jala.gumaapi.dtos.response.ChatDTOResponse;
import university.jala.gumaapi.entity.LessonReviews;
import university.jala.gumaapi.mock.ChatDTOResponseMock;
import university.jala.gumaapi.mock.LessonReviewsMocks;
import university.jala.gumaapi.service.ChatReviewService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static reactor.core.publisher.Mono.when;
import org.mockito.Mockito;


@ExtendWith(MockitoExtension.class)
class ChatReviewControllerTest {
    @InjectMocks
    private ChatReviewController chatReviewController;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatReviewService chatReviewService;

    @BeforeEach
    void setUp() {
        Mockito.when(chatReviewService.getLessonReviewsById(anyString()))
                .thenReturn(LessonReviewsMocks.mockLessonReviews());

        Mockito.when(chatReviewService.verifyAssignment(any(ChatDTO.class), anyInt(), anyInt(), anyString()))
                .thenReturn(ChatDTOResponseMock.mockChatDTO());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should get lesson review successfully")
    public void getLessonReviews() {
        Mono<LessonReviews> result = chatReviewController.getLessonReviews("test-id");

        StepVerifier.create(result)
                .expectNextMatches(review ->
                        "312".equals(review.getLessonId()) &&
                                "model".equals(review.getModel()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get a review successfully")
    public void getReview() {
        Flux<ChatDTOResponse> chatDTOResponseFlux = this.chatReviewController.verifyAssignment(ChatDTOResponseMock.returnValidChatDTO(), 1, 1, "test-id");

        assertNotNull(chatDTOResponseFlux);
    }
}
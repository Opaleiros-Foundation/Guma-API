package university.jala.gumaapi.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;
import university.jala.gumaapi.dtos.request.ChatDTO;
import university.jala.gumaapi.dtos.response.ChatDTOResponse;
import university.jala.gumaapi.dtos.response.canvas.Assignment;
import university.jala.gumaapi.entity.LessonReviews;
import university.jala.gumaapi.mock.AssignmentMock;
import university.jala.gumaapi.mock.ChatDTOMock;
import university.jala.gumaapi.service.CanvasService;
import university.jala.gumaapi.service.ReviewObserver;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Chat review service")
class ChatReviewServiceImplTest {

    @InjectMocks
    ChatReviewServiceImpl chatReviewService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    ChatClient chatClient;

    @Mock
    LessonReviewsServiceImpl lessonReviewsService;

    @Mock
    List<ReviewObserver> observers;

    @Mock
    CanvasService canvasService;

    @BeforeEach
    void setUp() {

        Assignment aValidAssignment = AssignmentMock.createAValidAssignment();
        when(chatClient.prompt().system(anyString()).user(anyString()).stream().content()).thenReturn(
                Flux.just("this is the way")
        );
        doNothing().when(lessonReviewsService).saveLessonReview(any(LessonReviews.class));
        doNothing().when(observers).forEach(any());
        when(canvasService.getAssignmentById(anyInt(), anyInt(), anyString())).thenReturn(aValidAssignment);

    }

    @AfterEach
    void tearDown() {
        reset(chatClient, lessonReviewsService, observers, canvasService);
    }

    @Test
    public void should_review_lesson_successfully() {

        ChatDTO aValidChatDTO = ChatDTOMock.createAValidChatDTO();

        Flux<ChatDTOResponse> chatReviewResponse = this.chatReviewService.verifyAssignment(aValidChatDTO, 153, 124, "this is the way");
        assertNotNull(chatReviewResponse);

    }
}
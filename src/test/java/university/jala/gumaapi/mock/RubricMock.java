package university.jala.gumaapi.mock;

import university.jala.gumaapi.dtos.response.canvas.Rubric;

public class RubricMock {

    public static Rubric createAValidRubricMock() {
        return Rubric.builder()
                .id("1")
                .points(1)
                .criterionUseRange(false)
                .description("worth it")
                .ignoreForScoring(false)
                .longDescription("worth it")
                .build();
    }
}

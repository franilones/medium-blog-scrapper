package com.franilones.medium_blog_scrapper.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostTest {

    @Test
    void givenValidPostData_whenCreatingPost_thenAllFieldsAreSetCorrectly() {
        //Given
        String expectedTitle = " Principales cambios en WCAG 2.2: ¿Qué hay de nuevo en accesibilidad web?";
        String expectedImg = "https://miro.medium.com/v2/resize:fit:720/format:webp/1*j-LztKW8mJe5C8B6jjpg_g.jpeg";
        String expectedSummary = "¡La accesibilidad web sigue evolucionando! La actualización WCAG 2.2 introduce nuevos criterios para mejorar la usabilidad...";
        int expectedClaps = 150;

        //When
        Post post = Post.builder()
                .title(expectedTitle)
                .imageUrl(expectedImg)
                .summary(expectedSummary)
                .claps(expectedClaps)
                .build();

        assertAll(
                () -> assertEquals(expectedTitle, post.getTitle()),
                () -> assertEquals(expectedImg, post.getImageUrl()),
                () -> assertEquals(expectedClaps, post.getClaps())
        );
    }
}

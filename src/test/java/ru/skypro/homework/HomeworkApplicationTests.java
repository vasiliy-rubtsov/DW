package ru.skypro.homework;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.skypro.homework.dto.*;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HomeworkApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final String login = "kirill.lobanov@mycompany.ru";
    private final String password = "12345678";

    private static final Map<String, Long> ids = new HashMap<>();

    @Test
    @Order(1)
    @DisplayName("Проверка регистрации пользователя")
    public void testUserRegistration() {
        Register register = new Register();
        register.setUsername(login);
        register.setPassword(password);
        register.setFirstName("Кирилл");
        register.setLastName("Лобанов");
        register.setPhone("+7(911) 111-11-11");
        register.setRole(Role.ADMIN);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/register", register, Void.class);

        HttpStatus status  = responseEntity.getStatusCode();
        boolean criteria =  status==HttpStatus.CREATED || status==HttpStatus.BAD_REQUEST;

        Assertions.assertTrue(criteria);
    }

    @Test
    @Order(2)
    @DisplayName("Проверка удачной авторизации")
    public void testUserLoginSuccess() {
        Login login = new Login();
        login.setUsername(this.login);
        login.setPassword(this.password);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/login", login, Void.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(3)
    @DisplayName("Проверка ошибочной авторизации")
    public void testUserLoginError() {
        Login login = new Login();
        login.setUsername(this.login);
        login.setPassword("00000000");

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/login", login, Void.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    @Order(4)
    @DisplayName("Проверка вывода данных по текущему пользователю (запрос без авторизации)")
    public void testGetUserNoAuth() {
        ResponseEntity<Void> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/users/me", Void.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("Проверка вывода данных по текущему пользователю (запрос авторизацией)")
    public void testGetUserAuth() {
        ResponseEntity<User> responseEntity = restTemplate.withBasicAuth(login, password).getForEntity("http://localhost:" + port + "/users/me", User.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        User user = responseEntity.getBody();
        Assertions.assertNotNull(user);
        Assertions.assertNotEquals(0L, user.getId());
        Assertions.assertEquals(user.getEmail(), login);
        Assertions.assertEquals("Кирилл", user.getFirstName());
        Assertions.assertEquals("Лобанов", user.getLastName());
        Assertions.assertEquals("+7(911) 111-11-11", user.getPhone());
        Assertions.assertEquals(Role.ADMIN.toString(), user.getRole());
    }

    @Test
    @Order(6)
    @DisplayName("Проверка добавления объявления текущим пользователем")
    public void testAddAd() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setTitle("Пробный товар");
        createOrUpdateAd.setDescription("Описание пробного товара");
        createOrUpdateAd.setPrice(100);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("properties", createOrUpdateAd);
        body.add("image", new FileSystemResource("src/test/resources/apple.jpg"));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Ad> response =  restTemplate.withBasicAuth(login, password).postForEntity("http://localhost:" + port + "/ads", request, Ad.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        long adId =  response.getBody().getPk();
        Assertions.assertNotEquals(0L, adId);
        ids.put("adId", adId);
    }

    @Test
    @Order(7)
    @DisplayName("Проверка добавления комментария")
    public void testAddComment() {
        Assertions.assertTrue(ids.containsKey("adId"));
        long adId = ids.get("adId");
        Assertions.assertNotEquals(0L, adId);

        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText("Тестовый комментарий");

        ResponseEntity<Comment> response =  restTemplate.withBasicAuth(login, password).postForEntity("http://localhost:" + port + "/ads/" + adId + "/comments", createOrUpdateComment, Comment.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Assertions.assertNotNull(response.getBody());
        long commentId =  response.getBody().getPk();
        Assertions.assertNotEquals(0L, commentId);

        ids.put("commentId", commentId);
    }


    @Test
    @Order(8)
    @DisplayName("Проверка удаления комментария")
    public void testDeleteComment() {
        Assertions.assertTrue(ids.containsKey("adId"));
        long adId = ids.get("adId");
        Assertions.assertNotEquals(0L, adId);

        Assertions.assertTrue(ids.containsKey("commentId"));
        long commentId = ids.get("commentId");
        Assertions.assertNotEquals(0L, commentId);


        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/ads/" + adId + "/comments/" + commentId, HttpMethod.DELETE, null, Void.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response = restTemplate.withBasicAuth(login, password).exchange("http://localhost:" + port + "/ads/" + adId + "/comments/" + commentId, HttpMethod.DELETE, null, Void.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(9)
    @DisplayName("Проверка удаления объявления")
    public void testDeleteAd() {
        Assertions.assertTrue(ids.containsKey("adId"));
        long adId = ids.get("adId");
        Assertions.assertNotEquals(0L, adId);

        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/ads/" + adId, HttpMethod.DELETE, null, Void.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response = restTemplate.withBasicAuth(login, password).exchange("http://localhost:" + port + "/ads/" + adId, HttpMethod.DELETE, null, Void.class);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}

package net.ukr.sawkone.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.ukr.sawkone.model.entity.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final CloseableHttpClient CLOSEABLE_HTTP_CLIENT = HttpClientBuilder.create().build();
    private static final Gson GSON = new Gson();
    private static final String URL_MAIN = "https://petstore.swagger.io/v2";
    private static final String URL_PET = "/pet";
    private static final String UPLOAD_IMAGE = "/uploadImage";
    private static final String URL_FIND_BY_STATUS = "/findByStatus?status=";
    private static final String URL_STORE = "https://petstore.swagger.io/v2/store/";
    private static final String URL_INVENTORY = "inventory";
    private static final String URL_ORDER = "order";
    private static final String URL_USER = "https://petstore.swagger.io/v2/user";
    private static final String URL_CREATE_WITH_LIST = "/createWithList";
    private HttpResponse<String> response;

    public ApiResponse uploadImage(long id, String metaData, File file) throws IOException {
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
        StringBody stringBody = new StringBody(metaData, ContentType.MULTIPART_FORM_DATA);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addPart("additionalMetadata", stringBody)
                .addPart("file", fileBody);
        HttpEntity build = builder.build();
        HttpPost post = new HttpPost(String.format("%s%s/%d%s", URL_MAIN, URL_PET, id, UPLOAD_IMAGE));
        post.setEntity(build);
        return GSON.fromJson(CLOSEABLE_HTTP_CLIENT.execute(post, new BasicResponseHandler()), ApiResponse.class);
    }

    public PetEntity getPetById(long idPet) throws IOException, InterruptedException {
        String urlUserById = String.format("%s%s/%d", URL_MAIN, URL_PET, idPet);
        response = getGETResponse(urlUserById);
        return GSON.fromJson(response.body(), PetEntity.class);
    }


    public PetEntity createPet(PetEntity petEntity) throws IOException, InterruptedException {
        String url = String.format("%s%s", URL_MAIN, URL_PET);
        response = getPOSTResponse(url, petEntity);
        return GSON.fromJson(response.body(), PetEntity.class);
    }

    public PetEntity updatePet(PetEntity petEntity) throws IOException, InterruptedException {
        String url = String.format("%s%s", URL_MAIN, URL_PET);
        response = getPUTResponse(url, petEntity);
        return GSON.fromJson(response.body(), PetEntity.class);
    }

    public PetEntity updatePetById(Long id, String name, PetStatus status) throws IOException, InterruptedException {
        PetEntity petEntity = getPetById(id);
        petEntity.setName(name);
        petEntity.setStatus(status);
        return updatePet(petEntity);
    }

    public List<PetEntity> findByStatus(PetStatus status) throws IOException, InterruptedException {
        String urlPetStatus = String.format("%s%s%s%s", URL_MAIN, URL_PET, URL_FIND_BY_STATUS, status.name());
        response = getGETResponse(urlPetStatus);
        return GSON.fromJson(response.body(), new TypeToken<List<PetEntity>>() {
        }.getType());
    }

    public ApiResponse deleteById(long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", URL_MAIN, URL_PET, id)))
                .DELETE()
                .build();
        response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), ApiResponse.class);
    }

    public Map<String, Integer> mapOfStatusCodesToQuantities() throws IOException, InterruptedException {
        String url = String.format("%s%s", URL_STORE, URL_INVENTORY);
        response = getGETResponse(url);
        return GSON.fromJson(response.body(), new TypeToken<Map<String, Integer>>() {
        }.getType());
    }

    public OrderEntity createOrder(OrderEntity orderEntity) throws IOException, InterruptedException {
        String url = String.format("%s%s", URL_STORE, URL_ORDER);
        response = getPOSTResponse(url, orderEntity);
        return GSON.fromJson(response.body(), OrderEntity.class);
    }


    public OrderEntity getOrderById(long id) throws IOException, InterruptedException {
        String urlUserById = String.format("%s%s/%d", URL_STORE, URL_ORDER, id);
        response = getGETResponse(urlUserById);
        return GSON.fromJson(response.body(), OrderEntity.class);
    }

    public ApiResponse deleteOrderById(long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", URL_STORE, URL_ORDER, id)))
                .DELETE()
                .build();
        response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), ApiResponse.class);
    }

    public ApiResponse createWithListUsers(List<UserEntity> entity) throws IOException, InterruptedException {
        String url = String.format("%s%s", URL_USER, URL_CREATE_WITH_LIST);
        response = getPOSTResponse(url, entity);
        return GSON.fromJson(response.body(), ApiResponse.class);
    }

    public UserEntity getUserByName(String name) throws IOException, InterruptedException {
        String url = String.format("%s/%s", URL_USER, name);
        response = getGETResponse(url);
        return GSON.fromJson(response.body(), UserEntity.class);
    }

    public ApiResponse updateUserByName(String name, UserEntity userEntity) throws IOException, InterruptedException {
        String url = String.format("%s/%s", URL_USER, name);
        response = getPUTResponse(url, userEntity);
        return GSON.fromJson(response.body(), ApiResponse.class);
    }

    public ApiResponse deleteUserByName(String name) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/%s", URL_USER, name)))
                .DELETE()
                .build();
        response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), ApiResponse.class);
    }

    public ApiResponse createUser(UserEntity userEntity) throws IOException, InterruptedException {
        response = getPOSTResponse(URL_USER, userEntity);
        return GSON.fromJson(response.body(), ApiResponse.class);
    }

    private HttpResponse<String> getPUTResponse(String url, Object entity) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(GSON.toJson(entity)))
                .header("Content-type", "application/json")
                .build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getPOSTResponse(String url, Object entity) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(entity)))
                .header("Content-type", "application/json")
                .build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getGETResponse(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
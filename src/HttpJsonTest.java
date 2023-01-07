import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.*;
import java.util.*;



public class HttpJsonTest {
    public void getUsersList() throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.noBody();
        HttpResponse<String> response = getResponse("https://jsonplaceholder.typicode.com/users","GET",bodyPublisher);
        System.out.println("List of users =  " + response.body());
    }

    public void createNewUser(String line) throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(line);
        HttpResponse<String> response = getResponse("https://jsonplaceholder.typicode.com/users","POST",bodyPublisher);
        System.out.println("request = " + response.body());
    }

    public void deleteUser(String line) throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(line);
        HttpResponse<String> response = getResponse("https://jsonplaceholder.typicode.com/users","DELETE",bodyPublisher);
        System.out.println("request = " + response);
    }

    public void updateUser(String line) throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(line);
        HttpResponse<String> response = getResponse("https://jsonplaceholder.typicode.com/users","PUT",bodyPublisher);
        System.out.println("request = " + response.body());
    }

    public void getUserById(String id) throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.noBody();
        String url = String.format("https://jsonplaceholder.typicode.com/users/%s", id);
        HttpResponse<String> response = getResponse(url,"GET",bodyPublisher);
        System.out.println("userById = " + response.body());
    }

    public void getUserByName(String userName) throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.noBody();
        String url = String.format("https://jsonplaceholder.typicode.com/users?username=%s", userName);
        HttpResponse<String> response = getResponse(url,"GET",bodyPublisher);
        System.out.println("userByName = " + response.body());
    }
    public void showAllOpenTasks(String id) throws IOException, InterruptedException{
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.noBody();
        String url = String.format("https://jsonplaceholder.typicode.com/users/%s/todos",id);
        HttpResponse<String> response = getResponse(url,"GET",bodyPublisher);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<UserTodoList>>() {}.getType();
        List<UserTodoList> list = gson.fromJson(response.body(), type);
        for (UserTodoList user : list) {
            if (!user.isCompleted()){
                System.out.println(user);
            }
        }
    }
    public void showAllCommentsToPost(String id) throws IOException, InterruptedException{
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.noBody();
        String url = String.format("https://jsonplaceholder.typicode.com/users/%s/posts",id);
        HttpResponse<String> response = getResponse(url,"GET",bodyPublisher);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<UserPosts>>() {}.getType();
        List<UserPosts> list = gson.fromJson(response.body(), type);
        int lastPost = list.get(list.size() - 1).getId();
        String line = String.format("https://jsonplaceholder.typicode.com/posts/%s/comments",lastPost);
        HttpResponse<String> allPosts = getResponse(line,"GET",bodyPublisher);
        System.out.println("response1.body() = " + allPosts.body());
        gson.toJson(allPosts.body(), new FileWriter(String.format("user-%s-post-%s-comments.json",id,lastPost)));
    }
    public HttpResponse<String> getResponse(String url,String method,HttpRequest.BodyPublisher body) throws IOException,InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .header("Content-type", " application/json")
                .method(method, body)
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}

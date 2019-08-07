package cn.cityworks.graphql.config;

import cn.cityworks.graphql.domain.User;
import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GraphQLDataFetchers {

    //模拟数据源
    private static List<Map<String, String>> books = Arrays.asList(
            ImmutableMap.of("id", "book-1",
                    "name", "Harry Potter and the Philosopher's Stone",
                    "pageCount", "223",
                    "authorId", "author-1"),
            ImmutableMap.of("id", "book-2",
                    "name", "Moby Dick",
                    "pageCount", "635",
                    "authorId", "author-2"),
            ImmutableMap.of("id", "book-3",
                    "name", "Interview with the vampire",
                    "pageCount", "371",
                    "authorId", "author-3")
    );

    //模拟数据源
    private static List<Map<String, String>> authors = Arrays.asList(
            ImmutableMap.of("id", "author-1",
                    "firstName", "Joanne",
                    "lastName", "Rowling"),
            ImmutableMap.of("id", "author-2",
                    "firstName", "Herman",
                    "lastName", "Melville"),
            ImmutableMap.of("id", "author-3",
                    "firstName", "Anne",
                    "lastName", "Rice")
    );

    public DataFetcher getBookByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String bookId = dataFetchingEnvironment.getArgument("id");
            return books
                    .stream()
                    .filter(book -> book.get("id").equals(bookId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getAuthorByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String authorId = dataFetchingEnvironment.getArgument("id");
            if(authorId.equals("all")){
                return authors;
            } else {
                List<Map<String, String>> results = new ArrayList<>();
                Map<String, String> result = authors
                        .stream()
                        .filter(author -> author.get("id").equals(authorId))
                        .findFirst()
                        .orElse(null);
                results.add(result);
                return results;
            }
        };
    }

    public DataFetcher getUserByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            User user = new User();
            if(id.equals("all")){
                user.setId("all");
                user.setName("nameAll");
                user.setPwd("pwdAll");
                user.setDate(new Date());
                return user;
            } else {
                user.setId(id);
                user.setName("name"+id);
                user.setPwd("pwd"+id);
                user.setDate(new Date());
                return user;
            }
        };
    }

    public DataFetcher getAuthorDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String,String> book = dataFetchingEnvironment.getSource();
            String authorId = book.get("authorId");
            return authors
                    .stream()
                    .filter(author -> author.get("id").equals(authorId))
                    .findFirst()
                    .orElse(null);
        };
    }
}

package cn.cityworks.graphql.config;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {

    private GraphQL graphQL;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        //声明向外暴露的方法
        Map<String, DataFetcher> dataFetchersMap = new LinkedHashMap<>();
        dataFetchersMap.put("bookById", graphQLDataFetchers.getBookByIdDataFetcher());
        dataFetchersMap.put("authorById", graphQLDataFetchers.getAuthorByIdDataFetcher());
        dataFetchersMap.put("userById", graphQLDataFetchers.getUserByIdDataFetcher());
        //声明自定义标量
        GraphQLScalarType dateScalarType = new DateScalarType();
        return RuntimeWiring.newRuntimeWiring()
                //暴露方法
                .type(newTypeWiring("Query")
                        .dataFetchers(dataFetchersMap))
                //为关联提供查询
                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                //自定义标量
                .scalar(dateScalarType)
                .build();
    }

}

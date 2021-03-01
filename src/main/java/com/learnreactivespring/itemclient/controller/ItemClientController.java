package com.learnreactivespring.itemclient.controller;

import com.learnreactivespring.itemclient.domain.Item;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemClientController {

    WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping("/client/retrieve")
    public Flux<Item> getAllItemsUsingRetrieve() {
        return webClient.get().uri("/v1/items")
                .retrieve()
                .bodyToFlux(Item.class)
                .log("Items in client project");
    }

    @GetMapping("/client/exchange")
    public Flux<Item> getAllItemsUsingExchange() {
        return webClient.get().uri("/v1/items")
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Item.class))
                .log("Items in client project exchange: ");
    }


    @GetMapping("/client/retrieve/item")
    public Mono<Item> getItemUsingRetrieve() {

        String id = "ABC";

        return webClient.get().uri("/v1/items/{id}", id)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Single Item in client project");
    }

    @GetMapping("/client/exchange/item")
    public Mono<Item> getItemUsingExchange() {

        String id = "ABC";

        return webClient.get().uri("/v1/items/{id}", id)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log("Single Item in client project exchange: ");
    }

    @PostMapping("/client/createItem")
    public Mono<Item> createItem(@RequestBody Item item) {

        Mono<Item> itemMono = Mono.just(item);

        return webClient.post().uri("/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemMono, Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("created Item");
    }

    @PutMapping("/client/item/{id}")
    public Mono<Item> updateItem(@PathVariable String id, @RequestBody Item item) {

        Mono<Item> itemBody = Mono.just(item);

        return webClient.put().uri("/v1/items/{id}", id)
                .body(itemBody, Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Updated Item is: ");
    }

    @DeleteMapping("/client/item/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {

        return webClient.delete().uri("/v1/items/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .log("Deleted item is: ");
    }

}

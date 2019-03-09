package com.lens.learningspringboot.learningspringbootch2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class LearningSpringBootCh2Application {

	public static void main(String[] args) {
		SpringApplication.run(LearningSpringBootCh2Application.class, args);


		/*Flux.just("alpha","bravo","charlie")
				.map(String::toUpperCase)
				.flatMap(s -> Flux.fromArray(s.split("")))
				.groupBy(String::toString)
				.sort((o1,o2) -> o1.key().compareTo(o2.key()))
				.flatMap(group -> Mono.just(group.key()).and(group.count()))
				.map(keyAndCount ->
						keyAndCount.getT1() + " => " + keyAndCount.getT2())
				.subscribe(System.out::println);*/

		Flux.just("alpha","bravo","charlie")
				.map(String::toUpperCase)
				.flatMap(s -> Flux.fromArray(s.split("")))
				.groupBy(String::toString)
				.flatMap(group -> Mono.just(group.key()).and(group.count()))
				//.map(keyAndCount -> keyAndCount.toString())
				.subscribe(System.out::println);
	}

}


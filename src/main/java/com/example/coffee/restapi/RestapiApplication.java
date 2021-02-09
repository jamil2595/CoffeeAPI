package com.example.coffee.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.*;

@SpringBootApplication
public class RestapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestapiApplication.class, args);
	}

}

class Coffee{
	private String id;
	private String name;

	public Coffee(String id, String name){
		this.id = id;
		this.name = name;
	}

	public Coffee(String name){
		this(UUID.randomUUID().toString(),name);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name){
		this.name = name;
	}
}


@RestController
@RequestMapping("/coffees")
class RestApiDemoController{
	private List<Coffee> coffees = new ArrayList<>();
	List<Coffee> namesList = Arrays.asList(
			new Coffee("Cafe Cereza"),
			new Coffee("Cafe Ganador"),
			new Coffee("Cafe Lareno"),
			new Coffee("Cafe Tres Pontas")
	);

	public RestApiDemoController(){
		coffees.addAll(namesList);
	}

	@GetMapping
	Iterable<Coffee> getCoffees(){
		return coffees;
	}

	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable String id){
		for (Coffee coffee:coffees){
			if (coffee.getId().equals(id)){
				return Optional.of(coffee);
			}
		}
		return Optional.empty();
	}

	@PostMapping
	Coffee postCoffee(@RequestBody Coffee coffee){
		coffees.add(coffee);
		return coffee;
	}

	@PutMapping("/{id}")
	ResponseEntity<Coffee> putCoffee(@PathVariable String id,
									 @RequestBody Coffee coffee) {
		int coffeeIndex = -1;
		for (Coffee c : coffees) {
			if (c.getId().equals(id)) {
				coffeeIndex = coffees.indexOf(c);
				coffees.set(coffeeIndex, coffee);
			}
		}
		return (coffeeIndex == -1) ?
				new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED) :
				new ResponseEntity<>(coffee, HttpStatus.OK);
	}


	@DeleteMapping("/{id}")
	void deleteCoffee(@PathVariable String id){
		coffees.removeIf(c -> c.getId().equals(id));
	}

}

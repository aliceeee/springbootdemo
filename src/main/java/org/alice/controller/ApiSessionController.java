/**
 * 
 */
package org.alice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alice
 *
 */
@RestController
@RequestMapping("/api")
public class ApiSessionController {

	@RequestMapping("/auth")
	public String auth() {
		return "Greetings from Spring Boot!ad";
	}
}

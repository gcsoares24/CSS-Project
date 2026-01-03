package pt.ul.fc.css.urbanwheels.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.urbanwheels.dto.CreateAdminDTO;
import pt.ul.fc.css.urbanwheels.dto.CreateClientDTO;
import pt.ul.fc.css.urbanwheels.dto.CreateUserDTO;
import pt.ul.fc.css.urbanwheels.dto.UserDTO;
import pt.ul.fc.css.urbanwheels.dto.UserDetailsDTO;
import pt.ul.fc.css.urbanwheels.mapper.AdminMapper;
import pt.ul.fc.css.urbanwheels.mapper.ClientMapper;
import pt.ul.fc.css.urbanwheels.services.TripService;
import pt.ul.fc.css.urbanwheels.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
	
	
	@PostMapping("/createClient")
	@ApiOperation(value = "Create Client", notes = "Creates a new client user with subscription type")
	public ResponseEntity<UserDTO> createClient(@RequestBody CreateClientDTO dto) {
		CreateUserDTO userDTO = ClientMapper.fromCreateClientDTO(dto);
	    UserDTO created = userService.createUser(userDTO);
	    return ResponseEntity.ok(created);
	}
	
	@PostMapping("/createAdmin")
	@ApiOperation(value = "Create Admin", notes = "Creates a new admin user")
	public ResponseEntity<UserDTO> createAdmin(@RequestBody CreateAdminDTO dto) {
		CreateUserDTO userDTO = AdminMapper.fromCreateAdminDTO(dto);
	    UserDTO created = userService.createUser(userDTO);
	    return ResponseEntity.ok(created);
	}
	
	@GetMapping
    @ApiOperation(value = "Get all users", notes = "Returns a list of all users.")
    public ResponseEntity<List<UserDetailsDTO>> getAllUsers() {
        List<UserDetailsDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users); // 200 OK com lista de users
    }
	
	@GetMapping("/{id}")
    @ApiOperation(value = "Get user details", notes = "Returns the details of a user by ID")
    public ResponseEntity<UserDetailsDTO> getDetailsById(@PathVariable Long id) {
		UserDetailsDTO user = userService.getUserDetails(id);
        return ResponseEntity.ok(user); // 200 OK com detalhes do user
    }
	
	@PostMapping("/login")
	@ApiOperation(value = "User login", notes = "Authenticates a user and returns a token (user Id)")
	public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
	    // Chama o serviço para autenticação
	    String token = userService.loginClient(email);
	    return ResponseEntity.ok(token); // retorna 200 OK com token
	}

	
}

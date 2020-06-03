package com.pierina.psiweb.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.pierina.psiweb.modal.Paciente;
import com.pierina.psiweb.modal.Post;
import com.pierina.psiweb.modal.Profissional;
import com.pierina.psiweb.modal.User;
import com.pierina.psiweb.repository.PacienteRepository;
import com.pierina.psiweb.repository.PostRepository;
import com.pierina.psiweb.repository.ProfissionalRepository;

@Controller
public class UserController {
	@Autowired
	private PacienteRepository pacienteRepository;
	
	@Autowired
	private ProfissionalRepository profissionalRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	Paciente paciente =  new Paciente();
	Profissional profissional = new Profissional();
	Post post = new Post();
	User user = new User();
	
	@PostMapping("/registerPatient")
	public String savePaciente(@ModelAttribute("Paciente") Paciente formData,Model model) {
		paciente.setBirthDate(formData.getBirthDate());
		paciente.setEmail(formData.getEmail());
		paciente.setGender(formData.getGender());
		paciente.setLastName(formData.getLastName());
		paciente.setName(formData.getName());
		paciente.setPasswordHash(new BCryptPasswordEncoder().encode(formData.getPassword()));
		paciente.setSpecialty(formData.getSpecialty());
		
		pacienteRepository.save(paciente);
		return "feed";
	}
	
	@PostMapping("/registerProfessional")
	public String saveProfissional(@ModelAttribute("Profissional") Profissional formData, Model model) {
		profissional.setBirthDate(formData.getBirthDate());
		profissional.setEmail(formData.getEmail());
		profissional.setGender(formData.getGender());
		profissional.setLastName(formData.getLastName());
		profissional.setLocalizacao(formData.getLocalizacao());
		profissional.setName(formData.getName());
		profissional.setPasswordHash(new BCryptPasswordEncoder().encode(formData.getPassword()));
		profissional.setPreco(formData.getPreco());
		profissional.setSpecialty(formData.getSpecialty());
		
		profissionalRepository.save(profissional);
		return "feed";
	}
	
//	@PostMapping("/feed")
//	public String savePost(@ModelAttribute("Post") Post formData, Model model) {
//		post = formData;
//		post.setUserEmail(user.getUserEmail());
//		postRepository.save(post);
//		
//		return"feed";
//	}
	
	@MessageMapping("/feed")
	@SendTo("/topic/posts")
	public Post sendMessage(@RequestBody Post formData) {
		System.out.println("Entrei socketController");
		
		post = formData;
		post.setUserEmail(user.getUserEmail());
		postRepository.save(post);
		
		System.out.println(post);
        return post;
	}
	
	@PostMapping("/login")
	public String validarUser(String username, String password) {
		Optional<Profissional> userFound = profissionalRepository.findByEmail(username);
		String name = new String();
		
		if (userFound.isPresent()) {
			if (new BCryptPasswordEncoder().matches(password, userFound.get().getPasswordHash())) {
				name = userFound.get().getName() + " " + userFound.get().getLastName();
				
				user.setLogado(true);
				user.setUserEmail(username);
				user.setGender(userFound.get().getGender());
				user.setName(name);
				user.setLocalizacao(userFound.get().getLocalizacao());
				user.setSpecialty(userFound.get().getSpecialty());
				
				return "feed";
			}else {
				return"login";
			}
			
		}else {
			Optional<Paciente> pacienteFound = pacienteRepository.findByEmail(username);
			
			if (pacienteFound.isPresent()) {
				if (new BCryptPasswordEncoder().matches(password, pacienteFound.get().getPasswordHash())) {
					user.setLogado(true);
					user.setUserEmail(username);
					user.setGender(pacienteFound.get().getGender());
					return "feed";
				}else {
					return"login";
				}
			}else {
				return"login";
			}
		}
		
	}
	
	@RequestMapping("/logout")
    public String loginPage(){
		user.setLogado(false);
    	return "home";
	}
	
	@RequestMapping("/feed")
    public String feedPage() {
		if (user.isLogado()) {
			return "feed";
		}else {
			return "home";
		}
    	
    }
	
	@RequestMapping("/profile")
    public ModelAndView  profilePage(){
		ModelAndView modelAndView = new ModelAndView("home");
		
		if (user.isLogado()) {
			modelAndView = new ModelAndView("profile");
		}
		
		modelAndView.addObject("user", user);
		return modelAndView;
	}
}

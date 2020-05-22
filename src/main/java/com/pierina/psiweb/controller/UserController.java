package com.pierina.psiweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.pierina.psiweb.modal.Paciente;
import com.pierina.psiweb.modal.Profissional;
import com.pierina.psiweb.repository.PacienteRepository;
import com.pierina.psiweb.repository.ProfissionalRepository;

@Controller
public class UserController {
	@Autowired
	private PacienteRepository pacienteRepository;
	
	@Autowired
	private ProfissionalRepository profissionalRepository;
	
	Paciente paciente =  new Paciente();
	Profissional profissional = new Profissional();
	
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
}

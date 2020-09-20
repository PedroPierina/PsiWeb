package com.pierina.psiweb.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.common.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;

import com.pierina.psiweb.modal.Comment;
import com.pierina.psiweb.modal.Friend;
import com.pierina.psiweb.modal.FriendPaciente;
import com.pierina.psiweb.modal.Message;
import com.pierina.psiweb.modal.Paciente;
import com.pierina.psiweb.modal.Post;
import com.pierina.psiweb.modal.Profissional;
import com.pierina.psiweb.modal.User;
import com.pierina.psiweb.repository.CommentRepository;
import com.pierina.psiweb.repository.FriendPacienteRepository;
import com.pierina.psiweb.repository.FriendRepository;
import com.pierina.psiweb.repository.MessageRepository;
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

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private FriendRepository friendRepository;

	@Autowired
	private FriendPacienteRepository friendPacienteRepository;

	@Autowired
	private MessageRepository messageRepository;

	Paciente paciente = new Paciente();
	Profissional profissional = new Profissional();
	Post post = new Post();
	User user = new User();
//	File file =  new File("/psiweb/src/main/resources/static/assets/img/default-user-male.png");
	String encodedfile;
	private BufferedImage img;
//	File file = new File(getClass().getClassLoader().getResource("teste.jpg").getFile());

//	  SavedModelBundle bundle=SavedModelBundle.load("/psiweb/target/classes/my_model","serve");
//      Session s = bundle.session();

	@PostMapping("/registerPatient")
	public String savePaciente(@ModelAttribute("Paciente") Paciente formData, Model model) {
		paciente.setBirthDate(formData.getBirthDate());
		paciente.setEmail(formData.getEmail());
		paciente.setGender(formData.getGender());
		paciente.setLastName(formData.getLastName());
		paciente.setName(formData.getName());
		paciente.setPasswordHash(new BCryptPasswordEncoder().encode(formData.getPassword()));
		paciente.setSpecialty(formData.getSpecialty());
		paciente.setProfilePicture(
				"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");

		pacienteRepository.save(paciente);
		return "login";
	}

	@PostMapping("/registerProfessional")
	public String saveProfissional(@ModelAttribute("Profissional") Profissional formData, Model model)
			throws IOException {
		profissional.setBirthDate(formData.getBirthDate());
		profissional.setEmail(formData.getEmail());
		profissional.setGender(formData.getGender());
		profissional.setLastName(formData.getLastName());
		profissional.setLocalizacao(formData.getLocalizacao());
		profissional.setName(formData.getName());
		profissional.setPasswordHash(new BCryptPasswordEncoder().encode(formData.getPassword()));
		profissional.setPreco(formData.getPreco());
		profissional.setSpecialty(formData.getSpecialty());
		profissional.setProfilePicture(
				"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");
		profissional.setCrp(formData.getCrp());
		profissional.setLinkPublicacoes(formData.getLinkPublicacoes());

		profissionalRepository.save(profissional);
		return "login";
	}

	@PostMapping("/updateProfessional")
	public ModelAndView updateProfissional(@ModelAttribute("Profissional") Profissional formData, Model model)
			throws IOException {
		ModelAndView modelAndView = new ModelAndView("profile");
		profissionalRepository.deleteByEmail(formData.getEmail());
		String name = new String();

		profissional.setBirthDate(formData.getBirthDate());
		profissional.setEmail(formData.getEmail());
		profissional.setGender(user.getGender());
		profissional.setLastName(formData.getLastName());
		profissional.setLocalizacao(formData.getLocalizacao());
		profissional.setName(formData.getName());
		profissional.setPasswordHash(new BCryptPasswordEncoder().encode(formData.getPassword()));
		profissional.setPreco(formData.getPreco());
		profissional.setSpecialty(formData.getSpecialty());
		profissional.setCrp(formData.getCrp());
		profissional.setLinkPublicacoes(formData.getLinkPublicacoes());
		profissional.setProfilePicture(formData.getProfilePicture());
		profissional.setBirthDate(user.getBirthDate());

		profissionalRepository.save(profissional);

		name = profissional.getName() + " " + profissional.getLastName();
		LocalDate birthDate = LocalDate.parse(profissional.getBirthDate());
		Period idadeCompleta = Period.between(birthDate, LocalDate.now());

		user.setLogado(true);
		user.setUserEmail(formData.getEmail());
		user.setGender(profissional.getGender());
		user.setName(name);
		user.setLocalizacao(profissional.getLocalizacao());
		user.setSpecialty(profissional.getSpecialty());
		user.setIdade(idadeCompleta.getYears());
		user.setProfilePicture(profissional.getProfilePicture());
		user.setCrp(profissional.getCrp());
		user.setType(1);
		user.setId(profissional.getId());
		user.setPassword(profissional.getPasswordHash());
		user.setFirstName(profissional.getName());
		user.setLastName(profissional.getLastName());
		user.setBirthDate(profissional.getBirthDate());
		user.setGender(profissional.getGender());
		user.setPreco(profissional.getPreco());

		modelAndView.addObject("user", user);
		return modelAndView;
	}

	@PostMapping("/updatePaciente")
	public ModelAndView updatePaciente(@ModelAttribute("Paciente") Paciente formData, Model model) throws IOException {
		ModelAndView modelAndView = new ModelAndView("profilePaciente");
		pacienteRepository.deleteByEmail(formData.getEmail());
		String name = new String();

		paciente.setBirthDate(formData.getBirthDate());
		paciente.setEmail(formData.getEmail());
		paciente.setGender(user.getGender());
		paciente.setLastName(formData.getLastName());
		paciente.setName(formData.getName());
		paciente.setPasswordHash(new BCryptPasswordEncoder().encode(formData.getPassword()));
		paciente.setSpecialty(formData.getSpecialty());
		paciente.setProfilePicture(formData.getProfilePicture());
		paciente.setBirthDate(user.getBirthDate());

		pacienteRepository.save(paciente);

		name = paciente.getName() + " " + paciente.getLastName();
		LocalDate birthDate = LocalDate.parse(paciente.getBirthDate());
		Period idadeCompleta = Period.between(birthDate, LocalDate.now());

		user.setLogado(true);
		user.setUserEmail(formData.getEmail());
		user.setGender(paciente.getGender());
		user.setName(name);
		user.setSpecialty(paciente.getSpecialty());
		user.setIdade(idadeCompleta.getYears());
		user.setProfilePicture(paciente.getProfilePicture());
		user.setType(2);
		user.setId(paciente.getId());
		user.setPassword(paciente.getPasswordHash());
		user.setFirstName(paciente.getName());
		user.setLastName(paciente.getLastName());
		user.setBirthDate(paciente.getBirthDate());
		user.setGender(paciente.getGender());

		modelAndView.addObject("user", user);
		return modelAndView;
	}

	@PostMapping("/feed")
	public ModelAndView savePost(@ModelAttribute("Post") Post formData, Model model) {
		ModelAndView modelAndView = new ModelAndView("feed");

		post = formData;
		post.setUserEmail(user.getUserEmail());
		post.setName(user.getName());
		postRepository.save(post);

		List<Post> posts = postRepository.findAllByUserEmail(user.getUserEmail());
		modelAndView.addObject("posts", posts);
		return modelAndView;
	}

	@PostMapping("/feed/comment")
	public ModelAndView saveComment(@ModelAttribute("Comment") Comment formData, Model model) {
		ModelAndView modelAndView = new ModelAndView("feed");
		Optional<Post> tempPost = postRepository.findById(formData.getIdPost());

		Comment commentario = formData;
		commentario.setName(user.getName());

		if (tempPost.isPresent()) {
			Post tempFoundPost = new Post();
			List<Comment> comments = tempPost.get().getComments();
			comments.add(commentario);

			tempFoundPost.setBody(tempPost.get().getBody());
			tempFoundPost.setComments(comments);
			tempFoundPost.setCreatDate(tempPost.get().getCreatDate());
			tempFoundPost.setId(tempPost.get().getId());
			tempFoundPost.setName(tempPost.get().getName());
			tempFoundPost.setTitle(tempPost.get().getTitle());
			tempFoundPost.setUserEmail(tempPost.get().getUserEmail());

			commentario.setPost(tempFoundPost);

		}
		commentRepository.save(commentario);

		List<Post> posts = postRepository.findAllByUserEmail(user.getUserEmail());

		modelAndView.addObject("posts", posts);
		return modelAndView;
	}

	@PostMapping("/chat")
	public ModelAndView enviaMensagem(@ModelAttribute("Message") Message formData, Model model,
			@ModelAttribute("friendId") int friendId, @ModelAttribute("friendType") int friendType) {
		ModelAndView modelAndView = new ModelAndView("chat");

		if (user.isLogado()) {

			formData.setFromEmail(user.getUserEmail());
			formData.setFromName(user.getName());
			formData.setFromType(user.getType());
			if (friendType == 1) {
				formData.setToEmail(profissionalRepository.findById(friendId).get().getEmail());
			} else {
				formData.setToEmail(pacienteRepository.findById(friendId).get().getEmail());
			}

			formData.setToType(friendType);

			messageRepository.save(formData);

			if (user.getType() == 1) {
				List<Friend> friends = friendRepository
						.findAllByProfissional(profissionalRepository.findByEmail(user.getUserEmail()));

				modelAndView.addObject("friends", friends);
				modelAndView.addObject("chatFriend", friends.get(0));

				if (friends.get(0).getType() == 1) {
					List<Message> messages = messageRepository.findByfromEmailtoEmail(user.getUserEmail(),
							profissionalRepository.findById(friends.get(0).getType()).get().getEmail());
					messages.addAll(messageRepository.findByfromEmailtoEmail(
							profissionalRepository.findById(friends.get(0).getType()).get().getEmail(),
							user.getUserEmail()));

					Collections.sort(messages);

					modelAndView.addObject("messages", messages);

				} else {
					List<Message> messages = messageRepository.findByfromEmailtoEmail(user.getUserEmail(),
							pacienteRepository.findById(friends.get(0).getType()).get().getEmail());
					messages.addAll(messageRepository.findByfromEmailtoEmail(
							pacienteRepository.findById(friends.get(0).getType()).get().getEmail(),
							user.getUserEmail()));

					Collections.sort(messages);

					modelAndView.addObject("messages", messages);
				}

			} else {
				List<FriendPaciente> friends = friendPacienteRepository
						.findAllByPaciente(pacienteRepository.findByEmail(user.getUserEmail()));
				modelAndView.addObject("friends", friends);
				modelAndView.addObject("chatFriend", friends.get(0));
			}
		}

		modelAndView.addObject("user", user);
		return modelAndView;
	}

//	Socket
//	@MessageMapping("/feed")
//	@SendTo("/topic/posts")
//	public Post sendMessage(@RequestBody Post formData) {
//		System.out.println("Entrei socketController");
//		
//		post = formData;
//		post.setUserEmail(user.getUserEmail());
//		postRepository.save(post);
//		
//		System.out.println(post);
//        return post;
//	}

	@PostMapping("/login")
	public ModelAndView validarUser(String username, String password) throws ParseException, IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
		Optional<Profissional> userFound = profissionalRepository.findByEmail(username);
		String name = new String();
		ModelAndView modelAndView = new ModelAndView("login");
		
//		String entrada = "A psicologia humanista é um ramo da psicologia em geral, e da psicoterapia, considerada como a terceira força, ao lado da psicanálise e da psicologia comportamental. A psicologia humanista surgiu como uma reação ao determinismo dominante nas outras práticas psicoterapêuticas, ensinando que o ser humano possui em si uma força de autorrealização, que conduz o indivíduo ao desenvolvimento de uma personalidade criativa e saudável.";
		String fullmodel = new ClassPathResource("saved_model.h5").getFile().getPath();
		MultiLayerNetwork model = KerasModelImport.importKerasSequentialModelAndWeights(fullmodel);
		
		if (userFound.isPresent()) {
			if (new BCryptPasswordEncoder().matches(password, userFound.get().getPasswordHash())) {
				name = userFound.get().getName() + " " + userFound.get().getLastName();
				LocalDate birthDate = LocalDate.parse(userFound.get().getBirthDate());
				Period idadeCompleta = Period.between(birthDate, LocalDate.now());

				user.setLogado(true);
				user.setUserEmail(username);
				user.setGender(userFound.get().getGender());
				user.setName(name);
				user.setLocalizacao(userFound.get().getLocalizacao());
				user.setSpecialty(userFound.get().getSpecialty());
				user.setIdade(idadeCompleta.getYears());
				user.setProfilePicture(userFound.get().getProfilePicture());
				user.setCrp(userFound.get().getCrp());
				user.setType(1);
				user.setId(userFound.get().getId());
				user.setPassword(userFound.get().getPasswordHash());
				user.setFirstName(userFound.get().getName());
				user.setLastName(userFound.get().getLastName());
				user.setBirthDate(userFound.get().getBirthDate());
				user.setGender(userFound.get().getGender());
				user.setPreco(userFound.get().getPreco());

				List<Post> posts = postRepository.findAllByUserEmail(user.getUserEmail());
//				System.out.println(user.getId());
				List<Friend> friends = friendRepository.findAllByProfissional(userFound);
				modelAndView = new ModelAndView("feed");

				for (Friend friend : friendRepository
						.findAllByProfissional(profissionalRepository.findByEmail(user.getUserEmail()))) {
					Optional<Profissional> optPro = profissionalRepository.findById(friend.getFriendId());

					if (optPro.isPresent()) {
						String proEmail = optPro.get().getEmail();
						posts.addAll(postRepository.findAllByUserEmail(proEmail));
					} else {
						Optional<Paciente> optPaciente = pacienteRepository.findById(friend.getFriendId());
						if (optPaciente.isPresent()) {
							String pacEmail = optPaciente.get().getEmail();
							posts.addAll(postRepository.findAllByUserEmail(pacEmail));
						}
					}

				}

				modelAndView.addObject("posts", posts);
				modelAndView.addObject("friends", friends);

//				if (friends.isEmpty()) {
//					System.out.println("Vazio");
//				}else {
//					System.out.println("Tem coisa");
//					System.out.println(friends.get(0).getName());
//				}

				return modelAndView;

			} else {
				return modelAndView;
			}

		} else {
			Optional<Paciente> pacienteFound = pacienteRepository.findByEmail(username);
			if (pacienteFound.isPresent()) {
				if (new BCryptPasswordEncoder().matches(password, pacienteFound.get().getPasswordHash())) {
					name = pacienteFound.get().getName() + " " + pacienteFound.get().getLastName();
					LocalDate birthDate = LocalDate.parse(pacienteFound.get().getBirthDate());
					Period idadeCompleta = Period.between(birthDate, LocalDate.now());

					user.setLogado(true);
					user.setUserEmail(username);
					user.setGender(pacienteFound.get().getGender());
					user.setName(name);
					user.setIdade(idadeCompleta.getYears());
					user.setProfilePicture(pacienteFound.get().getProfilePicture());
					user.setType(2);
					user.setId(pacienteFound.get().getId());
					user.setPassword(pacienteFound.get().getPasswordHash());
					user.setFirstName(pacienteFound.get().getName());
					user.setLastName(pacienteFound.get().getLastName());
					user.setBirthDate(pacienteFound.get().getBirthDate());
					user.setGender(pacienteFound.get().getGender());

					List<Post> posts = postRepository.findAllByUserEmail(user.getUserEmail());
					modelAndView = new ModelAndView("feed");
					List<FriendPaciente> friends = friendPacienteRepository
							.findAllByPaciente(pacienteRepository.findByEmail(user.getUserEmail()));
					modelAndView.addObject("friends", friends);

					for (FriendPaciente friend : friendPacienteRepository
							.findAllByPaciente(pacienteRepository.findByEmail(user.getUserEmail()))) {
						Optional<Profissional> optPro = profissionalRepository.findById(friend.getFriendId());

						if (optPro.isPresent()) {
							String proEmail = optPro.get().getEmail();
							posts.addAll(postRepository.findAllByUserEmail(proEmail));
						} else {
							Optional<Paciente> optPaciente = pacienteRepository.findById(friend.getFriendId());
							if (optPaciente.isPresent()) {
								String pacEmail = optPaciente.get().getEmail();
								posts.addAll(postRepository.findAllByUserEmail(pacEmail));
							}
						}

					}

					modelAndView.addObject("posts", posts);

					return modelAndView;
				} else {
					return modelAndView;
				}
			} else {
				return modelAndView;
			}
		}

	}

	@RequestMapping("/logout")
	public String loginPage() {
		user.setLogado(false);
		return "home";
	}

	@RequestMapping("/feed")
	public ModelAndView feedPage() {
//		puxar todos posts dos amigos
		ModelAndView modelAndView = new ModelAndView("home");

		if (user.isLogado()) {
			List<Post> posts = postRepository.findAllByUserEmail(user.getUserEmail());
			modelAndView = new ModelAndView("feed");

			if (user.getType() == 1) {
				for (Friend friend : friendRepository
						.findAllByProfissional(profissionalRepository.findByEmail(user.getUserEmail()))) {
					Optional<Profissional> optPro = profissionalRepository.findById(friend.getFriendId());

					if (optPro.isPresent()) {
						String proEmail = optPro.get().getEmail();
						posts.addAll(postRepository.findAllByUserEmail(proEmail));
					} else {
						Optional<Paciente> optPaciente = pacienteRepository.findById(friend.getFriendId());
						if (optPaciente.isPresent()) {
							String pacEmail = optPaciente.get().getEmail();
							posts.addAll(postRepository.findAllByUserEmail(pacEmail));
						}
					}

				}
			} else {
				for (FriendPaciente friend : friendPacienteRepository
						.findAllByPaciente(pacienteRepository.findByEmail(user.getUserEmail()))) {
					Optional<Profissional> optPro = profissionalRepository.findById(friend.getFriendId());

					if (optPro.isPresent()) {
						String proEmail = optPro.get().getEmail();
						posts.addAll(postRepository.findAllByUserEmail(proEmail));
					} else {
						Optional<Paciente> optPaciente = pacienteRepository.findById(friend.getFriendId());
						if (optPaciente.isPresent()) {
							String pacEmail = optPaciente.get().getEmail();
							posts.addAll(postRepository.findAllByUserEmail(pacEmail));
						}
					}

				}
			}

			modelAndView.addObject("posts", posts);

			if (user.getType() == 1) {
				List<Friend> friends = friendRepository
						.findAllByProfissional(profissionalRepository.findByEmail(user.getUserEmail()));
				modelAndView.addObject("friends", friends);
			} else {
				List<FriendPaciente> friends = friendPacienteRepository
						.findAllByPaciente(pacienteRepository.findByEmail(user.getUserEmail()));
				modelAndView.addObject("friends", friends);
			}
			return modelAndView;
		} else {
			return modelAndView;
		}

	}

	@RequestMapping("/profile")
	public ModelAndView profilePage() {
		ModelAndView modelAndView = new ModelAndView("home");

		if (user.isLogado()) {
			if (user.getType() == 1) {
				modelAndView = new ModelAndView("profile");
			} else {
				modelAndView = new ModelAndView("profilePaciente");
			}
		}

		modelAndView.addObject("user", user);
		return modelAndView;
	}

	@RequestMapping("/profile/{id}/{type}")
	public ModelAndView searchProfilePage(@PathVariable("id") int id, @PathVariable("type") int type) {
		ModelAndView modelAndView = new ModelAndView("home");
		User temp = new User();
		String name = new String();

		if (user.isLogado()) {
			if (type == 1) {
				Optional<Profissional> pro = profissionalRepository.findById(id);
				if (pro.isPresent()) {
					name = pro.get().getName() + " " + pro.get().getLastName();
					LocalDate birthDate = LocalDate.parse(pro.get().getBirthDate());
					Period idadeCompleta = Period.between(birthDate, LocalDate.now());

					temp.setId(id);
					temp.setType(type);
					temp.setCrp(pro.get().getCrp());
					temp.setGender(pro.get().getGender());
					temp.setLocalizacao(pro.get().getLocalizacao());
					temp.setProfilePicture(pro.get().getProfilePicture());
					temp.setSpecialty(pro.get().getSpecialty());
					temp.setUserEmail(pro.get().getEmail());
					temp.setIdade(idadeCompleta.getYears());
					temp.setName(name);

					modelAndView = new ModelAndView("profileDefault");
					modelAndView.addObject("user", temp);

					return modelAndView;
				}
			} else {
				Optional<Paciente> pac = pacienteRepository.findById(id);

				name = pac.get().getName() + " " + pac.get().getLastName();
				LocalDate birthDate = LocalDate.parse(pac.get().getBirthDate());
				Period idadeCompleta = Period.between(birthDate, LocalDate.now());

				temp.setId(id);
				temp.setType(type);
				temp.setGender(pac.get().getGender());
				temp.setProfilePicture(pac.get().getProfilePicture());
				temp.setSpecialty(pac.get().getSpecialty());
				temp.setUserEmail(pac.get().getEmail());
				temp.setIdade(idadeCompleta.getYears());
				temp.setName(name);

				modelAndView = new ModelAndView("profileDefaultPaciente");
				modelAndView.addObject("user", temp);

				return modelAndView;
			}
		}
		return null;

	}

	@RequestMapping("/view/profilePhoto/{email}")
	public void viewProfilePhoto(@PathVariable("email") String email, HttpServletResponse response) throws IOException {
		Optional<Profissional> pro = profissionalRepository.findByEmail(email);

		if (pro.isPresent()) {
			response.setContentType("image/png");
			try (OutputStream out = response.getOutputStream()) {
				out.write(Base64.getDecoder().decode(pro.get().getProfilePicture()));
			}
		} else {
			Optional<Paciente> pac = pacienteRepository.findByEmail(email);
			if (pac.isPresent()) {
				response.setContentType("image/png");
				try (OutputStream out = response.getOutputStream()) {
					out.write(Base64.getDecoder().decode(pac.get().getProfilePicture()));
				}
			}
		}
	}

	@PostMapping("/profile")
	public ModelAndView editProfile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView("profile");
		modelAndView.addObject("user", user);

		System.out.println("Entrei");

		if (file.isEmpty()) {
			return modelAndView;
		} else {
			try {
				byte[] bytes = file.getBytes();
				String base64 = Base64.getEncoder().encodeToString(bytes);

				if (user.getType() == 1) {
					profissionalRepository.updatePicture(user.getUserEmail(), base64);
				} else {
					pacienteRepository.updatePicture(user.getUserEmail(), base64);
				}
				user.setProfilePicture(base64);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return modelAndView;
	}

	@PostMapping("/search")
	public ModelAndView search(@RequestParam("search") String nome, Model model) {
		List<Profissional> achadosPro = new ArrayList<Profissional>();
		List<Paciente> achadosPac = new ArrayList<Paciente>();
		List<Profissional> achadosTotal = new ArrayList<Profissional>();
		List<User> users = new ArrayList<>();
		User temp = new User();
		String name = new String();
		ModelAndView modelAndView = new ModelAndView("searchResults");

		String[] splitted = nome.split("\\s+");

		if (splitted.length == 1) {
			achadosPro = profissionalRepository.searchSimple(splitted[0]);
			achadosPac = pacienteRepository.searchSimple(splitted[0]);
		} else {
			achadosPro = profissionalRepository.search(splitted[0], splitted[1]);
			achadosPac = pacienteRepository.search(splitted[0], splitted[1]);
		}

		for (Profissional pro : achadosPro) {
			name = pro.getName() + " " + pro.getLastName();
			LocalDate birthDate = LocalDate.parse(pro.getBirthDate());
			Period idadeCompleta = Period.between(birthDate, LocalDate.now());

			temp.setUserEmail(pro.getEmail());
			temp.setGender(pro.getGender());
			temp.setName(name);
			temp.setLocalizacao(pro.getLocalizacao());
			temp.setSpecialty(pro.getSpecialty());
			temp.setIdade(idadeCompleta.getYears());
			temp.setProfilePicture(pro.getProfilePicture());
			temp.setType(1);
			temp.setId(pro.getId());

			users.add(temp);

			temp = new User();
		}

		for (Paciente paciente : achadosPac) {
			name = paciente.getName() + " " + paciente.getLastName();
			LocalDate birthDate = LocalDate.parse(paciente.getBirthDate());
			Period idadeCompleta = Period.between(birthDate, LocalDate.now());

			temp.setLogado(true);
			temp.setUserEmail(paciente.getEmail());
			temp.setGender(paciente.getGender());
			temp.setName(name);
			temp.setIdade(idadeCompleta.getYears());
			temp.setProfilePicture(paciente.getProfilePicture());
			temp.setType(2);
			temp.setId(paciente.getId());

			users.add(temp);

			temp = new User();
		}

		modelAndView.addObject("user", users);

		return modelAndView;
	}

	@RequestMapping("/editProfile")
	public ModelAndView editProfilePage() {
		ModelAndView modelAndView = new ModelAndView("home");

		if (user.isLogado()) {
			if (user.getType() == 1) {
				modelAndView.addObject("user", user);
				modelAndView = new ModelAndView("editProfile");
			} else {
				modelAndView.addObject("user", user);
				modelAndView = new ModelAndView("editPacienteProfile");
			}
		}

		modelAndView.addObject("user", user);
		return modelAndView;
	}

	@RequestMapping("/deleteUser")
	public String deleteUser() {
		if (user.getType() == 1) {
			profissionalRepository.deleteByEmail(user.getUserEmail());
		} else {
			pacienteRepository.deleteByEmail(user.getUserEmail());
		}

		return "home";
	}

	@PostMapping("/follow/{id}/{type}")
	public String follow(@PathVariable("id") int id, @PathVariable("type") int type) {
		if (type == 1) {
			Optional<Profissional> proFriend = profissionalRepository.findById(id);

			if (user.getType() == 1) {
				Optional<Profissional> proFound = profissionalRepository.findByEmail(user.getUserEmail());
				Profissional proUser = new Profissional();
				Friend friend = new Friend();

				String name = proFriend.get().getName() + proFriend.get().getLastName();
				proUser = proFound.get();

				friend.setName(name);
				friend.setProfilePicture(proFriend.get().getProfilePicture());
				friend.setType(type);
				friend.setProfissional(proUser);
				friend.setFriendId(proFriend.get().getId());

				friendRepository.save(friend);

//				Addicionar o cara novo na lista
				List<Friend> friendList = proUser.getFriendList();
				friendList.add(friend);
				proUser.setFriendList(friendList);

			} else {
				Optional<Paciente> pacFound = pacienteRepository.findByEmail(user.getUserEmail());
				Paciente pacUser = new Paciente();
				FriendPaciente friend = new FriendPaciente();

				String name = proFriend.get().getName() + proFriend.get().getLastName();
				pacUser = pacFound.get();

				friend.setName(name);
				friend.setProfilePicture(proFriend.get().getProfilePicture());
				friend.setType(type);
				friend.setPaciente(pacUser);
				friend.setFriendId(proFriend.get().getId());

				friendPacienteRepository.save(friend);

//				Addicionar o cara novo na lista
				List<FriendPaciente> friendList = pacUser.getFriendList();
				friendList.add(friend);
				pacUser.setFriendList(friendList);
			}
		} else {
			Optional<Paciente> proFriend = pacienteRepository.findById(id);

			if (user.getType() == 1) {
				Optional<Profissional> proFound = profissionalRepository.findByEmail(user.getUserEmail());
				Profissional proUser = new Profissional();
				Friend friend = new Friend();

				String name = proFriend.get().getName() + proFriend.get().getLastName();
				proUser = proFound.get();

				friend.setName(name);
				friend.setProfilePicture(proFriend.get().getProfilePicture());
				friend.setType(type);
				friend.setProfissional(proUser);
				friend.setFriendId(proFriend.get().getId());

				friendRepository.save(friend);

//				Addicionar o cara novo na lista
				List<Friend> friendList = proUser.getFriendList();
				friendList.add(friend);
				proUser.setFriendList(friendList);

			} else {
				Optional<Paciente> pacFound = pacienteRepository.findByEmail(user.getUserEmail());
				Paciente pacUser = new Paciente();
				FriendPaciente friend = new FriendPaciente();

				String name = proFriend.get().getName() + proFriend.get().getLastName();
				pacUser = pacFound.get();

				friend.setName(name);
				friend.setProfilePicture(proFriend.get().getProfilePicture());
				friend.setType(type);
				friend.setPaciente(pacUser);
				friend.setFriendId(proFriend.get().getId());

				friendPacienteRepository.save(friend);

//				Addicionar o cara novo na lista
				List<FriendPaciente> friendList = pacUser.getFriendList();
				friendList.add(friend);
				pacUser.setFriendList(friendList);
			}
		}

		return "home";
	}

	@RequestMapping("/chat")
	public ModelAndView chat() {
		ModelAndView modelAndView = new ModelAndView("chat");

		if (user.isLogado()) {
			if (user.getType() == 1) {
				List<Friend> friends = friendRepository
						.findAllByProfissional(profissionalRepository.findByEmail(user.getUserEmail()));

				modelAndView.addObject("friends", friends);
				modelAndView.addObject("chatFriend", friends.get(0));

				if (friends.get(0).getType() == 1) {
					List<Message> messages = messageRepository.findByfromEmailtoEmail(user.getUserEmail(),
							profissionalRepository.findById(friends.get(0).getType()).get().getEmail());
					messages.addAll(messageRepository.findByfromEmailtoEmail(
							profissionalRepository.findById(friends.get(0).getType()).get().getEmail(),
							user.getUserEmail()));

					Collections.sort(messages);

					modelAndView.addObject("messages", messages);

				} else {
					List<Message> messages = messageRepository.findByfromEmailtoEmail(user.getUserEmail(),
							pacienteRepository.findById(friends.get(0).getType()).get().getEmail());
					messages.addAll(messageRepository.findByfromEmailtoEmail(
							pacienteRepository.findById(friends.get(0).getType()).get().getEmail(),
							user.getUserEmail()));

					Collections.sort(messages);

					modelAndView.addObject("messages", messages);
				}

			} else {
				List<FriendPaciente> friends = friendPacienteRepository
						.findAllByPaciente(pacienteRepository.findByEmail(user.getUserEmail()));
				modelAndView.addObject("friends", friends);
				modelAndView.addObject("chatFriend", friends.get(0));
			}
		}

		modelAndView.addObject("user", user);
		return modelAndView;
	}

	@RequestMapping("/chat/requestInvidualChat/{id}/{type}")
	public ModelAndView requestInvidualChat(@PathVariable("id") int id, @PathVariable("type") int type) {
		ModelAndView modelAndView = new ModelAndView("chat");

		if (user.isLogado()) {
			if (type == 1) {
				List<Message> messages = messageRepository.findByfromEmailtoEmail(user.getUserEmail(),
						profissionalRepository.findById(id).get().getEmail());
				messages.addAll(messageRepository.findByfromEmailtoEmail(
						profissionalRepository.findById(id).get().getEmail(), user.getUserEmail()));

				Collections.sort(messages);
				modelAndView.addObject("messages", messages);

			} else {
				List<Message> messages = messageRepository.findByfromEmailtoEmail(user.getUserEmail(),
						pacienteRepository.findById(id).get().getEmail());
				messages.addAll(messageRepository
						.findByfromEmailtoEmail(pacienteRepository.findById(id).get().getEmail(), user.getUserEmail()));

				Collections.sort(messages);
				modelAndView.addObject("messages", messages);
			}

			if (user.getType() == 1) {
				List<Friend> friends = friendRepository
						.findAllByProfissional(profissionalRepository.findByEmail(user.getUserEmail()));
				modelAndView.addObject("friends", friends);

				for (Friend friend : friends) {
					if (friend.getFriendId() == id) {
						modelAndView.addObject("chatFriend", friend);
					}
				}

			} else {
				List<FriendPaciente> friends = friendPacienteRepository
						.findAllByPaciente(pacienteRepository.findByEmail(user.getUserEmail()));
				modelAndView.addObject("friends", friends);

				for (FriendPaciente friend : friends) {
					if (friend.getFriendId() == id) {
						modelAndView.addObject("chatFriend", friend);
					}
				}
			}
		}

		modelAndView.addObject("user", user);
		return modelAndView;
	}

}

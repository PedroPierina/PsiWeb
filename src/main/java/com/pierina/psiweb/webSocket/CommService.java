//package com.pierina.psiweb.webSocket;
//
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//
//import com.pierina.psiweb.modal.Post;
//
//public class CommService {
//	private static SimpMessagingTemplate template;
//
//	  public static void setTemplate(SimpMessagingTemplate tmplt) {
//	    template = tmplt;
//	  }
//
//	  public static void send(Post post) {
//	    template.convertAndSend("/feed", post);
//	  }
//}

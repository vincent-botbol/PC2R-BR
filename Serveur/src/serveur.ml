open Unix

type client = { nom : string;
		thread: Thread.t
		  (* bateaux, pos etc plus tard*)
	      }

let threads : Thread.t list ref = ref []

let establish_server server_fun sockaddr =
  let domain = domain_of_sockaddr sockaddr in
  let sock = Unix.socket domain Unix.SOCK_STREAM 0 
  in Unix.bind sock sockaddr ;
    Unix.listen sock 3;
    while true do
      Printf.printf "mypid = %d\n%!" (getpid ());
      let (s, caller) = Unix.accept sock
      in
	threads := (Thread.create (
		     fun () -> 
		       let inchan = Unix.in_channel_of_descr s 
		       and outchan = Unix.out_channel_of_descr s 
		       in server_fun inchan outchan ;
			 close_in inchan ;
			 close_out outchan ;
			 close s
		    ) ())::!threads
	    done

(* adresse localhost : 127.0.1.1 *)
let get_my_addr () =
  (Unix.gethostbyname(Unix.gethostname())).Unix.h_addr_list.(0)
    
let main_serveur serv_fun =
  try
    let port = 2012 in 
    let mon_adresse = get_my_addr()
    in establish_server serv_fun  (Unix.ADDR_INET(mon_adresse, port))
  with
      Failure("int_of_string") -> 
        Printf.eprintf "serv_up : bad port number\n" ;;

let connexion_client ic oc =
  (* lire sur l'entrée : connect/nom sinon erreur *)
  (* envoi welcome *)
  (* .. *)
  (* synchronize sur le jeu *)
  ()

let uppercase_service ic oc =
  Printf.printf "serveur fun\n%!";
  try while true do    
    let s = input_line ic in 
    let r = String.uppercase s 
    in output_string oc (r^"\n") ; flush oc
  done
  with _ -> Printf.printf "Fin du traitement\n%!" ; exit 0

let () =
  main_serveur uppercase_service

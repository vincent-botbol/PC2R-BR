open Unix

type client = { nom : string;
		thread: Thread.t
		  (* bateaux, pos etc plus tard*)
	      }

let threads : Thread.t list ref = ref []

let clients = ref []

open Str    

(* adresse localhost : 127.0.1.1 *)
let get_my_addr () =
  (Unix.gethostbyname(Unix.gethostname())).Unix.h_addr_list.(0)

let my_input_line fd = 
   let s = " "  and  r = ref "" 
   in while (ThreadUnix.read fd s 0 1 > 0) && s.[0] <> '\n' do r := !r ^s done ;
      !r

let my_output_line fd str =
  ignore (ThreadUnix.write fd str 0 (String.length str))

let connexion_client s_descr =
  let regex_conn = regexp "^CONNECT/\\([^/]+\\)/$" (* Marche sans le $ *)
  and commande_recue = my_input_line s_descr in
    Printf.printf "commande reçue : %s\n%!" commande_recue;
    if string_match regex_conn commande_recue 0 then
      let nom = matched_group 1 commande_recue in
	my_output_line s_descr (Printf.sprintf "WELCOME/%s/\n" nom);
	clients:={nom=nom; thread=Thread.self ()}::!clients
    else
      my_output_line s_descr (Printf.sprintf "ACCESSDENIED/\n%!")

let stop_thread_client s_descr = 
  Printf.printf "Fin d'un thread client\n%!";
  Unix.close s_descr

let demarrer_thread_client (s_descr, s_addr) =
  Thread.create (fun () ->
		   (try 
		      connexion_client s_descr
		    with  
			exn  -> print_string (Printexc.to_string exn) ; print_newline());
		   stop_thread_client s_descr
		) ()


let establish_server sockaddr =
  let sock =  ThreadUnix.socket Unix.PF_INET Unix.SOCK_STREAM 0
  in Unix.bind sock sockaddr ;
    Unix.listen sock 3;
    while true do
      let (s_descr, s_addr) as arg = ThreadUnix.accept sock
      in
	(* Création du thread client *)
	threads:=demarrer_thread_client arg::!threads
    done

    
let start_serveur () =
  try
    let port = 2012 in 
    let mon_adresse = get_my_addr()
    in establish_server (Unix.ADDR_INET(mon_adresse, port))
  with
      Failure("int_of_string") -> 
        Printf.eprintf "serv_up : bad port number\n" ;;

let () =
  start_serveur ()

open Str

type phase =
  | CONNECTING
  | PREPARATION of int
  | YOURTURN
  | WAITING
  | DEAD

type etat = Alive | Dead

type case = int * int * etat


type bateau =
    { mutable cases : case list;
      mutable etat : etat
    }


type client = {
  nom : string;
  chan : Unix.file_descr;
  mutable bateaux: bateau list;
  mutable phase : phase;
  mutable drone : int * int
}

let clients = ref []

let spectators = ref []

let map = Array.make_matrix 16 16 []

let cmd_list = ref []

let clients_mutex = Mutex.create ()
let map_mutex = Mutex.create ()
let cmd_mutex = Mutex.create ()

let start_cond = Condition.create ()




module RegExp =
  struct
    (* Marche sans le $ *)
    let regex_conn = regexp (Printf.sprintf "^CONNECT/\\(.+\\)/%c?$" (char_of_int 13))
    let regex_quit = regexp (Printf.sprintf "^QUIT/%c?" (char_of_int 13))
    let str_entier = "\\([0-9]\\|1[0-5]\\)"
    let reg_entier = regexp str_entier
    let regex_putship = regexp ("^PUTSHIP/\\([A-H]/"^str_entier^"/\\)+")
    let reg_action = regexp "^ACTION/\\([UDLRE]/\\)+"
    let reg_chat = regexp (Printf.sprintf "^TALK/\\([^/]+\\)/%c?$" (char_of_int 13))
    let reg_spectator = regexp (Printf.sprintf "^SPECTATOR/%c?$" (char_of_int 13))
    let reg_register = regexp (Printf.sprintf "^REGISTER/\\([^/]+\\)/\\([^/]+\\)/%c?$" (char_of_int 13))
    let reg_login = regexp (Printf.sprintf "^LOGIN/\\([^/]+\\)/\\([^/]+\\)/%c?$" (char_of_int 13))
    let reg_exists = regexp "^\\([^/]+\\)/\\([^/]+\\)$"
    let reg_bye = regexp (Printf.sprintf "^BYE/%c?$" (char_of_int 13))
    let reg_playagain = regexp (Printf.sprintf "^PLAYAGAIN/%c?$" (char_of_int 13))
  end


module Utils =
  struct

    let gen_num = let c = ref 0 in (fun () -> incr c; !c)

    let gen_num2 = let c = ref 0 in (fun () -> incr c; !c)

    let random_drone () =
      Random.self_init();
      Random.int 16, Random.int 16

    let client_from_name name  =
      try
	List.find (fun c -> c.nom = name) !clients
      with
	| Not_found -> failwith "searching for inexistant client"
	  
    let client_from_fd fd =
      try
	List.find (fun c -> c.chan = fd) !clients
      with
	| Not_found -> failwith "searching for inexistant client"


    let names () = List.fold_left (fun acc c-> "/"^c.nom^acc) "" !clients

    let my_split str =
      ()

    let my_input_line  fd = 
      let s = " "  and  r = ref "" 
      in while (ThreadUnix.read fd s 0 1 > 0) && s.[0] <> '\n' do r := !r ^s done ;
      !r
	
    let my_output_line fd str =
      Printf.printf "TRACE : command send : %s\n%!" str;
      ignore (ThreadUnix.write fd str 0 (String.length str))
	
    (* adresse localhost : 1270.1.1 *)
    let get_my_addr () =
<<<<<<< HEAD
      (Unix.gethostbyname "192.168.1.74").Unix.h_addr_list.(0)
=======
      try
	(Unix.gethostbyname Sys.argv.(1)).Unix.h_addr_list.(0)
      with
	| Invalid_argument "index out of bounds" ->
<<<<<<< HEAD
	    (Unix.gethostbyname(Unix.gethostname())).Unix.h_addr_list.(0)
>>>>>>> update java
=======
	  (Unix.gethostbyname(Unix.gethostname())).Unix.h_addr_list.(0)
>>>>>>> Ajout spectateur Java
      (*(Unix.gethostbyname(Unix.gethostname())).Unix.h_addr_list.(0)*)
	

    let send_to_players msg s_descr =
      List.iter (fun c -> my_output_line c.chan msg) !clients

    let send_to_spectators msg s_descr =
      List.iter
	(fun spec_chan -> my_output_line spec_chan msg)
	!spectators

    let send_to_all msg s_descr =
      send_to_players msg s_descr;
      send_to_spectators msg s_descr

    let nb_state etat = List.length (List.filter (fun c -> c.phase = etat) !clients)

    let all_waiting () = List.for_all (fun c -> c.phase = WAITING) !clients

    let print_liste chan l =
      let rec aux = function
	| [x] -> Printf.fprintf chan "%d)%!" x
	| x::xs -> Printf.fprintf chan "%d," x;aux xs
	| [] -> Printf.fprintf chan ")%!"
      in
      Printf.fprintf chan "(%!";
      aux l

    let print_liste2 chan l =
      let rec aux = function
	| [x] -> Printf.fprintf chan "%s)%!" x
	| x::xs -> Printf.fprintf chan "%s," x;aux xs
	| [] -> Printf.fprintf chan ")%!"
      in
      Printf.fprintf chan "(%!";
      aux l

    let print_boat chan b =
      let rec aux = function
	| [(x,y,_)] -> Printf.fprintf chan "(%d,%d) ]%!" x y
	| (x,y,_)::xs -> Printf.fprintf chan "(%d,%d) ; " x y;aux xs
	| [] -> Printf.fprintf chan "]\n%!"
      in
      Printf.fprintf chan "[%!";
      aux b

    let nb_coups boats =
      List.fold_left
	(fun acc b ->
	  acc - (List.fold_left (fun acc (_,_,state) -> acc + (if state = Alive then 1 else 0)) 0 b.cases)
	)
	17 boats

    let add_cmd l s_descr =
      let rec aux acc = function
	| [] -> acc
	| [x] ->
	    if ((String.length x) = 1) && ((int_of_char x.[0]) = 13) then
	      acc
	    else
	      acc^x^"/"
	| x::xs -> aux (acc^x^"/") xs
      in
      let msg = (aux "" l) in
      Mutex.lock cmd_mutex;
      cmd_list := msg::!cmd_list;
      Mutex.unlock cmd_mutex;
      send_to_spectators (Printf.sprintf "%s\n%!" msg) s_descr

    let clean_map player =
      Array.iteri (fun i -> Array.iteri (fun j _ -> map.(i).(j) <- List.filter ((<>) player) map.(i).(j))) map

    let reset_client c =
      c.phase <- WAITING;
      c.bateaux <- [];
      c.drone <- random_drone ()

  end

open Utils




module Next =
  struct
    let cpt = ref 0


    let rec next () =
      let current = List.nth !clients !cpt in
      if current.phase = YOURTURN then
	current.phase <- WAITING;
      cpt := (!cpt + 1) mod (List.length !clients);
      let suiv = List.nth !clients !cpt in
      if suiv.phase = WAITING then
	begin
	  suiv.phase <- YOURTURN;
	  my_output_line suiv.chan
	    (Printf.sprintf "YOURTURN/%d/%c/%d/\n%!"
	       (fst suiv.drone)
	       (char_of_int ((snd suiv.drone) + 65))
	       (Utils.nb_coups suiv.bateaux)
	    )
	end
      else
	next ()
      



  end





module Register =
struct
<<<<<<< HEAD
=======

<<<<<<< HEAD
  let () = if not (Sys.file_exists "logins.txt") then ignore (open_out "logins.txt")
>>>>>>> update java
=======
  let () =
    if not (Sys.file_exists "./logins.txt") then
      ignore (open_out "logins.txt")
>>>>>>> Ajout spectateur Java

    let check_connect name = 
    let chan = open_in "logins.txt" in
      try
	let cont = ref true in
	while !cont do
	  let ligne = Str.split (regexp "/") (input_line chan) in
	  assert ((List.length ligne) = 2);
	  if (List.hd ligne) = name then cont := false
	done;
	Printf.printf "TRACE : Login %s is already used\n%!" name;
	!cont
      with
	| End_of_file -> Printf.printf "TRACE : Login %s is available\n%!" name;true
	| Assert_failure _ -> Printf.printf "TRACE.exn -> malformed logins file\n%!"; false

    let check_login name pswd =
      let chan = open_in "logins.txt" in
      try
	while input_line chan <> (name^"/"^pswd) do () done;
	true
      with
	| End_of_file -> false

    let add name pswd =
      let chan = open_out_gen [Open_append] 0 "logins.txt" in
      output_string chan (Printf.sprintf "%s/%s\n" name pswd);
      flush chan
	  

  end





module Stop =
  struct
    
    let stop_thread_client ?(timer:Thread.t option ref=ref None) s_descr = 
      Printf.printf "Fin d'un thread client\n%!";
      Unix.close s_descr;
      clients := List.filter (fun c -> c.chan <> s_descr) !clients;
      let name = try (Utils.client_from_fd s_descr).nom with Failure "searching for inexistant client" -> "" in
      clean_map name;
      if List.length !clients = 1 then
	begin
	  List.iter Utils.reset_client !clients;
	  match !timer with
	    |  Some _ -> timer := None
	    | _ -> ()
	end;
      Thread.exit ()
	
  end





module Connexion =

struct
  
  let timer = ref None

    let start_game () =
      Mutex.lock clients_mutex;
      let names = Utils.names() in
      List.iter (fun c -> my_output_line c.chan (Printf.sprintf "PLAYERS%s/\n" names)) !clients;
      List.iter (fun c -> c.phase <- PREPARATION 2) !clients;
      List.iter (fun c -> my_output_line c.chan (Printf.sprintf "SHIP/1/\n%!")) !clients;
      Mutex.unlock clients_mutex

	
    let timer_thread () =
      Unix.sleep 30;
      match !timer with
	| Some th ->
	  if th = Thread.self () then
	    begin
	      Printf.printf "Fin des 30 secondes, la partie va commencer\n%!";
	      start_game()
	    end
	  else
	    Printf.printf "TRACE : Timer inutile fini\n"
	| _ -> Printf.printf "TRACE : Timer inutile fini\n"



    exception Acces_denied

    let treat_connexion nom s_descr =
      if ((List.length !clients) < 4) &&
	(all_waiting ())
      then
	begin
	  let name =
	    let rec aux nom =
	      if (List.exists (fun c -> c.nom = nom) !clients) || not (Register.check_connect nom) then
		aux (nom^(string_of_int (Utils.gen_num2())))
	      else
		nom
	    in
	    aux nom
	  in
	  my_output_line s_descr (Printf.sprintf "WELCOME/%s/\n" name);
	  Mutex.lock clients_mutex;
	  (* TO DO : gerer le placement du drone *)
	  clients:={nom=name;chan=s_descr;bateaux=[];phase=WAITING;drone=Utils.random_drone()}::!clients;
	  Mutex.unlock clients_mutex;
	  match (nb_state WAITING) with
	    | 2 -> 
<<<<<<< HEAD
	      (*timer := Some (Thread.create timer_thread ()); *)
	      Printf.printf "2 joueurs sont connectés, lancement d'un timer de 30 secondes\n";
	      start_game ()
=======
	      timer := Some (Thread.create timer_thread ());
	      Printf.printf "2 joueurs sont connectés, lancement d'un timer de 30 secondes\n"
	      (*start_game ()*)
>>>>>>> update java
	    | 4 ->
	      timer := None;
	      Printf.printf "4 joueurs sont connectés, début de la partie\n";
	      start_game()
	    | _ -> ()
	end
      else
	raise Acces_denied

	  

    let connexion_client s_descr =
      let exit_value = ref 0 in
      let reg = RegExp.regex_conn in
      while !exit_value = 0 do
	try
	  let commande_recue = my_input_line s_descr in
	  Printf.printf "\ncommande reçue : %s\n%!" commande_recue;
	  if string_match reg commande_recue 0 then
	    let nom = matched_group 1 commande_recue in
	    treat_connexion nom s_descr;
	    exit_value := 1
	  else if string_match RegExp.reg_spectator commande_recue 0 then
	    exit_value := 2
	  else if string_match RegExp.reg_register commande_recue 0 then
	    let nom = matched_group 1 commande_recue in
	    let pswd = matched_group 2 commande_recue in
	    if Register.check_connect nom then
	      begin
		treat_connexion nom s_descr;
		Register.add nom pswd;
		Printf.printf "TRACE : login %s added with password %s\n%!" nom pswd;
		exit_value := 1
	      end
	    else
	      begin
		my_output_line s_descr (Printf.sprintf "ACCESSDENIED/\n%!");
		Stop.stop_thread_client s_descr
	      end
	  else if string_match RegExp.reg_login commande_recue 0 then
	    let nom = matched_group 1 commande_recue in
	    let pswd = matched_group 2 commande_recue in
	    if Register.check_login nom pswd then
	      begin
		Printf.printf "TRACE : correct login (%s) and password (%s)\n%!" nom pswd;
		treat_connexion nom s_descr;
		exit_value := 1
	      end
	    else
	      begin
		my_output_line s_descr (Printf.sprintf "ACCESSDENIED/\n%!");
		Stop.stop_thread_client s_descr
	      end
	  else if commande_recue = "" then
	    Stop.stop_thread_client ~timer:timer s_descr
	  else
	    my_output_line s_descr (Printf.sprintf "ACCESSDENIED/\n%!")
	with
	  | Acces_denied -> my_output_line s_descr (Printf.sprintf "ACCESSDENIED/\n%!")
      done;
      !exit_value

  end







module Placement =
  struct

    exception Wrong_position of string

    let rec check_straight_boat even = function
      | a::b::[] -> true
      | a::b::c::d::xs ->
	(if even then
	    b = d && ((a - c) = 1 || (a - c) = -1)
	 else
	    a = c && ((b - d) = 1 || (b - d) = -1)
	)
	&& check_straight_boat even (c::d::xs)
      | _ ->
	false

    let poslist_transfo l =
      let rec aux tmp = function
	| [] -> tmp
	| [x] ->
	  if x = "\013" then
	    tmp
	  else
	    raise (Wrong_position "last character is maxi chelou")
	| x::y::xs ->
	  if (String.length y) = 1 then
	    aux ((int_of_string x)::((int_of_char y.[0]) - 65)::tmp) xs 
	  else raise (Wrong_position "first position is not a letter")
      in
      aux [] l
	
    let add_boat poslist client n =
      let l = poslist_transfo poslist in
      Printf.printf "TRACE : positions received : %a\n%!" print_liste l;
      Printf.printf "TRACE : %d positions received\n%!" (List.length l);
      if ((List.length l) <> 2 * n) then raise (Wrong_position "Uncorrect boat length");
      Printf.printf "TRACE : boat length ok\n%!";
      if not ((check_straight_boat false l) || (check_straight_boat true l)) then
	raise (Wrong_position "you can't build such a boat, you fool !");
      Printf.printf "TRACE : boat disposition ok\n%!";
      let boat = ref [] in
      let rec add_case =
	function
	  | [] -> ()
	  | x::y::xs ->
	    if x >= 0 && x < 16 && y >= 0 && y < 16
	      && not (List.mem client.nom map.(x).(y))
	      && not (List.mem (x,y,Alive) !boat)
	    then
	      begin
		Printf.printf "TRACE : adding case (%d,%d) squatted by %a\n%!" x y Utils.print_liste2 map.(x).(y);
		boat := (x,y,Alive)::!boat;
		add_case xs
	      end
	    else
	      raise (Wrong_position (Printf.sprintf "maformed cmd for coordonates (%d,%d)" x y))
	  | _ -> raise (Wrong_position "Should not happen, uncorrect list")
      in
      add_case l;
      Printf.printf "TRACE : boat built <=> %a\n%!" print_boat !boat;
      client.bateaux <- {cases = !boat;etat=Alive}::client.bateaux;
      List.iter (fun (x,y,_) -> map.(x).(y) <- client.nom::map.(x).(y)) !boat;
      my_output_line client.chan "OK/\n";
      add_cmd ("PLAYERSHIP"::client.nom::poslist) client.chan
	
	

  end

	  



module End_of_game =
  struct

    

    let timer_thread () =
      Unix.sleep 30;
      List.iter (fun c -> if c.phase = DEAD then Stop.stop_thread_client c.chan)
    
    let new_game client =
      let cont = ref true in
      while !cont do
	let cmd = my_input_line client.chan in
	Printf.printf "\nTRACE : command received : %s\n%!" cmd;
	if string_match RegExp.reg_bye cmd 0 then
	  begin
	    Printf.printf "TRACE : player %s wants to leave\n%!" client.nom;
	    cont := false;
	    Stop.stop_thread_client client.chan
	  end
	else if string_match RegExp.reg_playagain cmd 0 then
	  begin
	    Printf.printf "TRACE : player %s wants to play again\n%!" client.nom;
	    cont := false;
	    reset_client client;
	    match nb_state WAITING with
	      | 2 -> Connexion.timer := Some (Thread.create Connexion.timer_thread ())
	      | 4 -> Connexion.timer := None; Connexion.start_game ()
	      | _ -> ()
	  end
      done
	    
	  

  end 



module Game =
  struct
    
    exception Uncorrect_action

    let check_action actions n pos_drone=
      let rec aux l bool nb (x,y) =
	Printf.printf "TRACE.tmp : check_action nb=%d length=%d\n%!" nb (List.length l);
	if x >= 0 && x < 16 && y >= 0 && y < 16 then
	  if (*nb = 0*) false then
	    List.length l < 2
	  else
	    match l with
	      | [] -> true
	      | "E"::xs  -> if bool then false else aux xs true (nb - 1) (x,y)
	      | "U"::xs -> aux xs bool (nb - 1) (x,y+1)
	      | "D"::xs -> aux xs bool (nb - 1) (x,y-1)
	      | "L"::xs -> aux xs bool (nb - 1) (x-1,y)
	      | "R"::xs -> aux xs bool (nb - 1) (x+1,y)
	      | [x] ->
		Printf.printf "TRACE : dernier caractere de code %d\n%!" (int_of_char x.[0]);
		x = "\013" && nb >= 0
	      | _ -> Printf.printf "TRACE.exn : Unknown action\n%!"; false
	else
	  false
      in
      aux actions false n pos_drone

    let switch_case_state boats state x y =
      List.iter
	(fun b -> b.cases <-
	  (List.map
	     (fun (xx,yy,etat) -> if x = xx & y = yy then (xx,yy,state) else (xx,yy,etat))
	     b.cases
	  )
	)
	boats
      
	    
    let end_game () = ()
      
      


      (* peut-on buter son bateau ?? *)
    let laserize s_descr x y =
      Printf.printf "TRACE : laserizing case (%d,%d)\n%!" x y;
      match map.(x).(y) with
	| [] -> my_output_line s_descr (Printf.sprintf "MISS/%d/%c/\n%!" x (char_of_int (y+65)))
	| l ->
	  my_output_line s_descr (Printf.sprintf "TOUCHE/%d/%c/\n%!" x (char_of_int (y+65)));
	  let game_over = ref [] in
	  List.iter
	    (fun name ->
	      let client = Utils.client_from_name name in
	      switch_case_state client.bateaux Dead x y;
	      my_output_line client.chan (Printf.sprintf "OUCH/%d/%c/\n%!" x (char_of_int (y+65)));
	      add_cmd ["PLAYEROUCH";client.nom;(string_of_int x);(String.make 1 (char_of_int (y + 65)))] s_descr;
	      (* client mort *)
	      if (nb_coups client.bateaux) = 17 then
		begin
		  client.phase <- DEAD;
		  send_to_all (Printf.sprintf "DEATH/%s/\n" name) client.chan;
		  game_over := name::!game_over
		end
	    )
	    l;
	  map.(x).(y) <- [];
	  if ((List.length !clients) - (nb_state DEAD)) < 2  then
	    match !game_over with
	      | [] -> raise Uncorrect_action
	      | [c] -> send_to_all (Printf.sprintf "AWINNERIS/%s/\n" c) s_descr; end_game ()
	      | _ -> send_to_all "DRAWGAME/\n" s_descr; end_game ()
	    
    

    let rec treat actions client =
      let step x y next =
	client.drone <- (x,y);
	add_cmd ["PLAYERMOVE";client.nom;(string_of_int x);(String.make 1 (char_of_int (y + 65)))] client.chan;
	treat next client
      in
      let (x,y) = client.drone in
      match actions with
	| [] -> ()
	| "E"::xs -> laserize client.chan x y; step x y xs
	| "U"::xs -> step x (y+1) xs
	| "D"::xs -> step x (y-1) xs
	| "L"::xs -> step (x-1) y xs
	| "R"::xs -> step (x+1) y xs
	| [x] ->
	  Printf.printf "TRACE : last action element is very chelou\n%!";
	  if x <> "\013" then raise Uncorrect_action
	| l -> Utils.print_liste2 stdout l; raise Uncorrect_action
	  
    
  end













let rec main_joueur s_descr =
  let client = Utils.client_from_fd s_descr in
  while true do
    let commande_recue = my_input_line client.chan in
    Printf.printf "\nTRACE : commande reçue = %s\n%!" commande_recue;
    let new_cmd = split (regexp "/") commande_recue in
    Printf.printf "TRACE : new command formed = %a\n%!" print_liste2 new_cmd;
    match client.phase, new_cmd with
      | _, "TALK"::msg::_ ->
	Printf.printf "TRACE : chat détecté %d\n%!" (List.length new_cmd);
	(*List.iter (fun s -> Printf.printf "TRACE : %s\n%!" s) l;*)
	send_to_all (Printf.sprintf "HEYLISTEN/%s/%s/\n" client.nom msg) client.chan
      | PREPARATION n, "PUTSHIP"::placements ->
	let true_n = n / 2 in
	Mutex.lock clients_mutex;
	begin
	  try
	    if true_n > 0 && true_n < 4 then
	      begin
		Placement.add_boat placements client true_n;
		client.phase <- PREPARATION (n + 1);
		(*add_cmd ("PLAYERSHIP"::client.nom::placements) s_descr;*)
		my_output_line client.chan (Printf.sprintf "SHIP/%d/\n%!" ((n+1)/2))
	      end
	    else if (true_n = 4) then
	      begin
		Placement.add_boat placements client 4;
		client.phase <- WAITING;
		(*add_cmd ("PLAYERSHIP"::client.nom::placements) s_descr;*)
		my_output_line client.chan (Printf.sprintf "ALLYOURBASE/\n%!");
		if List.for_all (fun c -> c.phase = WAITING) !clients then
		  Next.next ()
	      (* check si c'est le last *)
	      end
	    else
	      Printf.printf "TRACE.exn : pas cool\n%!"
	  with
	    |  Placement.Wrong_position msg ->
	      Printf.printf "TRACE.exn : Wrong_position exception (%s)\n%!" msg;
	      my_output_line client.chan (Printf.sprintf "WRONG/\n%!");
	      my_output_line client.chan (Printf.sprintf "SHIP/%d/\n%!" true_n)
	    | Failure "int_of_string" ->
	      Printf.printf
		"TRACE.exn : int_of_string failure, we could avoid it by checking command with RegExp.regex_putship\n%!";
	      my_output_line client.chan (Printf.sprintf "WRONG/\n%!");
	      my_output_line client.chan (Printf.sprintf "SHIP/%d/\n%!" true_n)
	end;
	Mutex.unlock clients_mutex
      | YOURTURN, "ACTION"::xs ->
	Mutex.lock clients_mutex;
	begin
	  try
	    if Game.check_action xs (Utils.nb_coups client.bateaux) client.drone then
	      begin
		Mutex.lock map_mutex;
		Game.treat xs client;
		Mutex.unlock map_mutex
	      end
	    else
	      Printf.printf "TRACE : uncorrect move received from %s\n%!" client.nom;
	  with
	    | Game.Uncorrect_action ->
	      Printf.printf "TRACE.exn : uncorrect checked actions received from %s\n%!" client.nom
	end;
	Next.next();
	Mutex.unlock clients_mutex
      | _, [] ->
	Printf.printf "TRACE.exn : maxi relou command incoming\n%!";
	Stop.stop_thread_client ~timer:Connexion.timer s_descr
      | _ -> (*my_output_line s_descr "UNKNOWNCOMMAND\n%!"*)
	Printf.printf "TRACE.exn : unknown command\n%!"
  done
  
	  

let main_spectator s_descr =
  spectators := s_descr::!spectators;
  Mutex.lock cmd_mutex;
  List.iter (fun cmd -> my_output_line s_descr (Printf.sprintf "%s\n%!" cmd)) (List.rev !cmd_list);
  Mutex.unlock cmd_mutex;
  while true do
    let cmd_recue = my_input_line s_descr in
    Printf.printf "\nTRACE : commande reçue = %s\n%!" cmd_recue;
    let new_cmd = split (regexp "/") cmd_recue in
    match new_cmd with
      | "TALK"::msg::_ ->
	send_to_all (Printf.sprintf "HEYLISTEN/(spectateur)/%s/\n" msg) s_descr
      | _ -> () (* pas le droit boloss *)
  done
	
let main_client s_descr =
  match Connexion.connexion_client s_descr with
    | 0 -> ()
    | 1 -> 
      Printf.printf "TRACE : connexion of player %s ok\n%!" (Utils.client_from_fd s_descr).nom;
      main_joueur s_descr
    | 2 ->
      Printf.printf "TRACE : new specator\n%!";
      main_spectator s_descr
    | _ -> failwith "wrong connexion's exit value (<> from 0,1,2)"












(**********************************************************************)
(****************   CLIENT / SERVOR   BASIC STUFFS   ******************)
(**********************************************************************)


class connexion (sd,sa) = 
   object (self) 
     val s_descr = sd
     val s_addr = sa
     
     val mutable numero = 0
     initializer 
       numero <- Utils.gen_num();
       Printf.printf "TRACE.connexion : objet traitant %d créé\n" numero ;
       print_newline()
 
     method start () =  Thread.create (fun x -> self#run x ; self#stop x) ()
 
     method stop() = 
       Printf.printf "TRACE.connexion : fin objet traitant %d\n" numero ;
       print_newline () ;
       Stop.stop_thread_client s_descr
	 
     method run () = 
       try 
         main_client s_descr
       with
	 | exn  -> print_string (Printexc.to_string exn) ; print_newline()
   end


class serv_socket p = 
object (self)
  val port = p 
  val mutable sock = ThreadUnix.socket Unix.PF_INET Unix.SOCK_STREAM 0
    
  initializer 
    Unix.setsockopt sock Unix.SO_REUSEADDR true;
    let mon_adresse = get_my_addr () in
    Printf.printf "Adresse du serveur : %s\n%!" (Unix.string_of_inet_addr mon_adresse);
    Unix.bind sock (Unix.ADDR_INET(mon_adresse,port)) ;
    Unix.listen sock 3
    
  method private client_addr = function 
  Unix.ADDR_INET(host,_) -> Unix.string_of_inet_addr host
    | _ -> "Unexpected client"
      
  method run () = 
    while(true) do
      try
	let (sd,sa) = ThreadUnix.accept sock in
	let connexion = new connexion(sd,sa) 
	in Printf.printf "TRACE.serv: nouvelle connexion de %s\n\n"
	(self#client_addr sa) ;
	ignore (connexion#start ())
      with
	| exn -> print_string (Printexc.to_string exn) ; print_newline()
    done
end
  
let go_serv () =
  let s = new serv_socket 2012 in
  s#run ()

let () =
  Unix.handle_unix_error go_serv ()

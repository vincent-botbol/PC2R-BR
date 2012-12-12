EXEC=my_serveur

CC=ocamlc
FLAGS=str.cma unix.cma threads.cma -thread

SRC=my_serveur.ml
OBJ=$(SRC:.ml=.cmo)

all:$(EXEC)

run: all
	./$(EXEC)

$(EXEC): $(OBJ)
	$(CC) $(FLAGS) -o $@ $^

%.cmo: %.ml
	$(CC) $(FLAGS) -c $<

depend:
	@echo [OCAMLDEP]
	ocamldep *.ml *.mli > .depend

-include .depend

clean:
	rm -f *.cm[io] $(EXEC) *~

.PHONY: all clean depend
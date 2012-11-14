EXEC=serveur

CC=ocamlfind ocamlc
FLAGS=-package unix -package threads -thread -linkpkg

SRC=serveur.ml
OBJ=$(SRC:.ml=.cmo)

all:$(EXEC)

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
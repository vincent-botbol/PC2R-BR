SRC=src
EXEC=my_serveur

all: $(SRC)/$(EXEC)

$(SRC)/$(EXEC):
	cd $(SRC) && $(MAKE)
	mv $@ .
	cd $(SRC) && $(MAKE) clean

clean:
	cd $(SRC) && $(MAKE) clean
	rm $(EXEC)
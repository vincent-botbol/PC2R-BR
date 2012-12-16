TARGET=rapport-java.pdf

all: $(TARGET)

showpdf: all
	evince $(TARGET)

$(TARGET): $(TARGET:%.pdf=%.tex)
	pdflatex $< -o $@

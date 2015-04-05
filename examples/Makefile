DOT_DIR := ./out/dot
DOT_FILES := $(wildcard $(DOT_DIR)/*.dot)

SVG_DIR := ../out/svg
PNG_DIR := ../out/png

SVG_FILES := $(addprefix $(SVG_DIR)/, $(notdir $(DOT_FILES:.dot=.svg)))
PNG_FILES := $(addprefix $(PNG_DIR)/, $(notdir $(DOT_FILES:.dot=.png)))

# adjusted for latex export
# PDF_DIR := $(SVG_DIR)
# PDF_FILES := $(SVG_FILES:.svg=.pdf)

production: $(PNG_FILES) $(SVG_FILES)

clean:
	rm ../out/svg/*
	rm ../out/png/*
    			
$(SVG_DIR)/%.svg: $(DOT_DIR)/%.dot	
	dot -Tsvg $< -o $@

$(PNG_DIR)/%.png: $(DOT_DIR)/%.dot	
	dot -Tpng $< -o $@

/**
 * Wrapper around ace editor to provide syntax-highlighting
 */
var syntaxHighlighter = {

	/**
	 * Converts a DOM element to a syntax-highlighted code viewer (read only)
	 * @param paneElement DOM element to convert to code viewer
	 */
	initViewCodePane: function(paneElement) {
		if($(paneElement).length) {
			var editor =  ace.edit(paneElement);
			editor.getSession().setMode("ace/mode/javascript");
			editor.setReadOnly(true);
			editor.setOptions({
		        maxLines: 30
		    });
		}
	},

	/**
	 * Converts a DOM element to a syntax-highlighted code editor
	 * @param editorElement DOM element to convert to code editor
	 */
	initEditCodePane: function(editorElement, textareaElement) {

		if($(editorElement).length && $(textareaElement).length) {

			var textarea = $(textareaElement).hide();

			var editor = ace.edit(editorElement);
			editor.getSession().setValue(textarea.val());
			editor.getSession().setMode("ace/mode/javascript");
			editor.setOptions({
		        maxLines: 32
		    });

			editor.getSession().on('change', function(){
			  textarea.val(editor.getSession().getValue());
			});

		}

	}

}
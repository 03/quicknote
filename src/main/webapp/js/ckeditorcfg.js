CKEDITOR.editorConfig = function(config) {
	config.resize_enabled = true;
    config.toolbar = 'MyToolbar';
    config.toolbar_MyToolbar = [
            [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript',
                    'Superscript', 'TextColor', 'BGColor', '-', 'Cut', 'Copy',
                    'Paste', 'Link', 'Unlink', 'Image'],
            [ 'Undo', 'Redo', '-', 'JustifyLeft', 'JustifyCenter',
                    'JustifyRight', 'JustifyBlock' ],
            [ 'Table', 'Smiley', 'SpecialChar', 'PageBreak',
                    'Styles', 'Format', 'Font', 'FontSize', 'Maximize'] ];
};

/* 
// valid on v3.6.4.0
CKEDITOR.editorConfig = function(config) {
    config.extraPlugins = 'uicolor';
    config.resize_enabled = false;
    config.toolbar = 'MyToolbar';
   
    config.toolbar_MyToolbar =
    	[
    		{ name: 'document',		items : [ 'Source','-','NewPage','DocProps','Print','-','Templates','Maximize' ] },
    		{ name: 'editing',		items : [ 'Undo','Redo', '-', 'Find','Replace','-','SelectAll','-','SpellChecker' ] },
    		{ name: 'insert',		items : [ 'Image','Flash','Table','HorizontalRule','Link','Smiley','SpecialChar','PageBreak','Iframe' ] },
    		'/',
    		{ name: 'styles',		items : [ 'Font','FontSize' ] },
    		{ name: 'colors',		items : [ 'TextColor','BGColor' ] },
    		{ name: 'basicstyles',	items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
    		{ name: 'paragraph',	items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-' ] },
    	];
    
    config.toolbar_Full =
    	[
    		{ name: 'document',		items : [ 'Source','-','Save','NewPage','DocProps','Preview','Print','-','Templates' ] },
    		{ name: 'clipboard',	items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
    		{ name: 'editing',		items : [ 'Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt' ] },
    		{ name: 'forms',		items : [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField' ] },
    		'/',
    		{ name: 'basicstyles',	items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
    		{ name: 'paragraph',	items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },
    		{ name: 'links',		items : [ 'Link','Unlink','Anchor' ] },
    		{ name: 'insert',		items : [ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe' ] },
    		'/',
    		{ name: 'styles',		items : [ 'Styles','Format','Font','FontSize' ] },
    		{ name: 'colors',		items : [ 'TextColor','BGColor' ] },
    		{ name: 'tools',		items : [ 'Maximize', 'ShowBlocks','-','About' ] }
    	];
    
    config.toolbar_Basic =
    	[
    		['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink','-','About']
    	];
    
};
*/
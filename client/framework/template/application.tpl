<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>{$title}</title>
        <meta charset="utf-8" />

        {* INCLUDE CSS HEADERS *}
        {foreach from=$includes_css item=incl_file}
        <link rel="stylesheet" href="{$incl_file}" type="text/css" media="screen" />
        {/foreach}

        {* INCLUDE JS HEADERS *}
        {foreach from=$includes_js item=incl_file}
        <script type="text/javascript" src="{$incl_file}"></script>
        {/foreach}
    </head>
	
    <body>
    </body>
</html>
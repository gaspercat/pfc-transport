<?php /* Smarty version Smarty-3.1.7, created on 2012-04-25 19:28:03
         compiled from "/Applications/MAMP/htdocs/transport/framework/template/application.tpl" */ ?>
<?php /*%%SmartyHeaderCode:585535914f982c46800ee8-20469200%%*/if(!defined('SMARTY_DIR')) exit('no direct access allowed');
$_valid = $_smarty_tpl->decodeProperties(array (
  'file_dependency' => 
  array (
    '8f8a6258709e4d8bbd8f63dcb4feb00f60750d75' => 
    array (
      0 => '/Applications/MAMP/htdocs/transport/framework/template/application.tpl',
      1 => 1335374879,
      2 => 'file',
    ),
  ),
  'nocache_hash' => '585535914f982c46800ee8-20469200',
  'function' => 
  array (
  ),
  'version' => 'Smarty-3.1.7',
  'unifunc' => 'content_4f982c46ccba1',
  'variables' => 
  array (
    'title' => 0,
    'includes_css' => 0,
    'incl_file' => 0,
    'includes_js' => 0,
  ),
  'has_nocache_code' => false,
),false); /*/%%SmartyHeaderCode%%*/?>
<?php if ($_valid && !is_callable('content_4f982c46ccba1')) {function content_4f982c46ccba1($_smarty_tpl) {?><!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title><?php echo $_smarty_tpl->tpl_vars['title']->value;?>
</title>
        <meta charset="utf-8" />

        
        <?php  $_smarty_tpl->tpl_vars['incl_file'] = new Smarty_Variable; $_smarty_tpl->tpl_vars['incl_file']->_loop = false;
 $_from = $_smarty_tpl->tpl_vars['includes_css']->value; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array');}
foreach ($_from as $_smarty_tpl->tpl_vars['incl_file']->key => $_smarty_tpl->tpl_vars['incl_file']->value){
$_smarty_tpl->tpl_vars['incl_file']->_loop = true;
?>
        <link rel="stylesheet" href="<?php echo $_smarty_tpl->tpl_vars['incl_file']->value;?>
" type="text/css" media="screen" />
        <?php } ?>

        
        <?php  $_smarty_tpl->tpl_vars['incl_file'] = new Smarty_Variable; $_smarty_tpl->tpl_vars['incl_file']->_loop = false;
 $_from = $_smarty_tpl->tpl_vars['includes_js']->value; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array');}
foreach ($_from as $_smarty_tpl->tpl_vars['incl_file']->key => $_smarty_tpl->tpl_vars['incl_file']->value){
$_smarty_tpl->tpl_vars['incl_file']->_loop = true;
?>
        <script type="text/javascript" src="<?php echo $_smarty_tpl->tpl_vars['incl_file']->value;?>
"></script>
        <?php } ?>
    </head>
	
    <body>
    </body>
</html><?php }} ?>
<?php
# Example WikiMedia extension
# with WikiMedia's extension mechanism it is possible to define
# new tags of the form
# <TAGNAME> some text </TAGNAME>
# the function registered by the extension gets the text between the
# tags as input and can transform it into arbitrary HTML code.
# Note: The output is not interpreted as WikiText but directly
#       included in the HTML output. So Wiki markup is not supported.
# To activate the extension, include it from your LocalSettings.php
# with: include("extensions/YourExtensionName.php");

$wgExtensionFunctions[] = "wfWiki3DExtension";

function wfWiki3DExtension() {
    global $wgParser;
    # register the extension with the WikiText parser
    # the first parameter is the name of the new tag.
    # In this case it defines the tag <Wiki3D> ... </Wiki3D>
    # the second parameter is the callback function for
    # processing the text between the tags
    $wgParser->setHook( "Wiki3D", "renderWiki3D" );
}

# The callback function for converting the input text to HTML output
function renderWiki3D( $input, $argv = null ) {
    # $argv is an array containing any arguments passed to the
    # extension like <example argument="foo" bar>..

$width = 480;
$height = 320;

if ($argv && $argv["width"]) {
	$width = $argv["width"];
}

if ($argv && $argv["height"]) {
	$height = $argv["height"];
}

$i = 0;
$l = strlen($input);
$last = " ";
$model = "";

while ($i < $l) {
	$char = $input[$i];
	if ($char == " " || $char == "\t" || $char == "\n" || $char == "\r") {
		if ($last != " ") {
			$model .= " ";
		}
		$last = " " ;
	}
	else {
		if ($char == "{" || $char == "}" || $char == "[" || $char == "]") {
			$model .= " $char ";
		}
		else {
			$model .= $char;
		}
		$last = $char;
	}
	$i++;
}


$output  = "<applet code=\"org.yriarte.wiki3D.Wiki3DApplet.class\" " ;
$output .= " width=\"$width\" height=\"$height\" " ;
$output .= " archive=\"" . $GLOBALS["wgScriptPath"] . "/extensions/Wiki3D/mini3D.jar\">" ;
$output .= "<param name=\"Model\" value=\"" . $model . "\">" ;

if ($argv && $argv["Z"] != null)
	$output .= "<param name=\"Z\" value=\"" . $argv["Z"] . "\">" ;
if ($argv && $argv["N"] != null)
	$output .= "<param name=\"N\" value=\"" . $argv["N"] . "\">" ;
if ($argv && $argv["Focal"] != null)
	$output .= "<param name=\"Focal\" value=\"" . $argv["Focal"] . "\">" ;

$output .= "</applet>" ;

    return $output;
}

?>